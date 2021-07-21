/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.ElementDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.HasNoDatabaseColumn;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.TransactionItemDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeClob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.attributes.entity.AttributeMultiReference;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.io.instance.StringInstanceExporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.transaction.ActionEnumDAO;
import com.runwaysdk.dataaccess.transaction.TransactionItemDAO;
import com.runwaysdk.dataaccess.transaction.TransactionRecordDAO;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.system.metadata.MdAttribute;

public class EntityDAOFactory
{

  /**
   * Returns a List Strings for all IDs of EntityDAOs of the given entity type
   * or that are sub entities. CAUTION!!!! This stores all Ids in a List. If the
   * entity type has too many instances, this can eat up your memory in a hurry.
   * 
   * <br/>
   * <b>Precondition: </b> mdEntity != null
   * 
   * @param mdEntity
   * @return List Strings for all IDs of EntityDAOs of the given entity type or
   *         that are sub entities.
   */
  public static List<String> getEntityIds(MdEntityDAOIF mdEntity)
  {
    return getEntityIds(mdEntity.definesType());
  }

  /**
   * Returns a List of Strings for all IDs of BusinessDAOs of the given class or
   * that are subclasses. Gets the table name for the class by queriying the
   * database. CAUTION!!!! This stores all Ids in a List. If the entity type has
   * too many instances, this can eat up your memory in a hurry.
   * 
   * <br/>
   * <b>Precondition: </b> type != null <br/>
   * <b>Precondition: </b> !type().equals("") <br/>
   * 
   * @param type
   * @return LinkedList of Strings for all IDs of BusinessDAOs of the given
   *         class name or that are sub classes.
   */
  public static List<String> getEntityIds(String type)
  {
    return Database.getEntityIds(type);
  }

  /**
   * Returns the attribute objects from a single query row necessary to
   * instantiate an entity object.
   * 
   * @param columnInfoMap
   *          contains information about attributes used in the query
   * @param definedByTableClassMap
   *          sort of a hack. It is a map where the key is the oid of an
   *          MdAttribute and the value is the MdEntity that defines the
   *          attribute. This is used to improve performance.
   * @param MdAttributeIFList
   *          contains MdAttribute objects for the attributes used in this query
   * @param resultSet
   *          ResultSet object from a query.
   */
  protected static Map<String, Attribute> getAttributesFromQuery(Map<String, ColumnInfo> columnInfoMap, Map<String, MdTableClassIF> definedByTableClassMap, List<? extends MdAttributeConcreteDAOIF> MdAttributeIFList, ResultSet resultSet)
  {

    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    for (MdAttributeConcreteDAOIF mdAttributeIF : MdAttributeIFList)
    {
      MdTableClassIF mdTableClassIF = definedByTableClassMap.get(mdAttributeIF.getOid());

      if (mdTableClassIF == null)
      {
        mdTableClassIF = (MdTableClassIF) mdAttributeIF.definedByClass();
        definedByTableClassMap.put(mdAttributeIF.getOid(), mdTableClassIF);
      }

      String attributeName = mdAttributeIF.definesAttribute();
      String attributeNameSpace = mdTableClassIF.definesType();
      String attributeQualifiedName = attributeNameSpace + "." + attributeName;
      
      ColumnInfo columnInfo = columnInfoMap.get(attributeQualifiedName);

      if (columnInfo != null)
      {
        String columnAlias = columnInfo.getColumnAlias();
        Object columnValue = AttributeFactory.getColumnValueFromRow(resultSet, columnAlias, mdAttributeIF.getType(), false);

        Attribute attribute = AttributeFactory.createAttribute(mdAttributeIF.getKey(), mdAttributeIF.getType(), attributeName, mdTableClassIF.definesType(), columnValue);

        attributeMap.put(attributeName, attribute);

        if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
        {
          MdAttributeEnumerationDAOIF mdAttributeEnumIF = (MdAttributeEnumerationDAOIF) mdAttributeIF;
          AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;

          String cacheColumnName = mdAttributeEnumIF.getCacheColumnName();
          String cacheAttributeQualifiedName = attributeNameSpace + "." + cacheColumnName;
          ColumnInfo cacheColumnInfo = columnInfoMap.get(cacheAttributeQualifiedName);

          String cachedEnumerationMappings = (String) AttributeFactory.getColumnValueFromRow(resultSet, cacheColumnInfo.getColumnAlias(), MdAttributeCharacterInfo.CLASS, false);
          attributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
        }
        else if (mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF)
        {
          /*
           * Force a refresh of the item list. It is possible this needs to be
           * refactored as it causes another query to the database.
           */
          AttributeMultiReference attributeMultiReference = (AttributeMultiReference) attribute;
          attributeMultiReference.getItemIdList();
        }
        else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
        {
          MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
          MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
          List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

          Map<String, Attribute> structAttributeMap = new HashMap<String, Attribute>();
          for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
          {
            String structQualifiedAttributeName = attributeQualifiedName + "." + structMdAttributeIF.definesAttribute();
            ColumnInfo structColumnInfo = columnInfoMap.get(structQualifiedAttributeName);
            String structColumnAlias = structColumnInfo.getColumnAlias();
            String structColumnValue = (String) AttributeFactory.getColumnValueFromRow(resultSet, structColumnAlias, structMdAttributeIF.getType(), false);

            Attribute structAttribute = AttributeFactory.createAttribute(structMdAttributeIF.getKey(), structMdAttributeIF.getType(), structMdAttributeIF.definesAttribute(), mdStructIF.definesType(), structColumnValue);

            if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
            {
              AttributeEnumeration structAttributeEnumeration = (AttributeEnumeration) structAttribute;
              String cacheColumnName = ( (MdAttributeEnumerationDAOIF) structMdAttributeIF ).getCacheColumnName();
              String cacheAttributeQualifiedName = attributeQualifiedName + "." + cacheColumnName;

              ColumnInfo cacheColumnInfo = columnInfoMap.get(cacheAttributeQualifiedName);

              String cachedEnumerationMappings = (String) AttributeFactory.getColumnValueFromRow(resultSet, cacheColumnInfo.getColumnAlias(), MdAttributeCharacterInfo.CLASS, false);
              structAttributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
            }

            structAttributeMap.put(structMdAttributeIF.definesAttribute(), structAttribute);
          }

          StructDAO structDAO = null;
          Attribute idAttribute = structAttributeMap.get(EntityInfo.OID);
          if (!idAttribute.getValue().trim().equals(""))
          {
            structDAO = (StructDAO) StructDAOFactory.factoryMethod(structAttributeMap, mdStructIF.definesType());
          }
          else
          {
            structDAO = (StructDAO) StructDAO.newInstance(mdStructIF.definesType());
            structDAO.setIsNew(true);
            structDAO.setAppliedToDB(false);
          }

          ( (AttributeStruct) attribute ).setStructDAO(structDAO);
        }
      } // if (columnInfo != null)
      else if (mdAttributeIF instanceof HasNoDatabaseColumn)
      {
        Attribute attribute = AttributeFactory.createAttribute(mdAttributeIF.getKey(), mdAttributeIF.getType(), attributeName, mdTableClassIF.definesType(), "");
        attributeMap.put(attributeName, attribute);
      }
        
    }
    return attributeMap;
  }

