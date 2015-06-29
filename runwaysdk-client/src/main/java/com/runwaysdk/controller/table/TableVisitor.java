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
package com.runwaysdk.controller.table;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ClientException;
import com.runwaysdk.constants.JSON;


public class TableVisitor
{
  /**
   * The context of the table
   */
  private Context   context;
  
  private boolean isAsynchronous;
  
  /**
   * jsp writer
   */
  private JspWriter out;
  
  private String tableId;

  /**
   * Flag denoting if this table contains a struct column
   */
  private boolean   structColumn;

  public TableVisitor(JspWriter out, String tableId, boolean isAsynchronous)
  {
    this.out = out;
    this.tableId = tableId;
    this.isAsynchronous = isAsynchronous;
    this.structColumn = false;
  }

  public void setStructColumn(boolean structColumn)
  {
    this.structColumn = structColumn;
  }

  public JspWriter getOut()
  {
    return out;
  }

  /**
   * @return The rowspan of a non struct header in the table
   */
  public int getStandardRowspan()
  {
    return ( structColumn ? 2 : 1 );
  }
  
  /**
   * Encloses the content of an Entry in <td> and </td> tags 
   * 
   * @param entry Entry to write
   * 
   * @throws IOException
   */
  void visitEntry(Entry entry) throws IOException
  {
    StringBuffer buffer = new StringBuffer("<td");

    if(entry.getClasses() != null)
    {
      buffer.append(" class = \"" + entry.getClasses() + "\"");
    }
    
    buffer.append(">");
        
    out.println(buffer.toString());
    out.println(entry.getWriter().getBuffer().toString());
    out.println("</td>");
  }

  public void visitHeader(Header header, int rowspan, int colspan) throws IOException
  {
    out.println("<th rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\">");
    out.println(header.getWriter().getBuffer().toString());
    out.println("</th>");
  }
  
  public void visitSortableHeader(SortableHeader header, int rowspan, int colspan) throws IOException
  {
    String sortAttribute = header.getSortAttribute();
    String headerDisplay = header.getWriter().getBuffer().toString();
    
    if(isAsynchronous)
    {
      Map<String, String> paramMap = context.generateParameterMap(sortAttribute);
      
      JSONObject params = new JSONObject();
      for(String key : paramMap.keySet())
      {
        try
        {
          params.put(key, paramMap.get(key));
        }
        catch (JSONException e)
        {
          String error = "The key ["+key+" could not be added to the Command Link.";
          throw new ClientException(error, e);
        }  
      }
      
      String action = this.context.getAction();
      String notifyCall = JSON.createControllerNotifyListenerCall(action);
      
      String spanId = tableId + "-" + sortAttribute;

      out.print("<span id='"+spanId+"'>"+headerDisplay+"</span>");
      out.println("<script type=\"text/javascript\">");
      out.println("(function(){ ");
      out.println("  document.getElementById('"+spanId+"').onclick = function(){");
      out.println("    var params = "+params.toString()+";");
      out.println("    "+notifyCall+"(params, '"+action+"', '"+spanId+"');");
      out.println("  };");
      out.println("})();");
      out.println("</script>");
    }
    else
    {
      String href = context.generateLink(sortAttribute);

      out.println("<th rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\">");
      out.print("<a href=\"" + href + "\">");
      out.print(headerDisplay);
      out.println("</a>");
      out.println("</th>");
    }    

  }

  public void setContext(Context context)
  {
    this.context = context;
  }
}
