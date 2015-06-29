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
package com.runwaysdk;

import com.runwaysdk.business.NotificationDTOIF;

public interface AttributeNotificationDTO extends NotificationDTOIF
{
  /**
   * Constant to use when a component doesn't exist
   */
  public static final String NO_COMPONENT = "---";
  
  /**
   * Returns the id of the component that defines the attributed.
   * @return id of the component that defines the attributed.
   */
  public String getComponentId();
  
  /**
   * Returns the name of the attribute that this notification pertains to.
   * @return name of the attribute that this notification pertains to.
   */
  public String getAttributeName();
 
  /**
   * Returns the name of the 
   * @return
   */
  public String getDefiningType();
  
  /**
   * Returns the display label of the type of the component that defines this attribute.
   * @return display label of the type of the component that defines this attribute.
   */
  public String getDefiningTypeDisplayLabel();
  
  /**
   * Returns the display label of the attribute that this notification pertains to.
   * @return display label of the attribute that this notification pertains to.
   */
  public String getAttributeDisplayLabel();

  /**
   * @return The localized end user Message.
   */
  public String getMessage();
}
