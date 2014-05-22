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
package com.runwaysdk.web.javascript;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Custom HttpServletResponseWrapper subclass to manipulate any HttpServletResponse
 * that returns javascript.
 */
public class JavascriptResponseWrapper extends HttpServletResponseWrapper
{
  private JavascriptOutputStream outputStream;

  private PrintWriter writer;

  private HttpServletResponse response;

  /**
   * Constructs a JavascriptResponseWrapper with the given HttpServletResponse.
   *
   * @param response
   * @param gzip Flag denoting if the response should be gziped
   */
  public JavascriptResponseWrapper(HttpServletResponse response)
  {
    super(response);

    outputStream = null;
    writer = null;
    this.response = response;
  }

  /**
   * Creates a new JavascriptOutputStream.
   *
   * @return
   * @throws IOException
   */
  private JavascriptOutputStream createOutputStream() throws IOException
  {
    return new JavascriptOutputStream(response);
  }

  /**
   *
   */
  public JavascriptOutputStream getOutputStream() throws IOException
  {
    // check to comply with ServletResponse interface
    if(writer != null)
    {
      String error = "Cannot get the OutputStream because the PrintWriter has already been created.";
      throw new IOException(error);
    }

    if(outputStream == null)
    {
      outputStream = createOutputStream();
    }

    return outputStream;
  }

  public PrintWriter getWriter() throws IOException
  {
    // check to comply with ServletResponse interface
    if(outputStream != null)
    {
      String error = "Cannot get the PrintWriter because the ServletOutputStream has already been created.";
      throw new IOException(error);
    }

    if(writer == null)
    {
      writer = new PrintWriter(createOutputStream());
    }

    return writer;
  }

  public void finishResponse() throws IOException
  {
    if(writer != null)
    {
      writer.close();
    }

    if(outputStream != null)
    {
      outputStream.close();
    }
  }
}
