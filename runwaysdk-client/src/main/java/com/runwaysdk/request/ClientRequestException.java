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

import com.runwaysdk.ClientException;
import com.runwaysdk.ClientExceptionMessageLocalizer;

/**
 * Thrown when an error occurs while creating or using a ClientRequest.
 * 
 * @author justin
 *
 */
public class ClientRequestException extends ClientException
{  
  /**
   * Auto-generated serial oid.
   */
  private static final long serialVersionUID = 6710563701340650225L;

  /**
   * Constructor.
   * 
   * @param devMessage
   */
  public ClientRequestException(String devMessage)
  {
    super(devMessage);
  }

  /**
   * Constructs a new ApplicationException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * <code>ClientRequestException</code> is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public ClientRequestException(Throwable cause)
  {
    super(cause);
  }
  
  /**
   * Constructs a new <code>ClientRequestException</code> with the specified developer message
   * and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this <code>ClientRequestException</code>'s detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public ClientRequestException(String devMessage, Throwable throwable)
  {
    super(devMessage, throwable);
  }
  
  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  protected String defaultLocalizedMessage()
  {
    return ClientExceptionMessageLocalizer.clientRequestException(this.getLocale());
  }

}
