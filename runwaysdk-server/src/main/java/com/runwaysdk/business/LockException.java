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
package com.runwaysdk.business;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.localization.CommonExceptionMessageLocalizer;


/**
 * Indicates an error while locking/unlocking an object
 *
 * @author Eric Grunzke
 */
public class LockException extends BusinessException
{
  /**
   *
   */
  private static final long serialVersionUID = 6635148125254443511L;


  private transient Entity entity = null;

  private String errorProperty = null;

  /**
   * Constructs a new LockException with the specified
   * developer message.
   *
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   */
  public LockException(String devMessage)
  {
    super(devMessage);
  }

  /**
   * Constructs a new LockException with the specified
   * developer message.
   *
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param entity entity on which a lock was attempted.
   * @param userId oid of the user attempting a lock.
   */
  public LockException(String devMessage, Entity entity, String errorProperty)
  {
    super(devMessage);
    this.entity = entity;
    this.errorProperty = errorProperty;
  }

  /**
   * Constructs a new LockException with the specified
   * developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this LockException's detail
   * message.
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
  public LockException(String devMessage, Throwable cause)
  {
    super(devMessage, cause);
  }

  /**
   * Constructs a new LockException with the specified cause
   * and a developer message taken from the cause. This constructor is useful if
   * the LockException is a wrapper for another throwable.
   *
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public LockException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   *
   * @param entity
   *          The entity that failed to lock/unlock
   */
  public String getLocalizedMessage()
  {
    if (this.entity != null && this.errorProperty != null)
    {
      return ServerExceptionMessageLocalizer.lockException(this.getLocale(), this.entity, this.errorProperty);
    }
    else
    {
      return CommonExceptionMessageLocalizer.runwayException(this.getLocale());
    }
  }
}