  /**
   * 
   * @param row
   * @param columnAlias
   * @return
   */
  protected static byte[] getBlobColumnValueFromRow(ResultSet resultSet, String columnAlias)
  {
    byte[] columnValue = new byte[0];

    String columnAliasLowerCase = columnAlias.toLowerCase();

    try
    {
      if (resultSet.getObject(columnAliasLowerCase) instanceof byte[])
      {
        columnValue = (byte[]) resultSet.getObject(columnAliasLowerCase);
      }
    }
    catch (SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
    return columnValue;
  }

  /**
   * Returns a Map of Attribute objects for the BusinessDAO with the given OID
   * and type. It only returns attributes that are explicitly defined by the
   * given type.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> oid != null <br/>
   * <b>Precondition:</b> !oid.trim().equals("")
   * 
   * @param oid
   * @param type
   * @param tableName
   * @param relationshipAttributesHackMap
   *          this is a total hack. If the instance is a relationship, then
   *          return the parent_oid and child_oid values in this map.
   * @return <code>Map</code> of Attribute objects for the BusinessDAO with the
   *         given OID and type.
   */
  public static Map<String, Attribute> getAttributesForHardcodedMetadata(String oid, String type, String tableName, Map<String, String> relationshipAttributesHackMap, boolean rootClass)
  {
    return Database.getAttributesForHardcodedMetadataObject(oid, type, tableName, relationshipAttributesHackMap, rootClass);
  }

  /**
   * Returns a <code>Map</code> of Attribute objects for the given entity name,
   * not including attributes that are inherited from other entities. Attributes
   * are initialized to an empty String. If a default value is defined for the
   * attribute, it is assigned to the attribute.
   * 
   * <br/>
   * <b>Precondition:</b> mdEntityIF != null
   * 
   * @param mdEntityDAOIF
   *          Create Attribute objects for attributes defined by the given
   *          entity.
   * @return <code>Map</code> of Attribute objects for the given entity, not
   *         including attributes that are inherited from other entities
   */
  protected static Map<String, Attribute> createRecordsForEntity(MdEntityDAOIF mdEntityDAOIF)
  {
    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    // get the all attribute meta data for this class
    List<? extends MdAttributeConcreteDAOIF> mdAttributeList = mdEntityDAOIF.definesAttributes();

    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributeList)
    {
      Attribute attribute = EntityDAOFactory.createAttributeForEntity(mdEntityDAOIF, mdAttribute);
      attributeMap.put(attribute.getName(), attribute);
    }

    return attributeMap;
  }

