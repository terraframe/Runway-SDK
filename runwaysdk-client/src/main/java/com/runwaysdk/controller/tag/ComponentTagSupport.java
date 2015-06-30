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
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "component", bodyContent = "scriptless", description = "Parent tag used when inputing component values")
public class ComponentTagSupport extends SimpleTagSupport implements ComponentMarkerIF
{
  /**
   * Name of the controller parameter
   */
  private String     param;

  /**
   * Instance of the component
   */
  private MutableDTO item = null;

  public void setItem(MutableDTO item)
  {
    this.item = item;
  }

  @AttributeAnnotation(required = true, rtexprvalue = true, description = "Instance of the component")
  public MutableDTO getItem()
  {
    return item;
  }

  public void setParam(String param)
  {
    this.param = param;
  }

  @AttributeAnnotation(required = true, description = "The name of the servlet parameter")
  public String getParam()
  {
    return param;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();

    doComponent(out, param, item);

    if (this.getJspBody() != null)
    {
      this.getJspBody().invoke(null);
    }
  }

  static void doComponent(JspWriter out, String param, MutableDTO item) throws IOException
  {
    out.println("<input type=\"hidden\" name=\"" + param + ".componentId\" value=\"" + item.getId() + "\" />");
    out.println("<input type=\"hidden\" name=\"" + param + ".isNew\" value=\"" + item.isNewInstance() + "\" />");
    out.println("<input type=\"hidden\" name=\"#" + param + ".actualType\" value=\"" + item.getType() + "\" />");

    if (item instanceof EntityDTO)
    {
      out.println("<input type=\"hidden\" name=\"#" + param + ".disconnected\" value=\"" + ( (EntityDTO) item ).isDisconnected() + "\" />");
    }

    // If the item is a RelationshipDTO and new then the componentId is
    // insufficient contextual information
    // thus the parent and child id must also be conveyed to the form
    if (item.isNewInstance() && item instanceof RelationshipDTO)
    {
      RelationshipDTO relationship = (RelationshipDTO) item;

      out.println("<input type=\"hidden\" name=\"#" + param + ".parent.id\" value=\"" + relationship.getParentId() + "\" />");
      out.println("<input type=\"hidden\" name=\"#" + param + ".child.id\" value=\"" + relationship.getChildId() + "\" />");
    }
  }
}
