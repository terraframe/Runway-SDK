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
package com.runwaysdk.generation;

public class CommonGenerationUtil
{

  public static final String SET   = "set";
  public static final String GET   = "get";
  public static final String ADD   = "add";
  
  /**
   * Returns the given string with the first character capitalized.
   *
   * @param string
   *          String to capitalize
   * @return Input with the first character capitalized
   */
  public static String upperFirstCharacter(String string)
  {
    return string.substring(0,1).toUpperCase() + string.substring(1);
  }

  /**
   * Returns the given string with the first character lower cased.
   *
   * @param string
   *          String to lower case
   * @return Input with the first character lower cased
   */
  public static String lowerFirstCharacter(String string)
  {
    return string.substring(0,1).toLowerCase() + string.substring(1);
  }
  
  public static String replacePackageDotsWithSlashes(String pack)
  {
    return pack.replace('.', '/') + '/';
  }

  public static String replaceTypeDotsWithSlashes(String pack)
  {
    return pack.replace('.', '/');
  }
}
