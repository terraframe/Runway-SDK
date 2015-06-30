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
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "groupOption", bodyContent = "scriptless", description = "Template for a check box option")
public class GroupOptionTagSupport extends CheckableTagSupport implements InputMarker
{
  public GroupOptionTagSupport()
  {
    super();
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = SimpleTagSupport.findAncestorWithClass(this, GroupTagSupport.class);
    JspWriter out = this.getJspContext().getOut();

    if (parent != null)
    {
      GroupTagSupport group = (GroupTagSupport) parent;
      String name = group.getParam();
      String type = group.getType();

      JspTag component = findAncestorWithClass(this, ComponentMarkerIF.class);

      // If the combo box is used in the context of a component then
      // the generated parameter name needs to prefix the name of the component
      if (component != null)
      {
        name = ( (ComponentMarkerIF) component ).getParam() + "." + name;
      }

      MutableDTO current = group.getItem();
      String valueAttribute = group.getValueAttribute();
      
      this.addAttribute("type", type);
      this.addAttribute("name", name);
      this.addAttribute("value", current.getValue(valueAttribute));

      this.openTag("input", out);
      
      if (this.getJspBody() != null)
      {
        this.getJspBody().invoke(null);
      }

      this.closeTag("input", out);
    }
  }
}
