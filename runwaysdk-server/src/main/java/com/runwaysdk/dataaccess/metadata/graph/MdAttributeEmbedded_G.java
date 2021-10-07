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
package com.runwaysdk.dataaccess.metadata.graph;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;

public class MdAttributeEmbedded_G extends MdAttributeConcrete_G
{

  /**
   * 
   */
  private static final long serialVersionUID = -8831375476104335207L;

  /**
   * Precondition:
   * 
   * @param {@link
   *          MdAttributeEmbedded_G}
   */
  public MdAttributeEmbedded_G(MdAttributeEmbeddedDAO mdAttribute)
  {
    super(mdAttribute, null);
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

  /**
   * Returns the MdAttribute.
   * 
   * @return the MdAttribute
   */
  protected MdAttributeEmbeddedDAO getMdAttribute()
  {
    return (MdAttributeEmbeddedDAO) super.getMdAttribute();
  }

  protected String getEmbeddedClassType()
  {
    return GraphDBService.getInstance().getDbColumnType(this.getMdAttribute());
  }

  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String embeddedClassType = this.getEmbeddedClassType();

    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createEmbeddedAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, embeddedClassType, required, this.isChangeOverTime());
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), new DeleteContext());

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
    String embeddedClassType = this.getEmbeddedClassType();

    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), context);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createEmbeddedAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, embeddedClassType, required, this.isChangeOverTime());

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, !context.isExecuteImmediately());
    graphCommand.doIt();
  }
}
