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

/**
 * A common util class for MdAttributeBoolean objects
 * 
 * @author jsmethie
 *
 */
public class MdAttributeBooleanUtil extends MdAttributeUtil
{
  /**
   * Returns the type safe object with the given value for this attribute type.  Returns
   * null iff the value is an empty string.  Assumes the value is not null and represents
   * a valid value of the return type.
   * @param toParse
   * @return type safe object with the given value for this attribute type. 
   */
  public static Boolean getTypeSafeValue(String toParse)
  { 
    if (toParse == null || toParse.trim().equals(""))
    {
      return null;
    }
    else
    {
      return Boolean.parseBoolean(toParse);
    }
  }

  /**
   * Converts the value of the attribute (stored as an integer either 1 or 0) and returns a string
   * equivalent (either <code>MdAttributeBooleanInfo.TRUE</code> or <code>MdAttributeBooleanInfo.FALSE</code>).
   * @param value
   * @return value of the attribute (stored as an integer either 1 or 0) and returns a string
   */
  public static String convertIntToString(String value)
  {
    if (value.equals("1"))
    {
      return MdAttributeBooleanInfo.TRUE;
    }
    else if (value.equals("0"))
    {
      return MdAttributeBooleanInfo.FALSE;
    }
    else
    {
      return "";
    }
  }

  /**
     *Returns true if the given string is MdAttributeBooleanIF.TRUE, false if the value is MdAttributeBooleanIF.FALSE
     *
     * <br/><b>Precondition: </b> stringBoolean.getValue().equalsIgnoreCase(MdAttributeBooleanIF.TRUE) ||
     *                            stringBoolean.getValue().equalsIgnoreCase(MdAttributeBooleanIF.FALSE)
     *
     * @param stringBoolean string boolean value
     * @return
     */
    public static boolean getBooleanValue(String stringBoolean)
    {
      if (stringBoolean.trim().equalsIgnoreCase(MdAttributeBooleanInfo.TRUE) ||
          stringBoolean.trim().equals("1"))
      {
        return true;
      }
      else if (stringBoolean.trim().equalsIgnoreCase(MdAttributeBooleanInfo.FALSE) ||
          stringBoolean.trim().equals("0"))
      {
        return false;
      }
      else
      {
        return false;
      }
   }
    
   /**
    * Returns the corresponding boolean as a string, either {@link MdAttributeBooleanInfo.TRUE)
    * or {@link MdAttributeBooleanInfo.FALSE).
    * 
    * @param booleanValue
    * @return the corresponding boolean as a string, either {@link MdAttributeBooleanInfo.TRUE)
    * or {@link MdAttributeBooleanInfo.FALSE).
    */
   public static String getStringValue(boolean value)
   {
     if (value)
     {
       return MdAttributeBooleanInfo.TRUE;
     }
     else
     {
       return MdAttributeBooleanInfo.FALSE;
     }
   }

  /**
   * Format the the given value to the internal DAO  boolean format.  If it is not a valid
   * boolean value, than the original value is returned.
   * 
   * <br/><b>Precondition: </b> valueToValidate != null 
   * 
   * @param valueToValidate the primitive attribute value to be validated
   */
  public static String format(String valueToFormat)
  {
    if (valueToFormat.trim().equalsIgnoreCase(MdAttributeBooleanInfo.TRUE) ||
        valueToFormat.trim().equals("1"))
    {
      return "1";
    }
    else if (valueToFormat.trim().equalsIgnoreCase(MdAttributeBooleanInfo.FALSE) ||
        valueToFormat.trim().equals("0"))
    {
      return "0";
    }
  
    return valueToFormat;
  }
  
  /**
   * Format the the given value to the internal DAO  boolean format. If the integer equals 1, then true
   * is returned false otherwise.  
   * 
   * <br/><b>Precondition: </b> valueToValidate != null 
   * 
   * @param valueToValidate the primitive attribute value to be validated
   */
  public static String format(Integer valueToFormat)
  {
    if (valueToFormat == 1)
    {
      return "1";
    }
    else
    {
      return "0";
    }
  }
}
