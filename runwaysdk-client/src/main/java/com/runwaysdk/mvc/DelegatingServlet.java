/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.runwaysdk.controller.ControllerVersion;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.controller.URLConfigurationManager;
import com.runwaysdk.controller.URLConfigurationManager.UriMapping;
import com.runwaysdk.request.RequestDecorator;
import com.runwaysdk.web.ServletUtility;

public class DelegatingServlet extends HttpServlet
{
  /**
   * 
   */
  private static final long       serialVersionUID = 1178843679987880045L;

  private URLConfigurationManager xmlMapper;

  private ServletDispatcher       v1;

  private DispatcherServlet       v2;

  public DelegatingServlet()
  {
    this(new URLConfigurationManager());
  }

  public DelegatingServlet(URLConfigurationManager manager)
  {
    this.xmlMapper = manager;

    this.v1 = new ServletDispatcher(manager);
    this.v2 = new DispatcherServlet(manager);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    this.delegate(new RequestManager(req, resp, ServletMethod.POST));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    this.delegate(new RequestManager(req, resp, ServletMethod.GET));
  }

  public void delegate(RequestManager manager) throws IOException
  {
    String servletPath = ServletUtility.getServletPath(manager.getReq());

    UriMapping uriMapping = xmlMapper.getMapping(servletPath);

    if (uriMapping != null && uriMapping.getVersion().equals(ControllerVersion.V2))
    {
      v2.checkAndDispatch(manager);
    }
    else
    {
      v1.checkAndDispatch(manager);
    }
  }

  public boolean hasXmlMapping(HttpServletRequest req, HttpServletResponse resp)
  {
    String servletPath = ServletUtility.getServletPath(new RequestDecorator(req));

    return ( xmlMapper.getMapping(servletPath) != null );
  }
}
