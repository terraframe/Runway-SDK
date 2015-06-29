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
package com.runwaysdk.dataaccess.attributes;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.AttributeIF;

public class AttributeLengthByteException extends AttributeLengthException
{
  /**
   * 
   */
  private static final long serialVersionUID = 5749390675526011772L;

  /**
   * Constructs a new AttributeLengthException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data access
   *          layer information useful for application debugging. The developer message is
   *          saved for later retrieval by the {@link #getMessage()} method.
   * @param attribute
   *          The attribute that is too long.
   * @param maxLength
   *          Maximum allowable length.
   */
  public AttributeLengthByteException(String devMessage, AttributeIF attribute, Integer maxLength)
  {
    super(devMessage, attribute, maxLength);
  }

  /**
   * Constructs a new AttributeLengthException with the specified developer message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this AttributeLengthException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data access
   *          layer information useful for application debugging. The developer message is
   *          saved for later retrieval by the {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()}
   *          method). (A <tt>null</tt> value is permitted, and indicates that the cause
   *          is nonexistent or unknown.)
   * @param attribute
   *          The attribute that is too long.
   * @param maxLength
   *          Maximum allowable length.
   */
  public AttributeLengthByteException(String devMessage, Throwable cause, AttributeIF attribute, Integer maxLength)
  {
    super(devMessage, cause, attribute, maxLength);
  }
  
  /**
   * Constructs a new AttributeLengthException with the specified cause and a developer
   * message taken from the cause. This constructor is useful if the
   * AttributeLengthException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()}
   *          method). (A <tt>null</tt> value is permitted, and indicates that the cause
   *          is nonexistent or unknown.)
   * @param attribute
   *          The attribute that is too long.
   * @param maxLength
   *          Maximum allowable length.
   */
  public AttributeLengthByteException(Throwable cause, AttributeIF attribute, Integer maxLength)
  {
    super(cause, attribute, maxLength);
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters to set the
   * business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.attributeLengthByteException(this.getLocale(), this.attribute, this.maxLength);
  }
  
}
