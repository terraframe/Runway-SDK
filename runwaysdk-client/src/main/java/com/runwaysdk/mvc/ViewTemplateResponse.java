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
package com.runwaysdk.mvc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;

import com.runwaysdk.controller.JSPFetcher;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

public class ViewTemplateResponse implements ResponseIF
{
  private Map<String, Object> attributes;

  private String              template;

  private String              path;

  public ViewTemplateResponse(String template, String directory, String view)
  {
    this(template, directory + File.separator + view);
  }

  public ViewTemplateResponse(String template, String path)
  {
    super();

    this.template = template;
    this.path = path;
    this.attributes = new HashMap<String, Object>();
  }

  public Map<String, Object> getAttributes()
  {
    return attributes;
  }

  public void set(String name, Object o)
  {
    this.attributes.put(name, o);
  }

  public Object getAttribute(String name)
  {
    return this.attributes.get(name);
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    ServletRequestIF req = manager.getReq();
    ServletResponseIF resp = manager.getResp();

    Set<Entry<String, Object>> entries = this.attributes.entrySet();

    for (Entry<String, Object> entry : entries)
    {
      req.setAttribute(entry.getKey(), entry.getValue());
    }

    req.setAttribute(JSPFetcher.INNER_JSP, this.path);
    req.getRequestDispatcher(this.template).forward(req, resp);
  }
}
