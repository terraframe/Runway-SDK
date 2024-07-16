/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.metadata.graph;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphGeometryFieldProperties;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeConcrete_G;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeGeometryDAO;
import com.runwaysdk.dataaccess.metadata.DeleteContext;

public class MdAttributeGeometry_G extends MdAttributeConcrete_G
{
  /**
   * 
   */
  private static final long serialVersionUID = -6108473202975565175L;

  /**
   * The database vendor formated column datatype.
   */
  protected boolean         createIndex;

  /**
   * @param {@link
   *          MdAttributeGeometryDAO}
   */
  public MdAttributeGeometry_G(MdAttributeGeometryDAO mdAttribute)
  {
    super(mdAttribute);

    this.createIndex = mdAttribute.isCreateIndex();
  }

  /**
   * Returns the {@link MdAttributeGeometryDAO}.
   *
   * @return the {@link MdAttributeGeometryDAO}
   */
  protected MdAttributeGeometryDAO getMdAttribute()
  {
    return (MdAttributeGeometryDAO) this.mdAttribute;
  }

  public boolean isCreateIndex()
  {
    return createIndex;
  }

  public void setCreateIndex(boolean createIndex)
  {
    this.createIndex = createIndex;
  }

  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createGeometryAttribute(graphRequest, graphDDLRequest, GraphGeometryFieldProperties.build(dbClassName, dbAttrName, required, this.isChangeOverTime(), this.dbColumnType, this.isCreateIndex()));
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), new DeleteContext());

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();
  }

  /**
   * Drops the attribute from the graph database
   *
   */
  @Override
  protected void dropDbAttribute(DeleteContext context)
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), context);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createGeometryAttribute(graphRequest, graphDDLRequest, GraphGeometryFieldProperties.build(dbClassName, dbAttrName, required, this.isChangeOverTime(), this.dbColumnType, this.isCreateIndex()));

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, !context.isExecuteImmediately());
    graphCommand.doIt();
  }
}
