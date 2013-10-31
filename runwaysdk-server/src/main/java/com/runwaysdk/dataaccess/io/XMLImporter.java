/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Created on Jul 15, 2005
 */
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.dataaccess.metadata.ReservedWords;
import com.runwaysdk.util.Base64;

/**
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 **/
public class XMLImporter
{
  private Document[]                 metadataDocuments;

  private HashMap<String, EnityInfo> inheritanceMap;

  private HashMap<String, String>    attributeTypeMap;

  static final String                DOMAIN_TAG = "@@domain";

  Pattern                            idPattern  = Pattern.compile("\\w{64}");

  public static void main(String args[])
  {
    int argOffSet = 1;

    if (args.length <= argOffSet)
    {
      String errMsg = "Two arguments are required for XMLImporter:\n" + "  1) metadata XSD file path\n" + "  2) list of metadata files separated by a space";
      throw new CoreException(errMsg);
    }
    String schemaFile = args[0];

    String[] xmlFiles = new String[args.length - argOffSet];

    for (int i = 0; i < xmlFiles.length; i++)
    {
      xmlFiles[i] = args[i + argOffSet];
    }

    XMLImporter x = new XMLImporter(schemaFile, xmlFiles);
    x.toDatabase();
  }

  public XMLImporter(String schemaSource, String[] xmlFiles)
  {
    inheritanceMap = new HashMap<String, EnityInfo>(100);

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try
    {
      factory.setValidating(true);
      // factory.setNamespaceAware(true);
      factory.setAttribute(XMLConstants.JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA);
      factory.setAttribute(XMLConstants.JAXP_SCHEMA_SOURCE, new File(schemaSource));
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new XMLErrorHandler());

      metadataDocuments = new Document[xmlFiles.length];

      for (int i = 0; i < xmlFiles.length; i++)
      {
        metadataDocuments[i] = builder.parse(new File(xmlFiles[i]));
      }

    }
    catch (ParserConfigurationException e)
    {
      throw new XMLException(e);
    }
    catch (SAXException e)
    {
      throw new XMLException(e);
    }
    catch (IOException e)
    {
      throw new XMLException(e);
    }

