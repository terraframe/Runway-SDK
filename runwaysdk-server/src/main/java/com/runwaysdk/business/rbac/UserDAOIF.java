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
package com.runwaysdk.business.rbac;

import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;


public interface UserDAOIF extends SingleActorDAOIF
{
  /**
   * The name of the public user
   */
  public static final String PUBLIC_USER_NAME =  UserInfo.PUBLIC_USER_NAME;
  
  public static final String PUBLIC_USER_ID = ServerConstants.PUBLIC_USER_ID;
  
  /**
   * @return
   */
  public UserDAO getBusinessDAO();
  
  /**
   * Compares the input string password to the current user password.
   * 
   * @return true if the passwords match, false otherwise.
   */
  public boolean compareToPassword(String passwordToCompare);
  
  /**
   * Get the username
   * 
   * @return The username
   */
  public String getUsername();
  
  /**
   * Return the maximum number of sessions a user can have open concurrently
   * 
   * @return The maximum number of sessions a user can have open concurrently
   */
  public int getSessionLimit();

  /**
   * Return the inactive flag of the user
   * 
   * @return
   */
  public boolean getInactive();
  
  /**
   * Return the locale of the user.
   * 
   * @return A string representing the locale of the user.
   */
  public String getLocale();        
}