  public static Attribute createAttributeForEntity(MdEntityDAOIF mdEntityDAOIF, MdAttributeConcreteDAOIF mdAttribute)
  {
    Attribute attribute;
    String attrName = mdAttribute.definesAttribute();

    // assign a default value (if any) as defined in the meta data
    String attrDefaultValue = mdAttribute.getAttributeInstanceDefaultValue();

    // Check for sessionDefaultValue

    // New enumeration attributes need a unique oid so they can map to the
    // MdEnumerationIF.getDatabaseTableName() table
    if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
    {
      String setOid = ServerIDGenerator.nextID();
      attribute = AttributeFactory.createAttribute(mdAttribute.getKey(), mdAttribute.getType(), attrName, mdEntityDAOIF.definesType(), setOid);

      AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;

      if (!attrDefaultValue.equals(""))
      {
        attributeEnumeration.setDefaultValue(attrDefaultValue);
      }
    }
    else if (mdAttribute instanceof MdAttributeMultiReferenceDAOIF)
    {
      String setOid = ServerIDGenerator.nextID();
      attribute = AttributeFactory.createAttribute(mdAttribute.getKey(), mdAttribute.getType(), attrName, mdEntityDAOIF.definesType(), setOid);

      AttributeMultiReference attributeMultiReference = (AttributeMultiReference) attribute;

      if (!attrDefaultValue.equals(""))
      {
        attributeMultiReference.setDefaultValue(attrDefaultValue);
      }
    }
    else if (mdAttribute instanceof MdAttributeMultiReferenceDAOIF)
    {
      String setOid = ServerIDGenerator.nextID();
      attribute = AttributeFactory.createAttribute(mdAttribute.getKey(), mdAttribute.getType(), attrName, mdEntityDAOIF.definesType(), setOid);

      AttributeMultiReference attributeMultiReference = (AttributeMultiReference) attribute;

      if (!attrDefaultValue.equals(""))
      {
        attributeMultiReference.setDefaultValue(attrDefaultValue);
      }
    }
    else if (mdAttribute instanceof MdAttributeStructDAOIF)
    {
      MdStructDAOIF mdStructIF = ( (MdAttributeStructDAO) mdAttribute ).getMdStructDAOIF();
      StructDAO structDAO = StructDAO.newInstance(mdStructIF.definesType());

      attribute = AttributeFactory.createAttribute(mdAttribute.getKey(), mdAttribute.getType(), attrName, mdEntityDAOIF.definesType(), attrDefaultValue);
      ( (AttributeStruct) attribute ).setStructDAO(structDAO);
    }
    else
    {
      attribute = AttributeFactory.createAttribute(mdAttribute.getKey(), mdAttribute.getType(), attrName, mdEntityDAOIF.definesType(), attrDefaultValue);
    }
    return attribute;
  }

