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

import com.runwaysdk.controller.table.Column;
import com.runwaysdk.controller.table.Entry;
import com.runwaysdk.controller.table.SortableHeader;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.transport.attributes.AttributeDTO;

@TagAnnotation(name = "attributeColumn", bodyContent = "scriptless", description = "Defines a single column of a table")
public class AttributeColumnTagSupport extends SimpleTagSupport implements ConcreteColumnTagIF
{
  /**
   * Name of the attribute specified for the column
   */
  private String attributeName;

  /**
   * The Column data mapping which this tag creates
   */
  private Column column;

  public AttributeColumnTagSupport()
  {
    this.column = new Column(new SortableHeader(false));
  }

  @AttributeAnnotation(required = true, description = "The attribute of the column")
  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public Column getColumn()
  {
    return column;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = findAncestorWithClass(this, ColumnableTagIF.class);

    if (parent != null)
    {
      ColumnableTagIF columnable = ( (ColumnableTagIF) parent );

      // IMPORTANT: This value may be null as the parent columnable may be a non
      // readable struct attribute
      AttributeDTO attribute = columnable.getAttributeDTO(attributeName);

      // attrib will be null if this attribute is not readable
      // exit early if is this column is not readable
      if (attribute == null || !attribute.isReadable())
      {
        return;
      }

      if (this.getJspBody() != null)
      {
        // Invoke the body: This should generate the header, rows, and footer
        this.getJspBody().invoke(column.getWriter());

        // If the header has not been populated then generate a default
        // header
        if (!column.isHeaderPopulated())
        {
          generateHeader(attribute);
        }

        // If none of the rows have been populated then generate default
        // rows
        if (!column.hasRows())
        {
          generateRows(columnable);
        }
      }
      else
      {
        // A body has not been defined for the tag: Generate default
        // header and rows
        generateHeader(attribute);

        generateRows(columnable);
      }

      // Add the column to its parent Columnable data structure
      column.setAttributeName(attributeName);
      columnable.getColumnar().addColumn(column);
    }
  }

  /**
   * Generate a default row for each item in the result set
   * 
   * @param columnable
   */
  private void generateRows(ColumnableTagIF columnable)
  {
    columnable.reset(attributeName);

    while (columnable.hasNext())
    {
      Entry row = new Entry();

      Object value = columnable.getValue(attributeName);

      if (value != null)
      {
        row.getWriter().write(value.toString());
      }

      column.addRow(row);
      columnable.next(attributeName);
    }
  }

  /**
   * Generate a header
   */
  private void generateHeader(AttributeDTO attribute)
  {
    String label = attributeName;

    if (attribute != null)
    {
      label = attribute.getAttributeMdDTO().getDisplayLabel();
    }

    Entry header = column.getHeader();
    header.getWriter().write(label);
  }
}
