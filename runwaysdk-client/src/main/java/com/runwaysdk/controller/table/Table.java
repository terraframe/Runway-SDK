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
package com.runwaysdk.controller.table;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ClientException;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.controller.table.Pagination.Page;
import com.runwaysdk.util.IDGenerator;

public class Table implements ColumnableIF
{
  /**
   * Class of the span element containing all of the pagination links
   */
  public static final String PAGINATION_SPAN_CLASS = "paginationSpan";

  public static final String CURRENT_PAGE_CLASS    = "currentPage";

  /**
   * List of columns in the table
   */
  private List<Column>       columns;

  /**
   * Table Writer
   */
  private StringWriter       writer;

  /**
   * Unique id of the table
   */
  private String             id;

  private String             classes;

  private String             odd;

  private String             even;

  /**
   * Number of rows in the table
   */
  private int                rowCount;

  /**
   * Flag indicating if the table contains a struct column
   */
  private boolean            structColumns;

  /**
   * The contex of the table
   */
  private Context            context;

  /**
   * The pagination of the table
   */
  private Pagination         pagination;

  public Table()
  {
    this.columns = new LinkedList<Column>();
    this.writer = new StringWriter();
    this.structColumns = false;
    this.context = new Context();
    this.pagination = null;
    this.id = IDGenerator.nextID();
  }

  /**
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.table.ColumnableIF#addColumn(com.runwaysdk.controller.table.Column)
   */
  public synchronized void addColumn(Column column)
  {
    if (column instanceof StructColumn)
    {
      structColumns = true;
    }

    column.setColumnable(this);
    columns.add(column);
  }

  public StringWriter getWriter()
  {
    return writer;
  }

  public void setRowCount(int rowCount)
  {
    this.rowCount = rowCount;
  }

  public void setContext(Context context)
  {
    this.context = context;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    return this.id;
  }

  public String getClasses()
  {
    return classes;
  }

  public void setClasses(String classes)
  {
    this.classes = classes;
  }

  public String getOdd()
  {
    return odd;
  }

  public void setOdd(String odd)
  {
    this.odd = odd;
  }

  public String getEven()
  {
    return even;
  }

  public void setEven(String even)
  {
    this.even = even;
  }

  public void accept(TableVisitor visitor) throws IOException
  {
    JspWriter out = visitor.getOut();

    visitor.setStructColumn(structColumns);
    visitor.setContext(context);

    out.print("<table id=\"" + id + "\"");

    if (classes != null)
    {
      out.print(" class=\"" + classes + "\"");
    }

    out.println(">");

    // Generate headers
    this.generateHeaders(visitor);

    // Generate all rows
    this.generateRows(visitor);

    // Generate footers
    this.generateFooter(visitor);

    out.println("</table>");

    // Generate pagination
    if (pagination != null)
    {
      out.println("<span class='" + PAGINATION_SPAN_CLASS + "'>");

      pagination.reset();

      while (pagination.hasNext())
      {
        Page page = pagination.next();

        if (!page.getLeftGap() && !page.getRightGap())
        {
          if (pagination.isAsynchronous())
          {
            Map<String, String> paramMap = context.generateParameterMap(page.getPageNumber());

            JSONObject params = new JSONObject();
            for (String key : paramMap.keySet())
            {
              try
              {
                params.put(key, paramMap.get(key));
              }
              catch (JSONException e)
              {
                String error = "The key [" + key + " could not be added to the Command Link.";
                throw new ClientException(error, e);
              }
            }

            String notifyCall = JSON.createControllerNotifyListenerCall(this.context.getAction());

            String spanId = this.id + "-" + page.getPageNumber();

            out.print("<span id='" + spanId + "'>" + page.getPageNumber() + "</span>");
            out.println("<script type=\"text/javascript\">");
            out.println("(function(){ ");
            out.println("  document.getElementById('" + spanId + "').onclick = function(){");
            out.println("    var params = " + params.toString() + ";");
            out.println("    " + notifyCall + "(params, '" + this.context.getAction() + "', '" + spanId + "');");
            out.println("  };");
            out.println("})();");
            out.println("</script>");
          }
          else
          {
            if (page.getCurrentPage())
            {
              out.print("<span class='" + CURRENT_PAGE_CLASS + "'>");
            }
            
            String link = page.generateLink(context);
            out.print("<a href = \"" + link + "\"> " + page.getPageNumber() + "</a>");
            
            if (page.getCurrentPage())
            {
              out.print("</span>");
            }
            
          }
        }
        else
        {
          out.print(page.getWriter().getBuffer().toString());
        }
      }

      out.println("</span>");
    }
  }

  private void generateHeaders(TableVisitor visitor) throws IOException
  {
    JspWriter out = visitor.getOut();

    out.println("<tr>");

    for (Column column : columns)
    {
      column.visitHeader(visitor);
    }

    out.println("</tr>");

    if (this.structColumns)
    {
      visitor.setStructColumn(false);

      out.println("<tr>");

      for (Column column : columns)
      {
        column.visitStructHeader(visitor);
      }

      out.println("</tr>");
    }
  }

  private void generateRows(TableVisitor visitor) throws IOException
  {
    JspWriter out = visitor.getOut();

    for (int i = 0; i < rowCount; i++)
    {
      String c = ( i % 2 == 0 ? even : odd );

      out.println("<tr>");

      for (Column column : columns)
      {
        column.visitRow(i, visitor, c);
      }

      out.println("</tr>");
    }
  }

  private void generateFooter(TableVisitor visitor) throws IOException
  {
    JspWriter out = visitor.getOut();

    out.println("<tr>");

    for (Column column : columns)
    {
      column.visitFooter(visitor);
    }

    out.println("</tr>");
  }

  public void setPagination(Pagination pagination)
  {
    this.pagination = pagination;
  }

  public Context getContext()
  {
    return context;
  }
}