  /**
   * Creates the given EntityDAO in the database. Records are inserted into the
   * database.
   * 
   * <br/>
   * <b>Precondition:</b> entityDAO != null
   * 
   * @param entityDAO
   *          EntityDAO to create in the database
   * @param validateRequired
   *          true if attributes should be checked for required values, false
   *          otherwise. StructDAOs used by struct attributes may or may not
   *          need required attributes validated.
   */
  public static void insert(EntityDAO entityDAO, boolean validateRequired)
  {
    boolean addedId = false;

    // Get the inheritance list for this class
    MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();
    List<? extends MdEntityDAOIF> superMdEntityList = mdEntityDAOIF.getSuperClasses();

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();

    // Insert records into tables for each class in the hierarchy for this
    // EntityDAO's class
    for (MdEntityDAOIF mdEntity : superMdEntityList)
    {
      addedId = false;

      List<? extends MdAttributeConcreteDAOIF> mdAttributeList = mdEntity.definesAttributes();

      List<String> columnNames = new LinkedList<String>();
      List<String> prepStmtVars = new LinkedList<String>();
      List<Object> values = new LinkedList<Object>();
      List<String> attributeTypes = new LinkedList<String>();

      for (MdAttributeConcreteDAOIF mdAttribute : mdAttributeList)
      {
        if (mdAttribute instanceof HasNoDatabaseColumn)
        {
          continue;
        }
        
        String fieldName = mdAttribute.definesAttribute();

        // A new attribute may have been added since this object was
        // instantiated.
        // Only apply the attribute if it exists in the entityDAO and is
        // defined
        // by the metadata.
        // Indicator attributes have no column representation in the database
        if (entityDAO.hasAttribute(fieldName) && !(mdAttribute instanceof MdAttributeIndicatorDAOIF))
        {
          columnNames.add(mdAttribute.getColumnName());
          attributeTypes.add(mdAttribute.getType());

          // custom behavior for blobs
          if (mdAttribute instanceof MdAttributeBlobDAOIF)
          {
            byte[] blobBytes = null;
            AttributeBlob blobAttr = (AttributeBlob) entityDAO.getAttribute(mdAttribute.definesAttribute());
            blobBytes = blobAttr.getBlobAsBytes();

            prepStmtVars.add(blobAttr.getPreparedStatementVar());
            values.add(blobBytes);
          }
          else
          {
            Attribute attribute = entityDAO.getAttribute(mdAttribute.definesAttribute());

            Object inputValue = attribute.getRawValueObject();
            prepStmtVars.add(attribute.getPreparedStatementVar());
            values.add(inputValue);

            if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
            {
              AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;
              columnNames.add( ( (MdAttributeEnumerationDAOIF) mdAttribute ).getCacheColumnName());
              prepStmtVars.add(attribute.getPreparedStatementVar());
              values.add(attributeEnumeration.getCachedEnumItemIds());
              attributeTypes.add(MdAttributeClobInfo.CLASS);
            }
          }

          if (fieldName.equalsIgnoreCase(EntityInfo.OID))
          {
            addedId = true;
          }
        } // if (entityDAO.hasAttribute(fieldName) && !(mdAttribute instanceof MdAttributeIndicatorDAOIF))
      }

      if (addedId == false)
      {
        columnNames.add(EntityDAOIF.ID_COLUMN);
        prepStmtVars.add("?");
        values.add(entityDAO.getOid());
        attributeTypes.add(MdAttributeUUIDInfo.CLASS);
        addedId = true;
      }

      if (entityDAO instanceof RelationshipDAO)
      {
        RelationshipDAO relationshipDAO = (RelationshipDAO) entityDAO;
        columnNames.add(RelationshipDAOIF.PARENT_OID_COLUMN);
        prepStmtVars.add("?");
        values.add(relationshipDAO.getParentOid());
        attributeTypes.add(MdAttributeUUIDInfo.CLASS);
        columnNames.add(RelationshipDAOIF.CHILD_OID_COLUMN);
        prepStmtVars.add("?");
        values.add(relationshipDAO.getChildOid());
        attributeTypes.add(MdAttributeUUIDInfo.CLASS);
      }

      PreparedStatement preparedStmt = Database.buildPreparedSQLInsertStatement(mdEntity.getTableName(), columnNames, prepStmtVars, values, attributeTypes);
      preparedStatementList.add(preparedStmt);
    }

    try
    {
      Database.executeStatementBatch(preparedStatementList);
    }
    catch (DuplicateGraphPathException duplicateGraphPathException)
    {
      RelationshipDAO relationshipDAO = (RelationshipDAO) entityDAO;
      duplicateGraphPathException.init(relationshipDAO.getMdRelationshipDAO(), relationshipDAO.getParentOid(), relationshipDAO.getChildOid());
      throw duplicateGraphPathException;
    }
    catch (DuplicateDataDatabaseException duplicateDataDatabaseException)
    {
      // Check to see if this is an exception due to an OID primary key violation
      if (!duplicateDataDatabaseException.isIdPrimaryKeyViolation())
      {
        throw duplicateDataDatabaseException;
      }
      // An OID primary key violation occurred, check to see if it was the result
      // of a duplicate
      // key value. The error message should display the key value, rather than
      // the OID as that would make
      // more sense to the end user.
      else
      {
        String oid = entityDAO.getOid();
        String keyValue = entityDAO.getKey();

        // There was a duplicate OID violation because the OID was derived
        // (hashed) from another record
        // with a duplicate key value. If no key value is supplied, then it is
        // given the oid. If the oid
        // and the key value are not equal, then the oid was hashed from the key
        // name
        if (!oid.equals(keyValue))
        {
          AttributeIF keyAttribute = entityDAO.getAttributeIF(ElementInfo.KEY);
          String msg = "Duplicate value on [" + mdEntityDAOIF.definesType() + "] for attribute [" + ElementInfo.KEY + "] with value [" + keyAttribute.getValue() + "]";

          List<MdAttributeDAOIF> attributeIFList = new LinkedList<MdAttributeDAOIF>();
          attributeIFList.add(keyAttribute.getMdAttribute());

          List<String> valueList = new LinkedList<String>();
          valueList.add(keyValue);

          throw new DuplicateDataException(msg, mdEntityDAOIF, attributeIFList, valueList);
        }
        else
        {
          throw duplicateDataDatabaseException;
        }
      }
    }
    if (ServerProperties.logTransactions() && mdEntityDAOIF.isExported())
    {
      logTransactionItem(entityDAO, ActionEnumDAO.CREATE);
    }
  }

