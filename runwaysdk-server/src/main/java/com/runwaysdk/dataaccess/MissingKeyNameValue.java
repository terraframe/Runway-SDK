/**
 * 
 */
package com.runwaysdk.dataaccess;

import com.runwaysdk.ServerExceptionMessageLocalizer;

/*******************************************************************************
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
 ******************************************************************************/
public class MissingKeyNameValue extends DataAccessException
{

  /**
   * 
   */
  private static final long serialVersionUID = 448253504677700873L;
  
  /**
   * The metadata describing the type of the object with a missing key name value.
   */
  private MdEntityDAOIF mdEntityDAOIF;

  /**
   * Constructs a new <code>MissingKeyNameValue</code> with the specified developer
   * message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdEntityDAOIF
   *          The metadata describing the type of object missing a <code>EntityInfo.KEY</code> value
   */
  public MissingKeyNameValue(String devMessage, MdEntityDAOIF mdEntityDAOIF)
  {
    super(devMessage);
    this.mdEntityDAOIF = mdEntityDAOIF;
  }

  /**
   * Constructs a new <code>MissingKeyNameValue</code> with the specified developer message
   * and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this DataNotFoundException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdEntityDAOIF
   *          The metadata describing the type of object missing a <code>EntityInfo.KEY</code> value
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public MissingKeyNameValue(String devMessage, MdEntityDAOIF mdEntityDAOIF, Throwable cause)
  {
    super(devMessage, cause);
    this.mdEntityDAOIF = mdEntityDAOIF;
  }

  /**
   * Constructs a new <code>MissingKeyNameValue</code> with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * DataNotFoundException is a wrapper for another throwable.
   * 
   * @param mdEntityDAOIF
   *          The metadata describing the type of object missing a <code>EntityInfo.KEY</code> value
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public MissingKeyNameValue(MdEntityDAOIF mdEntityDAOIF, Throwable cause)
  {
    super(cause);
    this.mdEntityDAOIF = mdEntityDAOIF;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.missingKeyNameValue(this.getLocale(), this.mdEntityDAOIF);
  }
}