    attributeTypeMap = new HashMap<String, String>(250);
  }

  /**
   * This constructor is used for when we read these files from the classpath,
   * which allows for these files to be embedded in the server jar.
   */
  public XMLImporter(InputStream schemaSource, InputStream[] xmlFiles)
  {
    inheritanceMap = new HashMap<String, EnityInfo>(100);

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try
    {
      factory.setValidating(true);
      // factory.setNamespaceAware(true);
      factory.setAttribute(XMLConstants.JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA);
      factory.setAttribute(XMLConstants.JAXP_SCHEMA_SOURCE, schemaSource);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new XMLErrorHandler());

      metadataDocuments = new Document[xmlFiles.length];

      for (int i = 0; i < xmlFiles.length; i++)
      {
        metadataDocuments[i] = builder.parse(xmlFiles[i]);
      }

    }
    catch (ParserConfigurationException e)
    {
      throw new XMLException(e);
    }
    catch (SAXException e)
    {
      throw new XMLException(e);
    }
    catch (IOException e)
    {
      throw new XMLException(e);
    }

    attributeTypeMap = new HashMap<String, String>(250);
  }

  public void toDatabase()
  {
    // Build metadata
    for (Document dependentDocument : metadataDocuments)
    {
      buildInheritance(dependentDocument);

      getTableNames(dependentDocument);

      buildAttributeMap(dependentDocument);
    }

    // Create DB sequence
    Database.createObjectSequence();
    Database.createTransactionSequence();

    Database.setupPropertiesTable();

    for (Document dependentDocument : metadataDocuments)
    {
      ddl(dependentDocument);
    }

    for (Document dependentDocument : metadataDocuments)
    {
      insertValues(dependentDocument);
    }
  }

  private void ddl(Document metaDataDocument)
  {
    Element root = metaDataDocument.getDocumentElement();
    NodeList objects = root.getElementsByTagName("object");
    NodeList entityIndexRelationships = root.getElementsByTagName("entityIndex");

    // Process MD_Entities
    for (int i = 0; i < objects.getLength(); i++)
    {
      Element object = (Element) objects.item(i);
      String type = getValueFromChildTag(object, EntityInfo.TYPE);

      if (isEntityClass(type))
      {
        this.createType(object, type);
      }
    }

    // Add indexes
    for (int i = 0; i < entityIndexRelationships.getLength(); i++)
    {
      Element entityIndex = (Element) entityIndexRelationships.item(i);
      this.createIndex(entityIndex);
    }
  }

  private void insertValues(Document metaDataDocument)
  {
    Element root = metaDataDocument.getDocumentElement();

    NodeList relationships = root.getElementsByTagName("relationship");
    for (int i = 0; i < relationships.getLength(); i++)
    {
      Element relationship = (Element) relationships.item(i);

      // Insert attribtues on the relationship
      this.insert(getValueFromChildTag(relationship, "id"), getAttributeValue(relationship, EntityInfo.TYPE), (Element) relationship.getElementsByTagName("attributes").item(0), getValueFromChildTag(relationship, RelationshipInfo.PARENT_ID), getValueFromChildTag(relationship, RelationshipInfo.CHILD_ID));

    }

    NodeList entityIndexRelationships = root.getElementsByTagName("entityIndex");
    // now do the same thing for entity relationships
    for (int i = 0; i < entityIndexRelationships.getLength(); i++)
    {
      Element entityIndex = (Element) entityIndexRelationships.item(i);

      // Insert attribtues on the relationship
      this.insert(getValueFromChildTag(entityIndex, "id"), getAttributeValue(entityIndex, EntityInfo.TYPE), (Element) entityIndex.getElementsByTagName("attributes").item(0), getValueFromChildTag(entityIndex, RelationshipInfo.PARENT_ID), getValueFromChildTag(entityIndex, RelationshipInfo.CHILD_ID));

    }

    // Insert attribute definitions
    Iterator<Element> mdAttributes = getDefinitionElements(root).iterator();
    while (mdAttributes.hasNext())
    {
      insertMdAttributeRecord(mdAttributes.next());
    }

    NodeList objects = root.getElementsByTagName("object");
    // Insert values for object
    for (int i = 0; i < objects.getLength(); i++)
    {
      Element object = (Element) objects.item(i);
      insert(getValueFromChildTag(object, EntityInfo.ID), getValueFromChildTag(object, EntityInfo.TYPE), (Element) object.getElementsByTagName("attributes").item(0), null, null);
    }

    NodeList structs = root.getElementsByTagName("struct");
    for (int i = 0; i < structs.getLength(); i++)
    {
      Element object = (Element) structs.item(i);
      insert(getValueFromChildTag(object, EntityInfo.ID), getValueFromChildTag(object, EntityInfo.TYPE), (Element) object.getElementsByTagName("attributes").item(0), null, null);
    }
  }

  private boolean isEntityClass(String type)
  {
    if (type.equalsIgnoreCase(MdBusinessInfo.CLASS) || type.equalsIgnoreCase(MdTermInfo.CLASS) || type.equalsIgnoreCase(MdStructInfo.CLASS) || type.equalsIgnoreCase(MdLocalStructInfo.CLASS) || type.equalsIgnoreCase(MdRelationshipInfo.CLASS) || type.equalsIgnoreCase(MdTreeInfo.CLASS) || type.equalsIgnoreCase(MdGraphInfo.CLASS) || type.equalsIgnoreCase(MdTermRelationshipInfo.CLASS) || type.equalsIgnoreCase(MdEnumerationInfo.CLASS) || type.equalsIgnoreCase(MdFacadeInfo.CLASS))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  private void getTableNames(Document metadataDocument)
  {
    Element root = metadataDocument.getDocumentElement();
    NodeList objects = root.getElementsByTagName("object");
    // Process MD_Entities
    for (int i = 0; i < objects.getLength(); i++)
    {
      Element object = (Element) objects.item(i);
      String type = getValueFromChildTag(object, EntityInfo.TYPE);

      if (isEntityClass(type))
      {
        String entity_name = null;
        String package_name = null;
        String table_name = "";

        NodeList attributes = object.getElementsByTagName("attribute");

        for (int j = 0; j < attributes.getLength(); j++)
        {
          Element attribute = (Element) attributes.item(j);

          if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdTypeInfo.NAME))
          {
            entity_name = getValueFromChildTag(attribute, "value");
          }
          else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdTypeInfo.PACKAGE))
          {
            package_name = getValueFromChildTag(attribute, "value");
          }
          else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdElementInfo.TABLE_NAME))
          {
            table_name = getValueFromChildTag(attribute, "value");
          }

          if (entity_name != null && package_name != null && table_name != null)
          {
            break;
          }
        }

        String definesType = package_name + "." + entity_name;

        EnityInfo entityInfo = inheritanceMap.get(definesType);
        if (entityInfo == null)
          throw new ProgrammingErrorException("Type [" + definesType + "] is not defined in the inheritance hierarchy.");

        entityInfo.setTableName(table_name);
      }
    }
  }

  private void buildAttributeMap(Document metadataDocument)
  {
    Element rootElement = metadataDocument.getDocumentElement();

    Iterator<Element> i = getDefinitionElements(rootElement).iterator();
    while (i.hasNext())
    {
      Element mdAttribute = i.next();

      String type = getValueFromChildTag(mdAttribute, EntityInfo.TYPE);
      String key = getValueFromChildTag(mdAttribute, MdAttributeConcreteInfo.KEY);
      attributeTypeMap.put(key, type);
    }
  }

  /**
   *
   *
   */
  private void createIndex(Element entityIndex)
  {
    String tableName = this.getValueFromChildTag(entityIndex, MdElementInfo.TABLE_NAME);

    String indexName = this.getValueFromChildTag(entityIndex, MdIndexInfo.INDEX_NAME);

    String uniqueValue = this.getValueFromChildTag(entityIndex, MdIndexInfo.UNIQUE);

    NodeList nodeList = entityIndex.getElementsByTagName("columnName");

    List<String> columnNameList = new LinkedList<String>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Element columnNameElement = (Element) nodeList.item(i);

      if (columnNameElement.getFirstChild() != null)
      {
        columnNameList.add(columnNameElement.getFirstChild().getNodeValue());
      }
    }

    boolean isUnique = AttributeBoolean.getBooleanValue(uniqueValue);

    Database.addGroupAttributeIndex(tableName, indexName, columnNameList, isUnique);
  }

  /**
   * 
   * @return
   */
  private String insertEnumMapping(Element enumeration)
  {
    List<String> columnNames = new LinkedList<String>();
    columnNames.add(MdEnumerationDAOIF.SET_ID_COLUMN);
    columnNames.add(MdEnumerationDAOIF.ITEM_ID_COLUMN);

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
    List<String> prepSmtVars = new LinkedList<String>();
    LinkedList<Object> values = new LinkedList<Object>();
    LinkedList<String> attributeTypes = new LinkedList<String>();

    String enumerationType = getValueFromChildTag(enumeration, EntityInfo.TYPE);
    prepSmtVars.add("?");
    values.add(getValueFromChildTag(enumeration, MdEnumerationInfo.SET_ID));
    attributeTypes.add(MdAttributeCharacterInfo.CLASS);

    String enumString = "";

    PreparedStatement preparedStmt = null;

    NodeList attrIdList = enumeration.getElementsByTagName(MdEnumerationInfo.ITEM_ID);
    for (int j = 0; j < attrIdList.getLength(); j++)
    {
      String itemId = attrIdList.item(j).getFirstChild().getNodeValue();
      prepSmtVars.add("?");
      values.add(itemId);
      String tableName = ( inheritanceMap.get(enumerationType) ).getTableName();
      attributeTypes.add(MdAttributeCharacterInfo.CLASS);

      preparedStmt = Database.buildPreparedSQLInsertStatement(tableName, columnNames, prepSmtVars, values, attributeTypes);
      preparedStatementList.add(preparedStmt);

      values.removeLast();
      attributeTypes.removeLast();

      if (j != 0)
      {
        enumString += ",";
      }
      enumString += itemId;
    }

    Database.executeStatementBatch(preparedStatementList);

    return enumString;
  }

  private void createType(Element object, String metaDataType)
  {
    // Create the table
    NodeList attributes = object.getElementsByTagName("attribute");
    String id = null;
    String entity_name = null;
    String package_name = null;
    String table_name = null;
    String index1_name = null;
    String index2_name = null;
    String isAbstract = null;

    for (int i = 0; i < attributes.getLength(); i++)
    {
      Element attribute = (Element) attributes.item(i);
      if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdTypeInfo.ID))
      {
        id = getValueFromChildTag(attribute, "value");
      }
      else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdTypeInfo.NAME))
      {
        entity_name = getValueFromChildTag(attribute, "value");
      }
      else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdTypeInfo.PACKAGE))
      {
        package_name = getValueFromChildTag(attribute, "value");
      }
      else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdElementInfo.TABLE_NAME))
      {
        table_name = getValueFromChildTag(attribute, "value");
        // check for reserved words
        if (ReservedWords.sqlContains(table_name))
        {
          throw new ReservedWordException("The table name [" + table_name + "] is reserved.", table_name, ReservedWordException.Origin.TABLE);
        }
      }
      else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdRelationshipDAOIF.INDEX1_NAME))
      {
        index1_name = getValueFromChildTag(attribute, "value");
      }
      else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdRelationshipDAOIF.INDEX2_NAME))
      {
        index2_name = getValueFromChildTag(attribute, "value");
      }
      else if (getValueFromChildTag(attribute, "name").equalsIgnoreCase(MdElementInfo.ABSTRACT))
      {
        isAbstract = getValueFromChildTag(attribute, "value");
      }
      if (entity_name != null && package_name != null && table_name != null && isAbstract != null && index1_name != null && index2_name != null && id != null)
      {
        break;
      }
    }

    // create the table in the database
    if (metaDataType.equals(MdBusinessInfo.CLASS) || metaDataType.equals(MdTermInfo.CLASS) || metaDataType.equals(MdStructInfo.CLASS) || metaDataType.equals(MdLocalStructInfo.CLASS) || metaDataType.equals(MdStateMachineInfo.CLASS))
    {
      Database.createClassTable(table_name);
    }
    else if (metaDataType.equals(MdRelationshipInfo.CLASS) || metaDataType.equals(MdGraphInfo.CLASS) || metaDataType.equals(MdTermRelationshipInfo.CLASS) || metaDataType.equals(MdTreeInfo.CLASS))
    {
      boolean isUnique = false;

      // MdRelationships do not have unique parent->child pairs
      if (!metaDataType.equals(MdRelationshipInfo.CLASS))
      {
        if (isAbstract != null && isAbstract.trim().equalsIgnoreCase(MdAttributeBooleanDAOIF.DB_FALSE))
        {
          isUnique = true;
        }
      }

      Database.createRelationshipTable(table_name, index1_name, index2_name, isUnique);
    }
    else if (metaDataType.equals(MdEnumerationInfo.CLASS))
    {
      Database.createEnumerationTable(table_name, id);
    }

    String type = package_name + "." + entity_name;

    // Add the attributes to the table
    Iterator<Element> i = getDefinitionElements(object).iterator();
    while (i.hasNext())
    {
      Element mdAttribute = i.next();

      createAttribute(mdAttribute, type, table_name);
    }

    if (type.equals(EntityTypes.METADATADISPLAYLABEL.getType()) && !Database.sharesDDLandDMLconnection())
    {
      String tableName = EntityTypes.METADATADISPLAYLABEL.getTableName();
      String displayLabelColumName = MdAttributeLocalDAOIF.METADATA_DISPLAY_LABEL_COLUMN_TEMP;
      String displayLabelColumnType = DatabaseProperties.getDatabaseType(MdAttributeCharacterInfo.CLASS);
      String displayColumnSize = MdAttributeLocalDAOIF.METADATA_DISPLAY_LABEL_COLUMN_SIZE;
      String displayLabelFormatedType = Database.formatCharacterField(displayLabelColumnType, displayColumnSize);

      Database.addTempFieldsToTable(tableName, displayLabelColumName, displayLabelFormatedType, MdAttributeLocalDAOIF.NUMBER_OF_TEMP_COLUMNS);
    }
  }

  private void createAttribute(Element mdAttribute, String entityType, String table)
  {
    String type = getValueFromChildTag(mdAttribute, EntityInfo.TYPE);
    String name = getValueFromChildTag(mdAttribute, MdAttributeConcreteInfo.NAME);
    String columnName = getValueFromChildTag(mdAttribute, MdAttributeConcreteInfo.COLUMN_NAME);

    // ID is automatically added to all tables. Don't add it here.
    if (name.equalsIgnoreCase(EntityInfo.ID))
    {
      return;
    }

    if (type.equalsIgnoreCase(MdAttributeFloatInfo.CLASS) || type.equalsIgnoreCase(MdAttributeDoubleInfo.CLASS) || type.equalsIgnoreCase(MdAttributeDecimalInfo.CLASS))
    {
      Database.addDecField(table, columnName, DatabaseProperties.getDatabaseType(type), getValueFromChildTag(mdAttribute, MdAttributeDecInfo.LENGTH), getValueFromChildTag(mdAttribute, MdAttributeDecInfo.DECIMAL));
    }
    else if (type.equalsIgnoreCase(MdAttributeHashInfo.CLASS))
    {
      String size = Integer.toString(MdAttributeHashInfo.HASH_SIZE);
      Database.addField(table, columnName, DatabaseProperties.getDatabaseType(type), size);
    }
    else if (type.equalsIgnoreCase(MdAttributeSymmetricInfo.CLASS))
    {
      Database.addField(table, columnName, DatabaseProperties.getDatabaseType(type), (String) null);
    }
    else
    {
      String size = null;
      if (type.equalsIgnoreCase(MdAttributeCharacterInfo.CLASS))
      {
        size = getValueFromChildTag(mdAttribute, MdAttributeCharacterInfo.SIZE);
      }
      else if (type.equalsIgnoreCase(MdAttributeReferenceInfo.CLASS) || type.equalsIgnoreCase(MdAttributeTermInfo.CLASS) || type.equalsIgnoreCase(MdAttributeStructInfo.CLASS) || type.equalsIgnoreCase(MdAttributeLocalCharacterInfo.CLASS) || type.equalsIgnoreCase(MdAttributeLocalTextInfo.CLASS))

      {
        size = Database.DATABASE_ID_SIZE;
      }
      else if (type.equalsIgnoreCase(MdAttributeEnumerationInfo.CLASS))
      {
        size = Database.DATABASE_SET_ID_SIZE;

        // Add the cache column to the table.
        String cacheColumnName = getValueFromChildTag(mdAttribute, MdAttributeEnumerationInfo.CACHE_COLUMN_NAME);
        Database.addField(table, cacheColumnName, DatabaseProperties.getDatabaseType(MdAttributeEnumerationInfo.CACHE_COLUMN_DATATYPE), (String) null);
      }

      Database.addField(table, columnName, DatabaseProperties.getDatabaseType(type), size);
    }

    Element cacheIndexType = (Element) mdAttribute.getElementsByTagName(MdAttributeConcreteDAOIF.INDEX_TYPE_CACHE).item(0);
    String index_id = getValueFromChildTag(cacheIndexType, MdEnumerationInfo.ITEM_ID);

    if (index_id.equalsIgnoreCase(IndexTypes.UNIQUE_INDEX.getId()) && !type.equalsIgnoreCase(MdAttributeEnumerationInfo.CLASS))
    {
      String indexName = this.getValueFromChildTag(mdAttribute, MdAttributeConcreteInfo.INDEX_NAME);

      Database.addUniqueIndex(table, columnName, indexName);
    }
    else if ( ( index_id.equalsIgnoreCase(IndexTypes.NON_UNIQUE_INDEX.getId()) && !type.equalsIgnoreCase(MdAttributeEnumerationInfo.CLASS) ) || type.equalsIgnoreCase(MdAttributeReferenceInfo.CLASS) || type.equalsIgnoreCase(MdAttributeTermInfo.CLASS))
    {
      String indexName = this.getValueFromChildTag(mdAttribute, MdAttributeConcreteInfo.INDEX_NAME);

      Database.addNonUniqueIndex(table, columnName, indexName);
    }
  }

  /**
   * 
   * @param id
   * @param type
   * @param attributes
   * @param parentId
   * @param childId
   */
  private void insert(String id, String type, Element attributes, String parentId, String childId)
  {
    validateId(id);

    List<List<Element>> attributeLists = new LinkedList<List<Element>>();

    List<Element> attributeNodeList = new LinkedList<Element>();

    NodeList attributesChildNodeList = attributes.getChildNodes();

    for (int i = 0; i < attributesChildNodeList.getLength(); i++)
    {
      Node attributeNode = attributesChildNodeList.item(i);
      if (attributeNode instanceof Element)
      {
        Element attributeElement = (Element) attributeNode;
        if (attributeElement.getTagName().equals("attribute") || attributeElement.getTagName().equals("attributeStruct"))
        {
          attributeNodeList.add(attributeElement);
        }
      }
    }

    attributeLists.add(attributeNodeList);

    EnityInfo entityInfo = inheritanceMap.get(type);

    List<String> inheritanceList = entityInfo.getInheritanceList();

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();

    List<List<String>> columnNameList = new ArrayList<List<String>>(inheritanceList.size());
    List<List<String>> prepSmtVarList = new ArrayList<List<String>>(inheritanceList.size());
    List<List<Object>> valueList = new ArrayList<List<Object>>(inheritanceList.size());
    List<List<String>> attributeTypeList = new ArrayList<List<String>>(inheritanceList.size());

    // Add the ID attribute to all tables in the inherited hierarchy
    for (int i = 0; i < inheritanceList.size(); i++)
    {
      List<String> fields = new LinkedList<String>();
      List<String> prepSmtVars = new LinkedList<String>();
      List<Object> values = new LinkedList<Object>();
      List<String> attributeTypes = new LinkedList<String>();

      fields.add(EntityDAOIF.ID_COLUMN);
      prepSmtVars.add("?");
      values.add(id);
      attributeTypes.add(MdAttributeCharacterInfo.CLASS);

      columnNameList.add(fields);
      prepSmtVarList.add(prepSmtVars);
      valueList.add(values);
      attributeTypeList.add(attributeTypes);
    }

    for (List<Element> attributeList : attributeLists)
    {
      // Add the value for each attribute to the correct table
      for (int i = 0; i < attributeList.size(); i++)
      {
        Element attribute = (Element) attributeList.get(i);
        String definingEntity = getValueFromChildTag(attribute, "definingComponent");
        String name = getValueFromChildTag(attribute, "name");
        String columName = MetadataDAO.convertCamelCaseToUnderscore(name);
        String value = getValueFromChildTag(attribute, "value");

        // If the value equals DOMAIN_TAG then substitute the value for the
        // domain specified in the system properties file
        if (value.equalsIgnoreCase(DOMAIN_TAG))
        {
          value = CommonProperties.getDomain();
        }

        // ID is automatically added to all lists in the inheritance hierarchy
        if (name.equalsIgnoreCase(EntityInfo.ID))
        {
          continue;
        }

        int index = inheritanceList.indexOf(definingEntity);

        List<String> fields = columnNameList.get(index);
        List<String> prepSmtVars = prepSmtVarList.get(index);
        List<Object> values = valueList.get(index);
        List<String> attributeTypes = attributeTypeList.get(index);

        NodeList enumerations = attribute.getElementsByTagName("enumeration");
        if (enumerations.getLength() > 0)
        {
          Element enumeration = (Element) enumerations.item(0);

          fields.add(MdAttributeEnumerationDAO.getCacheDbColumnName(columName));
          prepSmtVars.add("?");
          values.add(this.insertEnumMapping(enumeration));
          attributeTypes.add(MdAttributeClobInfo.CLASS);
        }

        String attributeType = attributeTypeMap.get(definingEntity + '.' + name);

        fields.add(columName);
        attributeTypes.add(attributeType);

        // BLOB attributes require special handling
        if (attributeType.equals(MdAttributeBlobInfo.CLASS))
        {
          byte[] decoded = Base64.decodeFast(value.toCharArray());
          prepSmtVars.add("?");
          values.add(decoded);
        }
        else if (name.equals(ElementInfo.SEQUENCE))
        {
          // Set the SEQUENCE value to the next global sequence number from the
          // database
          String sequenceNumber = Database.getNextSequenceNumber();
          prepSmtVars.add("?");
          values.add(sequenceNumber);
        }
        else
        {
          prepSmtVars.add("?");
          values.add(value);
        }
      }
    }

    for (int i = 0; i < inheritanceList.size(); i++)
    {
      EnityInfo loopEntityInfo = inheritanceMap.get(inheritanceList.get(i));
      String tableName = loopEntityInfo.getTableName();

      List<String> columnNames = columnNameList.get(i);
      List<String> prepSmtVars = prepSmtVarList.get(i);
      List<Object> values = valueList.get(i);
      List<String> attributeTypes = attributeTypeList.get(i);

      PreparedStatement preparedStmt = null;

      if (parentId != null && childId != null && !tableName.equals(ElementInfo.CLASS))
      {
        // Add the parent_id and child_id values to a relationship table
        columnNames.add(RelationshipDAOIF.PARENT_ID_COLUMN);
        prepSmtVars.add("?");
        values.add(parentId);
        attributeTypes.add(MdAttributeCharacterInfo.CLASS);

        columnNames.add(RelationshipDAOIF.CHILD_ID_COLUMN);
        prepSmtVars.add("?");
        values.add(childId);
        attributeTypes.add(MdAttributeCharacterInfo.CLASS);
      }

      preparedStmt = Database.buildPreparedSQLInsertStatement(tableName, columnNames, prepSmtVars, values, attributeTypes);

      // Bind the variables
      preparedStatementList.add(preparedStmt);
    }

    Database.executeStatementBatch(preparedStatementList);
  }

  private void validateId(String id)
  {
    Matcher matcher = idPattern.matcher(id);
    if (!matcher.matches())
    {
      String msg = "The id [" + id + "] is invalid";
      throw new XMLException(msg);
    }
  }

  /**
   * Inserts records into the md_attribute and all subtables.
   * 
   * @param mdAttribute
   */
  private void insertMdAttributeRecord(Element mdAttribute)
  {
    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();

    String entityType = getValueFromChildTag(mdAttribute, EntityInfo.TYPE);
    String id = null;

    EnityInfo classInfo = inheritanceMap.get(entityType);

    List<String> inheritance = classInfo.getInheritanceList();

    List<List<String>> fieldList = new ArrayList<List<String>>(inheritance.size());
    List<List<String>> prepSmtVarList = new ArrayList<List<String>>(inheritance.size());
    List<List<Object>> valueList = new ArrayList<List<Object>>(inheritance.size());
    List<List<String>> attributeTypeList = new ArrayList<List<String>>(inheritance.size());

    for (int i = 0; i < inheritance.size(); i++)
    {
      List<String> fields = new LinkedList<String>();
      List<String> prepSmtVars = new LinkedList<String>();
      List<Object> values = new LinkedList<Object>();
      List<String> attributeTypes = new LinkedList<String>();

      fields.add(EntityDAOIF.ID_COLUMN);
      id = getValueFromChildTag(mdAttribute, EntityInfo.ID);
      validateId(id);
      prepSmtVars.add("?");
      values.add(id);
      attributeTypes.add(MdAttributeCharacterInfo.CLASS);

      fieldList.add(fields);
      prepSmtVarList.add(prepSmtVars);
      valueList.add(values);
      attributeTypeList.add(attributeTypes);
    }

    NodeList attributes = mdAttribute.getChildNodes();
    for (int i = 0; i < attributes.getLength(); i++)
    {
      Node next = attributes.item(i);
      if (! ( next instanceof Element ) || next.getNodeName().equalsIgnoreCase(EntityInfo.ID))
      {
        continue;
      }

      String name = next.getNodeName();

      if (name.equals(MdAttributeConcreteDAOIF.INDEX_TYPE_CACHE) || name.equals(MdAttributeConcreteDAOIF.SETTER_VISIBILITY_CACHE) || name.equals(MdAttributeConcreteDAOIF.GETTER_VISIBILITY_CACHE))
      {
        int index = inheritance.indexOf(MdAttributeConcreteInfo.CLASS);
        List<String> fields = fieldList.get(index);
        List<String> prepSmtVars = prepSmtVarList.get(index);
        List<Object> values = valueList.get(index);
        List<String> attributeTypes = attributeTypeList.get(index);

        fields.add(name);

        NodeList enumerations = ( (Element) next ).getElementsByTagName("enumeration");
        Element enumeration = (Element) enumerations.item(0);

        prepSmtVars.add("?");
        values.add(this.insertEnumMapping(enumeration));
        attributeTypes.add(MdAttributeClobInfo.CLASS);
      }
      else if (name.equals(MdAttributeHashDAOIF.HASH_METHOD_CACHE))
      {
        int index = inheritance.indexOf(MdAttributeHashInfo.CLASS);
        List<String> fields = fieldList.get(index);
        List<String> prepSmtVars = prepSmtVarList.get(index);
        List<Object> values = valueList.get(index);
        List<String> attributeTypes = attributeTypeList.get(index);
        fields.add(name);

        NodeList enumerations = ( (Element) next ).getElementsByTagName("enumeration");
        Element enumeration = (Element) enumerations.item(0);
        prepSmtVars.add("?");
        values.add(this.insertEnumMapping(enumeration));
        attributeTypes.add(MdAttributeClobInfo.CLASS);
      }
      else
      {
        String loopAttributeType = new String("");

        // This really should be in its own method, but two values are needed
        // and I did not
        // want to traverse the structure twice.
        Iterator<String> j = inheritance.iterator();
        while (j.hasNext())
        {
          String iteratorType = j.next();

          if (this.attributeTypeMap.containsKey(iteratorType + '.' + name))
          {
            loopAttributeType = iteratorType;
            break;
          }
        }

        String value;
        if (name.equals(ElementInfo.SEQUENCE))
        {
          value = Database.getNextSequenceNumber();
        }
        else if (name.equals(MdAttributeConcreteInfo.DISPLAY_LABEL) || name.equals(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL) || name.equals(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL) || name.equals(MdAttributeConcreteInfo.DESCRIPTION))
        {
          value = ServerIDGenerator.nextID() + "NM200904120000000000000000000030";

          NodeList nextChildren = next.getChildNodes();

          List<String> columnNames = new LinkedList<String>();
          List<String> prepSmtVars = new LinkedList<String>();
          List<Object> values = new LinkedList<Object>();
          List<String> attributeTypes = new LinkedList<String>();

          columnNames.add(MdAttributeConcreteDAOIF.ID_COLUMN);
          prepSmtVars.add("?");
          values.add(value);
          attributeTypes.add(MdAttributeCharacterInfo.CLASS);

          columnNames.add(MdAttributeConcreteDAOIF.KEY_COLUMN);
          prepSmtVars.add("?");
          values.add(value);
          attributeTypes.add(MdAttributeCharacterInfo.CLASS);

          columnNames.add(MdAttributeConcreteDAOIF.SITE_MASTER_COLUMN);
          prepSmtVars.add("?");
          values.add("www.runwaysdk.com");
          attributeTypes.add(MdAttributeCharacterInfo.CLASS);

          PreparedStatement preparedStmt;

          for (int k = 0; k < nextChildren.getLength(); k++)
          {
            Node nextChild = nextChildren.item(k);

            if (nextChild instanceof Element)
            {
              Element locale = (Element) nextChild;
              String localeName = locale.getTagName();
              String localeColumnName = MetadataDAO.convertCamelCaseToUnderscore(localeName);
              String localeValue = locale.getTextContent().trim();

              columnNames.add(localeColumnName);
              prepSmtVars.add("?");
              values.add(localeValue);
              attributeTypes.add(MdAttributeCharacterInfo.CLASS);

            }
          }

          preparedStmt = Database.buildPreparedSQLInsertStatement(EntityTypes.METADATADISPLAYLABEL.getTableName(), columnNames, prepSmtVars, values, attributeTypes);
          preparedStatementList.add(preparedStmt);
        }
        else
        {
          value = getValueFromChildTag(mdAttribute, name);
        }

        int index = inheritance.indexOf(loopAttributeType);

        List<String> fields = fieldList.get(index);
        List<String> prepSmtVars = prepSmtVarList.get(index);
        List<Object> values = valueList.get(index);
        List<String> attributeTypes = attributeTypeList.get(index);

        String columnName = MetadataDAO.convertCamelCaseToUnderscore(name);
        fields.add(columnName);
        String qualifiedAttributeName = ( loopAttributeType + '.' + name );

        String attributeType = this.attributeTypeMap.get(qualifiedAttributeName);

        if (attributeType.equals(MdAttributeBlobInfo.CLASS))
        {
          byte[] decoded = Base64.decodeFast(value.toCharArray());
          prepSmtVars.add("?");
          values.add(decoded);
        }
        else
        {
          prepSmtVars.add("?");
          values.add(value);
        }

        attributeTypes.add((String) this.attributeTypeMap.get(qualifiedAttributeName));

      }
    }

    PreparedStatement preparedStmt;

    for (int i = 0; i < inheritance.size(); i++)
    {
      List<String> fields = fieldList.get(i);
      List<String> prepSmtVars = prepSmtVarList.get(i);
      List<Object> values = valueList.get(i);
      List<String> attributeTypes = attributeTypeList.get(i);

      EnityInfo loopClassInfo = this.inheritanceMap.get(inheritance.get(i));

      preparedStmt = Database.buildPreparedSQLInsertStatement(loopClassInfo.getTableName(), fields, prepSmtVars, values, attributeTypes);

      // Bind the variables
      for (int j = 0; j < fields.size(); j++)
      {
        Database.bindPreparedStatementValue(preparedStmt, j + 1, values.get(j), attributeTypes.get(j));
      }
      preparedStatementList.add(preparedStmt);
    }

    Database.executeStatementBatch(preparedStatementList);
  }

  private List<Element> getDefinitionElements(Element object)
  {
    LinkedList<Element> elements = new LinkedList<Element>();
    NodeList defs = object.getElementsByTagName("definitions");
    for (int i = 0; i < defs.getLength(); i++)
    {
      Element definitions = (Element) defs.item(i);

      for (Node next = definitions.getFirstChild(); next != null; next = next.getNextSibling())
      {
        if (next.getNodeName().startsWith("mdattribute"))
        {
          elements.add((Element) next);
        }
      }
    }
    return elements;
  }

  private String getAttributeValue(Element element, String name)
  {
    NodeList attributes = element.getElementsByTagName("attribute");
    for (int i = 0; i < attributes.getLength(); i++)
    {
      if (getValueFromChildTag((Element) attributes.item(i), "name").equalsIgnoreCase(name))
      {
        return getValueFromChildTag((Element) attributes.item(i), "value");
      }
    }
    return "";
  }

  private String getValueFromChildTag(Element element, String tag)
  {
    Element child = (Element) element.getElementsByTagName(tag).item(0);

    if (child.getFirstChild() == null)
    {
      return new String();
    }
    else
    {
      return child.getFirstChild().getNodeValue();
    }
  }

  /**
   * Builds a temporary data structure to keep track of inheritance
   * relationships.
   * 
   */
  private void buildInheritance(Document metadataDocument)
  {
    NodeList nodeList = metadataDocument.getDocumentElement().getElementsByTagName("inheritance").item(0).getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node child = nodeList.item(i);
      if (child instanceof Element)
      {
        LinkedList<String> inheritance = null;
        if (inheritanceMap.containsKey(child.getNodeName()))
        {
          inheritance = inheritanceMap.get(child.getNodeName()).getInheritanceList();
        }
        else
        {
          inheritance = new LinkedList<String>();
          inheritance.addFirst(child.getNodeName());

          EnityInfo classInfo = new EnityInfo(inheritance);
          inheritanceMap.put(inheritance.getFirst(), classInfo);
        }

        buildInheritance(child, inheritance);
      }
    }
  }

  private void buildInheritance(Node parent, LinkedList<String> parentInheritance)
  {
    NodeList children = parent.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      if (child instanceof Element)
      {
        LinkedList<String> childInheritance = null;

        if (inheritanceMap.containsKey(child.getNodeName()))
        {
          childInheritance = inheritanceMap.get(child.getNodeName()).getInheritanceList();
        }
        else
        {
          childInheritance = new LinkedList<String>(parentInheritance);
          childInheritance.addFirst(child.getNodeName());

          EnityInfo classInfo = new EnityInfo(childInheritance);
          inheritanceMap.put(childInheritance.getFirst(), classInfo);
        }

        buildInheritance(child, childInheritance);
      }
    }
  }

  private class XMLErrorHandler implements ErrorHandler
  {
    public void fatalError(SAXParseException exception) throws SAXException
    {
    }

    // treat validation errors as warnings
    public void error(SAXParseException e) throws SAXParseException
    {
      System.out.println(e);
    }

    // dump warnings too
    public void warning(SAXParseException err) throws SAXParseException
    {
      System.out.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
      System.out.println("   " + err.getMessage());
    }
  }

  private class EnityInfo
  {
    private String             tableName;

    private LinkedList<String> inheritanceList;

    public EnityInfo(LinkedList<String> inheritanceList)
    {
      this.inheritanceList = inheritanceList;
    }

    public String getTableName()
    {
      return this.tableName;
    }

    public void setTableName(String tableName)
    {
      this.tableName = tableName;
    }

    public LinkedList<String> getInheritanceList()
    {
      return this.inheritanceList;
    }
  }
}
