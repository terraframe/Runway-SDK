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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class SimpleMapTagSupport extends SimpleTagSupport
{
  private Map<String, String> map;

  public SimpleMapTagSupport()
  {
    super();

    this.map = new HashMap<String, String>();
  }

  public void addAttribute(String key, String value)
  {
    map.put(key, value);
  }

  protected void removeAttribute(String key)
  {
    map.remove(key);
  }

  protected Map<String, String> getAttributes()
  {
    return map;
  }

  protected String getAttribute(String key)
  {
    return map.get(key);
  }

  protected void writeAttributes(JspWriter writer) throws IOException
  {
    writer.write(this.getAttributeBuffer());
  }

  protected String getAttributeBuffer()
  {
    StringBuffer buffer = new StringBuffer();
    Set<String> keys = map.keySet();

    for (String key : keys)
    {
      buffer.append(" " + key + "=\"" + map.get(key) + "\"");
    }

    return buffer.toString();
  }
  
  protected String getAttributeBuffer(String... keys)
  {
    StringBuffer buffer = new StringBuffer();
    
    for (String key : keys)
    {
      if(map.containsKey(key))
      {
        buffer.append(" " + key + "=\"" + map.get(key) + "\"");
      }
    }
    
    return buffer.toString();
  }

  protected void openTag(String tagname, JspWriter writer) throws IOException
  {
    // Write the value of the option
    writer.print("<" + tagname);

    this.writeAttributes(writer);

    writer.println(" >");
  }

  protected void closeTag(String tagname, JspWriter writer) throws IOException
  {
    writer.println("</" + tagname + ">");
  }

  protected void writeEmptyTag(String tagname, JspWriter writer) throws IOException
  {
    // Write the value of the option
    writer.print("<" + tagname);

    this.writeAttributes(writer);

    writer.println(" />");
  }
}
