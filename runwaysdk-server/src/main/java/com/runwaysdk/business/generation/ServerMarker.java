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
package com.runwaysdk.business.generation;

import com.runwaysdk.constants.LocalProperties;

/**
 * A marker to denote a GeneratorIF that writes to the server directory
 * 
 * @author Justin Smethie
 */
public interface ServerMarker
{
  /**
   * The directory of server stub .java files
   */
  public static final String SOURCE_DIRECTORY = LocalProperties.getServerSrc() + "/stub/";
  
  /**
   * The directory of server base .java files
   */
  public static final String BASE_DIRECTORY = LocalProperties.getServerSrc() + "/base/";

  /**
   * The directory of server .class files
   */
  public static final String CLASS_DIRECTORY = LocalProperties.getServerBin();
}
