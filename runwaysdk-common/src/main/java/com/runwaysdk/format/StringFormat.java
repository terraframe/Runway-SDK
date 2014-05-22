/**
 * 
 */
package com.runwaysdk.format;

import java.util.Locale;

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
public class StringFormat extends AbstractFormat<String>
{

  /**
   * 
   */
  public StringFormat()
  {
    super();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#format(java.lang.Object, java.util.Locale)
   */
  @Override
  public String format(Object value, Locale locale) throws FormatException
  {
    if(value == null)
    {
      return null;
    }
    
    try
    {
      return value.toString();
    }
    catch(Throwable t)
    {
      throw this.createFormatException(t, locale, value);
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#parse(java.lang.String, java.util.Locale)
   */
  @Override
  public String parse(String value, Locale locale) throws ParseException
  {
    if(value == null)
    {
      return null;
    }
    
    try
    {
      return value;
    }
    catch(Throwable t)
    {
      throw this.createParseException(t, locale, value);
    }
  }

}
