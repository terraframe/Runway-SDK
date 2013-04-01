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
package com.runwaysdk.format;

import java.util.Locale;


public class CharacterFormat extends AbstractFormat<Character>
{

  public CharacterFormat()
  {
    super();
  }

  @Override
  public Character parse(String value, Locale locale) throws ParseException
  {
    if(!this.isValid(value))
    {
      return null;
    }
    
    try
    {
      return new Character(value.charAt(0));
    }
    catch(Throwable throwable)
    {
      throw this.createParseException(throwable, locale, value);
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
