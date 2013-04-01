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
package com.runwaysdk.controller.tag;

import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name="group", bodyContent="scriptless", description="List of checkbox or radio options")
public class GroupTagSupport extends ListTagSupport
{
  /**
   * Name of the accessor from which to retrieve the value
   */
  protected String  valueAttribute;
  
  /**
   * Type of the group.  Must be radio or checkbox
   */
  protected String type;
  
  public GroupTagSupport()
  {
    super();
  }

  @AttributeAnnotation(required=true, description="Name of the accessor of the desired value parameter")
  public String getValueAttribute()
  {
    return valueAttribute;
  }

  public void setValueAttribute(String valueAttribute)
  {
    this.valueAttribute = valueAttribute;
  }

  @AttributeAnnotation(required=true, description="Type of the group.  Must be radio or checkbox.")
  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }  
}
