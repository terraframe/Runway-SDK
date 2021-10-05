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

import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;

public class MdAttributeUUID_G extends MdAttributeConcrete_G
{

  /**
   * 
   */
  private static final long serialVersionUID = 1619277479919028965L;

  /**
   * @param {@link
   *          MdAttributeUUIDDAO}
   */
  public MdAttributeUUID_G(MdAttributeUUIDDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the {@link MdAttributeUUIDDAO}.
   *
   * @return the {@link MdAttributeUUIDDAO}
   */
  protected MdAttributeUUIDDAO getMdAttribute()
  {
    return (MdAttributeUUIDDAO) this.mdAttribute;
  }

  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeUUIDInfo.COLUMN_NAME).getValue();
    boolean required = true;
    int maxLength = MdAttributeUUIDInfo.UUID_STRING_LENGTH;

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength, this.isChangeOverTime());
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), true);

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();
  }

  @Override
  protected void dropDbAttribute(boolean removeValues)
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeUUIDInfo.COLUMN_NAME).getValue();
    boolean required = true;
    int maxLength = MdAttributeUUIDInfo.UUID_STRING_LENGTH;

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime(), removeValues);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength, this.isChangeOverTime());

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, true);
    graphCommand.doIt();
  }

}
