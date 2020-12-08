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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

public class RedirectResponse implements ResponseIF
{
  private String url;
  
  private boolean addContext;
  
  private List<Cookie>        cookies = new ArrayList<Cookie>();
  
  public RedirectResponse(String url)
  {
    this.url = url;
    this.addContext = true;
  }
  
  public RedirectResponse(String url, boolean addContextPath)
  {
    this.url = url;
    this.addContext = addContextPath;
  }
  
  public void addCookie(Cookie cookie)
  {
    this.cookies.add(cookie);
  }
  
  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    ServletRequestIF req = manager.getReq();
    ServletResponseIF resp = manager.getResp();
    
    String contextPath = manager.getReq().getContextPath();

    if (contextPath.equals("") || contextPath.length() == 0)
    {
      contextPath = "/";
    }
    
    String finalUrl = this.url;
    
    if (this.addContext)
    {
      if (this.url.startsWith("/"))
      {
        finalUrl = req.getContextPath() + this.url;
      }
      else
      {
        finalUrl = req.getContextPath() + "/" + this.url;
      }
    }
    
    for (Cookie cookie : this.cookies)
    {
      if (cookie.getPath() == null || cookie.getPath().length() == 0)
      {
        cookie.setPath(contextPath);
      }
      
      resp.addCookie(cookie);
    }
    
    resp.sendRedirect(finalUrl);
  }
}
