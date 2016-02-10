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
/**
 * Created on Aug 15, 2005
 *
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.ColumnDDLCommand;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DropColumnDDLCommand;
import com.runwaysdk.dataaccess.database.MetadataDisplayLabelDDLCommand;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.transaction.AbortIfProblem;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;


/**
 * @author nathan
 *
 */
public class MdAttributeConcrete_E extends MdAttributeConcreteStrategy
{
  /**
   *
   */
  private static final long serialVersionUID = 4531802008791787181L;
  /**
   * The database vendor formated column datatype.
   */
  protected String dbColumnType;

  /**
   * @param mdAttribute
   */
  public MdAttributeConcrete_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
    this.dbColumnType = this.getDbColumnType();
  }

  /**
   * Returns the formated DB column type used in the database in the syntax
   * of the current DB vendor.
   *
   * @return formated DB column type used in the database in the syntax of
   *         the current DB vendor.
   */
  protected String getDbColumnType()
  {
    return DatabaseProperties.getDatabaseType(this.getMdAttribute());
  }

  /**
   * Returns the MdEntityIF that defines this MdAttribute.
   *
   * @return the MdEntityIF that defines this MdAttribute.
   */
  public MdEntityDAOIF definedByClass()
  {
    return (MdEntityDAOIF) this.getMdAttribute().definedByClass();
  }

  protected void preSaveValidate()
  {
    super.preSaveValidate();

    if (this.getMdAttribute().isNew())
    {
      MdEntityDAOIF definingEntity = this.definedByClass();
      String definesAttribute = this.getMdAttribute().definesAttribute();
      String attributeId = this.getMdAttribute().getId();

      boolean preExistingAttribute = false;
      
      List<? extends MdAttributeConcreteDAOIF> attributeList = definingEntity.definesAttributes();
      for (MdAttributeConcreteDAOIF attribute : attributeList)
      {
        if (definesAttribute.equals(attribute.definesAttribute()) && !attribute.getId().equals(attributeId))
        {
          preExistingAttribute = true;
        }
      }
      
      if (!preExistingAttribute)
      {
        // Check to see if there is an existing attribute definition with the identical id that may have been overwritten
        // in the transaction cache with this newly created attribute, due to an identical key name value. We need to go to 
        // the database to see if an existing attribute definition exists.
        MdAttributeDAOIF possibleExistingMdAttributeDAOIF = (MdAttributeDAOIF)BusinessDAOFactory.get(this.getMdAttribute().getId());

        // A possible pre-existing attribute was found
        if (possibleExistingMdAttributeDAOIF != null)
        {
          String thisSequence = this.getMdAttribute().getAttributeIF(ElementInfo.SEQUENCE).getValue();
          String existingSequence = possibleExistingMdAttributeDAOIF.getAttributeIF(ElementInfo.SEQUENCE).getValue();
          
          // If the sequence numbers do not match, then there is a pre-existing attribute definition
          if (!thisSequence.equals(existingSequence))
          {
            preExistingAttribute = true;
          }
        }
      }

      if (preExistingAttribute)
      {
        String msg = "Cannot add an attribute named [" + definesAttribute + "] to class ["
            + definingEntity.definesType() + "] because that class already has defined an attribute with that name.";
        throw new DuplicateAttributeDefinitionException(msg, this.getMdAttribute(), definingEntity);
      }       
    }    

    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      // Supply a column name if one was not provided
      Attribute columnNameAttribute = this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME);
      if (!columnNameAttribute.isModified() || columnNameAttribute.getValue().trim().length() == 0)
      {
          // Automatically create a column name
        columnNameAttribute.setValue(this.autoGenCreateColumnName(this.getMdAttribute().definesAttribute()));
      }
      else
      {
        // Manually create a column name
        columnNameAttribute.setValue(columnNameAttribute.getValue().toLowerCase());
      }
    }
  }

  /**
   * Returns a column name generated from the given attribute name.  The attribute name is converted to
   * underscore case and truncated to the maximum column size.  If a column with the resultant name
   * already exists, then the last two characters are replaced with an underscore and a two digit number.
   *
   * @param attributeName
   *
   * @return
   */
  private String autoGenCreateColumnName(String attributeName)
  {
    if (attributeName.equals(ComponentInfo.ID))
    {
      return EntityDAOIF.ID_COLUMN;
    }

    String columnName = MetadataDAO.convertCamelCaseToUnderscore(this.getMdAttribute().definesAttribute());

    // Truncate if necessary
    if (columnName.length() > Database.MAX_ATTRIBUTE_NAME_SIZE)
    {
      columnName = columnName.substring(0, Database.MAX_ATTRIBUTE_NAME_SIZE);
    }

    String tableName = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();

    if (!Database.columnExists(columnName, tableName))
    {
      return columnName;
    }

    // The column exists, but check to see if it is marked to be deleted at the end of the transaction.

    TransactionState transactionState = TransactionState.getCurrentTransactionState();
    List<Command> notUndoableCommandList = transactionState.getNotUndoableCommandList();
    String ddlCommandDatabaseKey = ColumnDDLCommand.buildCurrentDatabaseColumnNameKey(tableName, columnName);
    for (Command command : notUndoableCommandList)
    {
      if (command instanceof DropColumnDDLCommand)
      {
        DropColumnDDLCommand dropColumnDDLCommand = (DropColumnDDLCommand)command;
        if (ddlCommandDatabaseKey.equals(dropColumnDDLCommand.getCurrentDatabaseColumnNameKey()) ||
            // A hashed column command may represent the a column of the same name
            ddlCommandDatabaseKey.equals(dropColumnDDLCommand.getDefinedColumnNameKey()))
        {
          // We will use the given column name, because the existing column will be deleted
          return columnName;
        }
      }
    }


    int maxColumnPrefix = Database.MAX_DB_IDENTIFIER_SIZE - 3;
    if (columnName.length() > maxColumnPrefix)
    {
      columnName = columnName.substring(0, maxColumnPrefix);
    }

    String tempColumnName = columnName;
    for (int i = 0; i < 1000; i++)
    {
      tempColumnName += i;
      if (!Database.columnExists(tempColumnName, tableName))
      {
        return tempColumnName;
      }
      tempColumnName = columnName;
    }

    // In this rare case, create a unique string for the table name
    columnName = ServerIDGenerator.nextID().toLowerCase();
    // prepend the table name with an A
    columnName = "a" + columnName.substring(1, Database.MAX_DB_IDENTIFIER_SIZE);

    return columnName;
  }

  /**
   * Validates this metadata object.
   *
   * @throws DataAccessException
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    if (this.getMdAttribute().isUnique() && !this.getMdAttribute().isRequired() && !Database.allowsUniqueNonRequiredColumns())
    {
      String error = "Attribute [" + this.getMdAttribute().definesAttribute() + "] on type ["
          + definedByClass().definesType()
          + "] cannot participate in a uniqueness constraint because it isn't required.";
      throw new RequiredUniquenessConstraintException(error, this.getMdAttribute());
    }

    if (this.getMdAttribute().hasAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE))
    {
      AttributeIF attribute = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.DEFAULT_VALUE);

      String value = attribute.getValue();

      if (attribute.isModified() && !value.trim().equals(""))
      {
        // Get the class that defines the MdAttribute class
        MdClassDAOIF mdClassIF = this.getMdAttribute().getMdClassDAO();

        Attribute spoofAttribute =
          AttributeFactory.createAttribute(this.getMdAttribute().getKey(), this.getMdAttribute().getType(), MdAttributeConcreteInfo.DEFAULT_VALUE,
              mdClassIF.definesType(), value);

        spoofAttribute.setContainingComponent(this.getMdAttribute());

        spoofAttribute.validate(this.getMdAttribute(), value);
      }
    }
  }

  /**
   * Adds a column in the database to the given table.
   *
   * <br/><b>Precondition:</b> tableName != null
   * <br/><b>Precondition:</b> !tableName.trim().equals("")
   *
   */
  @AbortIfProblem
  protected void createDbColumn(String tableName)
  {
    if(!this.appliedToDB)
    {
      // DDL cannot occur at runtime on the metadata display label table on databases that use a separate
      // DDL and DML connections.
      if (this.definedByClass().definesType().equals(EntityTypes.METADATADISPLAYLABEL.getType()) &&
          !Database.sharesDDLandDMLconnection())
      {
        new MetadataDisplayLabelDDLCommand(this.getMdAttribute()).doIt();
      }
      else
      {
        Database.addField(tableName, this.getMdAttribute().getColumnName(), this.dbColumnType, this.getMdAttribute());
      }
    }
  }

  /**
   * Modifies the column definition in the database to the given table, if such
   * a modification is necessary.
   *
   * <br/><b>Precondition:</b> tableName != null
   * <br/><b>Precondition:</b> !tableName.trim().equals("")
   *
   */
  protected void modifyDbColumn(String tableName) {}

  /**
   * Contains special logic for saving an attribute.
   */
  public void save()
  {
    this.validate();

    if (this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.NAME).isModified())
    {
      MdEntityDAOIF definingEntity = this.definedByClass();
      List<? extends MdAttributeConcreteDAOIF> attributeList = null;
      List<? extends MdEntityDAOIF> parentsList = definingEntity.getSuperClasses();

      // loop through parents and check for an attribute of the same name
      for (MdEntityDAOIF parent : parentsList)
      {
        attributeList = parent.definesAttributes();
        for (MdAttributeConcreteDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being compared with itself
          if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()) && !parent.definesType().equals(definingEntity.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class ["
                + definingEntity.definesType() + "] because its parent class ["+parent.definesType()+"] already defines an attribute with that name.";
            throw new DuplicateAttributeInInheritedHierarchyException(msg, this.getMdAttribute(), definingEntity, parent);
          }
        }
      }

      List<? extends MdEntityDAOIF> childrenList = definingEntity.getAllSubClasses();
      // loop through children and check for an attribute of the same name
      for (MdEntityDAOIF child : childrenList)
      {
        attributeList = child.definesAttributes();
        for (MdAttributeConcreteDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being compared with itself
          if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()) && !child.definesType().equals(definingEntity.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class ["
            + definingEntity.definesType() + "] because a child class ["+child.definesType()+"] already defines an attribute with that name.";
            throw new DuplicateAttributeDefinedInSubclassException(msg, this.getMdAttribute(), definingEntity, child);
          }
        }
      }
    }
    
    Attribute columnAttribute = this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME);

    if ((!this.getMdAttribute().isNew() || this.getMdAttribute().isAppliedToDB()) &&
        columnAttribute.isModified())
    {
      List<MdIndexDAOIF> mdIndexList = this.getMdAttribute().getMdIndecies();

      for (MdIndexDAOIF mdIndexDAOIF : mdIndexList)
      {
        MdIndexDAO mdIndexDAO = mdIndexDAOIF.getBusinessDAO();
        mdIndexDAO.apply();
      }
    }
    
    
    if (this.getMdAttribute().isAppliedToDB())
    {
      MdAttributeConcreteDAO mdAttributeConcreteDAO = this.getMdAttribute();
      Attribute keyAttribute = mdAttributeConcreteDAO.getAttribute(ComponentInfo.KEY);
      
      if (keyAttribute.isModified())
      {
        mdAttributeConcreteDAO.changeClassAttributeRelationshipKey();
      }
    }
    
    if (!this.getMdAttribute().isNew())
    {
      this.modifyAttribute();
    }
    else
    {
      //This attribute may have already been created
      this.createAttribute();
      this.refreshReferencingCachedEntityTypes();
    }
  }


  /**
   * Refreshes any cached types that reference a struct that defines this attribute.
   */
  private void refreshReferencingCachedEntityTypes()
  {
    MdEntityDAOIF definingMdEntityDAOIF = this.definedByClass();
    if (definingMdEntityDAOIF instanceof MdStructDAOIF )
    {
      MdStructDAOIF definingMdStructDAOIF = (MdStructDAOIF)definingMdEntityDAOIF;

      QueryFactory queryFactory = new QueryFactory();
      BusinessDAOQuery mdAttrStructQ = queryFactory.businessDAOQuery(MdAttributeStructInfo.CLASS);
      mdAttrStructQ.WHERE(mdAttrStructQ.aReference(MdAttributeStructInfo.MD_STRUCT).EQ(definingMdStructDAOIF.getId()));

      OIterator<BusinessDAOIF> mdAttrStructIerator = mdAttrStructQ.getIterator();

      try
      {
        for (BusinessDAOIF businessDAOIF : mdAttrStructIerator)
        {
          MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) businessDAOIF;

          MdClassDAOIF attrStructDefiningClass = mdAttributeStructIF.definedByClass();

          if (attrStructDefiningClass instanceof MdEntityDAOIF)
          {
            ObjectCache.updateParentCacheStrategy(((MdEntityDAO)attrStructDefiningClass).getBusinessDAO());
          }

        }
      }
      finally
      {
        mdAttrStructIerator.close();
      }
    }

  }

  /**
   * Makes corresponding DDL statements to the database for changes made to
   * attributes of an attribute BusinessDAO.
   *
   * <br/><b>Postcondition:</b> database schema changes are made, if
   * appropriate
   */
  private void modifyAttribute()
  {    
    // get the MdEntity that defines this attribute
    MdEntityDAOIF mdEntityIF = this.definedByClass();

    boolean rebuildIndexOnModifyColumn = Database.rebuildIndexOnModifyColumn();

    Set<String> appliedIndexes = new HashSet<String>();

    if (rebuildIndexOnModifyColumn)
    {
      cleanIndices(false);

      List<MdIndexDAOIF> mdIndexIFList = this.getMdAttribute().getMdIndecies();

      for (MdIndexDAOIF mdIndexIF : mdIndexIFList)
      {
        MdIndexDAO mdIndex = mdIndexIF.getBusinessDAO();
        if (mdIndex.existsInDatabase())
        {
          mdIndex.dropDatabaseIndex(false);
          appliedIndexes.add(mdIndex.getIndexName());
        }

      }
    }

    this.modifyDbColumn(mdEntityIF.getTableName());

    if (rebuildIndexOnModifyColumn)
    {
      createIndices();
      List<MdIndexDAOIF> mdIndexIFList = this.getMdAttribute().getMdIndecies();

      for (MdIndexDAOIF mdIndexIF : mdIndexIFList)
      {
        MdIndexDAO mdIndex = mdIndexIF.getBusinessDAO();

        if (appliedIndexes.contains(mdIndex.getIndexName()))
        {
          mdIndex.buildDatabaseIndex();
        }
      }
    }

    // No need to build the index if it was already rebuilt above.
    if (!rebuildIndexOnModifyColumn &&
        this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE).isModified())
    {
      /* Clean all previous indices */
      cleanIndices(false);
      createIndices();
    }
  }

  /**
   * creates an index if requested by the attribute
   */
  private void createIndices()
  {
    // Get the defining parent type
    MdEntityDAOIF parentMdEntity = this.definedByClass().getBusinessDAO();

    /* Check for the new requested kind of index (if any) */
    AttributeEnumerationIF index = (AttributeEnumerationIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
    String derefId = index.dereference()[0].getId();

    if (derefId.equalsIgnoreCase(IndexTypes.UNIQUE_INDEX.getId()) && !this.getMdAttribute().definesAttribute().equals(EntityInfo.ID))
    {
      // add a unique index
      String indexName = this.getMdAttribute().getIndexName();
      Database.addUniqueIndex(parentMdEntity.getTableName(), this.getMdAttribute().getColumnName(), indexName);
    }
    else if (derefId.equalsIgnoreCase(IndexTypes.NON_UNIQUE_INDEX.getId()))
    {
      // add a non unique index
      String indexName = this.getMdAttribute().getIndexName();
      Database.addNonUniqueIndex(parentMdEntity.getTableName(), this.getMdAttribute().getColumnName(), indexName);
    }
  }

  /**
   * Cleans (erases) all current indices of this attribute.
   * @param delete true if the index is being deleted, false otherwise.
   */
  private void cleanIndices(boolean delete)
  {
    // get the MdEntity that defines this attribute
    MdEntityDAOIF mdEntityIF = this.definedByClass();

    // create a list of attribute names
    List<String> attr = new LinkedList<String>();
    attr.add(this.getMdAttribute().definesAttribute());

    String indexName = this.getMdAttribute().getIndexName();

    if (Database.uniqueAttributeExists(mdEntityIF.getTableName(), this.getMdAttribute().getColumnName(), indexName))
    {
      // delete unique index
      Database.dropUniqueIndex(mdEntityIF.getTableName(), this.getMdAttribute().getColumnName(), indexName, delete);
    }
    else if (Database.nonUniqueAttributeExists(mdEntityIF.getTableName(), this.getMdAttribute().getColumnName(), indexName))
    {
      // delete non unique index
      Database.dropNonUniqueIndex(mdEntityIF.getTableName(), this.getMdAttribute().getColumnName(), indexName, delete);
    }
  }
  
  /**
   * Adds the appropriate column to and index (if appropriate) to the database.
   * Creates a relationship between this MdAttribute object and the MdEntity that
   * defines it.
   *
   * <br/><b>Postcondition:</b> database schema changes are made
   *
   */
  private void createAttribute()
  {
    // Get the defining parent type
    MdEntityDAO parentMdEntity = this.definedByClass().getBusinessDAO();

    this.createDbColumn(parentMdEntity.getTableName());

    //Do not create the CLASS_ATTRIBUTE relationship on imports,
    //because the relationship is included in the import file
    if(!this.getMdAttribute().isImport() && !this.appliedToDB)
    {
      parentMdEntity.addAttributeConcrete(this.getMdAttribute());
    }
   
    // Create a unique index on this attribute, if specified.
    if (this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE).isModified())
    {
      if (this.appliedToDB)
      {
        cleanIndices(false);
      }
      this.createIndices();
    }

    // Refresh all caches of classes that define or inherit this attribute
    ObjectCache.updateParentCacheStrategy(parentMdEntity);


    // If this attribute belongs to a structs, then refersh the cache for all classes that reference
    // the structs as a struct attribute.
    if (parentMdEntity instanceof MdStructDAOIF)
    {
      MdStructDAOIF mdStructIF = (MdStructDAOIF)parentMdEntity;
      List<? extends MdAttributeStructDAOIF> mdAttributeStructIFList = mdStructIF.getMdAttributeStruct();

      for (MdAttributeStructDAOIF mdAttributeStructIF : mdAttributeStructIFList)
      {
        MdClassDAO mdClassDAO = mdAttributeStructIF.definedByClass().getBusinessDAO();

        if(mdClassDAO instanceof MdEntityDAO)
        {
          ObjectCache.updateParentCacheStrategy((MdEntityDAO)mdClassDAO);
        }
      }

    }

  }

  /**
   * Deletes an attribute from runway. The <code>BusinessDAO</code> is deleted from the
   * database and removed from the cache. All relationships pertaining to this
   * <code>BusinessDAO</code> are also removed as well.
   *
   * <br/><b>Postcondition: </b> BusinessDAO and all dependencies are removed
   * from the runway <br/><b>Postcondition: </b> Coresponding column from
   * the defining table is dropped
   *
   * @param p_mdAttribute
   *          Attribute metadata BusinessDAO
   */
  public void delete()
  {
    //Delete all tuples this MdAttribute is part of
    this.dropTuples();

    boolean rebuildIndexOnModifyColumn = Database.rebuildIndexOnModifyColumn();

    if (rebuildIndexOnModifyColumn)
    {
      List<MdIndexDAOIF> mdIndexIFList = this.getMdAttribute().getMdIndecies();

      for (MdIndexDAOIF mdIndexIF : mdIndexIFList)
      {
        MdIndexDAO mdIndex = mdIndexIF.getBusinessDAO();
        mdIndex.dropDatabaseIndex(true);
      }
    }

    // get the MdEntity that defines this attribute
    MdEntityDAOIF mdEntityIF = this.definedByClass();

    // drop any indexes used on this attribute
    this.cleanIndices(true);

    MdEntityDAO mdEntity = mdEntityIF.getBusinessDAO();

    // Some dbs require that all tables have at least one column. Therefore, I
    // am making the assumption that if you are deleting the ID field, then you are also
    // dropping the defining type within the same transaction, which will drop the table.
    if (!this.getMdAttribute().definesAttribute().equalsIgnoreCase(EntityInfo.ID))
    {
      // drop the attribute from the table
      this.dropAttribute(mdEntityIF.getTableName(), this.getMdAttribute().getColumnName(), this.dbColumnType);
    }

    // Refresh any collection that contains instances of the defining type
    ObjectCache.updateParentCacheStrategy(mdEntity);

    this.refreshReferencingCachedEntityTypes();

    // Reminder: Transaction management aspect will update the cache
  }


  /**
   * Deletes any TypeTuples in which this MdAttribute
   * is part of.
   */
  private void dropTuples()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(TypeTupleDAOIF.CLASS);
    query.WHERE(query.aReference(TypeTupleDAOIF.METADATA).EQ(this.getMdAttribute().getId()));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while(iterator.hasNext())
    {
      BusinessDAOIF businessDAOIF = iterator.next();
      businessDAOIF.getBusinessDAO().delete(false);
    }
  }

  /**
   * Drops the column of the given name from the given table.
   *
   * <br/><b>Precondition:</b> tableName != null <br/><b>Precondition:</b>
   * attrName != null
   *
   * <br/><b>Postcondition:</b> All instances of the given class have the
   * given attribute cleared
   *
   * @param tableName
   *          Name of the table the attribute belongs to.
   * @param columnName
   *          Column name of the attribute to drop from the database.
   * @param the
   *          database column type formatted to the database vendor syntax.
   */
  protected void dropAttribute(String tableName, String columnName, String dbColumnType)
  {
    Database.dropField(tableName, columnName, dbColumnType, this.getMdAttribute());
  }

  /**
   * Sets isNew = false and sets all attributes to isModified = false.
   *
   */
  public void setCommitState()
  {
    this.dbColumnType = this.getDbColumnType();
  }

}
