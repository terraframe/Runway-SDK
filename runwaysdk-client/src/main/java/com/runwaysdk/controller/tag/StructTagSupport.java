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
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.generation.CommonGenerationUtil;

@TagAnnotation(name = "struct", bodyContent = "scriptless", description = "Tag to specify struct attributes")
public class StructTagSupport extends SimpleTagSupport implements ComponentMarkerIF
{
  /**
   * Name of the parent mutableDTO accessor
   */
  private String     componentParam;

  /**
   * Current StructDTO item
   */
  private MutableDTO item;

  /**
   * Name of the struct accessor
   */
  private String     param;

  @AttributeAnnotation(required = true, description = "The name of the struct accessor")
  public String getParam()
  {
    return componentParam + "." + param;
  }

  public void setParam(String param)
  {
    this.param = param;
  }

  /**
   * @see com.runwaysdk.controller.tag.ComponentMarkerIF#getItem()
   */
  public MutableDTO getItem()
  {
    return item;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);

    if (parent != null)
    {
      ComponentMarkerIF component = ( (ComponentMarkerIF) parent );

      // Get the MutableDTO on which the attribute StructDTO is defined
      MutableDTO mutableDTO = component.getItem();

      try
      {
        // Get the name of the method to retrive the the StructDTO out of the
        // MutableDTO
        String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(param);
        Class<?> clazz = mutableDTO.getClass();

        // Set the value of the current item to the StructDTO
        this.item = (MutableDTO) clazz.getMethod(methodName).invoke(mutableDTO);

        // Update the name of the parent mutableDTO accessor
        this.componentParam = component.getParam();
      }
      catch (Exception e)
      {
        // TODO Fix exception type
        e.printStackTrace();
      }
    }

    JspWriter out = this.getJspContext().getOut();

    out.println("<dl>");

    this.getJspBody().invoke(null);

    out.println("</dl>");
  }
}
