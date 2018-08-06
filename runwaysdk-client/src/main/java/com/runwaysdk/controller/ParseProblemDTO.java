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
package com.runwaysdk.controller;

import java.util.Locale;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.RunwayProblemDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

public abstract class ParseProblemDTO extends RunwayProblemDTO implements AttributeNotificationDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 6321049278249059387L;
  
  private String            componentId;
  
  private String            attributeName;

  private String            attributeId;

  private String            attributeLabel;
  
  private String            componentType;
  
  private String            componentLabel;
  
  private Locale            locale;
  
  private String            value;
  

  public ParseProblemDTO(ComponentDTO component, AttributeMdDTO attributeMd, Locale locale, String value)
  {
    super(ParseProblemDTO.class.getName(), "");

    this.componentId = component.getOid();
    this.componentType = component.getType();
    this.componentLabel = component.getMd().getDisplayLabel();
    
    this.attributeName = attributeMd.getName();
    this.attributeId = attributeMd.getOid();
    this.attributeLabel = attributeMd.getDisplayLabel();
    
    this.locale = locale;
    this.value = value;
  }
  
  public ParseProblemDTO(String attributeName, Locale locale, String value)
  {
    super(ParseProblemDTO.class.getName(), "");

    this.componentId = "---";
    this.componentType = "";
    this.componentLabel = "";
    
    this.attributeName = attributeName;
    this.attributeLabel = attributeName;
    
    this.locale = locale;
    this.value = value;
  }
  
  protected Locale getLocale()
  {
    return locale;
  }
  
  protected String getValue()
  {
    return value;
  }

  /**
   * @see com.runwaysdk.AttributeNotificationDTO#getAttributeDisplayLabel()
   */
  public String getAttributeDisplayLabel()
  {
    return attributeLabel;
  }

  /**
   * @see com.runwaysdk.AttributeNotificationDTO#getAttributeName()
   */
  public String getAttributeName()
  {
    return attributeName;
  }

  public String getAttributeId()
  {
    return attributeId;
  }

  public String getAttributeLabel()
  {
    return attributeLabel;
  }
  
  /**
   * @see com.runwaysdk.AttributeNotificationDTO#getComponentId()
   */
  public String getComponentId()
  {
    return componentId;
  }

  /**
   * @see com.runwaysdk.AttributeNotificationDTO#getDefiningType()
   */
  public String getDefiningType()
  {
    return componentType;
  }

  /**
   * @see com.runwaysdk.AttributeNotificationDTO#getDefiningTypeDisplayLabel()
   */
  public String getDefiningTypeDisplayLabel()
  {
    return componentLabel;
  }
}
