/**
*
*/
package com.runwaysdk.format;

import java.util.Calendar;
import java.util.Date;

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
public abstract class AbstractFormatTest extends FormatTest
{
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getIntegerDisplay()
   */
  @Override
  protected String getIntegerDisplay()
  {
    return this.getIntegerString();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getLongDisplay()
   */
  @Override
  protected String getLongDisplay()
  {
    return this.getLongString();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getDoubleDisplay()
   */
  @Override
  protected String getDoubleDisplay()
  {
    return this.getDoubleString();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getFloatDisplay()
   */
  @Override
  protected String getFloatDisplay()
  {
    return this.getFloatString();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getDecimalDisplay()
   */
  @Override
  protected String getDecimalDisplay()
  {
    return this.getDecimalString();
  }
  
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getCharacterObject()
   */
  @Override
  protected final Character getCharacterObject()
  {
    return new Character('c');
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getCharacterString()
   */
  @Override
  protected final String getCharacterString()
  {
    return "c";
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getCharacterDisplay()
   */
  @Override
  protected String getCharacterDisplay()
  {
    return this.getCharacterString();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getStringObject()
   */
  @Override
  protected final String getStringObject()
  {
    return new String("test_string");
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getStringString()
   */
  @Override
  protected final String getStringString()
  {
    return "test_string";
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getStringDisplay()
   */
  @Override
  protected String getStringDisplay()
  {
    return this.getStringString();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getDateObject()
   */
  @Override
  protected Date getDateObject()
  {
    Calendar cal = Calendar.getInstance();
    cal.set(1984, 4, 17, 10, 9, 8);
    
    return cal.getTime();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getDateString()
   */
  @Override
  protected String getDateString()
  {
    return this.getDateObject().toString();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getDateDisplay()
   */
  @Override
  protected String getDateDisplay()
  {
    return this.getDateString();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getBooleanObject()
   */
  @Override
  protected final Boolean getBooleanObject()
  {
    return new Boolean(true);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getBooleanString()
   */
  @Override
  protected final String getBooleanString()
  {
    return "true";
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getBooleanDisplay()
   */
  @Override
  protected String getBooleanDisplay()
  {
    return this.getBooleanString();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getPrimitiveObject()
   */
  @Override
  protected final Byte getPrimitiveObject()
  {
    return new Byte((byte)0);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getPrimitiveString()
   */
  @Override
  protected final String getPrimitiveString()
  {
    return "0";
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatTest#getPrimitiveDisplay()
   */
  @Override
  protected String getPrimitiveDisplay()
  {
    return this.getPrimitiveString();
  }
  
}
