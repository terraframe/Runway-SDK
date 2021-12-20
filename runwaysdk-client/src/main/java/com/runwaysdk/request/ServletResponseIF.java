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
package com.runwaysdk.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;

public interface ServletResponseIF
{
  public PrintWriter getWriter() throws IOException;

  public OutputStream getOutputStream() throws IOException;

  public void setStatus(int sc);

  public void sendRedirect(String location) throws IOException;

  public void setContentType(String string);

  public int getStatus();

  public String getContentType();

  public void setHeader(String name, String value);
  

  /**
   * Adds the specified cookie to the response.  This method can be called
   * multiple times to set more than one cookie.
   *
   * @param cookie the Cookie to return to the client
   *
   */
  public void addCookie(Cookie cookie);
}
