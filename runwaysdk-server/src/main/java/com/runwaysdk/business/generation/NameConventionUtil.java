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

package com.runwaysdk.business.generation;

import com.runwaysdk.dataaccess.metadata.ReservedWords;

/**
 * Utility class to build names according to valid Java conventions. Localization
 * is taken into account as well.
 */
public class NameConventionUtil
{
  /**
   * Builds an attribute name from the given input and suffix.
   * 
   * @param input
   * @param suffix
   * @return
   */
  public static String buildAttribute(String input, String suffix)
  {
    return buildInternal(input, suffix, false);
  }
  
  /**
   * Builds an attribute name from the given input.
   * @param input
   * @return
   */
  public static String buildAttribute(String input)
  {
    return buildInternal(input, "", false);
  }
  
  /**
   * Builds a class name from the given input and suffix.
   * 
   * @param input
   * @param suffix
   * @return
   */
  public static String buildClass(String input, String suffix)
  {
    return buildInternal(input, suffix, true);
  }
  
  /**
   * Builds a class name from the given input.
   * 
   * @param input
   * @return
   */
  public static String buildClass(String input)
  {
    return buildInternal(input, "", true);
  }
  
  /**
   * Internal method to build java standard identifiers.
   * 
   * @param input
   * @param suffix
   * @param isClassName
   * @return
   */
  private static String buildInternal(String input, String suffix, boolean isClassName)
  {
    String systemName = input;
    String name = input.replace("/", " Or ").replace("&", " And ");
    // String[] parts = name.split("[^a-zA-Z0-9]");
    String[] parts = name.split("[^\\p{L}0-9]");
    StringBuffer sb = new StringBuffer();
    if (parts.length == 1 && input.equals(input.toUpperCase()))
    {
      // It's an acronym, so use it as is.
      systemName = input;
    }
    else
    {
      // Create a camelcase representation of the input
      for (int i = 0; i < parts.length; i++)
      {
        String part = parts[i];
        if (part.length() > 0)
        {
          if (i == parts.length - 1)
          {
            // Last part
            String arabicPart = convertRomanToArabic(part);
            if (arabicPart.equals(part))
            {
              // Not a roman numeral
              sb.append(part.substring(0, 1).toUpperCase());
              sb.append(part.substring(1).toLowerCase());
            }
            else
            {
              // Roman numeral converted to arabic
              sb.append(arabicPart);
            }
          }
          else
          {
            sb.append(part.substring(0, 1).toUpperCase());
            sb.append(part.substring(1).toLowerCase());
          }
        }
      }
      systemName = sb.toString();

      if (ReservedWords.javaContains(systemName) || ReservedWords.sqlContains(systemName))
      {
        systemName += suffix;
      }

      if (!isClassName)
        systemName = systemName.substring(0, 1).toLowerCase() + systemName.substring(1);
    }
    return systemName;
  }
  
  /**
   * Converts Roman numbers to Arabic.
   * 
   * @param part
   * @return
   */
  private static String convertRomanToArabic(String part)
  {
    if ("IV".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 2) + "4";
    }
    if ("V".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 1) + "5";
    }
    if ("III".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 3) + "3";
    }
    if ("II".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 2) + "2";
    }
    if ("I".equals(part.toUpperCase()))
    {
      return part.substring(0, part.length() - 1) + "1";
    }
    return part;
  }
}
