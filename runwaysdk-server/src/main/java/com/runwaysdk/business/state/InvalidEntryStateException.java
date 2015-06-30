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
package com.runwaysdk.business.state;

import com.runwaysdk.ServerExceptionMessageLocalizer;

public class InvalidEntryStateException extends StateException
{

  /**
   * 
   */
  private static final long serialVersionUID = -7892010239959513164L;

  /**
   * Name of the new state to be defined.
   */
  private String entryState;
  
  /**
   * Type that has a state machine.
   */
  private String type;
  
  /**
   * Thrown when an invalid entry state is set on an object.
   * Constructs a new StateException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data access
   *          layer information useful for application debugging. The developer message is
   *          saved for later retrieval by the {@link #getMessage()} method.
   * @param entryState
   *          Name of the new state to be defined.
   * @param type
   *          Type that has a state machine.
   */
  public InvalidEntryStateException(String devMessage, String entryState, String type)
  {
    super(devMessage);
    this.entryState = entryState;
    this.type = type;
  }

  /**
   * Thrown when an invalid entry state is set on an object.
   * Constructs a new StateException with the specified developer message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this StateException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data access
   *          layer information useful for application debugging. The developer message is
   *          saved for later retrieval by the {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()}
   *          method). (A <tt>null</tt> value is permitted, and indicates that the cause
   *          is nonexistent or unknown.)
   * @param entryState
   *          Name of the new state to be defined.
   * @param type
   *          Type that has a state machine.
   */
  public InvalidEntryStateException(String devMessage, Throwable cause, String entryState, String type)
  {
    super(devMessage, cause);
    this.entryState = entryState;
    this.type = type;
  }
  
  /**
   * Thrown when an invalid entry state is set on an object.
   * Constructs a new StateException with the specified cause and a developer
   * message taken from the cause. This constructor is useful if the
   * StateException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()}
   *          method). (A <tt>null</tt> value is permitted, and indicates that the cause
   *          is nonexistent or unknown.)
   * @param entryState
   *          Name of the new state to be defined.
   * @param type
   *          Type that has a state machine.
   */
  public InvalidEntryStateException(Throwable cause, String entryState, String type)
  {
    super(cause);
    this.entryState = entryState;
    this.type = type;
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.invalidEntryStateException(this.getLocale(), this.entryState, this.type);
  }
}
