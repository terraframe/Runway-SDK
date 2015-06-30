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

import com.runwaysdk.ClientExceptionMessageLocalizer;

/**
 * Exception thrown when an error occurs with the RMI ClientRequest. In general, this
 * exception is used to wrap any exception RMI throws.
 */
public class RMIClientException extends ClientRequestException
{
  /**
   * Auto-generated serial id
   */
  private static final long serialVersionUID = -5910739424230215626L;

  /**
   * Constructs a new <code>RMIClientException</code> with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * <code>RMIClientException</code> is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public RMIClientException(Throwable cause)
  {
    super(cause);
  }
  
  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  protected String defaultLocalizedMessage()
  {
    return ClientExceptionMessageLocalizer.rmiClientRequestException(this.getLocale());
  }
}
