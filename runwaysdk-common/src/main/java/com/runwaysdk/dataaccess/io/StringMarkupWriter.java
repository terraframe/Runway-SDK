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

import java.io.IOException;
import java.io.StringWriter;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;

public class StringMarkupWriter extends MarkupWriter
{
  /**
   *
   */
  public StringMarkupWriter()
  {
    super(new StringWriter());
  }

  /**
   * Returns a reference to the <code>StringWriter</code>
   * @return reference to the <code>StringWriter</code>
   */
  protected StringWriter getWriter()
  {
    return (StringWriter)super.getWriter();
  }

  /**
   * Throws custom exception for the given <code>IOException</code> depending
   * on what the output stream is writing to.
   *
   * @param e
   */
  protected void throwIOException(IOException e)
  {
    CommonExceptionProcessor.processException(ExceptionConstants.ProgrammingErrorException.getExceptionClass(), e.getMessage(), e);
  }

  /**
   * Returns the XML output as a string.
   * @return the XML output as a string.
   */
  public String getOutput()
  {
    return this.getWriter().getBuffer().toString();
  }
}
