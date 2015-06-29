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
package com.runwaysdk.web.javascript;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runwaysdk.constants.ClientProperties;
import com.runwaysdk.web.json.JSONControllerServlet;

public class JavascriptFilter implements Filter
{
  public void destroy()
  {
  }

  public void init(FilterConfig config) throws ServletException
  {
  }

  /**
   * Filter method that looks for the
   * {@link JavascriptConstants#RUNWAY_JS_FILE} in the request object.
   * If that object is found, special transformations are performed on that
   * file. Otherwise, the filter chain continues as normal.
   *
   * @param req
   *            The ServletRequest
   * @param res
   *            The ServletResponse
   * @param chain
   *            The FilterChain
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
      ServletException
  {
    // check the request for the Runway javascript file
    HttpServletRequest httpRequest = (HttpServletRequest) req;

    String path = httpRequest.getRequestURI();

    if (path.endsWith(JSONControllerServlet.class.getSimpleName()))
    {
      httpRequest.setCharacterEncoding(JavascriptConstants.ENCODING);
      res.setCharacterEncoding(JavascriptConstants.ENCODING);

      String acceptEncoding = httpRequest.getHeader("accept-encoding");
      boolean doGzip = acceptEncoding != null && acceptEncoding.contains("gzip")
          && ClientProperties.getJavascriptGzip();

      if (doGzip)
      {
        JavascriptResponseWrapper wrapper = new JavascriptResponseWrapper((HttpServletResponse) res);

        String charEncoding = "gzip";
        String contentType = JavascriptConstants.CONTENT_TYPE;
        String enc = "gzip";
        wrapper.setHeader("Content-Encoding", enc);
        wrapper.setCharacterEncoding(charEncoding);
        wrapper.setContentType(contentType);

        chain.doFilter(req, wrapper);

        wrapper.finishResponse();
        return;
      }
      else
      {
        String charEncoding = JavascriptConstants.ENCODING;
        String contentType = JavascriptConstants.CONTENT_TYPE;
        res.setCharacterEncoding(charEncoding);
        res.setContentType(contentType);

        chain.doFilter(req, res);

        return;
      }
    }
    else
    {
      chain.doFilter(req, res);
    }
  }
}
