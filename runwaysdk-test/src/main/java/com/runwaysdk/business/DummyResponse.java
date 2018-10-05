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
package com.runwaysdk.business;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class DummyResponse implements HttpServletResponse
{

  @Override
  public String getCharacterEncoding()
  {

    return null;
  }

  @Override
  public String getContentType()
  {

    return null;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException
  {

    return null;
  }

  @Override
  public PrintWriter getWriter() throws IOException
  {

    return null;
  }

  @Override
  public void setCharacterEncoding(String charset)
  {

  }

  @Override
  public void setContentLength(int len)
  {

  }

  @Override
  public void setContentLengthLong(long length)
  {

  }

  @Override
  public void setContentType(String type)
  {

  }

  @Override
  public void setBufferSize(int size)
  {

  }

  @Override
  public int getBufferSize()
  {

    return 0;
  }

  @Override
  public void flushBuffer() throws IOException
  {

  }

  @Override
  public void resetBuffer()
  {

  }

  @Override
  public boolean isCommitted()
  {

    return false;
  }

  @Override
  public void reset()
  {

  }

  @Override
  public void setLocale(Locale loc)
  {

  }

  @Override
  public Locale getLocale()
  {

    return null;
  }

  @Override
  public void addCookie(Cookie cookie)
  {

  }

  @Override
  public boolean containsHeader(String name)
  {

    return false;
  }

  @Override
  public String encodeURL(String url)
  {

    return null;
  }

  @Override
  public String encodeRedirectURL(String url)
  {

    return null;
  }

  @Override
  public String encodeUrl(String url)
  {

    return null;
  }

  @Override
  public String encodeRedirectUrl(String url)
  {

    return null;
  }

  @Override
  public void sendError(int sc, String msg) throws IOException
  {

  }

  @Override
  public void sendError(int sc) throws IOException
  {

  }

  @Override
  public void sendRedirect(String location) throws IOException
  {

  }

  @Override
  public void setDateHeader(String name, long date)
  {

  }

  @Override
  public void addDateHeader(String name, long date)
  {

  }

  @Override
  public void setHeader(String name, String value)
  {

  }

  @Override
  public void addHeader(String name, String value)
  {

  }

  @Override
  public void setIntHeader(String name, int value)
  {

  }

  @Override
  public void addIntHeader(String name, int value)
  {

  }

  @Override
  public void setStatus(int sc)
  {

  }

  @Override
  public void setStatus(int sc, String sm)
  {

  }

  @Override
  public int getStatus()
  {

    return 0;
  }

  @Override
  public String getHeader(String name)
  {

    return null;
  }

  @Override
  public Collection<String> getHeaders(String name)
  {

    return null;
  }

  @Override
  public Collection<String> getHeaderNames()
  {
    return null;
  }

}
