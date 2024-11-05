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
package com.runwaysdk.web;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import com.runwaysdk.ClientException;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.web.json.JSONRunwayExceptionDTO;

public class MofoFilter implements Filter
{
  private ServletDispatcher dis;

  @Override
  public void init(FilterConfig arg0) throws ServletException
  {
    this.dis = new ServletDispatcher(true, true);
  }

  @Override
  public void destroy()
  {
    this.dis = null;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
  {
    try
    {
      dis.service(req, res);
    }
    catch (ClientException e)
    {
      JSONRunwayExceptionDTO ex = new JSONRunwayExceptionDTO(e);
      HttpServletResponse httpRes = (HttpServletResponse) res;

      httpRes.setStatus(500);
      String json = ex.getJSON();
      httpRes.getWriter().append(json);
    }
  }
}
