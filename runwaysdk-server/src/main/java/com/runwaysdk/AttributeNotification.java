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
package com.runwaysdk;

public interface AttributeNotification
{
  /**
   * Returns the id of the component that defines the attribute.
   * @return id of the component that defines the attribute.
   */
  public String getComponentId();

  /**
   * Sets the id of the component on which this attribute notification pertains to.
   */
  public void setComponentId(String componentId);


  public String getDefiningType();


  public void setDefiningType(String definingType);


  public String getDefiningTypeDisplayLabel();


  public void setDefiningTypeDisplayLabel(String containingTypeDisplayLabel);


  public String getAttributeName();


  public void setAttributeName(String attributeName);


  public String getAttributeDisplayLabel();


  public void setAttributeDisplayLabel(String attributeDisplayLabel);

  /**
   * Finalizes the Notification.  Transaction will obtain a reference
   * to this notification and prevent the transaction from completing
   * if the notification is a problem.
   */
  public void throwIt();
}
