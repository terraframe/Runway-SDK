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
package com.runwaysdk.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSPFetcher
{
  public static final String  INNER_JSP = "com.runwaysdk.INNER_JSP";

  public static final String  ENCODING  = "UTF-8";

  /**
   * Relatvie path of the jsp file
   */
  private String              path;

  /**
   * Encoding used to write the jsp file
   */
  private String              encoding;

  /**
   * Request object for the used to retrieve the jsp
   */
  private HttpServletRequest  request;

  /**
   * Response object for the
   */
  private HttpServletResponse response;

  public JSPFetcher(String path, HttpServletRequest request, HttpServletResponse response)
  {
    this(path, request, response, ENCODING);
  }

  public JSPFetcher(String path, HttpServletRequest request, HttpServletResponse response, String encoding)
  {
    this.path = path;
    this.encoding = encoding;
    this.request = request;
    this.response = response;
  }

  public ByteArrayOutputStream getByteStream() throws ServletException, IOException
  {
    // create an output stream - to file, to memory...
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    response.setCharacterEncoding(encoding);

    // create the "dummy" response object
    RedirectingServletResponse dummyResponse = new RedirectingServletResponse(response, out, encoding);

    // get a request dispatcher for the jsp template
    RequestDispatcher dispatcher = request.getRequestDispatcher(path);

    // execute the jsp and return the output stream
    dispatcher.include(request, dummyResponse);
    
    dummyResponse.flushBuffer();

    return out;
  }

  /**
   * This renders a jsp to a string, useful for emails and inside out rendering
   * 
   * @param request
   * @param response
   * @param path
   * 
   * @return
   */
  public String getString()
  {
    try
    {
      ByteArrayOutputStream out = this.getByteStream();

      try
      {
        return out.toString(encoding);
      }
      finally
      {
        out.close();
      }      
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.out);
      String text = "<pre> ";
      text += request.getAttribute("javax.servlet.forward.request_uri") + "\n\n";
      text += "Error in class: ";
      text += exception.getClass().getName() + "\n\n";
      text += exception.getLocalizedMessage() + "\n\n";
      text += request.getQueryString() + "\n\n";
      final Writer result = new StringWriter();
      final PrintWriter printWriter = new PrintWriter(result);
      exception.printStackTrace(printWriter);
      text += result.toString() + "\n\n</pre>";

      return text;
    }
  }

}
