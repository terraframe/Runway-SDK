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
package com.runwaysdk.controller.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "fieldProperty", bodyContent = "empty", description = "Key-value pairs for input fields")
public class FieldPropertyTagSupport extends SimpleTagSupport
{
  private String key;

  private String value;

  @AttributeAnnotation(required=true,rtexprvalue = true, description = "Property key")
  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  @AttributeAnnotation(required=true,rtexprvalue = true, description = "Property value")
  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = findAncestorWithClass(this, InputMarker.class);

    // If the input tag is in the context of a component then
    // load update the parameter name and display value
    if (parent != null)
    {
      InputMarker marker = (InputMarker) parent;
      
      marker.addAttribute(key, value);      
    }
  }

}
