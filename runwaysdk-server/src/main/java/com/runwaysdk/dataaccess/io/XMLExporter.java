/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Created on Jul 22, 2005
 */
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.attributes.entity.AttributeSymmetric;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.Base64;
import com.runwaysdk.util.FileIO;

/**
 * XMLExporter provides the structure to incrementally build a DOM Document
 * representing data from the core. The DOM can be written to an XML file, which
 * can be imported to other instances of the core using XMLImporter.
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 */
public class XMLExporter
{
  /**
   * The DOM <code>document</code> that is populated with data from the core.
   */
  private Document                        document;

  /**
   * The root of the parsed <code>schema</code>, used to determine the correct
   * sequence of tags in a definition.
   */
  private Element                         schema;

  /**
   * The <code>root</code> element of the DOM document.
   */
  private Element                         root;

  /**
   * <code>elementMap</code> stores id-->element mappings. Used for quick access
   * to an element if additional information needs to be added.
   */
  private Hashtable<String, Element>      elementMap;

  /**
   * <code>attributeMap</code> stores Element-->id mappings. When an <code>&lt;
   * md_attribute></code> tag is created, it is attached as a child to the
   * <code>&lt;
   * definitions></code> tag of the defining <code>&lt;object</code>. This map
   * allows the elements to be attached to the appropriate parents before
   * writing to the file.
   */
  private Hashtable<Element, String>      attributeMap;

  /**
   * When the correct order of tags in an attribute definition is built,
   * <code>attributeOrder</code> stores a type-->order mapping, so that future
   * tags of the same type don't need to rebuild their order.
   */
  private Hashtable<String, List<String>> attributeOrder;

  /**
   * Indicates whether or not duplicate instances of the same ID are allowed.
   * Documents with duplicates will not validate on schemas that enforce
   * referential integrity.
   */
  private boolean                         allowDuplicates;

  /**
   * <code>ids</code> contains every id that has been added to the Document.
   * Used when detecting duplicates. <code><b>false</b></code> by default.
   */
  private TreeSet<String>                 ids;

