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
package com.runwaysdk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.mvc.DelegatingServlet;

/**
 * This Filter forwards all requests that are mapped in urlmap.xml to our ServletDispatcher.
 */
public class UrlMapperFilter implements Filter
{
  // Always use the SLF4J logger.
  private static Logger     log = LoggerFactory.getLogger(UrlMapperFilter.class);

  private DelegatingServlet servlet;

  @Override
  public void init(FilterConfig arg0) throws ServletException
  {
    this.servlet = new DelegatingServlet();
  }

  @Override
  public void destroy()
  {
    this.servlet = null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    String path = null;
    HttpServletRequest req = null;
    HttpServletResponse resp = null;
    try
    {
      req = (HttpServletRequest) request;
      resp = (HttpServletResponse) response;
      path = req.getRequestURI().substring(req.getContextPath().length());
    }
    catch (Throwable e)
    {
      log.warn("Exception thrown while dispatching request, the request type was likely not HTTP.", e);
    }

    if (path != null && this.servlet.hasXmlMapping(req, resp))
    {
      servlet.service(request, response);
    }
    else
    {
      // Just let container's default servlet do its job.
      chain.doFilter(request, response);
    }
  }
}
