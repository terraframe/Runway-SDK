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
package com.runwaysdk.controller.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name="property", bodyContent="empty", description="Parameter name and value pairing")
public class PropertyTagSupport extends SimpleTagSupport
{
  /**
   * Name of the property
   */
  private String name;
  
  /**
   * Value of the property
   */
  private String value;
  
  public PropertyTagSupport()
  {
    this.name = null;
    this.value = null;
  }

  @AttributeAnnotation(required=true, description="Name of the property")
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @AttributeAnnotation(required=true, rtexprvalue=true, description="Value of the property")
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
     JspTag parent = SimpleTagSupport.findAncestorWithClass(this, LinkTagIF.class);
     
     if(parent != null)
     {
       ((LinkTagIF) parent).addProperty(name, value);
     }
  }
}
