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
package com.runwaysdk.web;

import com.runwaysdk.RunwayException;
import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.rbac.UserDAOIF;

/**
 * Exception thrown when a user tries to access the admin screen who does not have
 * permission or belong to the proper role.
 */
public class AdminScreenAccessException extends RunwayException
{

  /**
   * The User for which this exception has occured.
   */
  private UserDAOIF user;
  
  /**
   * 
   */
  private static final long serialVersionUID = 3255778121556844585L;

  /**
   * Constructor to set the developer message on the exception and the UserIF that represents
   * the user without permission.
   * 
   * @param devMessage
   */
  public AdminScreenAccessException(String devMessage, UserDAOIF user)
  {
    super(devMessage);
    this.user = user;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.adminScreenAccessException(this.getLocale(), user);
  }
}
