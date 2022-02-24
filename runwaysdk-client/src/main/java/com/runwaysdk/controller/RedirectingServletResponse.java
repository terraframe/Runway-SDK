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
package com.runwaysdk.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.runwaysdk.ClientException;

/**
 * @author Darrell Taylor
 * 
 *         This class is used to render a jsp to a string for inside out rendering.
 * 
 */
public class RedirectingServletResponse extends HttpServletResponseWrapper
{
  private RedirectServletStream out;

  private PrintWriter           writer;

  private OutputStreamWriter    osw;

  public RedirectingServletResponse(HttpServletResponse response, OutputStream out, String encoding)
  {
    super(response);

    this.out = new RedirectServletStream(out);

    try
    {
      this.osw = new OutputStreamWriter(out, encoding);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new ClientException(e);
    }

    this.writer = new PrintWriter(osw, true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#flushBuffer()
   */
  public void flushBuffer() throws IOException
  {
    writer.flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#getOutputStream()
   */
  public ServletOutputStream getOutputStream() throws IOException
  {
    return out;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#getWriter()
   */
  public PrintWriter getWriter() throws IOException
  {
    return writer;
  }

  private static class RedirectServletStream extends ServletOutputStream
  {
    private OutputStream out;

    RedirectServletStream(OutputStream out)
    {
      this.out = out;
    }

    public void write(int param) throws java.io.IOException
    {
      this.out.write(param);
    }

    public boolean isReady()
    {
      return false;
    }

    public void setWriteListener(WriteListener writeListener)
    {

    }
  }

}
