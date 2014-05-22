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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspTag;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.DTOFacade;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

@TagAnnotation(name = "input", bodyContent = "scriptless", description = "Text input tag")
public class InputTagSupport extends InputElementTagSupport
{
  /**
   * Name of the controller parameter or attribute being inputed
   */
  private String param;

  public void setParam(String param)
  {
    this.param = param;
  }

  @AttributeAnnotation(required = false, description = "The name of the controller parameter or attribute")
  public String getParam()
  {
    return param;
  }

  public void setType(String type)
  {
    this.addAttribute("type", type);
  }

  @AttributeAnnotation(required = true, description = "The type of the input field")
  public String getType()
  {
    return this.getAttribute("type");
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);

    String _param = this.getParam();
    String name = _param;
    String displayValue = this.getValue();

    // If the input tag is in the context of a component then
    // load update the parameter name and display value
    if (parent != null)
    {
      ComponentMarkerIF component = (ComponentMarkerIF) parent;
      MutableDTO item = component.getItem();

      if (_param != null && !_param.equals(""))
      {
        DTOFacade facade = new DTOFacade(_param, item);

        name = component.getParam() + "." + _param;

        if (displayValue == null)
        {
          try
          {
            displayValue = facade.getValue().toString();
          }
          catch (Exception e)
          {
            displayValue = item.getValue(_param);
          }
        }
        // Automatically set Readonly if it is not manually set
        if (this.getReadonly() == null && !facade.isWritable())
        {
          this.setReadonly("true");
        }

        if (this.getMaxlength() == null)
        {

          try
          {
            AttributeMdDTO attributeMdDTO = facade.getAttributeMdDTO();

            if (attributeMdDTO instanceof AttributeCharacterMdDTO)
            {
              AttributeCharacterMdDTO attributeCharacter = (AttributeCharacterMdDTO) attributeMdDTO;
              this.setMaxlength(new Integer(attributeCharacter.getSize()).toString());
            }
          }
          catch (Exception e)
          {
            // Do Nothing
          }
        }
      }

    }

    if(name != null && !name.equals(""))
    {
      this.addAttribute("name", name);
    }

    if (displayValue != null && !displayValue.equals(""))
    {
      this.addAttribute("value", displayValue);
    }
    
    if(this.getJspBody() != null)
    {
      this.getJspBody().invoke(null);
    }
        
    this.writeEmptyTag("input", out);
  }
}
