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
package com.runwaysdk.dataaccess.attributes;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.RunwayProblem;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.session.Session;

public abstract class AttributeProblem extends RunwayProblem implements AttributeNotification
{
  private String componentId;

  private MdClassDAOIF mdClassIF;

  private MdAttributeDAOIF mdAttributeIF;

  private String attributeName;
  
  private String attributeId;

  private String attributeDisplayLabel;

  private String containingType;

  private String containingTypeDisplayLabel;

  /**
   * Problem when an attribute is invalid.
   *
   * @param componentId id of the containing component.
   * @param mdClassDAOIF defines the type of the containing component.
   * @param mdAttributeDAOIF defines the attribute.
   * @param developerMessage developer error message.
   */
  protected AttributeProblem(String componentId, MdClassDAOIF mdClassDAOIF, MdAttributeDAOIF mdAttributeDAOIF, String developerMessage)
  {
    super(developerMessage);

    this.componentId = componentId;

    this.mdClassIF = mdClassDAOIF;

    this.mdAttributeIF = mdAttributeDAOIF;

    this.attributeName = mdAttributeDAOIF.definesAttribute();
    this.attributeId = mdAttributeDAOIF.getId();
    this.attributeDisplayLabel = mdAttributeDAOIF.getDisplayLabel(Session.getCurrentLocale());
    this.containingType = mdClassDAOIF.definesType();
    this.containingTypeDisplayLabel = mdClassDAOIF.getDisplayLabel(Session.getCurrentLocale());
  }

  protected MdClassDAOIF getMdClassIF()
  {
    return this.mdClassIF;
  }

  public MdAttributeDAOIF getMdAttributeIF()
  {
    return this.mdAttributeIF;
  }

  /**
   * Sets the id of the component on which this attribute notification pertains to.
   */
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }

  public String getComponentId()
  {
    return this.componentId;
  }

  public String getDefiningType()
  {
    return this.containingType;
  }

  public void setDefiningType(String containingType)
  {
    this.containingType = containingType;
  }

  public String getDefiningTypeDisplayLabel()
  {
    return this.containingTypeDisplayLabel;
  }

  public void setDefiningTypeDisplayLabel(String containingTypeDisplayLabel)
  {
    this.containingTypeDisplayLabel = containingTypeDisplayLabel;
  }

  public String getAttributeName()
  {
    return this.attributeName;
  }
  
  public String getAttributeId()
  {
    return this.attributeId;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public String getAttributeDisplayLabel()
  {
    return this.attributeDisplayLabel;
  }

  public void setAttributeDisplayLabel(String attributeDisplayLabel)
  {
    this.attributeDisplayLabel = attributeDisplayLabel;
  }

  /**
   * Finalizes the Problem.  Transaction will obtain a reference
   * to this problem and prevent the transaction from completing.
   */
  public final void throwIt()
  {
    // marker method.
  }
}