  /**
   * Logs a transaction for the given applied entityDAO.
   * 
   * <br>
   * precondition:</br> Method is called within a transaction, or else a null
   * pointer exception will occur if logging transactions is enabled.
   * 
   * @param entityDAOIF
   * @param actionEnumDAO
   */
  public static void logTransactionItem(EntityDAOIF entityDAOIF, ActionEnumDAO actionEnumDAO)
  {
    EntityDAOFactory.logTransactionItem(entityDAOIF, actionEnumDAO, false);
  }

  public static void logTransactionItem(EntityDAOIF entityDAOIF, ActionEnumDAO actionEnumDAO, boolean ignoreSequenceNumber)
  {
    if (TransactionRecordDAO.shouldLogEntity(entityDAOIF))
    {
      ElementDAOIF elementDAOIF = (ElementDAOIF) entityDAOIF;

      TransactionRecordDAO transactionRecordDAO = TransactionRecordDAO.getCurrentTransactionRecord();

      String exportXML;

      if (actionEnumDAO == ActionEnumDAO.CREATE || actionEnumDAO == ActionEnumDAO.UPDATE)
      {
        exportXML = StringInstanceExporter.export(CommonProperties.getInstanceXMLschemaLocation(), elementDAOIF, true);
      }
      else
      {
        exportXML = "";
      }

      TransactionItemDAO transactionItemDAO = TransactionItemDAO.newInstance();
      transactionItemDAO.setComponentId(elementDAOIF.getOid());
      transactionItemDAO.setComponentSeq(elementDAOIF.getSequence());
      transactionItemDAO.setComponentSiteMaster(elementDAOIF.getSiteMaster());
      transactionItemDAO.setXMLRecord(exportXML);
      transactionItemDAO.setItemAction(actionEnumDAO);
      transactionItemDAO.setTransactionRecordDAOIF(transactionRecordDAO);
      transactionItemDAO.setIgnoreSequenceNumber(ignoreSequenceNumber);
      transactionItemDAO.apply();
    }
  }

  /**
   * Since metadata artifacts are not generated until the very end of the
   * transaction, the
   * 
   * <code>TransactionItemDAO</code> XML needs to be rewritten to include the
   * generated artifacts.
   * 
   */
  public static void relogMetaDataTransaction(MdTypeDAOIF mdTypeDAOIF)
  {
    if (!ServerProperties.logTransactions())
    {
      return;
    }

    if (TransactionRecordDAO.shouldLogEntity(mdTypeDAOIF))
    {
      TransactionRecordDAO transactionRecordDAO = TransactionRecordDAO.getCurrentTransactionRecord();

      TransactionItemDAOIF transactionItemDAOIF = transactionRecordDAO.getTransactionItem(mdTypeDAOIF.getOid(), mdTypeDAOIF.getSequence());

      if (transactionItemDAOIF == null)
      {
        return;
      }

      // No need to relog if we are deleting the metadata anyway
      ActionEnumDAO actionEnumDAO = transactionItemDAOIF.getItemAction();
      if (actionEnumDAO == ActionEnumDAO.DELETE)
      {
        return;
      }

      String exportXML = StringInstanceExporter.export(CommonProperties.getInstanceXMLschemaLocation(), mdTypeDAOIF, true);

      TransactionItemDAO transactionItemDAO = transactionItemDAOIF.getBusinessDAO();
      transactionItemDAO.setXMLRecord(exportXML);
      // Do not call the save or apply method, as that will increment the
      // sequence number
      // for the transaction item object. This will change the ordering of the
      // transaction items
      // in the transaction which would be very very very bad.
      transactionItemDAO.updateXMLInDatabase();
    }
  }

