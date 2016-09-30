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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MockServletResponse implements ServletResponseIF
{
  private OutputStream ostream;

  private int          status;

  private String       location;

  private String       contentType;

  public MockServletResponse()
  {
    this.ostream = new ByteArrayOutputStream();
    this.status = 200;
  }

  @Override
  public PrintWriter getWriter() throws IOException
  {

    return null;
  }

  @Override
  public OutputStream getOutputStream() throws IOException
  {
    return ostream;
  }

  @Override
  public void setStatus(int sc)
  {
    this.status = sc;
  }

  @Override
  public int getStatus()
  {
    return this.status;
  }

  @Override
  public void sendRedirect(String location) throws IOException
  {
    this.location = location;
  }

  public String getRedirect()
  {
    return location;
  }

  @Override
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }

  public String getContentType()
  {
    return contentType;
  }
}
