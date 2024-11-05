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

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import com.runwaysdk.request.ServletRequestIF;

public class ServletUtility
{

  public static Locale[] getLocales(HttpServletRequest req)
  {
    Enumeration<Locale> enumeration = req.getLocales();
    List<Locale> locales = new LinkedList<Locale>();

    while (enumeration.hasMoreElements())
    {
      locales.add(enumeration.nextElement());
    }

    return locales.toArray(new Locale[locales.size()]);
  }

  /**
   * This method strips the context path from the request URI and returns it.
   * Use this method to handle URI's in a context path agnostic manner.
   * 
   * @param request
   * @return
   */
  public static final String getServletPath(ServletRequestIF request)
  {
    String servletPath = request.getServletPath();

    if (!"".equals(servletPath))
    {
      return servletPath;
    }

    String requestUri = request.getRequestURI();
    int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
    int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.indexOf(request.getPathInfo());

    return requestUri.substring(startIndex, endIndex);
  }
}
