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
package com.runwaysdk.query;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdClassDAOIF;

public class MissingHavingClauseAttributeException extends InvalidSelectableException
{
  /**
   *
   */
  private static final long serialVersionUID = 5405729832155224161L;

  private MdClassDAOIF definingMdClassDAOIF;

  /**
   * Constructs a new <code>MissingHavingClauseAttributeExceptio</code> with the specified developer message
   * and a default business message. Leaving the default business message is
   * discouraged, as it provides no context information for end users.
   *
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdAttributeConcreteDAOIF
   *          attribute metadata.
   * @param _selectable
   */
  public MissingHavingClauseAttributeException(String devMessage, Selectable _selectable, MdClassDAOIF definingMdClassDAOIF)
  {
    super(devMessage, _selectable);

    this.definingMdClassDAOIF = definingMdClassDAOIF;
  }

  /**
   * Constructs a new <code>MissingAttributeInSelectForGroupByException</code> with the specified detail message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this <code>RunwayException</code>'s detail message.
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
   * @param mdAttributeConcreteDAOIF
   *          attribute metadata.
   * @param _selectable
   */
  public MissingHavingClauseAttributeException(String devMessage, Throwable cause, Selectable _selectable, MdClassDAOIF definingMdClassDAOIF)
  {
    super(devMessage, cause, _selectable);

    this.definingMdClassDAOIF = definingMdClassDAOIF;
  }

  /**
   * Constructs a new <code>MissingAttributeInSelectForGroupByException</code> with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * <code>RunwayException</code> is a wrapper for another throwable.
   *
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param mdAttributeConcreteDAOIF
   *          attribute metadata.
   * @param _selectable
   */
  public MissingHavingClauseAttributeException(Throwable cause, Selectable _selectable, MdClassDAOIF definingMdClassDAOIF)
  {
    super(cause, _selectable);

    this.definingMdClassDAOIF = definingMdClassDAOIF;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   *
   * @param mdAttributeConcreteDAOIF
   *          attribute metadata.
   * @param definingMdClassDAOIF
   *          Entity that defines the attribute.
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.missingHavingClauseAttributeException(this.getLocale(), this.getMdAttributeConcreteDAOIF().getDisplayLabel(this.getLocale()), this.definingMdClassDAOIF.getDisplayLabel(this.getLocale()));
  }
}
