/**
*
*/
package com.runwaysdk.format;

import java.util.Locale;

import com.runwaysdk.constants.CommonProperties;

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
public class CustomFormatFactory implements FormatFactory
{
  private class StringFormat implements Format<String>
  {
    
    /**
     * For testing. Displays the value by lowercasing the toString() value.
     * 
     * @param value
     * @return
     * @throws DisplayException
     */
    @Override
    public String display(Object value) throws DisplayException
    {
      return value.toString().toLowerCase();
    }
    
    /* (non-Javadoc)
     * @see com.runwaysdk.format.Format#display(java.lang.Object, java.util.Locale)
     */
    @Override
    public String display(Object value, Locale locale) throws DisplayException
    {
      return this.display(value, CommonProperties.getDefaultLocale());
    }

    /**
     * Formats the string by removing all the whitespace from the front and back.
     */
    @Override
    public String format(Object value, Locale locale) throws FormatException
    {
      return value.toString().trim();
    }

    /* (non-Javadoc)
     * @see com.runwaysdk.format.Format#format(java.lang.Object)
     */
    @Override
    public String format(Object value) throws FormatException
    {
      return this.format(value, CommonProperties.getDefaultLocale());
    }

    /* (non-Javadoc)
     * @see com.runwaysdk.format.Format#parse(java.lang.Object, java.util.Locale)
     */
    @Override
    public String parse(Object value, Locale locale) throws ParseException
    {
      return this.parse(value.toString(), locale);
    }

    /* (non-Javadoc)
     * @see com.runwaysdk.format.Format#parse(java.lang.Object)
     */
    @Override
    public String parse(Object value) throws ParseException
    {
      return this.parse(value.toString());
    }

    /**
     * For testing this parses a string value and uppercases every letter.
     */
    @Override
    public String parse(String value, Locale locale) throws ParseException
    {
      return value.toUpperCase(locale);
    }

    /* (non-Javadoc)
     * @see com.runwaysdk.format.Format#parse(java.lang.String)
     */
    @Override
    public String parse(String value) throws ParseException
    {
      return this.parse(value, CommonProperties.getDefaultLocale());
    }
    
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.StandardFormat#getFormat(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> Format<T> getFormat(Class<T> clazz)
  {
    if(String.class.isAssignableFrom(clazz))
    {
      return (Format<T>) new StringFormat();
    }
    
    return null;
  }
  
  
}