  /**
   * Updates changes made to attributes of the given EntityDAO in the database.
   * 
   * <br/>
   * <b>Precondition:</b> entityDAO != null
   * 
   * @param entityDAO
   *          EntityDAO to update in the database
   * @param validateRequired
   *          true if attributes should be checked for required values, false
   *          otherwise. StructDAOs used by struct attributes may or may not
   *          need required attributes validated.
   * @param incrementSequence
   *          true if the sequence number needs to be incremented, false
   *          otherwise.
   */
  public static void update(EntityDAO entityDAO, boolean validateRequired)
  {
    String existingId = entityDAO.getOid();

    long oldSeq = 0;

    Attribute sequenceNumber = null;

    if (entityDAO instanceof ElementDAO)
    {
      oldSeq = ( (ElementDAO) entityDAO ).getSequence();
      sequenceNumber = entityDAO.getAttribute(ElementInfo.SEQUENCE);

      // update the entity object with the new sequence number
      if (!entityDAO.isImport() &&
      // update the sequence number during import resolution if the object is
      // mastered here
          ( !entityDAO.isImportResolution() || ( entityDAO.isImportResolution() && entityDAO.isMasteredHere() ) ))
      {
        // With changing predictive IDs, we will update the sequence number
        // anyway, even if it is not mastered here. This
        // will create stale entity exceptions to prevent objects with older and
        // out of date ids from being applied.
        // if (entityDAO.isMasteredHere())
        // {
        String nextSequence = Database.getNextSequenceNumber();
        sequenceNumber.setValue(nextSequence);
        // }
      }
    }

    MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
    List<? extends MdEntityDAOIF> superMdEntityList = mdEntityDAOIF.getSuperClasses();

    int count = 1;

    boolean addedId = false;

    for (MdEntityDAOIF currMdEntity : superMdEntityList)
    {
      addedId = false;

      List<String> columnNames = new LinkedList<String>();
      List<String> prepStmtVars = new LinkedList<String>();
      List<Object> values = new LinkedList<Object>();
      List<String> attributeTypes = new LinkedList<String>();

      PreparedStatement preparedStmt = null;

      List<? extends MdAttributeConcreteDAOIF> mdAttributeList = currMdEntity.definesAttributes();

      for (MdAttributeConcreteDAOIF mdAttribute : mdAttributeList)
      {
        if (mdAttribute instanceof HasNoDatabaseColumn)
        {
          continue;
        }
        
        String attrName = mdAttribute.definesAttribute();

        // A new attribute may have been added since this object was
        // instantiated.
        // Only apply the attribute if it exists in the entityDAO and is defined
        // by the metadata.
        // Indicator attributes have no column representation in the database
        if (entityDAO.hasAttribute(attrName)  && !(mdAttribute instanceof MdAttributeIndicatorDAOIF))
        {
          Attribute attribute = entityDAO.getAttribute(attrName);

          // Blobs and Clobs have been updated directly to the database anyway
          if (attribute.isModified() && ! ( attribute instanceof AttributeBlob ) && ! ( attribute instanceof AttributeClob ))
          {
            // value = Database.formatJavaToSQL(value, mdAttribute.getType(),
            // false);
            columnNames.add(mdAttribute.getColumnName());
            attributeTypes.add(mdAttribute.getType());

            prepStmtVars.add(attribute.getPreparedStatementVar());

            Object value = attribute.getRawValueObject();
            values.add(value);

            if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
            {
              AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;
              columnNames.add( ( (MdAttributeEnumerationDAOIF) mdAttribute ).getCacheColumnName());
              prepStmtVars.add(attribute.getPreparedStatementVar());
              values.add(attributeEnumeration.getCachedEnumItemIds());
              attributeTypes.add(MdAttributeClobInfo.CLASS);
            }
          }

          if (attrName.equalsIgnoreCase(EntityInfo.OID))
          {
            addedId = true;
          }
        } // if (entityDAO.hasAttribute(attrName))
      }

      if (!addedId)
      {
        columnNames.add(EntityDAOIF.ID_COLUMN);
        prepStmtVars.add("?");
        values.add(entityDAO.getOid());
        attributeTypes.add(MdAttributeUUIDInfo.CLASS);
        addedId = true;
      }

      if (columnNames.size() != 0)
      {
        // check if we're at the root entity (the last element)
        if (count == superMdEntityList.size() && ! ( entityDAO instanceof StructDAO ))
        {
          // On an import do not change the sequence number, increase the global
          // sequence counter, or check for stale objects
          if (!entityDAO.isImport())
          {
            preparedStmt = Database.buildPreparedSQLUpdateStatement(currMdEntity.getTableName(), columnNames, prepStmtVars, values, attributeTypes, existingId, oldSeq);
            preparedStatementList.add(preparedStmt);
          }
          else
          {
            preparedStmt = Database.buildPreparedSQLUpdateStatement(currMdEntity.getTableName(), columnNames, prepStmtVars, values, attributeTypes, existingId);
            preparedStatementList.add(preparedStmt);
          }
        }
        else
        {
          // Do not bother updating the table if only the oid is changing, but it
          // is being set to the same value it already is.
          if (! ( columnNames.size() == 1 && columnNames.get(0).equals(ComponentInfo.OID) && values.size() == 1 && values.get(0).equals(existingId) ))
          {
            preparedStmt = Database.buildPreparedSQLUpdateStatement(currMdEntity.getTableName(), columnNames, prepStmtVars, values, attributeTypes, existingId);
            preparedStatementList.add(preparedStmt);
          }
        }
      }
      count++;
    }

    // execute the batch and check for stale object exception by searching
    // for a value of 0 (e.g., an update statement had no effect).
    int[] batchResults = Database.executeStatementBatch(preparedStatementList);

    for (int i = 0; i < batchResults.length; i++)
    {
      if (batchResults[i] == 0)
      {
        String error = "Object [" + existingId + "] is stale and cannot be updated.";      
        throw new StaleEntityException(error, entityDAO);
      }
    }

    if (ServerProperties.logTransactions() && mdEntityDAOIF.isExported())
    {
      logTransactionItem(entityDAO, ActionEnumDAO.UPDATE);
    }
  }

