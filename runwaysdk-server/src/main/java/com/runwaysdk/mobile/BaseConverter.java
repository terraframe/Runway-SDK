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
package com.runwaysdk.mobile;

public class BaseConverter
{
  // Courtesy of JavaConfessions
  // http://javaconfessions.com/2008/09/convert-between-base-10-and-base-62-in_28.html
  
  //CAREFUL: do NOT use the KEY_CONCATENATION_SEPARATOR found in IdConverter.java or you will be sorry!
  private static final String baseDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()-=+[]{}|,./<>?`~'";
  
  public static int getMaxBase() { return baseDigits.length(); }
  
  public static String fromDecimalToOtherBase ( int base, int decimalNumber ) {  
    if (base > getMaxBase()) {
      throw new IdConversionException("Invalid base specified.");
    }
    
    String tempVal = decimalNumber == 0 ? "0" : "";  
    int mod = 0;  

    while( decimalNumber != 0 ) {  
        mod = decimalNumber % base;  
        tempVal = baseDigits.substring( mod, mod + 1 ) + tempVal;  
        decimalNumber = decimalNumber / base;  
    }  

    return tempVal;  
  }
  
  public static String toMaxBase(int decimalNumber) {
    return fromDecimalToOtherBase(getMaxBase(), decimalNumber);
  }
}
