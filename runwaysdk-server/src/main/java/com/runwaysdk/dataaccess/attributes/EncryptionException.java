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
package com.runwaysdk.dataaccess.attributes;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;


public class EncryptionException extends AttributeException
{
  /**
   * Generated by Eclipse
   */
  private static final long serialVersionUID = 1968802575191476147L;
  
  /**
   * The invalid encrypted attribute
   */
  private MdAttributeEncryptionDAOIF attribute;

  /**
   * Constructs a new EncryptionException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data access
   *          layer information useful for application debugging. The developer message is
   *          saved for later retrieval by the {@link #getMessage()} method.
   * @param attribute
   *          The invalid encrypted attribute
   */
  public EncryptionException(String devMessage, MdAttributeEncryptionDAOIF attribute)
  {
    super(devMessage);
    this.attribute = attribute;
  }

  /**
   * Constructs a new EncryptionException with the specified developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this EncryptionException's detail message.
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
   *          The invalid encrypted attribute
   */
  public EncryptionException(String devMessage, Throwable cause, MdAttributeEncryptionDAOIF attribute)
  {
    super(devMessage, cause);
    this.attribute = attribute;
  }

  /**
   * Constructs a new EncryptionException with the specified cause and a developer message
   * taken from the cause. This constructor is useful if the EncryptionException is a
   * wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()}
   *          method). (A <tt>null</tt> value is permitted, and indicates that the cause
   *          is nonexistent or unknown.)
   * @param attribute
   *          The invalid encrypted attribute
   */
  public EncryptionException(Throwable cause, MdAttributeEncryptionDAOIF attribute)
  {
    super(cause);
    this.attribute = attribute;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters to set the
   * business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.encryptionException(this.getLocale(), this.attribute);
  }
}
