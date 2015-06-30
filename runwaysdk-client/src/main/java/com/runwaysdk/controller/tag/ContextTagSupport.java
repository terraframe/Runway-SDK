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
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.controller.table.Context;
import com.runwaysdk.controller.table.Table;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "context", bodyContent = "scriptless", description = "Context of the sort table")
public class ContextTagSupport extends SimpleTagSupport implements LinkTagIF
{
  /**
   * Context data-structure generated from this context tag
   */
  private Context context;

  public ContextTagSupport()
  {
    this.context = new Context();
  }

  public void setAction(String action)
  {
    // All actions must end in the appropriate suffix
    if (!action.contains(MdActionInfo.ACTION_SUFFIX))
    {
      action = action.concat(MdActionInfo.ACTION_SUFFIX);
    }

    context.setAction(action);
  }

  @AttributeAnnotation(required = true, description = "Action to take when form is submitted")
  public String getAction()
  {
    return context.getAction();
  }

  public void addProperty(String name, String value)
  {
    context.addParameter(name, value);
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = this.getParent();

    if (parent instanceof TableTagSupport)
    {
      if (this.getJspBody() != null)
      {
        this.getJspBody().invoke(null);
      }

      Table table = (Table) ( (TableTagSupport) parent ).getColumnar();

      table.setContext(context);
    }
  }
}
