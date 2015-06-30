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

import com.runwaysdk.controller.table.Entry;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "row", bodyContent = "scriptless", description = "Template for a single row in a table")
public class RowTagSupport extends SimpleTagSupport
{   
  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = findAncestorWithClass(this, ConcreteColumnTagIF.class);
    JspTag ancestor = findAncestorWithClass(this, ColumnableTagIF.class);

    if (parent != null && ancestor != null)
    {
      ConcreteColumnTagIF column = ( (ConcreteColumnTagIF) parent );
      ColumnableTagIF columnable = ((ColumnableTagIF) ancestor);

      // Get the name of the attribute being generated: This value may be null
      String attributeName = column.getAttributeName();

      // Reset the iterator of the collection in the columnable ancestor
      columnable.reset(attributeName);
      
      // For each item in the columnable collection generate a row
      while(columnable.hasNext())
      {                
        Entry row = new Entry();

        // Digest the content of the RowTag to build the row data structure 
        if(this.getJspBody() != null)
        {
          this.getJspBody().invoke(row.getWriter());
        }

        // Add the new row to the Column data-structure
        column.getColumn().addRow(row);
        
        // Increment the columnable iterator
        columnable.next(attributeName);
      }
    }
  }
}
