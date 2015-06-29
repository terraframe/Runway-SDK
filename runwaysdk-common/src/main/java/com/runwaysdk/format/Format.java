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
package com.runwaysdk.format;

import java.util.Locale;


public interface Format<T>
{
  /**
   * Displays the given Object as a String using the given locale. The
   * returned representation is the user-friendly output of the value.
   * 
   * @param value
   * @param locale
   * @return
   * @throws DisplayException
   */
  public String display(Object value, Locale locale) throws DisplayException;
  
  /**
   * Displays the given Object as a String using the default locale of the locale. The
   * returned representation is the user-friendly output of the value.
   * 
   * @param value
   * @return
   * @throws DisplayException
   */
  public String display(Object value) throws DisplayException;
  
  /**
   * Formats the given Object as a String using the given locale. The
   * returned representation is for data transfer and may not be
   * a user-friendly representation of the value. For display purposes, in a 
   * user-interface for example, use Format.display().
   * 
   * @param value
   * @param locale
   * @throws FormatException
   * @return
   */
  public String format(Object value, Locale locale) throws FormatException;
  
  /**
   * Formats the given Object using the default locale of the system. The
   * returned representation is for data transfer and may not be
   * a user-friendly representation of the value. For display purposes, in a 
   * user-interface for example, use Format.display().
   * 
   * @param value
   * @return
   * @throws FormatException
   */
  public String format(Object value) throws FormatException;
  
  /**
   * Parses the toString() of the given value.
   * 
   * @param value
   * @param locale
   * @return
   * @throws ParseException
   */
  public T parse(Object value, Locale locale) throws ParseException;
  
  /**
   * Parses the toString() of the given value.
   * 
   * @param value
   * @return
   * @throws ParseException
   */
  public T parse(Object value) throws ParseException;
  
  /**
   * Parses the given String as a type-safe value using the given locale.
   * 
   * @param value
   * @param locale
   * @throws ParseException
   * @return
   */
  public T parse(String value, Locale locale) throws ParseException;
  
  /**
   * Parses the given String as a type-safe value using the default locale of the system.
   * 
   * @param value
   * @return
   * @throws ParseException
   */
  public T parse(String value) throws ParseException;
}
