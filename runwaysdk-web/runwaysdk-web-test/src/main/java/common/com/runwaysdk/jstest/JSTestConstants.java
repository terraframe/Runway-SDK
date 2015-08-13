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
package com.runwaysdk.jstest;

import com.runwaysdk.constants.Constants;

public class JSTestConstants {
	  /**
	   * Username of the user that has all permissions to modify the AjaxTest models.
	   */
	  public static final String USERNAME_WITH_ALL_PERMISSIONS = "AllPerm";
	  
	  /**
	   * Password for the user with all permissions.
	   */
	  public static final String USER_PASSWORD_WITH_ALL_PERMISSIONS = "Powerful";
	  
	  /**
	   * Username of the user that starts out with no initial permissions.
	   * (This user is required to test permission granting)
	   */
	  public static final String USERNAME_WITH_NO_PERMISSIONS = "NoPerm";
	  
	  /**
	   * Password for the user with no permissions.
	   */
	  public static final String USER_PASSWORD_WITH_NO_PERMISSIONS= "Powerless";
	  
	  
	  /**
	   * The base test package for AjaxTest.
	   */
	  public static final String TEST_PACKAGE = Constants.ROOT_PACKAGE+".jstest";
	  
	  /**
	   * The package used for running the invoke method tests.
	   * This is needed so that the outside project AjaxTest can
	   * reference it.
	   */
	  public static final String INVOKE_METHOD_TEST_PACKAGE = TEST_PACKAGE+".invokemethod";
	  
	  /**
	   * The package used for running the facade method tests.
	   * This is needed so that the outside project AjaxTest can
	   * reference it.
	   */
	  public static final String FACADE_METHOD_TEST_PACKAGE = TEST_PACKAGE+".facade";
	  
	  /**
	   * The package used for running the web service controller tests.
	   * This is needed so that the outside project AjaxTest can
	   * reference it.
	   */
	  public static final String CONTROLLER_TEST_PACKAGE = TEST_PACKAGE+".controller";
}
