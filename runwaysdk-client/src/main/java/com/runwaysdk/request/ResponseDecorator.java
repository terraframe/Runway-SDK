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
package com.runwaysdk.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseDecorator implements ServletResponseIF
{
  private HttpServletResponse response;

  public ResponseDecorator(HttpServletResponse response)
  {
    this.response = response;
  }

  public HttpServletResponse getResponse()
  {
    return response;
  }

  @Override
  public OutputStream getOutputStream() throws IOException
  {
    return this.response.getOutputStream();
  }

  public PrintWriter getWriter() throws IOException
  {
    return this.response.getWriter();
  }

  @Override
  public int getStatus()
  {
    return this.response.getStatus();
  }

  @Override
  public void setStatus(int sc)
  {
    this.response.setStatus(sc);
  }

  @Override
  public void setContentType(String type)
  {
    this.response.setContentType(type);
  }

  @Override
  public String getContentType()
  {
    return this.response.getContentType();
  }

  @Override
  public void sendRedirect(String location) throws IOException
  {
    this.response.sendRedirect(location);
  }
  
  @Override
  public void setHeader(String name, String value)
  {
    this.response.setHeader(name, value);
  }
}
