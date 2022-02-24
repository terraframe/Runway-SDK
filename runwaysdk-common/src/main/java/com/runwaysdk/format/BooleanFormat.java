/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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


public class BooleanFormat extends AbstractFormat<Boolean>
{

  public BooleanFormat()
  {
    super();
  }

  @Override
  public Boolean parse(String value, Locale locale) throws ParseException
  {
    if(!this.isValid(value))
    {
      return null;
    }

    // Ensure the value is 'true' or 'false' before parsing because all string
    // besides 'true' return false instead of throwing an exception
    if (! ( value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false") || value
        .equalsIgnoreCase("on") ))
    {
      throw this.createParseException(locale, value);
    }

    try
    {

      if (value.equalsIgnoreCase("on"))
      {
        return new Boolean(true);
      }

      return Boolean.valueOf(value);
    }
    catch (Throwable t)
    {
      throw this.createParseException(t, locale, value);
    }
  }

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
}
