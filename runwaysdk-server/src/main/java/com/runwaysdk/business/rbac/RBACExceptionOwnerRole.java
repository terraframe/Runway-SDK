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
package com.runwaysdk.business.rbac;

import com.runwaysdk.ServerExceptionMessageLocalizer;

public class RBACExceptionOwnerRole extends RBACException
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private SingleActorDAOIF singleActor;
  private RoleDAO role;
  
  /**
   * Constructs a new RBACExceptionOwnerRole with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param singleActor
   * @param role
   */
  public RBACExceptionOwnerRole(String devMessage, SingleActorDAOIF singleActor, RoleDAO role)
  {
    super(devMessage);
    this.singleActor = singleActor;
    this.role = role;
  }

  /**
   * Constructs a new RBACExceptionOwnerRole with the specified developer message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this RBACExceptionOwnerRole's detail message.
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
   * @param singleActor
   * @param role
   */
  public RBACExceptionOwnerRole(String devMessage, Throwable cause, SingleActorDAOIF singleActor, RoleDAO role)
  {
    super(devMessage, cause);
    this.singleActor = singleActor;
    this.role = role;
  }

  /**
   * Constructs a new RBACExceptionOwnerRole with the specified cause and a developer
   * message taken from the cause. This constructor is useful if the
   * RBACExceptionOwnerRole is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param singleActor
   * @param role
   */
  public RBACExceptionOwnerRole(Throwable cause, SingleActorDAOIF singleActor, RoleDAO role)
  {
    super(cause);
    this.singleActor = singleActor;
    this.role = role;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.rBACExceptionOwnerRole(this.getLocale(), singleActor.getSingleActorName(), this.role.getRoleName());
  }
}
