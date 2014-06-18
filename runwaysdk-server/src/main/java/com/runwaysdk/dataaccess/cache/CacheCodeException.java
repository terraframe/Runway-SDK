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
package com.runwaysdk.dataaccess.cache;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdElementDAOIF;


/**
 * Thrown when a new entity's cache algorithm is incompatible with its heirarchy
 * 
 * @author Eric Grunzke
 */
public class CacheCodeException extends DataAccessException
{
  /**
   * 
   */
  private static final long serialVersionUID = -7900823206350359730L;

  /**
   * The metadata describing the new type with the invalid caching algorithm
   */
  private MdElementDAOIF child;

  /**
   * The metadata describing the exiting parent type whose caching algorithm
   * conflicts with the new type's.
   */
  private MdElementDAOIF parent;

  /**
   * Constructs a new CacheCodeException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param child
   *          The metadata describing the new type with the invalid caching
   *          algorithm
   * @param parent
   *          The metadata describing the exiting parent type whose caching
   *          algorithm conflicts with the new type's.
   */
  public CacheCodeException(String devMessage, MdElementDAOIF child, MdElementDAOIF parent)
  {
    super(devMessage);
    this.child = child;
    this.parent = parent;
  }

  /**
   * Constructs a new CacheCodeException with the specified developer message
   * and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this CacheCodeException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param child
   *          The metadata describing the new type with the invalid caching
   *          algorithm
   * @param parent
   *          The metadata describing the exiting parent type whose caching
   *          algorithm conflicts with the new type's.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public CacheCodeException(String devMessage, MdElementDAOIF child, MdElementDAOIF parent, Throwable cause)
  {
    super(devMessage, cause);
    this.child = child;
    this.parent = parent;
  }

  /**
   * Constructs a new CacheCodeException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * CacheCodeException is a wrapper for another throwable.
   * 
   * @param child
   *          The metadata describing the new type with the invalid caching
   *          algorithm
   * @param parent
   *          The metadata describing the exiting parent type whose caching
   *          algorithm conflicts with the new type's.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public CacheCodeException(MdElementDAOIF child, MdElementDAOIF parent, Throwable cause)
  {
    super(cause);
    this.child = child;
    this.parent = parent;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.cacheCodeException(this.getLocale(), this.child, this.parent);
  }
}
