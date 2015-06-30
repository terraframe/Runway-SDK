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
package com.runwaysdk.constants;


public interface UserInfo
{

  /**
   * The type name of the users class
   */
  public static final String TYPENAME = "Users";

  /**
   * The type of the users class
   */
  public static final String CLASS = Constants.SYSTEM_PACKAGE +"."+TYPENAME;

  /**
   * The ID of the metadata that defines the users calss.
   */
  public static final String ID_VALUE = "0000000000000000000000000000000300000000000000000000000000000001";

  /**
   * The name of the database talbe for the users class
   */
  public static final String TABLENAME = "users";

  /**
   * The username attribute of the users table
   */
  public static final String USERNAME         = "username";
  /**
   * The password attribute of the users table
   */
  public static final String PASSWORD         = "password";

  /**
   * The locale attribute of the users table
   */
  public static final String LOCALE           = "locale";

  /**
   * The concurrent session attribute of the users table
   */
  public static final String SESSION_LIMIT = "sessionLimit";

  /**
   * An inactive flag on the user
   */
  public static final String INACTIVE = "inactive";

  /**
   * The name of the public user.
   */
  public static final String PUBLIC_USER_NAME                =  "PUBLIC";

}
