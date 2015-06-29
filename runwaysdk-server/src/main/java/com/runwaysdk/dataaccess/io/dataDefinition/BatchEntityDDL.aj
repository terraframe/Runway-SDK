/**
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
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.TransactionCache;
import com.runwaysdk.dataaccess.transaction.TransactionCacheIF;

public privileged aspect BatchEntityDDL percflow(runImport())
{

  public static class MdEntityInfo
  {
    private MdEntityDAO mdEntityDAO;
    // Name plus column type
    private List<String> columnDefs;
    // Just the column name
    private List<String> columnNames;

    private boolean isCreateState;

    private List<Command> commandList;

    protected MdEntityInfo(boolean isCreateState, MdEntityDAO mdEntityDAO)
    {
      this.isCreateState = isCreateState;
      this.mdEntityDAO = mdEntityDAO;
      this.columnDefs = new LinkedList<String>();
      this.columnNames = new LinkedList<String>();
      this.commandList = new LinkedList<Command>();
    }

    protected boolean isCreateState()
    {
      return this.isCreateState;
    }

    protected MdEntityDAO getMdEntityDAO()
    {
      return this.mdEntityDAO;
    }

    protected List<String> getColumnDefs()
    {
      return this.columnDefs;
    }

    protected void addColumnDef(String columnDef)
    {
      this.columnDefs.add(columnDef);
    }

    protected List<String> getColumnNames()
    {
      return this.columnNames;
    }

    protected void addColumnName(String columnName)
    {
      this.columnNames.add(columnName);
    }

    protected void addCommand(Command command)
    {
      this.commandList.add(command);
    }

    protected List<Command> getCommands()
    {
      return this.commandList;
    }

    protected void executeCommands()
    {
      for (Command command : commandList)
      {
        command.doIt();
      }
    }
  }

  public static abstract class Command
  {
    protected Command() {}

    protected abstract void doIt();
  }

  private class AddUniqueIndexCommand extends Command
  {
    private String tableName;
    private String columnName;
    private String indexName;

    protected AddUniqueIndexCommand(String tableName, String columnName, String indexName)
    {
      this.tableName = tableName;
      this.columnName = columnName;
      this.indexName = indexName;
    }

    protected void doIt()
    {
      Database.addUniqueIndex(this.tableName, this.columnName, this.indexName);
    }
  }

  private class AddNonUniqueIndexCommand extends Command
  {
    private String tableName;
    private String columnName;
    private String indexName;

    protected AddNonUniqueIndexCommand(String tableName, String columnName, String indexName)
    {
      this.tableName = tableName;
      this.columnName = columnName;
      this.indexName = indexName;
    }

    protected void doIt()
    {
      Database.addNonUniqueIndex(this.tableName, this.columnName, this.indexName);
    }
  }


  private class AddGroupAttributeIndexCommand extends Command
  {
    private String tableName;
    private String indexName;
    private List<String> columnNames;
    private Boolean isUnique;

    protected AddGroupAttributeIndexCommand(String tableName, String indexName, List<String> columnNames, Boolean isUnique)
    {
      this.tableName = tableName;
      this.indexName = indexName;
      this.columnNames = columnNames;
      this.isUnique = isUnique;
    }

    protected void doIt()
    {
      Database.addGroupAttributeIndex(tableName, indexName, columnNames, isUnique);
    }
  }

  private class AddGeometryColumnCommand extends Command
  {
    private String tableName;
    private String columnName;
    private Integer srid;
    private String geometryType;
    private Integer dimension;

    protected AddGeometryColumnCommand(String tableName, String columnName, Integer srid, String geometryType, Integer dimension)
    {
      this.tableName = tableName;
      this.columnName = columnName;
      this.srid = srid;
      this.geometryType = geometryType;
      this.dimension = dimension;
    }

    protected void doIt()
    {
      Database.addGeometryColumn(this.tableName, this.columnName, this.srid, this.geometryType, this.dimension);
    }
  }

  protected Map<String, MdEntityInfo> typeDDLMap = new HashMap<String, MdEntityInfo>();

  public pointcut runImport()
  :  (execution (* com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter.runImport(..)) ||
      execution (* com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.runImport(..))
     );

  /**
   * Check the transaction cache for column name conflicts
   *
   * @param columnName
   * @param tableName
   */
  public pointcut columnExists(String columnName, String tableName)
  : (call (* com.runwaysdk.dataaccess.database.Database.columnExists(String, String)) && args(columnName, tableName)) &&
  !within(BatchEntityDDL);
  Object around(String columnName, String tableName) : columnExists(columnName, tableName)
  {
    if (Database.tableExists(tableName) && Database.columnExists(columnName, tableName))
    {
      return true;
    }
    else
    {
      TransactionCacheIF transactionCache = TransactionCache.getCurrentTransactionCache();
      Map<String, MdAttributeDAO> mdAttributeMap = transactionCache.getAddedMdAttributes();
      for (MdAttributeDAO mdAttributeDAO : mdAttributeMap.values())
      {
        if (!(mdAttributeDAO instanceof MdAttributeConcreteDAO))
        {
          continue;
        }

        MdAttributeConcreteDAO mdAttributeConcreteDAO = (MdAttributeConcreteDAO)mdAttributeDAO;

        MdClassDAOIF mdClassDAOIF = mdAttributeDAO.definedByClass();
        if (mdClassDAOIF instanceof MdEntityDAOIF)
        {
          MdEntityDAOIF mdEntityDAOIF = (MdEntityDAOIF)mdClassDAOIF;
          String loopTableName = mdEntityDAOIF.getTableName();
          String loopColumnName = mdAttributeConcreteDAO.getColumnName();

          if (tableName.equals(loopTableName) &&
              columnName.equals(loopColumnName))
          {
            return true;
          }
        }
      }
      return false;
    }
  }

  public pointcut applyMdEntity(MdEntityDAO mdEntityDAO)
  : (call (* com.runwaysdk.dataaccess.metadata.MdEntityDAO+.apply(..)) && target(mdEntityDAO))
    && within(com.runwaysdk.dataaccess.io.dataDefinition.MdEntityHandler+) ;
  before(MdEntityDAO mdEntityDAO) : applyMdEntity(mdEntityDAO)
  {
    String type = mdEntityDAO.definesType();

    if (type != null && !this.typeDDLMap.containsKey(type))
    {
      this.typeDDLMap.put(type, null);
      getMdEntityInfo(this.typeDDLMap, mdEntityDAO);
    }
  }

  // Do not create the table in the database if we are going to create the table at the end of the tag.
  protected pointcut createClassTable(MdEntityDAO mdEntityDAO, String tableName)
  :  call (* com.runwaysdk.dataaccess.database.Database.createClassTable(String)) && this(mdEntityDAO) && args(tableName);
  void around(MdEntityDAO mdEntityDAO, String tableName) : createClassTable(mdEntityDAO, tableName)
  {
    String type = mdEntityDAO.definesType();
    if (!this.typeDDLMap.containsKey(type))
    {
      proceed(mdEntityDAO, tableName);
    }
  }

  // Do not create the table in the database if we are going to create the table at the end of the tag.
  protected pointcut createRelationshipTable(MdEntityDAO mdEntityDAO, String tableName, String index1Name, String index2Name, Boolean isUnique)
  :  call (* com.runwaysdk.dataaccess.database.Database.createRelationshipTable(String, String, String, Boolean))
      && this(mdEntityDAO)  && args(tableName, index1Name, index2Name, isUnique);
  void around(MdEntityDAO mdEntityDAO, String tableName, String index1Name, String index2Name, Boolean isUnique) :
    createRelationshipTable(mdEntityDAO, tableName, index1Name, index2Name, isUnique)
  {
    String type = mdEntityDAO.definesType();
    if (!this.typeDDLMap.containsKey(type))
    {
      proceed(mdEntityDAO, tableName, index1Name, index2Name, isUnique);
    }
  }

  protected pointcut addField(com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E mdAttribute_E,
      String tableName, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  :  call (* com.runwaysdk.dataaccess.database.Database.addField(String, String, String, MdAttributeConcreteDAO))
      && this(mdAttribute_E) && args(tableName, columnName, formattedType, mdAttributeConcreteDAO);
  void around(com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E mdAttribute_E, String tableName, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO) :
    addField(mdAttribute_E, tableName, columnName, formattedType, mdAttributeConcreteDAO)
  {
    // Cast is OK because we are not modifying the object.
    MdEntityDAO mdEntityDAO = (MdEntityDAO)mdAttribute_E.definedByClass();
    String definingType = mdEntityDAO.definesType();

    if (this.typeDDLMap.containsKey(definingType))
    {
      MdEntityInfo mdEntityInfo = getMdEntityInfo(this.typeDDLMap, mdEntityDAO);
      String addColumnDef = Database.addFieldBatch(tableName, columnName, formattedType, mdAttributeConcreteDAO);
      mdEntityInfo.addColumnDef(addColumnDef);

      String batchColumnName = null;
      if (mdAttributeConcreteDAO instanceof MdAttributeEnumerationDAO)
      {
        MdAttributeEnumerationDAO mdAttributeEnumerationDAO = (MdAttributeEnumerationDAO)mdAttributeConcreteDAO;
        if (columnName.equals(mdAttributeEnumerationDAO.getCacheColumnName()))
        {
          batchColumnName = mdAttributeEnumerationDAO.getCacheColumnName();
        }
      }

      if (batchColumnName == null)
      {
        batchColumnName = mdAttributeConcreteDAO.getColumnName();
      }

      mdEntityInfo.addColumnName(batchColumnName);
    }
    else
    {
      proceed(mdAttribute_E, tableName, columnName, formattedType, mdAttributeConcreteDAO);
    }
  }

  // Database.addUniqueIndex(String tableName, String columnName, String indexName)
  protected pointcut addUniqueIndex(com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E mdAttribute_E,
      String tableName, String columnName, String indexName)
  :  call (* com.runwaysdk.dataaccess.database.Database.addUniqueIndex(String, String, String))
      && this(mdAttribute_E) && args(tableName, columnName, indexName);
  void around(com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E mdAttribute_E, String tableName, String columnName, String indexName) :
    addUniqueIndex(mdAttribute_E, tableName, columnName, indexName)
  {
    // Cast is OK because we are not modifying the object.
    MdEntityDAO mdEntityDAO = (MdEntityDAO)mdAttribute_E.definedByClass();
    String definingType = mdEntityDAO.definesType();

    if (this.typeDDLMap.containsKey(definingType))
    {
      MdEntityInfo mdEntityInfo = getMdEntityInfo(this.typeDDLMap, mdEntityDAO);
      AddUniqueIndexCommand indexCommand = new AddUniqueIndexCommand(tableName, columnName, indexName);
      mdEntityInfo.addCommand(indexCommand);
    }
    else
    {
      proceed(mdAttribute_E, tableName, columnName, indexName);
    }
  }

  // Database.addNonUniqueIndex(String tableName, String columnName, String indexName)
  protected pointcut addNonUniqueIndex(com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E mdAttribute_E,
      String tableName, String columnName, String indexName)
  :  call (* com.runwaysdk.dataaccess.database.Database.addNonUniqueIndex(String, String, String))
      && this(mdAttribute_E) && args(tableName, columnName, indexName);
  void around(com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E mdAttribute_E, String tableName, String columnName, String indexName) :
    addNonUniqueIndex(mdAttribute_E, tableName, columnName, indexName)
  {
    // Cast is OK because we are not modifying the object.
    MdEntityDAO mdEntityDAO = (MdEntityDAO)mdAttribute_E.definedByClass();
    String definingType = mdEntityDAO.definesType();

    if (this.typeDDLMap.containsKey(definingType))
    {
      MdEntityInfo mdEntityInfo = getMdEntityInfo(this.typeDDLMap, mdEntityDAO);
      AddNonUniqueIndexCommand indexCommand = new AddNonUniqueIndexCommand(tableName, columnName, indexName);
      mdEntityInfo.addCommand(indexCommand);
    }
    else
    {
      proceed(mdAttribute_E, tableName, columnName, indexName);
    }
  }

// Database.addGroupAttributeIndex(String tableName, String indexName, List<String> attributeColumnNames, Boolean isUnique)
  protected pointcut addGroupAttributeIndex(MdIndexDAO mdIndexDAO,
      String tableName, String indexName, List<String> attributeColumnNames, Boolean isUnique)
  :  call (* com.runwaysdk.dataaccess.database.Database.addGroupAttributeIndex(String, String, List<String>, Boolean))
      && this(mdIndexDAO) && args(tableName, indexName, attributeColumnNames, isUnique);
  void around(MdIndexDAO mdIndexDAO, String tableName, String indexName, List<String> attributeColumnNames, Boolean isUnique) :
    addGroupAttributeIndex(mdIndexDAO, tableName, indexName, attributeColumnNames, isUnique)
  {
    // Cast is OK because we are not modifying the object.
    MdEntityDAO mdEntityDAO = (MdEntityDAO)mdIndexDAO.definesIndexForEntity();
    String definingType = mdEntityDAO.definesType();

    if (this.typeDDLMap.containsKey(definingType))
    {
      MdEntityInfo mdEntityInfo = getMdEntityInfo(this.typeDDLMap, mdEntityDAO);
      AddGroupAttributeIndexCommand indexCommand =
        new AddGroupAttributeIndexCommand(tableName, indexName, attributeColumnNames, isUnique);
      mdEntityInfo.addCommand(indexCommand);
    }
    else
    {
      proceed(mdIndexDAO, tableName, indexName, attributeColumnNames, isUnique);
    }
  }


  protected pointcut addGeometryColumn(String tableName, String columnName, Integer srid, String geometryType, Integer dimension)
  :  (execution (* com.runwaysdk.dataaccess.database.Database.addGeometryColumn(String, String, Integer, String, Integer))
       && args(tableName, columnName, srid, geometryType, dimension))
       && !cflow(execution (* com.runwaysdk.dataaccess.io.dataDefinition.BatchEntityDDL.Command.doIt()));
  void around(String tableName, String columnName, Integer srid, String geometryType, Integer dimension) :
    addGeometryColumn(tableName, columnName, srid, geometryType, dimension)
  {
    MdEntityDAO mdEntityDAO = findTypeByTable(this.typeDDLMap, tableName);
    if (mdEntityDAO != null && this.typeDDLMap.containsKey(mdEntityDAO.definesType()))
    {
      MdEntityInfo mdEntityInfo = BatchEntityDDL.getMdEntityInfo(this.typeDDLMap, mdEntityDAO);
      AddGeometryColumnCommand geometryCommand = new AddGeometryColumnCommand(tableName, columnName, srid, geometryType, dimension);
      mdEntityInfo.addCommand(geometryCommand);
    }
    else
    {
      proceed(tableName, columnName, srid, geometryType, dimension);
    }
  }


  protected pointcut endElement(MdEntityHandler mdEntityHandler, String namespaceURI, String localName, String fullName)
  :  execution (* com.runwaysdk.dataaccess.io.dataDefinition.MdEntityHandler+.endElement(String, String, String))
    && this(mdEntityHandler) && args(namespaceURI, localName, fullName);
  after (MdEntityHandler mdEntityHandler, String namespaceURI, String localName, String fullName) :
    endElement(mdEntityHandler, namespaceURI, localName, fullName)
  {
    if (isEntityEndElement(localName))
    {
      MdEntityDAO mdEntityDAO = mdEntityHandler.getMdEntityDAO();

      String definingType = mdEntityDAO.definesType();
      String tableName = mdEntityDAO.getTableName();

      if (this.typeDDLMap.containsKey(definingType))
      {
        MdEntityInfo mdEntityInfo = this.typeDDLMap.get(definingType);

        // We create the table, even if it does not add additional columns
        if (mdEntityInfo.isCreateState())
        {
          if (mdEntityHandler instanceof MdRelationshipHandler)
          {
            Database.createRelationshipTableBatch(tableName, mdEntityInfo.getColumnDefs());

            MdRelationshipDAO mdRelationshipDAO = (MdRelationshipDAO)mdEntityDAO;
            mdRelationshipDAO.createTableIndexes();
          }
          else
          {
            Database.createClassTableBatch(tableName, mdEntityInfo.getColumnDefs());
          }
        }
        // Only perform a DDL command IF columns are involved.
        else if (mdEntityInfo.getColumnDefs().size() > 0)
        {
          Database.alterClassTableBatch(tableName, mdEntityInfo.getColumnNames(), mdEntityInfo.getColumnDefs());
        }

        for (Command command : mdEntityInfo.getCommands())
        {
          command.doIt();
        }

        this.typeDDLMap.remove(definingType);
      }
    }
  }

  protected static MdEntityInfo getMdEntityInfo(Map<String, MdEntityInfo> typeDDLMap, MdEntityDAO mdEntityDAO)
  {
    String type = mdEntityDAO.definesType();

    if (typeDDLMap.get(type) == null)
    {
      String tableName = mdEntityDAO.getTableName();

      MdEntityInfo mdEntityInfo;

      if (!Database.tableExists(tableName))
      {
        mdEntityInfo = new MdEntityInfo(true, mdEntityDAO);
      }
      else
      {
        mdEntityInfo = new MdEntityInfo(false, mdEntityDAO);
      }

      typeDDLMap.put(type, mdEntityInfo);

      return mdEntityInfo;
    }
    else
    {
      return typeDDLMap.get(type);
    }
  }

  protected static MdEntityDAO findTypeByTable(Map<String, MdEntityInfo> typeDDLMap, String table)
  {
    for (String type : typeDDLMap.keySet())
    {
      MdEntityDAO mdEntityDAO = typeDDLMap.get(type).getMdEntityDAO();

      if (mdEntityDAO.getTableName().equals(table))
      {
        return mdEntityDAO;
      }
    }

    return null;
  }


  protected static boolean isEntityEndElement(String localName)
  {
    if (localName.equals(XMLTags.ENUMERATION_MASTER_TAG) ||
        localName.equals(XMLTags.MD_BUSINESS_TAG) ||
        localName.equals(XMLTags.MD_TERM_TAG) ||
        localName.equals(XMLTags.MD_RELATIONSHIP_TAG) ||
        localName.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG) ||
        localName.equals(XMLTags.MD_TREE_TAG) ||
        localName.equals(XMLTags.MD_GRAPH_TAG) ||
        localName.equals(XMLTags.MD_STRUCT_TAG) ||
        localName.equals(XMLTags.MD_LOCAL_STRUCT_TAG))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}
