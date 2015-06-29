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
package com.runwaysdk.dataaccess.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;

/**
 * Writes an XML document to an output stream.
 *
 * @author Richard Rowlands
 * @date 15/05/2014
 */
public class OutputStreamMarkupWriter extends MarkupWriter
{
  /**
   * @param file File information of the markup file to create
   * @throws IOException
   */
  public OutputStreamMarkupWriter(OutputStream stream)
  {
    try
    {
      OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(stream), "UTF8");
      init(out);
    }
    catch (IOException e)
    {
      throwIOException(e);
    }
  }

  /**
   * Instantiates XMLWriter to a given a file with a specific encryption type
   * @param stream output stream to print to
   * @param encryption the type of encryption to be used
   * @throws Exception if unable to create the file
   */
  public OutputStreamMarkupWriter(OutputStream stream, String encryption) throws IOException
  {
    OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(stream), encryption);
    init(out);
  }

  /**
   * Throws custom exception for the given <code>IOException</code> depending
   * on what the output stream is writing to.
   *
   * @param e
   */
  protected void throwIOException(IOException e)
  {
    CommonExceptionProcessor.processException(ExceptionConstants.SystemException.getExceptionClass(), "An exception occurred while writing to OutputStream", e);
  }
}
