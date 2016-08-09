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
import javax.servlet.jsp.JspWriter;

import com.runwaysdk.ClientException;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.util.IDGenerator;

@TagAnnotation(name = "command", bodyContent = "empty", description = "Input element to submit the form")
public class CommandTagSupport extends ClassTagSupport
{
  /**
   * URL path for the generated post
   */
  private String  action;

  public CommandTagSupport()
  {
    super();

    this.addAttribute("value", "Submit");
//    this.addAttribute("type", "button");
    this.setId(IDGenerator.nextID());
  }

  public void setAction(String action)
  {
    this.action = action;
  }

  @AttributeAnnotation(required = true, description = "Action to take when form is submitted")
  public String getAction()
  {
    return action;
  }

  public void setValue(String value)
  {
    this.addAttribute("value", value);
  }

  @AttributeAnnotation(description = "The value of the command button")
  public String getValue()
  {
    return this.getAttribute("value");
  }

  public void setName(String name)
  {
    this.addAttribute("name", name);
  }

  @AttributeAnnotation(required = true, description = "The name of the command button")
  public String getName()
  {
    return this.getAttribute("name");
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();
    FormTagSupport formTag = (FormTagSupport) findAncestorWithClass(this, FormTagSupport.class);

    if(formTag != null)
    {
      formTag.addCommand(this);
    }
    else
    {
      String error = "The command tag with action ["+this.getAction()+"] is not located within a [form] tag.";
      ClientException ex = new ClientException(error);
      throw ex;
    }

    this.writeTag("button", this.getValue(), out);
  }
}
