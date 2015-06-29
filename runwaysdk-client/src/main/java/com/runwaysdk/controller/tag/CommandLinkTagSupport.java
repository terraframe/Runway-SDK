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
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ClientException;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "commandLink", bodyContent = "scriptless", description = "Input element to submit the form")
public class CommandLinkTagSupport extends CommandTagSupport implements LinkTagIF
{
  /**
   * Property name-value mapping
   */
  private Map<String, String> properties;

  public CommandLinkTagSupport()
  {
    this.properties = new HashMap<String, String>();
  }

  public void addProperty(String name, String value)
  {
    this.properties.put(name, value);
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
    JspWriter out = this.getJspContext().getOut();
    StringWriter body = new StringWriter();
    
    if (this.getJspBody() != null)
    {
      this.getJspBody().invoke(body);
    }

    // Build a list of all the property name-value mappings

    if (isAsynchronous())
    {
      this.writeAsynchronousLink(out, body);
    }
    else
    {
      writeSynchronusLink(out, body);
    }
  }

  private void writeSynchronusLink(JspWriter out, StringWriter body) throws UnsupportedEncodingException, IOException
  {
    String action = this.getAction();
    StringBuffer buffer = new StringBuffer();
    Set<String> keySet = properties.keySet();

    // Seperate each mapping with the '&' character
    for (String key : keySet)
    {
      String encoded = URLEncoder.encode(properties.get(key), "UTF-8");
      buffer.append("&" + key + "=" + encoded);
    }

    out.print("<a href=\"" + action);

    // Replace the first '&' with '?' to make a valid link
    if (keySet.size() > 0)
    {
      out.print("?" + buffer.toString().replaceFirst("&", ""));
    }

    out.println("\"" + this.getAttributeBuffer("id", "class") + ">" + body.toString() + "</a>");
  }

  private void writeAsynchronousLink(JspWriter out, StringWriter body) throws IOException
  {
    String action = this.getAction();
    Set<String> keySet = properties.keySet();
    JSONObject params = new JSONObject();
    for (String key : keySet)
    {
      try
      {
        params.put(key, properties.get(key));
      }
      catch (JSONException e)
      {
        String error = "The key [" + key + " could not be added to the Command Link.";
        throw new ClientException(error, e);
      }
    }

    String notifyCall = JSON.createControllerNotifyListenerCall(action);

    out.print("<span " + this.getAttributeBuffer("id", "class") + ">" + body.toString() + "</span>");
    out.println("<script type=\"text/javascript\">");
    out.println("(function(){ ");
    out.println("  document.getElementById('" + this.getId() + "').onclick = function(){");
    out.println("    var params = " + params.toString() + ";");
    out.println("    " + notifyCall + "(params, '" + action + "', '" + this.getId() + "');");
    out.println("  };");
    out.println("})();");
    out.println("</script>");
  }

}
