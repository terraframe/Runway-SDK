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
/**
*
*/
package com.runwaysdk.format;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

import junit.framework.TestCase;

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
public abstract class FormatTest extends TestCase
{
  protected static FormatFactory formatFactory;

  public static Locale getDefaultLocale()
  {
    Locale locale = CommonProperties.getDefaultLocale();
    
    // as far as we know the default locale will always be English
    // for the develop/test environment. If this ever changes, so too
    // will this test. TODO rename this to EnglishFormatTest...maybe
    if(!locale.equals(Locale.ENGLISH))
    {
      String msg = String.format("TestCase [%s] expected [%s] as the locale.", DefaultFormatTest.class, Locale.ENGLISH);
      throw new ProgrammingErrorException(msg);
    }
    
    return locale;
  }
  
  public static void classSetUp()
  {
    formatFactory = AbstractFormatFactory.getFormatFactory();
  }
  
  public static void classTearDown()
  {
  }
  
  protected abstract Locale getLocale();

  // Integer
  protected abstract Integer getIntegerObject();
  protected abstract String getIntegerString();
  protected abstract String getIntegerDisplay();
  
  public void testParseInteger()
  {
     String value = this.getIntegerString();
     Integer obj = formatFactory.getFormat(Integer.class).parse(value, this.getLocale());
     Integer expected = this.getIntegerObject();
     
     assertEquals(expected, obj);
  }
  
  public void testFormatInteger()
  {
    Integer value = this.getIntegerObject();
    String str = formatFactory.getFormat(Integer.class).format(value, this.getLocale());
    String expected = this.getIntegerString();
    
    assertEquals(expected, str);
  }
  
  public void testDisplayInteger()
  {
    Integer value = this.getIntegerObject();
    String str = formatFactory.getFormat(Integer.class).display(value, this.getLocale());
    String expected = this.getIntegerDisplay();
    
    assertEquals(expected, str);
  }
  
  // Long
  protected abstract Long getLongObject();
  protected abstract String getLongString();
  protected abstract String getLongDisplay();
  
