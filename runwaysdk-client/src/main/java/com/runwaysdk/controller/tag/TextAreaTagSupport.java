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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspTag;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name="textarea", bodyContent = "scriptless", description="Text input tag")
public class TextAreaTagSupport extends InputElementTagSupport
{
  /**
   * Name of the controller parameter or attribute being inputed
   */
  private String param;

  public void setParam(String param)
  {
    this.param = param;
  }

  @AttributeAnnotation(required=true, description="The name of the controller parameter or attribute")
  public String getParam()
  {
    return param;
  }
  
  public void setCols(Integer columns)
  {
    this.addAttribute("columns", columns.toString());
  }
  
  @AttributeAnnotation(description="Specifies the visible width of a text-area")
  public Integer getCols()
  {
    return Integer.parseInt(this.getAttribute("columns"));
  }
  
  public void setRows(Integer rows)
  {
    this.addAttribute("rows", rows.toString());
  }

  @AttributeAnnotation(description="Specifies the visible number of rows in a text-area")
  public Integer getRows()
  {
    return Integer.parseInt(this.getAttribute("rows"));
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);

    String name = this.getParam();
    String displayValue = this.getValue();

    // If the input tag is in the context of a component then
    // load update the parameter name and display value
    if (parent != null)
    {
      ComponentMarkerIF component = (ComponentMarkerIF) parent;
      MutableDTO item = component.getItem();

      name = component.getParam() + "." + this.getParam();

      if (this.getValue() == null)
      {
        displayValue = item.getValue(this.getParam());
      }
    }

    this.addAttribute("name", name);
    
    this.openTag("textarea", out);
    
    out.write(displayValue);

    this.closeTag("textarea", out);
  }  
}
