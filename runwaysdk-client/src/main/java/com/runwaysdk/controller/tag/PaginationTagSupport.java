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
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.controller.table.Pagination;
import com.runwaysdk.controller.table.Table;
import com.runwaysdk.controller.table.Pagination.Page;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "pagination", bodyContent = "scriptless", description = "Tag to define pagination")
public class PaginationTagSupport extends SimpleTagSupport
{
  /**
   * The name of the scoped variable to assign each page object.
   */
  private String var;

  /**
   * The current Page data-structure being generated when as this PaginationTag is digested
   */
  private Page   current;

  public PaginationTagSupport()
  {
    current = null;
  }

  @AttributeAnnotation(description = "The name of the scoped variable to assign each page object.")
  public String getVar()
  {
    return var;
  }

  public void setVar(String var)
  {
    this.var = var;
  }
  
  /**
   * @return The current Page data-structure being generated
   */
  public Page getCurrent()
  {
    return current;
  }
  
  public boolean isAsynchronous()
  {
    Object asynchronous = this.getJspContext().findAttribute(ServletDispatcher.IS_ASYNCHRONOUS);

    if (asynchronous != null)
    {
      return (Boolean) asynchronous;
    }

    return false;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = SimpleTagSupport.findAncestorWithClass(this, TableTagSupport.class);

    if (parent != null)
    {
      TableTagSupport tag = (TableTagSupport) parent;
      Table table = (Table) tag.getColumnar();

      // Generate a new Pagination data-structure using the supplied query object
      Pagination pagination = new Pagination(tag.getQuery(), isAsynchronous());
      
      // Loop over all of the pages in the pagination
      while (pagination.hasNext())
      {
        // Update the current Page to generate
        current = pagination.next();

        if (var != null)
        {
          this.getJspContext().setAttribute(var, current);
        }

        // Generate the pre HTML, marker, and post HTML for the current page
        if (this.getJspBody() != null)
        {
          this.getJspBody().invoke(current.getWriter());
        }
      }

      // Add the pagination structure to the table
      table.setPagination(pagination);
    }
  }
}
