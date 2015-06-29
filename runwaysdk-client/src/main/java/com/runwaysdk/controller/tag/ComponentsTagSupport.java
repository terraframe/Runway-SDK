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
import javax.servlet.jsp.jstl.core.LoopTagStatus;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name="components", bodyContent="scriptless", description="Array of components")
public class ComponentsTagSupport extends ListTagSupport implements ComponentMarkerIF
{
  public void setParam(String param)
  {
    this.param = param;
  }

  @AttributeAnnotation(required=true, description="The name of the servlet parameter")
  public String getParam()
  {
    LoopTagStatus stat = this.getLoopStatus();
    int c = stat.getCount() - 1;

    return param + "_" + c;
  }

  @Override
  protected void invokeBody() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();

    LoopTagStatus stat = this.getLoopStatus();
    MutableDTO item = (MutableDTO) stat.getCurrent();

    ComponentTagSupport.doComponent(out, this.getParam(), item);

    if(this.getJspBody() != null)
    {
      this.getJspBody().invoke(null);
    }
  }
}
