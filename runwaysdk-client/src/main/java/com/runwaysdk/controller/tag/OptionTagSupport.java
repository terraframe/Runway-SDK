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

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "option", bodyContent = "scriptless", description = "Select option template")
public class OptionTagSupport extends StandardTagSupport
{
  public OptionTagSupport()
  {
    super();
  }

  @AttributeAnnotation(description = "Specifies that the option should be disabled when it first loads")
  public String getDisabled()
  {
    return this.getAttribute("disabled");
  }

  public void setDisabled(String disabled)
  {
    if (disabled.equalsIgnoreCase("disabled"))
    {
      this.addAttribute("disabled", disabled);
    }
  }

  @AttributeAnnotation(description = "Defines a label to use when using optgroup")
  public String getLabel()
  {
    return this.getAttribute("label");
  }

  public void setLabel(String label)
  {
    this.addAttribute("label", label);
  }

  @AttributeAnnotation(description = "Specifies that the option should appear selected (will be displayed first in the list)", rtexprvalue = true)
  public String getSelected()
  {
    return this.getAttribute("selected");
  }

  public void setSelected(String selected)
  {
    if (selected.equalsIgnoreCase("selected"))
    {
      this.addAttribute("selected", selected);
    }
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = SimpleTagSupport.findAncestorWithClass(this, SelectTagSupport.class);
    JspWriter out = this.getJspContext().getOut();

    if (parent != null)
    {
      SelectTagSupport select = (SelectTagSupport) parent;

      // Get the current item in the select collection
      ComponentDTO current = select.getCurrent();

      // Get the name of the attribute on which the value is determined
      String valueAttribute = select.getValueAttribute();

      // set selected=selected if the current value matches the value stored in
      // the DTO
      if (select.getSelectedValues().contains("|" + current.getValue(valueAttribute) + "|"))
      {
        this.setSelected("selected");
      }

      // Write the value of the option
      this.addAttribute("value", current.getValue(valueAttribute));

      this.openTag("option", out);

      if (this.getJspBody() != null)
      {
        this.getJspBody().invoke(null);
      }

      this.closeTag("option", out);
    }
  }
}
