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
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphRefDAO;

public class MdAttributeGraphRef_G extends MdAttributeConcrete_G
{

  /**
   * 
   */
  private static final long serialVersionUID = -8831375476104335207L;

  /**
   * Precondition:
   * 
   * @param {@link
   *          MdAttributeGraphRef_G}
   */
  public MdAttributeGraphRef_G(MdAttributeGraphRefDAO mdAttribute)
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
  protected MdAttributeGraphRefDAO getMdAttribute()
  {
    return (MdAttributeGraphRefDAO) super.getMdAttribute();
  }

  protected String getLinkClassType()
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
    MdVertexDAOIF referenceMdVertexDAOIF = (MdVertexDAOIF) this.getMdAttribute().getReferenceMdVertexDAOIF();
    String linkClassName = referenceMdVertexDAOIF.getDBClassName();
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createGraphReferenceAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, linkClassName, required, this.isChangeOverTime());
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
    String embeddedClassType = this.getLinkClassType();

    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), context);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createGraphReferenceAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, embeddedClassType, required, this.isChangeOverTime());

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, !context.isExecuteImmediately());
    graphCommand.doIt();
  }
}