  public void testParseLong()
  {
    String value = this.getLongString();
    Long obj = formatFactory.getFormat(Long.class).parse(value, this.getLocale());
    Long expected = this.getLongObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatLong()
  {
    Long value = this.getLongObject();
    String str = formatFactory.getFormat(Long.class).format(value, this.getLocale());
    String expected = this.getLongString();
    
    assertEquals(expected, str);
  }
  
  public void testDisplayLong()
  {
    Long value = this.getLongObject();
    String str = formatFactory.getFormat(Long.class).display(value, this.getLocale());
    String expected = this.getLongDisplay();
    
    assertEquals(expected, str);
  }

  // Decimal
  protected abstract BigDecimal getDecimalObject();
  protected abstract String getDecimalString();
  protected abstract String getDecimalDisplay();
  
  public void testParseDecimal()
  {
    String value = this.getDecimalString();
    BigDecimal obj = formatFactory.getFormat(BigDecimal.class).parse(value, this.getLocale());
    BigDecimal expected = this.getDecimalObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatDecimal()
  {
    BigDecimal value = this.getDecimalObject();
    String str = formatFactory.getFormat(BigDecimal.class).format(value, this.getLocale());
    String expected = this.getDecimalString();
    
    assertEquals(expected, str);
  }

  public void testDisplayDecimal()
  {
    BigDecimal value = this.getDecimalObject();
    String str = formatFactory.getFormat(BigDecimal.class).display(value, this.getLocale());
    String expected = this.getDecimalDisplay();
    
    assertEquals(expected, str);
  }
  
  // Double
  protected abstract Double getDoubleObject();
  protected abstract String getDoubleString();
  protected abstract String getDoubleDisplay();
  
  public void testParseDouble()
  {
    String value = this.getDoubleString();
    Double obj = formatFactory.getFormat(Double.class).parse(value, this.getLocale());
    Double expected = this.getDoubleObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatDouble()
  {
    Double value = this.getDoubleObject();
    String str = formatFactory.getFormat(Double.class).format(value, this.getLocale());
    String expected = this.getDoubleString();
    
    assertEquals(expected, str);
  }
  
  public void testDisplayDouble()
  {
    Double value = this.getDoubleObject();
    String str = formatFactory.getFormat(Double.class).format(value, this.getLocale());
    String expected = this.getDoubleDisplay();
    
    assertEquals(expected, str);
  }
  
  // Float
  protected abstract Float getFloatObject();
  protected abstract String getFloatString();
  protected abstract String getFloatDisplay();
  
  public void testParseFloat()
  {
    String value = this.getFloatString();
    Float obj = formatFactory.getFormat(Float.class).parse(value, this.getLocale());
    Float expected = this.getFloatObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatFloat()
  {
    Float value = this.getFloatObject();
    String str = formatFactory.getFormat(Float.class).format(value, this.getLocale());
    String expected = this.getFloatString();
    
    assertEquals(expected, str);
  }

  public void testDisplayFloat()
  {
    Float value = this.getFloatObject();
    String str = formatFactory.getFormat(Float.class).format(value, this.getLocale());
    String expected = this.getFloatDisplay();
    
    assertEquals(expected, str);
  }
  
  // Character
  protected abstract Character getCharacterObject();
  protected abstract String getCharacterString();
  protected abstract String getCharacterDisplay();
  
  public void testParseCharacter()
  {
    String value = this.getCharacterString();
    Character obj = formatFactory.getFormat(Character.class).parse(value, this.getLocale());
    Character expected = this.getCharacterObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatCharacter()
  {
    Character value = this.getCharacterObject();
    String str = formatFactory.getFormat(Character.class).format(value, this.getLocale());
    String expected = this.getCharacterString();
    
    assertEquals(expected, str);
  }

  public void testDisplayCharacter()
  {
    Character value = this.getCharacterObject();
    String str = formatFactory.getFormat(Character.class).format(value, this.getLocale());
    String expected = this.getCharacterDisplay();
    
    assertEquals(expected, str);
  }

  // String
  protected abstract String getStringObject();
  protected abstract String getStringString();
  protected abstract String getStringDisplay();
  
  public void testParseString()
  {
    String value = this.getStringString();
    String obj = formatFactory.getFormat(String.class).parse(value, this.getLocale());
    String expected = this.getStringObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatString()
  {
    String value = this.getStringObject();
    String str = formatFactory.getFormat(String.class).format(value, this.getLocale());
    String expected = this.getStringString();
    
    assertEquals(expected, str);
  }

  public void testDisplayString()
  {
    String value = this.getStringObject();
    String str = formatFactory.getFormat(String.class).format(value, this.getLocale());
    String expected = this.getStringDisplay();
    
    assertEquals(expected, str);
  }
  
  private void compare(int field, Calendar cal1, Calendar cal2)
  {
    assertEquals(cal1.get(field), cal2.get(field));
  }
  
  // Date
  protected abstract Date getDateObject();
  protected abstract String getDateString();
  protected abstract String getDateDisplay();
  
  public void testParseDate()
  {
    String value = this.getDateString();
    Date obj = formatFactory.getFormat(Date.class).parse(value, this.getLocale());
    Date expected = this.getDateObject();

    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(obj);
    
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(expected);

    compare(Calendar.MONTH, cal1, cal2);
    compare(Calendar.DAY_OF_MONTH, cal1, cal2);
    compare(Calendar.YEAR, cal1, cal2);
    compare(Calendar.HOUR, cal1, cal2);
    compare(Calendar.MINUTE, cal1, cal2);
    compare(Calendar.SECOND, cal1, cal2);
  }
  
  public void testFormatDate()
  {
    Date value = this.getDateObject();
    String str = formatFactory.getFormat(Date.class).format(value, this.getLocale());
    String expected = this.getDateString();
    
    assertEquals(expected, str);
  }
  
  public void testDisplayDate()
  {
    Date value = this.getDateObject();
    String str = formatFactory.getFormat(Date.class).format(value, this.getLocale());
    String expected = this.getDateDisplay();
    
    assertEquals(expected, str);
  }
  
  // Boolean
  protected abstract Boolean getBooleanObject();
  protected abstract String getBooleanString();
  protected abstract String getBooleanDisplay();
  
  public void testParseBoolean()
  {
    String value = this.getBooleanString();
    Boolean obj = formatFactory.getFormat(Boolean.class).parse(value, this.getLocale());
    Boolean expected = this.getBooleanObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatBoolean()
  {
    Boolean value = this.getBooleanObject();
    String str = formatFactory.getFormat(Boolean.class).format(value, this.getLocale());
    String expected = this.getBooleanString();
    
    assertEquals(expected, str);
  }
  
  public void testDisplayBoolean()
  {
    Boolean value = this.getBooleanObject();
    String str = formatFactory.getFormat(Boolean.class).format(value, this.getLocale());
    String expected = this.getBooleanDisplay();
    
    assertEquals(expected, str);
  }
  
  // Primitive (byte)
  protected abstract Byte getPrimitiveObject();
  protected abstract String getPrimitiveString();
  protected abstract String getPrimitiveDisplay();
  
  public void testParsePrimitive()
  {
    String value = this.getPrimitiveString();
    Byte obj = formatFactory.getFormat(byte.class).parse(value, this.getLocale());
    Byte expected = this.getPrimitiveObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatPrimitive()
  {
    byte value = this.getPrimitiveObject();
    String str = formatFactory.getFormat(byte.class).format(value, this.getLocale());
    String expected = this.getPrimitiveString();
    
    assertEquals(expected, str);
  }
  
  public void testDisplayPrimitive()
  {
    byte value = this.getPrimitiveObject();
    String str = formatFactory.getFormat(byte.class).format(value, this.getLocale());
    String expected = this.getPrimitiveDisplay();
    
    assertEquals(expected, str);
  }

  public void testParsePrimitiveObject()
  {
    String value = this.getPrimitiveString();
    byte obj = formatFactory.getFormat(Byte.class).parse(value, this.getLocale());
    byte expected = this.getPrimitiveObject();
    
    assertEquals(expected, obj);
  }
  
  public void testFormatPrimitiveObject()
  {
    Byte value = this.getPrimitiveObject();
    String str = formatFactory.getFormat(Byte.class).format(value, this.getLocale());
    String expected = this.getPrimitiveString();
    
    assertEquals(expected, str);
  }

  public void testDisplayPrimitiveObject()
  {
    Byte value = this.getPrimitiveObject();
    String str = formatFactory.getFormat(Byte.class).format(value, this.getLocale());
    String expected = this.getPrimitiveDisplay();
    
    assertEquals(expected, str);
  }
  
  public void testParseException()
  {
    try
    {
      String input = "asdf";
      Integer value = formatFactory.getFormat(Integer.class).parse(input, this.getLocale());
      
      fail("Able to incorrectly parse ["+input+"] into the Integer ["+value+"].");
    }
    catch(ParseException e)
    {
      // expected
    }
    catch(Throwable t)
    {
      fail(t.getLocalizedMessage());
    }
  }
  
  public void testFormatException()
  {
    try
    {
      String input = "asdf";
      String value = formatFactory.getFormat(Integer.class).format(input, this.getLocale());
      
      fail("Able to incorrectly format the Integer ["+input+"] into ["+value+"].");
    }
    catch(FormatException e)
    {
      // expected
    }
    catch(Throwable t)
    {
      fail(t.getLocalizedMessage());
    }
  }
}
