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

import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;

@TagAnnotation(bodyContent = "empty", name = "boolean", description = "Tag denoting a boolean input")
public class BooleanTagSupport extends InputElementTagSupport
{
  /**
   * Name of the controller parameter or attribute being inputed
   */
  private String param;

  private String trueLabel;

  private String falseLabel;

  public BooleanTagSupport()
  {
    super();
    
    this.addAttribute("type", "radio");
  }

  public void setParam(String param)
  {
    this.param = param;
  }

  @AttributeAnnotation(required = true, description = "The name of the controller parameter or attribute")
  public String getParam()
  {
    return param;
  }

  @AttributeAnnotation(description = "The display label for the true option", rtexprvalue = true)
  public String getTrueLabel()
  {
    return trueLabel;
  }

  public void setTrueLabel(String trueLabel)
  {
    this.trueLabel = trueLabel;
  }

  @AttributeAnnotation(description = "The display label for the false option", rtexprvalue = true)
  public String getFalseLabel()
  {
    return falseLabel;
  }

  public void setFalseLabel(String falseLabel)
  {
    this.falseLabel = falseLabel;
  }

  private void configureProperties(String id, Boolean checked, boolean value)
  {
    if (id != null)
    {
      String postfix = "positive";

      if (!value)
      {
        postfix = "negative";
      }

      this.addAttribute("id", id + "." + postfix);
    }

    if (checked)
    {
      this.addAttribute("checked", "checked");
    }
    else
    {
      this.removeAttribute("checked");
    }

    this.addAttribute("value", new Boolean(value).toString());
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);

    String name = this.getParam();
    String value = this.getValue();
    String _id = this.getId();

    // If the input tag is in the context of a component then
    // load update the parameter name and display value
    if (parent != null)
    {
      ComponentMarkerIF component = (ComponentMarkerIF) parent;
      MutableDTO item = component.getItem();

      name = component.getParam() + "." + this.getParam();

      if (this.getValue() == null)
      {
        value = item.getValue(this.getParam());
      }

      // set label defaults from metadata if no label is found
      try
      {
        AttributeBooleanDTO abDTO = ComponentDTOFacade.getAttributeBooleanDTO(item, this.getParam());
        if (trueLabel == null)
        {
          trueLabel = abDTO.getAttributeMdDTO().getPositiveDisplayLabel();
        }
        if (falseLabel == null)
        {
          falseLabel = abDTO.getAttributeMdDTO().getNegativeDisplayLabel();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

    }
    // set label defaults if no label is found
    if (trueLabel == null)
    {
      trueLabel = "true";
    }
    if (falseLabel == null)
    {
      falseLabel = "false";
    }
    
    this.addAttribute("name", name);
    
    this.writeOption(out, value, _id, true);
    this.writeOption(out, value, _id, false);
    
    this.addAttribute("id", _id);
  }

  private void writeOption(JspWriter out, String value, String _id, boolean option) throws IOException
  {
    String _label = option ? this.getTrueLabel() : this.getFalseLabel();
    String _value = new Boolean(option).toString();
    
    this.configureProperties(_id, value != null && value.equalsIgnoreCase(_value), option);

    this.writeEmptyTag("input", out);
    
    out.write(_label + " ");
  }
}
