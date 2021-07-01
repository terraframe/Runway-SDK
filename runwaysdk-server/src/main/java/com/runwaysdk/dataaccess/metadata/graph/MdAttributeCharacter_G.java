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

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeInteger;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;

public class MdAttributeCharacter_G extends MdAttributeConcrete_G
{
  /**
   * 
   */
  private static final long serialVersionUID = -6108473202975565175L;

  /**
   * @param {@link MdAttributeCharacterDAO}
   */
  public MdAttributeCharacter_G(MdAttributeCharacterDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the {@link MdAttributeCharacterDAO}.
   *
   * @return the {@link MdAttributeCharacterDAO}
   */
  protected MdAttributeCharacterDAO getMdAttribute()
  {
    return (MdAttributeCharacterDAO)this.mdAttribute;
  }

  /**
   * Contains special logic for saving an attribute.
   */
  public void save()
  {
    super.save();

    this.modifyMaxLength();
  }
  
  
  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.COLUMN_NAME).getValue();
    boolean required = ((AttributeBooleanIF)this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.REQUIRED)).getBooleanValue(); 
    int maxLength = Integer.parseInt(this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.SIZE).getValue());    
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength, this.isChangeOverTime());
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime());
    
    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();
  }
  
  /**
   * Drops the attribute from the graph database
   *
   */
  @Override
  protected void dropDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.COLUMN_NAME).getValue();
    boolean required = ((AttributeBooleanIF)this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.REQUIRED)).getBooleanValue(); 
    int maxLength = Integer.parseInt(this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.SIZE).getValue());
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();
    
    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime());    
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength, this.isChangeOverTime());
    
    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, true);
    graphCommand.doIt();
  }
  
  
  /**
   * Modify if the attribute is required or not.
   *
   */
  private void modifyMaxLength()
  {
    AttributeInteger attributeSize = (AttributeInteger)this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.SIZE);
    
    if (attributeSize.isModified())
    {
      int newMaxSize = attributeSize.getTypeSafeValue();
      
      String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
      String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeCharacterInfo.COLUMN_NAME).getValue();
      GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();      
      GraphRequest ddlGraphDBRequest = GraphDBService.getInstance().getDDLGraphDBRequest();   
      
      int existingMaxSize = GraphDBService.getInstance().getCharacterAttributeMaxLength(graphRequest, dbClassName, dbAttrName);
      
      if (newMaxSize != existingMaxSize)
      {
        GraphDDLCommandAction doItAction = GraphDBService.getInstance().modifiyCharacterAttributeLength(graphRequest, ddlGraphDBRequest, dbClassName, dbAttrName, newMaxSize);
        GraphDDLCommandAction undoItAction = GraphDBService.getInstance().modifiyCharacterAttributeLength(graphRequest, ddlGraphDBRequest, dbClassName, dbAttrName, existingMaxSize);
      
        GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
        graphCommand.doIt();
      }
    }
  }
}