  /**
   * Deletes records in the database that make up this EntityDAO. This method
   * performs DML operations against the database.
   * 
   * <br/>
   * <b>Precondition:</b> entityDAO != null
   * 
   * <br/>
   * <b>Postcondition:</b> entityDAO has been removed from the database.
   * 
   * @param entityDAO
   *          EntityDAO to delete
   */
  public static void delete(EntityDAO entityDAO)
  {
    MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();
    List<? extends MdEntityDAOIF> superMdEntityIF = mdEntityDAOIF.getSuperClasses();
    List<String> deleteStatements = new LinkedList<String>();
    String oid = entityDAO.getOid();

    // Delete the records in the database
    int count = 1;
    for (MdEntityDAOIF mdEntity : superMdEntityIF)
    {
      if (count == superMdEntityIF.size() && ( entityDAO instanceof ElementDAO ))
      {
        MdAttributeDAOIF mdAttr = mdEntity.definesAttribute(MdAttribute.SEQ);
        
        String seqColumnName = ElementDAOIF.SEQUENCE_COLUMN;
        if (mdAttr != null && mdAttr instanceof MdAttributeConcreteDAOIF)
        {
          seqColumnName = mdAttr.getColumnName();
        }
        
        long seq = ( (ElementDAO) entityDAO ).getSequence();
        deleteStatements.add(Database.buildSQLDeleteStatement(mdEntity.getTableName(), oid, seqColumnName, seq));
      }
      else
      {
        deleteStatements.add(Database.buildSQLDeleteStatement(mdEntity.getTableName(), oid));
      }
      count++;
    }

    int[] batchResults = Database.executeBatch(deleteStatements);

    // check for a stale object delete.
    for (int i = 0; i < batchResults.length; i++)
    {
      if (batchResults[i] == 0)
      {
        String type = entityDAO.getType();
        String key = entityDAO.getKey();
        String error = "Object with oid [" + oid + "] of type [" + type + "] with key [" + key + "] is stale and cannot be deleted.";
        throw new StaleEntityException(error, entityDAO);
      }
    }
  }

  /**
   * Returns a Map where the key is a parent of the given type and the value is
   * the database value..
   * 
   * <br/>
   * <b>Precondition:</b> mdEntityId != null <br/>
   * <b>Precondition:</b> !mdEntityId().equals("")
   * 
   * @param type
   * @return Map where the key is a parent of the given type and the value is
   *         the database value..
   */
  public static LinkedHashMap<String, String> getSuperEntityTypes(String type)
  {
    return Database.getSuperEntityTypes(type);
  }