  /**
   * Initializes the <code>document</code>, creates the <code>root</code>
   * element, and parses the <code>schema</code>.
   * 
   * @param schemaFile
   *          A path String that points to the schema for this XMLExporter
   */
  public XMLExporter(String schemaFile)
  {
    // This call serves only to garauntee that the system is initialized
    // ObjectCache.getMdBusiness(Attribute.BOOLEAN);

    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.newDocument();
      
      
      InputSource resolved = new RunwayClasspathEntityResolver().resolveEntity(null, schemaFile);
      
      if (resolved != null) {
        schema = builder.parse(resolved).getDocumentElement();
      }
      else {
        schema = builder.parse(schemaFile).getDocumentElement();
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

    elementMap = new Hashtable<String, Element>();

    attributeMap = new Hashtable<Element, String>();
    attributeOrder = new Hashtable<String, List<String>>();

    ids = new TreeSet<String>();
    allowDuplicates = false;

    root = document.createElement("Runway");
    root.setAttribute("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE);
    document.appendChild(root);
    long time = System.currentTimeMillis();
    buildInheritance();
    System.out.println("Time to build inheritance: " + ( System.currentTimeMillis() - time ));
  }

  public static void main(String[] args)
  {
    try
    {
      if (args.length != 2)
      {
        String errMsg = "Two arguments are required for XMLExporter:\n" + "  1) metadata XSD file path\n" + "  2) metadata XML file path";
        throw new CoreException(errMsg);
      }

      String schemaFile = args[0];
      String exportFile = args[1];

      exportAllMetadata(schemaFile, exportFile);
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }
  
  public static void exportAllMetadata(String schemaFile, String exportFile)
  { 
    XMLExporter xmlExporter = new XMLExporter(schemaFile);

    List<MdEntityDAOIF> rootEntityList = MdEntityDAO.getRootEntities();
    
    for(MdEntityDAOIF mdEntityIF : rootEntityList)
    {      
      QueryFactory queryFactory = new QueryFactory();
      EntityQuery entityQuery = queryFactory.entityQueryDAO(mdEntityIF);

      OIterator<? extends ComponentIF> componentIterator = entityQuery.getIterator();
      while (componentIterator.hasNext())
      {
        xmlExporter.add(componentIterator.next());
      }
    }
  
    xmlExporter.writeToFile(exportFile);
  }
  
  /**
   * Sets whether or not duplicate ids are permissible in this document.
   * 
   * @param allow
   */
  public void setAllowDuplicates(boolean allow)
  {
    allowDuplicates = allow;
  }

  /**
   * Adds data to the <code>document</code> for each element in the
   * <code>List</code>. The <code>List</code> does not need to be homogenous,
   * and accepts references (Strings), BusinessDAOs, and Relationships
   * (Enumerations soon). If an object is not of the accepted types, a
   * DataAccessException is thrown.
   * 
   * @param objects
   *          <code>List</code> of references, BusinessDAOs, Relationships, and
   *          Enumerations
   */
  public void add(List<? extends Object> objects)
  {
    for (Object next : objects)
    {
      this.addComponent(next);
    }
  }

  /**
   * Adds data to the <code>document</code> for each element in the
   * <code>List</code>. The <code>List</code> does not need to be homogenous,
   * and accepts references (Strings), BusinessDAOs, and Relationships
   * (Enumerations soon). If an object is not of the accepted types, a
   * DataAccessException is thrown.
   * 
   * @param objects
   *          <code>List</code> of references, BusinessDAOs, Relationships, and
   *          Enumerations
   */
  public void addComponent(Object object)
  {
    if (object instanceof ComponentIF)
    {
      add((ComponentIF) object);
    }
    else if (object instanceof String)
    {
      add((String) object);
    }
    else
    {
      String error = "[" + object.getClass().getName() + "] Objects are not supported by XMLExporter"
          + " - pass in only Components or Strings (ids).";
      throw new XMLException(error);
    }
  }

  /**
   * Adds a Component to the <code>document</code>. Handles the special case of
   * <code>
   * MD_ATTRIBUTE</code>, as well as any BusinessDAO or Relationship instance.
   * 
   * @param component
   *          The object being added to the <code>document</code>
   */
  public void add(ComponentIF component)
  {
    if (component instanceof BusinessDAO)
    {
      BusinessDAO businessDAO = (BusinessDAO) component;
      if (businessDAO instanceof MdAttributeConcreteDAOIF)
      {
        addAttributeDefinition((MdAttributeConcreteDAOIF) businessDAO);
      }
      else
      {
        addObject(businessDAO, this.root, "object");
      }
    }
    else if (component instanceof StructDAO)
    {
      StructDAO structDAO = (StructDAO) component;

      // Do not export a struct if it is part of a localized attribute
      if (! ( structDAO.getMdStructDAO() instanceof MdLocalStructDAO ))
      {
        addObject((StructDAO) component, this.root, "object");
      }
    }
    else if (component instanceof RelationshipDAO)
    {
      addRelationship((RelationshipDAO) component);
    }
    else
    {
      String error = "[" + component.getClass().getName() + "] Objects are not supported by XMLExporter"
          + " - pass in only Components or Strings (ids).";
      throw new XMLException(error);
    }
  }

  /**
   * Gets the instance of the <code>Component</code> referenced by the given
   * <code>id</code> and adds it to the <code>document</code>.
   * 
   * @param id
   *          of a Component in the database
   */
  public void add(String id)
  {
    EntityDAOIF entityDAOIF = EntityDAO.get(id);
    add(entityDAOIF);
    return;
  }

  /**
   * Generates the approriate tags for the given <code>EntityDAO</code> and adds
   * them to the <code>document</code>.
   * 
   * @param entityDAO
   *          A EntityDAO instance
   */
  private void addObject(EntityDAO entityDAO, Element entityElement, String tagName)
  {
    String id = entityDAO.getId();
    checkID(id);

    Element objectTag = document.createElement(tagName);
    elementMap.put(id, objectTag);
    entityElement.appendChild(objectTag);

    Element classType = document.createElement(EntityInfo.TYPE);
    classType.appendChild(document.createTextNode(entityDAO.getType()));
    objectTag.appendChild(classType);

    Element idTag = document.createElement(EntityInfo.ID);
    idTag.appendChild(document.createTextNode(id));
    objectTag.appendChild(idTag);

    Element attributesTag = document.createElement("attributes");
    objectTag.appendChild(attributesTag);

    addAttributes(attributesTag, entityDAO.getAttributeArrayIF());
  }

  /**
   * Generates the approriate tags for the given <code>Relationship</code> and
   * adds them to the <code>document</code>.
   * 
   * @param relationship
   *          A Relationship instance
   */
  private void addRelationship(RelationshipDAO relationship)
  {
    checkID(relationship.getId());

    Element relationshipTag;
    if (relationship.getType().equals(RelationshipTypes.ENTITY_INDEX.getType()))
    {
      relationshipTag = document.createElement("entityIndex");
    }
    else
    {
      relationshipTag = document.createElement("relationship");
    }

    root.appendChild(relationshipTag);

    Element idElement = document.createElement("id");
    idElement.appendChild(document.createTextNode(relationship.getId()));
    relationshipTag.appendChild(idElement);

    Element parentIdTag = document.createElement(RelationshipInfo.PARENT_ID);
    parentIdTag.appendChild(document.createTextNode(relationship.getParentId()));
    relationshipTag.appendChild(parentIdTag);

    Element childIdTag = document.createElement(RelationshipInfo.CHILD_ID);
    childIdTag.appendChild(document.createTextNode(relationship.getChildId()));
    relationshipTag.appendChild(childIdTag);

    Element attributesTag = document.createElement("attributes");
    relationshipTag.appendChild(attributesTag);

    addAttributes(attributesTag, relationship.getAttributeArrayIF());

    if (relationship.getType().equals(RelationshipTypes.ENTITY_INDEX.getType()))
    {
      MdEntityDAOIF mdEntityIF = (MdEntityDAOIF) relationship.getParent();
      MdIndexDAOIF mdIndexIF = (MdIndexDAOIF) relationship.getChild();

      Element tableNameTag = document.createElement(MdElementInfo.TABLE_NAME);
      tableNameTag.appendChild(document.createTextNode(mdEntityIF.getTableName()));
      relationshipTag.appendChild(tableNameTag);

      Element indexNameTag = document.createElement(MdIndexInfo.INDEX_NAME);
      indexNameTag.appendChild(document.createTextNode(mdIndexIF.getIndexName()));
      relationshipTag.appendChild(indexNameTag);

      Element uniqueValueTag = document.createElement(MdIndexInfo.UNIQUE);
      uniqueValueTag.appendChild(document.createTextNode(mdIndexIF.getUniqueValue()));
      relationshipTag.appendChild(uniqueValueTag);

      Element indexedAttributesTag = document.createElement("indexedColumns");
      relationshipTag.appendChild(indexedAttributesTag);

      List<MdAttributeConcreteDAOIF> indexedAttributeList = mdIndexIF.getIndexedAttributes();

      for (MdAttributeConcreteDAOIF mdAttributeIF : indexedAttributeList)
      {
        Element columnNameTag = document.createElement("columnName");
        columnNameTag.appendChild(document.createTextNode(mdAttributeIF.getColumnName()));
        indexedAttributesTag.appendChild(columnNameTag);
      }
    }

  }

  private void addAttributes(Element attributesTag, AttributeIF[] attributeArray)
  {
    for (AttributeIF attribute : attributeArray)
    {
      Element attributeTag;

      if (attribute instanceof AttributeStruct)
      {
        attributeTag = document.createElement("attributeStruct");
      }
      else
      {
        attributeTag = document.createElement("attribute");
      }

      attributesTag.appendChild(attributeTag);

      Element nameTag = document.createElement("name");
      nameTag.appendChild(document.createTextNode(attribute.getName()));
      attributeTag.appendChild(nameTag);

      // A quick condition to access Blobs correctly
      String value;
      if (attribute instanceof AttributeBlob)
      {
        value = Base64.encodeToString( ( (AttributeBlob) attribute ).getBlobAsBytes(), false);
      }
      else if (attribute instanceof AttributeSymmetric)
      {
        value = Base64.encodeToString(attribute.getRawValue().getBytes(), false);
      }
      else if (attribute.getDefiningClassType().equals(ActorDAOIF.CLASS)
          && attribute.getName().equals(ElementInfo.SITE_MASTER))
      {
        // The site attribute on the user class must be subsituted
        // with the domain tag so that it automatically changed to the
        // system properties defined domain during import.
        value = XMLImporter.DOMAIN_TAG;
      }
      else if (attribute.getDefiningClassType().equals(EntityTypes.PHONE_NUMBER.getType())
          && attribute.getName().equals(ElementInfo.SITE_MASTER))
      {
        // The site attribute on the phone number class must be subsituted
        // with the domain tag so that it automatically changed to the
        // system properties defined domain during import.
        value = XMLImporter.DOMAIN_TAG;
      }
      else if (attribute.getName().equals(ElementInfo.SITE_MASTER))
      {
        // All metadata that defines metadata should be mastered at runwaysdk
        value = "www.runwaysdk.com";
      }
      else
      {
        value = getAttributeValue(attribute);
      }

      // if (attribute.getName().equals("baseSource") ||
      // attribute.getName().equals("stubSource") ||
      // attribute.getName().equals("stubDTOsource") ||
      // attribute.getName().equals("querySource") ||
      // attribute.getName().equals("queryBaseSource") ||
      // attribute.getName().equals("queryDTOsource") ||
      // attribute.getName().equals("queryStubSource") ||
      // attribute.getName().equals("dtoSource") ||
      // attribute.getName().equals("baseClass") ||
      // attribute.getName().equals("serverClasses") ||
      // attribute.getName().equals("stubClass") ||
      // attribute.getName().equals("stubDTOclass") ||
      // attribute.getName().equals("queryClass") ||
      // attribute.getName().equals("commonClasses") ||
      // attribute.getName().equals("clientClasses") ||
      // attribute.getName().equals("queryBaseClass") ||
      // attribute.getName().equals("queryDTOclass") ||
      // attribute.getName().equals("queryStubClass") ||
      // attribute.getName().equals("queryDTOclass") ||
      // attribute.getName().equals("dtoClass") ||
      // attribute.getName().equals("stubClass")
      // )
      // {
      // value = "";
      // }

      Element valueTag = document.createElement("value");
      valueTag.appendChild(document.createTextNode(value));
      attributeTag.appendChild(valueTag);

      Element definingEntityTag = document.createElement("definingComponent");
      definingEntityTag.appendChild(document.createTextNode(attribute.getDefiningClassType()));
      attributeTag.appendChild(definingEntityTag);

      if (attribute instanceof AttributeEnumeration)
      {
        addEnumeration((AttributeEnumeration) attribute, attributeTag);
      }
      else if (attribute instanceof AttributeStruct)
      {
        addStruct((AttributeStruct) attribute, attributeTag);
      }
    }
  }

  /**
   * Handles the special case of <code>MdAttribute</code>s. <code>MdAttribute
   * </code> definitions
   * do not create new <code>&lt;object></code> tags like other
   * <code>EntityDAO<code>s.  Rather, they attach themselves to the <code>
   * &lt;definitions></code> tag of their parent type in the document.
   * 
   * @param mdAttribute
   */
  protected void addAttributeDefinition(MdAttributeConcreteDAOIF mdAttribute)
  {

    checkID(mdAttribute.getId());

    String typeName = mdAttribute.getType();
    String className = BusinessDAOFactory.getClassNameFromType(typeName);

    Element definitionTag = document.createElement(className.toLowerCase());

    if (!attributeOrder.containsKey(typeName))
    {
      attributeOrder.put(typeName, buildAttributeOrder(className.toLowerCase()));
    }

    // build map of set_ids and index_ids for the index type attribute
    for (String name : attributeOrder.get(typeName))
    {
      // MdAttributeEnumeration cache columns don't show up. We don't export
      // them anyway.
      if (name.trim().equals(""))
      {
        continue;
      }

      AttributeIF attribute = mdAttribute.getAttributeIF(name);
      Element attributeTag = document.createElement(attribute.getName());

      if (name.equals(MdAttributeConcreteInfo.DISPLAY_LABEL)
          || name.equals(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL)
          || name.equals(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL)
          || name.equals(MdAttributeConcreteInfo.DESCRIPTION))
      {
        AttributeLocalIF attributeLocalIF = (AttributeLocalIF) attribute;

        StructDAO structDAO = attributeLocalIF.getStructDAO();

        for (AttributeIF attributeFromStruct : structDAO.getNonSystemAttributes())
        {
          if (attributeFromStruct.getName().equals(EntityInfo.KEY))
          {
            continue;
          }

          Element displayLabelLocal = document.createElement(attributeFromStruct.getName());
          displayLabelLocal.appendChild(document.createTextNode(getAttributeValue(attributeFromStruct)));
          attributeTag.appendChild(displayLabelLocal);
        }

      }
      else
      {
        attributeTag.appendChild(document.createTextNode(getAttributeValue(attribute)));
      }

      definitionTag.appendChild(attributeTag);

      if (name.equals(MdAttributeConcreteInfo.INDEX_TYPE))
      {
        AttributeEnumeration index = (AttributeEnumeration) mdAttribute
            .getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
        Element indexType_cache = document.createElement(MdAttributeConcreteDAOIF.INDEX_TYPE_CACHE);
        definitionTag.appendChild(indexType_cache);
        addEnumeration(index, indexType_cache);
      }

      if (name.equals(MdAttributeConcreteInfo.SETTER_VISIBILITY))
      {
        AttributeEnumeration index = (AttributeEnumeration) mdAttribute
            .getAttributeIF(MdAttributeConcreteInfo.SETTER_VISIBILITY);
        Element indexType_cache = document
            .createElement(MdAttributeConcreteDAOIF.SETTER_VISIBILITY_CACHE);
        definitionTag.appendChild(indexType_cache);
        addEnumeration(index, indexType_cache);
      }

      if (name.equals(MdAttributeConcreteInfo.GETTER_VISIBILITY))
      {
        AttributeEnumeration index = (AttributeEnumeration) mdAttribute
            .getAttributeIF(MdAttributeConcreteInfo.GETTER_VISIBILITY);
        Element indexType_cache = document
            .createElement(MdAttributeConcreteDAOIF.GETTER_VISIBILITY_CACHE);
        definitionTag.appendChild(indexType_cache);
        addEnumeration(index, indexType_cache);
      }

      if (name.equals(MdAttributeHashInfo.HASH_METHOD))
      {
        AttributeEnumeration index = (AttributeEnumeration) mdAttribute
            .getAttributeIF(MdAttributeHashInfo.HASH_METHOD);
        Element hashCode_cache = document.createElement(MdAttributeHashDAOIF.HASH_METHOD_CACHE);
        definitionTag.appendChild(hashCode_cache);
        addEnumeration(index, hashCode_cache);
      }

    }

    String idString = mdAttribute.getAttributeIF(MdAttributeConcreteInfo.DEFINING_MD_CLASS).getValue();
    attributeMap.put(definitionTag, idString);
  }

  /**
   * @param attribute
   */
  private void addEnumeration(AttributeEnumeration attribute, Element parentElement)
  {
    Element enumerationTag = document.createElement("enumeration");
    parentElement.appendChild(enumerationTag);

    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();
    Element mdEnumerationTypeTag = document.createElement(EntityInfo.TYPE);

    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF) mdAttribute
        .getAttributeIF(MdAttributeEnumerationInfo.MD_ENUMERATION);
    MdEnumerationDAOIF mdEnumerationIF = (MdEnumerationDAOIF) attributeReferenceIF.dereference();
    mdEnumerationTypeTag.appendChild(document.createTextNode(mdEnumerationIF.definesEnumeration()));
    enumerationTag.appendChild(mdEnumerationTypeTag);

    Element setIdTag = document.createElement(MdEnumerationInfo.SET_ID);
    setIdTag.appendChild(document.createTextNode(getAttributeValue(attribute)));
    enumerationTag.appendChild(setIdTag);

    for (String id : attribute.getEnumItemIdList())
    {
      Element enumAttrIdTag = document.createElement(MdEnumerationInfo.ITEM_ID);
      enumAttrIdTag.appendChild(document.createTextNode(id));
      enumerationTag.appendChild(enumAttrIdTag);
    }
  }

  /**
   * @param attribute
   */
  private void addStruct(AttributeStruct attribute, Element parentElement)
  {
    addObject(attribute.getStructDAO(), parentElement, "struct");
  }

  /**
   * Checks an id to see if it has already been added to this document. If so
   * (and duplicates are not allowed), an exception is thrown.
   * 
   * @param id
   *          The id of an element to be added to the document.
   * @return <code>true</code> if the document does not already contain the id.
   */
  private boolean checkID(String id)
  {
    boolean newID = ids.add(id);
    if (allowDuplicates || newID)
    {
      return true;
    }

    String error = EntityInfo.ID + " [" + id + "] is already in the document.  If you want to "
        + "allow duplicate elements in your document, call setAllowDuplicates(true)";
    throw new XMLException(error);
  }

  /**
   * Builds the correct sequence of tags in attribute definitions for the given
   * attribute classType.
   * 
   * @param classType
   *          The type of the MD_ATTRIBUTE class whose sequence of tags is being
   *          built
   */
  private List<String> buildAttributeOrder(String classType)
  {
    Element sequence = getElementByName(schema, classType);
    String base = null;

    while (!sequence.getTagName().equalsIgnoreCase("xs:sequence"))
    {
      if (sequence.getTagName().equalsIgnoreCase("xs:extension"))
      {
        base = sequence.getAttribute("base");
      }
      Node sequenceFirstChild = sequence.getFirstChild();

      if (sequenceFirstChild != null)
      {
        sequence = (Element) sequenceFirstChild.getNextSibling();
      }
      else
      {
        break;
      }
    }

    List<String> order = new LinkedList<String>();
    if (base != null)
    {
      order.addAll(buildAttributeOrder(base));
    }

    NodeList xs_elements = sequence.getChildNodes();
    for (int i = 0; i < xs_elements.getLength(); i++)
    {
      if (xs_elements.item(i) instanceof Element)
      {
        order.add( ( (Element) xs_elements.item(i) ).getAttribute("name"));
      }
    }
    return order;
  }

  /**
   *
   */
  private void buildInheritance()
  {
    Element inheritanceTag = document.createElement("inheritance");
    root.appendChild(inheritanceTag);

    List<MdEnumerationDAOIF> mdEnumerationList = MdEnumerationDAO.getAllMdEnumerations();
    for (MdEnumerationDAOIF mdEnumerationIF : mdEnumerationList)
    {
      Element child = document.createElement(mdEnumerationIF.definesEnumeration());
      inheritanceTag.appendChild(child);
    }

    List<MdEntityDAOIF> rootEntityList = MdEntityDAO.getRootEntities();
    for (MdEntityDAOIF mdEntityIF : rootEntityList)
    {
      inheritanceTag.appendChild(buildInheritance(mdEntityIF.definesType()));
    }
  }

  /**
   * 
   * @param entityType
   * @return
   */
  private Element buildInheritance(String entityType)
  {
    Element child = document.createElement(entityType);

    MdEntityDAOIF mdEntityIF = MdEntityDAO.getMdEntityDAO(entityType);

    List<? extends MdEntityDAOIF> subMdEntityList = mdEntityIF.getSubClasses();
    for (MdEntityDAOIF subMdEntityIF : subMdEntityList)
    {
      child.appendChild(buildInheritance(subMdEntityIF.definesType()));
    }

    return child;
  }

  /**
   * Given an Element, returns the first <code>complexType</code> child node
   * with the specified <code>name</code> attribute.
   * 
   * @param source
   *          The parent Element of the search domain
   * @param name
   *          The desired value of the <code>name</code> attribute
   * @return The first Element with mathing <code>name</code> value
   */
  private Element getElementByName(Element parent, String name)
  {
    NodeList complexTypes = parent.getElementsByTagName("xs:complexType");

    for (int i = 0; i < complexTypes.getLength(); i++)
    {
      Element element = ( (Element) complexTypes.item(i) );
      if (element.getAttribute("name").equalsIgnoreCase(name))
      {
        return element;
      }
    }

    return null;
  }

  /**
   * Exports the <code>document</code> into an XML file, at the location
   * specified by <code>outFile</code>. If the file already exists, it will be
   * overwritten.
   * 
   * @param outFile
   */
  public void writeToFile(String outFile)
  {
    joinDefinitions();
    TransformerFactory tfactory = TransformerFactory.newInstance();

    try
    {
      Transformer serializer = tfactory.newTransformer();

      // Setup indenting to "pretty print"
      serializer.setOutputProperty(OutputKeys.INDENT, "yes");
      serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      StringWriter writer = new StringWriter();
      serializer.transform(new DOMSource(document), new StreamResult(writer));

      FileIO.write(new File(outFile), writer.toString());
    }
    catch (Exception e)
    {
      throw new XMLException(e);
    }
  }

  /**
   * Joins the <code>&lt;md_attribute></code> tags to their parent
   * <code>&lt;definitions>
   * </code> tags.
   */
  private void joinDefinitions()
  {
    for (Element attributeTag : attributeMap.keySet())
    {
      Element object = (Element) elementMap.get(attributeMap.get(attributeTag));

      if (object == null)
      {
        String attributeName = attributeTag.getElementsByTagName("attributeName").item(0)
            .getFirstChild().getNodeValue();
        String error = "The specified parent for mdAttribute [" + attributeName
            + "] is not in the document.";
        throw new XMLException(error);
      }

      NodeList tags = object.getElementsByTagName("definitions");
      Element definitionsTag;
      if (tags.getLength() == 0)
      {
        definitionsTag = document.createElement("definitions");
        object.insertBefore(definitionsTag, object.getElementsByTagName("attributes").item(0));
      }
      else
      {
        definitionsTag = (Element) tags.item(0);
      }

      definitionsTag.appendChild(attributeTag);
    }
  }

  private String getAttributeValue(AttributeIF attribute)
  {
    if (attribute.getName().equals("siteMaster"))
    {
      return "www.runwaysdk.com";
    }

    if (attribute instanceof AttributeBooleanIF)
    {
      if (!attribute.getValue().trim().equals(""))
      {
        return Integer.toString( ( (AttributeBooleanIF) attribute ).getBooleanValueInt());
      }
      else
      {
        return "";
      }
    }
    else
    {
      return attribute.getValue();
    }
  }
}
