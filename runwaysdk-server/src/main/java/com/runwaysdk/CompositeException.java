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
package com.runwaysdk;

import java.util.Collection;

public class CompositeException extends RunwayException
{
  /**
   * 
   */
  private static final long     serialVersionUID = 2240722709519031157L;

  private Collection<Throwable> exceptions;

  /**
   * Constructs a new ExceptionContainer with the specified developer message
   * and a default business message. Leaving the default business message is
   * discouraged, as it provides no context information for end users.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param problemList
   *          List of Problem objects.
   */
  public CompositeException(String devMessage, Collection<Throwable> exceptions)
  {
    super(devMessage);

    this.exceptions = exceptions;
  }

  /**
   * Constructs a new ProblemCollectionException with the specified detail
   * message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this ProblemCollectionException
   * detail message.
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
   * @param problemList
   *          List of Problem objects.
   */
  public CompositeException(String devMessage, Throwable cause, Collection<Throwable> exceptions)
  {
    super(devMessage, cause);

    this.exceptions = exceptions;
  }

  /**
   * Constructs a new ProblemCollectionException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * ProblemCollectionException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   * @param problemList
   *          List of Problem objects.
   */
  public CompositeException(Throwable cause, Collection<Throwable> exceptions)
  {
    super(cause);

    this.exceptions = exceptions;
  }

  public Collection<Throwable> getExceptions()
  {
    return exceptions;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    StringBuilder msg = new StringBuilder(ServerExceptionMessageLocalizer.problemCollectionException(this.getLocale()));

    for (Throwable exception : this.getExceptions())
    {
      msg.append("\n");
      msg.append(exception.getLocalizedMessage());
    }

    return msg.toString();
  }
}