  /**
   * 
   * @param type
   * @return
   */
  public static boolean typeExists(String type)
  {
    String typeName = getTypeNameFromType(type);
    String packageName = getPackageFromType(type);

    List<String> mdTypeFields = new LinkedList<String>();
    mdTypeFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdTypeFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdBusinessTable = new LinkedList<String>();
    mdBusinessTable.add(MdTypeDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();
    conditions.add(MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.TYPE_NAME_COLUMN + " = '" + typeName + "'");
    conditions.add(MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.PACKAGE_NAME_COLUMN + " = '" + packageName + "'");

    ResultSet resultSet = Database.query(Database.selectClause(mdTypeFields, mdBusinessTable, conditions));

    boolean returnResult = false;

    try
    {
      if (resultSet.next())
      {
        returnResult = true;
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnResult;
  }

  /**
   * Assumes that the type is valid with a package and a class name.
   * 
   * @param type
   * @return
   */
  public static String getTypeNameFromType(String type)
  {
    int startIndex = type.lastIndexOf(".") + 1;

    return type.substring(startIndex, type.length());
  }

  /**
   * Assumes that the type is valid with a package and a class name.
   * 
   * @param type
   * @return
   */
  public static String getPackageFromType(String type)
  {
    int endIndex = type.lastIndexOf(".");

    return type.substring(0, endIndex);
  }

  /**
   * Returns the oid to the MdEntity that defines the given type. given OID.
   * 
   * @param type
   * @return oid to the MdEntity that defines the given type.
   */
  public static String getMdEntityId(String mdEntityType)
  {
    return Database.getMdEntityId(mdEntityType);
  }

  /**
   * Checks if the given class name is a valid.
   * 
   * <br/>
   * <b>Precondition:</b> classType != null <br/>
   * <b>Precondition:</b> !classType.trim().equals("")
   * 
   * @param classType
   *          type of the class
   * @return True if given class name is valid, false otherwise
   */
  public static boolean isValidType(String classType)
  {
    return Database.isValidType(classType);
  }

  /**
   * 
   * @param typeName
   * @param packageName
   * @return
   */
  public static String buildType(String packageName, String typeName)
  {
    return packageName + "." + typeName;
  }

  /**
   * 
   * @return
   */
  public static List<String> getAllEntityNames()
  {
    List<String> mdBusinessFields = new LinkedList<String>();
    mdBusinessFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdBusinessFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdBusinessTable = new LinkedList<String>();
    mdBusinessTable.add(MdEntityDAOIF.TABLE);
    mdBusinessTable.add(MdTypeDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();
    conditions.add(MdEntityDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN);

    ResultSet resultSet = Database.query(Database.selectClause(mdBusinessFields, mdBusinessTable, conditions));

    List<String> returnList = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        String className = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
        String packageName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
        String type = buildType(packageName, className);
        returnList.add(type);
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnList;
  }

  /**
   * Changes all references to the given object to use the new oid. After this
   * method is called, the given <code>EntityDAO</code> will be assigned the new
   * OID.
   * 
   * @param entityDAO
   * @param oldId
   *          the old reference oid
   * @param newId
   *          the new oid that all of the references must point to
   */
  public static void floatObjectIdReferences(EntityDAO entityDAO, String oldId, String newId)
  {
    changeEntityId(entityDAO, oldId, newId);

    if (entityDAO instanceof BusinessDAO)
    {
      BusinessDAOFactory.floatObjectIdReferences((BusinessDAO) entityDAO, oldId, newId);
    }
  }

  /**
   * Changes the entity oid on just the entity itself and no other dependencies.
   *
   * @param entityDAO
   * @param oldId
   *          the old reference oid
   * @param newId
   *          the new oid that all of the references must point to
   */
  private static void changeEntityId(EntityDAO entityDAO, String oldId, String newId)
  {
    String existingId = oldId;

    MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
    List<? extends MdEntityDAOIF> superMdEntityList = mdEntityDAOIF.getSuperClasses();

    for (MdEntityDAOIF currMdEntity : superMdEntityList)
    {
      PreparedStatement preparedStmt = null;
      preparedStmt = Database.buildPreparedUpdateFieldStatement(currMdEntity.getTableName(), null, EntityDAOIF.ID_COLUMN, "?", oldId, newId, MdAttributeUUIDInfo.CLASS);
      preparedStatementList.add(preparedStmt);
    }

    // execute the batch and check for stale object exception by searching
    // for a value of 0 (e.g., an update statement had no effect).
    int[] batchResults = Database.executeStatementBatch(preparedStatementList);

    for (int i = 0; i < batchResults.length; i++)
    {
      if (batchResults[i] == 0)
      {
        String error = "Object [" + existingId + "] is stale and cannot be updated.";
        throw new StaleEntityException(error, entityDAO);
      }
    }

    entityDAO.setNewIdApplied(true);
  }
}
