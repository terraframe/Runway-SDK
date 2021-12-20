/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.metadata.graph;

import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteStrategy;

public class MdAttributeConcrete_G extends MdAttributeConcreteStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = 2659134669811043650L;

  /**
   * The database vendor formated column datatype.
   */
  protected String          dbColumnType;

  /**
   * @param {@link
   *          MdAttributeConcreteDAO}
   */
  public MdAttributeConcrete_G(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);

    this.dbColumnType = GraphDBService.getInstance().getDbColumnType(mdAttribute);
  }

  /**
   * @param {@link
   *          MdAttributeConcreteDAO}
   */
  public MdAttributeConcrete_G(MdAttributeConcreteDAO mdAttribute, String dbColumnType)
  {
    super(mdAttribute);

    this.dbColumnType = dbColumnType;
  }

  /**
   * Returns the {@link MdGraphClassDAOIF} that defines this MdAttribute.
   *
   * @return the {@link MdGraphClassDAOIF} that defines this MdAttribute.
   */
  public MdGraphClassDAOIF definedByClass()
  {
    return (MdGraphClassDAOIF) this.getMdAttribute().definedByClass();
  }

  protected boolean isChangeOverTime()
  {
    return this.definedByClass().isEnableChangeOverTime();
  }

  protected void preSaveValidate()
  {
    super.preSaveValidate();

    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      // Supply a column name if one was not provided
      Attribute columnNameAttribute = this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME);
      if (!columnNameAttribute.isModified() || columnNameAttribute.getValue().trim().length() == 0)
      {
        // Automatically create a column name
        columnNameAttribute.setValue(this.getMdAttribute().definesAttribute());
      }
      else
      {
        // Manually create a column name
        columnNameAttribute.setValue(columnNameAttribute.getValue().toLowerCase());
      }
    }

    this.nonMdEntityCheckExistingForAttributeOnCreate();
  }

  /**
   * Contains special logic for saving an attribute.
   */
  public void save()
  {
    this.validate();

    this.nonMdEntityPostSaveValidationOperations();

    if (this.getMdAttribute().isNew())
    {
      // This attribute may have already been created
      this.createDbAttribute();
    }

    this.modifyAttributeRequired();

    this.modifyAttributeIndex();
  }

  /**
   * Deletes an attribute from runway. The <code>BusinessDAO</code> is deleted
   * from the database and removed from the cache. All relationships pertaining
   * to this <code>BusinessDAO</code> are also removed as well.
   *
   * <br/>
   * <b>Postcondition: </b> BusinessDAO and all dependencies are removed from
   * the runway <br/>
   * <b>Postcondition: </b> Coresponding column from the defining table is
   * dropped
   * @param p_mdAttribute
   *          Attribute metadata BusinessDAO
   */
  public void delete(DeleteContext context)
  {
    // Delete all tuples this MdAttribute is part of
    // Heads up: Graph - do we need to implement this as for
    // MdAttributeConcrete_E?
    // this.dropTuples();

    // Some dbs require that all tables have at least one column. Therefore, I
    // am making the assumption that if you are deleting the OID field, then you
    // are also
    // dropping the defining type within the same transaction, which will drop
    // the table.
    if (!this.getMdAttribute().definesAttribute().equalsIgnoreCase(ComponentInfo.OID))
    {
      this.dropAttributeIndex();

      // drop the attribute from the table
      this.dropDbAttribute(context);
    }
  }

  /**
   * No special validation logic.
   */
  protected void validate()
  {
    this.nonMdEntityValidate();
  }

  /**
   * Modify the index on the attribute.
   *
   */
  private void modifyAttributeIndex()
  {
    AttributeEnumerationIF attributeIndex = (AttributeEnumerationIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);

    if (attributeIndex.isModified() || this.getMdAttribute().isNew())
    {
      IndexTypes newIndexType = this.getMdAttribute().getIndexType();

      setAttributeIndex(newIndexType);
    }
  }

  private void dropAttributeIndex()
  {
    this.setAttributeIndex(IndexTypes.NO_INDEX);
  }

  private void setAttributeIndex(IndexTypes newIndexType)
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.COLUMN_NAME).getValue();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest ddlGraphDBRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    IndexTypes originalIndex = GraphDBService.getInstance().getIndexType(graphRequest, dbClassName, dbAttrName);

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().modifiyAttributeIndex(graphRequest, ddlGraphDBRequest, dbClassName, dbAttrName, newIndexType);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().modifiyAttributeIndex(graphRequest, ddlGraphDBRequest, dbClassName, dbAttrName, originalIndex);

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();

    String indexName = GraphDBService.getInstance().getIndexName(graphRequest, dbClassName, dbAttrName);

    if (indexName == null)
    {
      indexName = "";
    }

    // Update the row in the database, as the {@link MdAttributeConcreteDAO} has
    // already been applied.
    String tableName = MdAttributeConcreteDAOIF.TABLE;
    String columnName = MdAttributeConcreteDAOIF.INDEX_TYPE_NAME;
    String entityOid = this.getMdAttribute().getOid();
    String prepStmtVar = "?";
    String newValue = indexName;
    String attributeType = MdAttributeCharacterInfo.CLASS;
    PreparedStatement statement = Database.buildPreparedUpdateFieldStatement(tableName, entityOid, columnName, prepStmtVar, newValue, attributeType);

    List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
    statements.add(statement);

    Database.executeStatementBatch(statements);

    this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.INDEX_NAME).setValue(indexName);
  }

  /**
   * Adds the attribute to the graph database
   *
   */
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createConcreteAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.dbColumnType, required, this.isChangeOverTime());
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), new DeleteContext());

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();
  }

  /**
   * Drops the attribute from the graph database
   * @param context TODO
   *
   */
  protected void dropDbAttribute(DeleteContext context)
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), context);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createConcreteAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.dbColumnType, required, this.isChangeOverTime());

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, !context.isExecuteImmediately());
    graphCommand.doIt();
  }

  /**
   * Modify if the attribute is required or not.
   *
   */
  private void modifyAttributeRequired()
  {
    AttributeBooleanIF attributeRequired = (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED);

    if (attributeRequired.isModified())
    {
      boolean required = attributeRequired.getBooleanValue();

      String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
      String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.COLUMN_NAME).getValue();
      GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
      GraphRequest ddlGraphDBRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

      boolean wasPreviouslyRequired = GraphDBService.getInstance().isAttributeRequired(graphRequest, dbClassName, dbAttrName);

      GraphDDLCommandAction doItAction = GraphDBService.getInstance().modifiyAttributeRequired(graphRequest, ddlGraphDBRequest, dbClassName, dbAttrName, required);
      GraphDDLCommandAction undoItAction = GraphDBService.getInstance().modifiyAttributeRequired(graphRequest, ddlGraphDBRequest, dbClassName, dbAttrName, wasPreviouslyRequired);

      GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
      graphCommand.doIt();
    }
  }
}
