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
package com.runwaysdk.session;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.BusinessException;

public class InvalidLocaleFormatException extends BusinessException
{
  /**
   * 
   */
  private static final long serialVersionUID = 8497174329279966173L;
  
  /**
   * Locale string unable to be parsed
   */
  private String localeString;

  /**
   * Constructs a new InvalidLocaleFormatException with the specified developer
   * message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   */
  public InvalidLocaleFormatException(String devMessage, String localeString)
  {
    super(devMessage);

    this.localeString = localeString;
  }

  /**
   * Constructs a new InvalidLocaleFormatException with the specified developer
   * message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this
   * InvalidLocaleFormatException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   */
  public InvalidLocaleFormatException(String devMessage, Throwable cause, String localeString)
  {
    super(devMessage, cause);

    this.localeString = localeString;
  }

  /**
   * Constructs a new InvalidLocaleFormatException with the specified cause and
   * a developer message taken from the cause. This constructor is useful if the
   * InvalidLocaleFormatException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   */
  public InvalidLocaleFormatException(Throwable cause, String localeString)
  {
    super(cause);

    this.localeString = localeString;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.InvalidLocaleFormatException(this.getLocale(), this.localeString);
  }

}
