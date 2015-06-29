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
package com.runwaysdk.business;

import java.io.Serializable;

import com.runwaysdk.constants.EnumDTOInfo;


/**
 * A wrapper class used to transport Enumerations to and from the server.
 * This class follows the immutable pattern.  Each time a new enumeration
 * needs to be sent to or from the server a new EnumDTO needs to be created.
 * 
 * @author jsmethie
 *
 */
public class EnumDTO implements Serializable
{
  /**
   * Auto-generated eclipse UID
   */
  private static final long serialVersionUID = -4577317296892435810L;

  /**
   * The fully qualified name of the EnumDTO class
   */
  public static final String CLASS = EnumDTOInfo.CLASS;
 
  /**
   * The type of the enumeration
   */
  private String enumType;
    
  /**
   * The name of the enumerated item
   */
  private String enumName;
  
  /**
   * @param enumType The type of the enumeration
   * @param enumName The name of the enumerated item
   */
  public EnumDTO(String enumType, String enumName)
  {
    this.enumType = enumType;
    this.enumName = enumName;
  }
  
  /**
   * Returns the name of the enumerated item
   * @return
   */
  public String getEnumName()
  { 
    return enumName;
  }
  
  /**
   * Returns the type of the enumeration
   * @return
   */
  public String getEnumType()
  {
    return enumType;
  }
}
