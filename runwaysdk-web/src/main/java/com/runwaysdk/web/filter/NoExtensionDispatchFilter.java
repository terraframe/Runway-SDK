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

import com.runwaysdk.ClientException;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.web.json.JSONRunwayExceptionDTO;

/**
 * This Filter forwards all requests that don't have an extension to our
 * ServletDispatcher.
 */
public class NoExtensionDispatchFilter implements Filter
{
  // Always use the SLF4J logger.
  private static Logger log = LoggerFactory.getLogger(NoExtensionDispatchFilter.class);

  public NoExtensionDispatchFilter()
  {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    // Creating the ServletDispatcher will read the urlmap.xml file. If there's
    // a problem in urlmap.xml we want that exception to be thrown, so keep this
    // out of the try/catch.
    ServletDispatcher dis = new ServletDispatcher(false, false);

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

    // This regex matches all urls with no extension. (Test it @
    // http://regex101.com/)
    if (path != null && path.matches("^.*\\/[^\\.]*$") && dis.hasXmlMapping(req, resp))
    {
//      try
//      {
        dis.service(request, response);
//      }
//      catch (ClientException e)
//      {
//        log.warn("Exception thrown while dispatching request.", e);
//
//        JSONRunwayExceptionDTO ex = new JSONRunwayExceptionDTO(e);
//
//        resp.setStatus(500);
//        String json = ex.getJSON();
//        resp.getWriter().append(json);
//      }
    }
    else
    {
      // Just let container's default servlet do its job.
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy()
  {

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException
  {

  }
}
