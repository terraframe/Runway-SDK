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

import com.runwaysdk.controller.table.Pagination;
import com.runwaysdk.controller.table.Pagination.Page;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name="page", bodyContent="empty", description="Template for page anchors")
public class PageTagSupport extends SimpleTagSupport
{
  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag tag = SimpleTagSupport.findAncestorWithClass(this, PaginationTagSupport.class);
    
    if(tag != null)
    {
      Page page = ((PaginationTagSupport) tag).getCurrent();
      
      if(!page.getLeftGap() && !page.getRightGap())
      {
        // Print out a text marker which will replaced with the appropriate page number
        // when the pagination data structure is processed
        this.getJspContext().getOut().print(Pagination.PAGE_MARKER);
      }
    }
  }
}
