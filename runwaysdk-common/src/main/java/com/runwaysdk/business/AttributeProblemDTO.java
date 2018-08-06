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
package com.runwaysdk.business;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.constants.AttributeProblemDTOInfo;

public class AttributeProblemDTO extends RunwayProblemDTO implements AttributeNotificationDTO
{  
  public static final String CLASS = AttributeProblemDTOInfo.CLASS;
  
  /**
   * 
   */
  private static final long serialVersionUID = -4665987188861697259L;

  private String componentId;
  
  private String definingType;
  
  private String definingTypeDisplayLabel;

  private String attributeName;
  
  private String attributeId;

  private String attributeDisplayLabel;

  /**
   * Constructs a new AttributeProblemDTO with the specified localized message from the server. 
   * 
   * @param type of the runway problem.
   * @param componentId oid of the containing component.
   * @param definingType type of the containing component.
   * @param definingTypeDisplayLabel display label of the type of the containing component.
   * @param attributeName name of the attribute.
   * @param attributeId oid of the attribute.
   * @param attributeDisplayLabel display label of the attribute.
   * @param localizedMessage end user error message.
   */
  public AttributeProblemDTO(
      String type, String componentId, 
      String definingType, String definingTypeDisplayLabel,
      String attributeName, String attributeId, String attributeDisplayLabel,
      String localizedMessage)
  {
    super(type, localizedMessage);

    this.componentId = componentId;
    this.definingType = definingType;
    this.definingTypeDisplayLabel = definingTypeDisplayLabel;
    this.attributeName = attributeName;
    this.attributeId = attributeId;
    this.attributeDisplayLabel = attributeDisplayLabel;
  }

  /**
   * Returns the oid of the component that defines the attributed.
   * @return oid of the component that defines the attributed.
   */
  public String getComponentId()
  {
    return this.componentId;
  }

  public String getDefiningType()
  {
    return this.definingType;
  }


  /**
   * Returns the display label of the type of the component that defines this attribute.
   * @return display label of the type of the component that defines this attribute.
   */
  public String getDefiningTypeDisplayLabel()
  {
    return this.definingTypeDisplayLabel;
  }

  /**
   * Returns the name of the attribute that this notification pertains to.
   * @return name of the attribute that this notification pertains to.
   */
  public String getAttributeName()
  {
    return this.attributeName;
  }

  public String getAttributeId()
  {
    return this.attributeId;
  }
  
  public String getAttributeDisplayLabel()
  {
    return this.attributeDisplayLabel;
  }
}
