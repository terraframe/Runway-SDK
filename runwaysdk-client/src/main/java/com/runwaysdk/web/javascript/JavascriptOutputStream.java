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
package com.runwaysdk.web.javascript;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;

public class JavascriptOutputStream extends ServletOutputStream
{
  /**
   * Flag denoting if this stream has been finalized or not.
   */
  private boolean               closed;

  private HttpServletResponse   original;

  private ByteArrayOutputStream baOS;

  private GZIPOutputStream      gZipOS;

  public JavascriptOutputStream(HttpServletResponse response) throws IOException
  {
    super();

    this.closed = false;
    this.original = response;
    this.baOS = new ByteArrayOutputStream();
    this.gZipOS = new GZIPOutputStream(baOS);
  }

  @Override
  public void write(int b) throws IOException
  {
    gZipOS.write(b);
  }

  @Override
  public void write(byte[] b) throws IOException
  {
    gZipOS.write(b);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException
  {
    gZipOS.write(b, off, len);
  }

  /**
   *
   */
  @Override
  public void flush() throws IOException
  {
    if (closed)
    {
      String error = "This output stream has already been closed.";
      throw new IOException(error);
    }

    gZipOS.flush();
  }

  /**
   *
   */
  @Override
  public void close() throws IOException
  {
    if (closed)
    {
      String error = "This output stream has already been closed.";
      throw new IOException(error);
    }

    gZipOS.finish();

    byte[] bytes = baOS.toByteArray();

    this.original.addHeader("Content-Length", Integer.toString(bytes.length));
    OutputStream output = this.original.getOutputStream();

    output.write(bytes);
    output.flush();
    output.close();

    closed = true;
  }

  public boolean isReady()
  {
    return false;
  }

  public void setWriteListener(WriteListener writeListener)
  {

  }
}
