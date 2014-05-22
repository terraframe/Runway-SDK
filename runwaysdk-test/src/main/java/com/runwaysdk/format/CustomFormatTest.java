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

/**
*
*/
package com.runwaysdk.format;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
public class CustomFormatTest extends TestCase
{
  // preserve the state before the test to reset it later.
  private final static boolean isDelegating = AbstractFormatFactory.isDelegating();
  private final static FormatFactory formatFactory = AbstractFormatFactory.getFormatFactory();
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite(CustomFormatTest.class.getSimpleName());
    suite.addTestSuite(CustomFormatTest.class);
    
    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }
  
  public static void classSetUp()
  {
    AbstractFormatFactory.setDelegating(true);
    AbstractFormatFactory.setFormatFactory(new CustomFormatFactory());
  }
  
  public static void classTearDown()
  {
    AbstractFormatFactory.setDelegating(isDelegating);
    AbstractFormatFactory.setFormatFactory(formatFactory);
  }

  /**
   * Ensures the setup was done correctly.
   */
  public void testInitialize()
  {
    assertTrue(AbstractFormatFactory.isDelegating());
    assertTrue(AbstractFormatFactory.getFormatFactory() instanceof DelegateFormatFactory);
  }
  
  /**
   * Ensures that the CustomFormatFactory is called to format the string
   * and remove any whitespace.
   */
  public void testCustomFormat()
  {
    String value = "  foo  ";
    String output = AbstractFormatFactory.getFormatFactory().getFormat(String.class).format(value);
    
    assertEquals(value.trim(), output);
  }
  
  /**
   * Ensures the CustomFormatFactory is called to display the string as lowercased.
   */
  public void testCustomDisplay()
  {
    String value = "FOO";
    String output = AbstractFormatFactory.getFormatFactory().getFormat(String.class).display(value);
    
    assertEquals(value.toLowerCase(), output);
  }
  
  /**
   * Ensures that the CustomFormatFactory is called to parse the string
   * and uppercase all letters.
   */
  public void testCustomParse()
  {
    String value = "foo";
    String output = AbstractFormatFactory.getFormatFactory().getFormat(String.class).parse(value);
    
    assertEquals(value.toUpperCase(), output);
  }
  
  /**
   * Tests format delegation by throwing in a type not recognized by
   * CustomFormatFactory. Due to delegation this should still work.
   */
  public void testDelegateFormat()
  {
    String output = AbstractFormatFactory.getFormatFactory().getFormat(Integer.class).format(new Integer(123));
    
    assertEquals("123", output);
  }
  
  /**
   * Tests display delegation by throwing in a type not recognized by
   * CustomFormatFactory. Due to delegation this should still work.
   */
  public void testDelegateDisplay()
  {
   String output = AbstractFormatFactory.getFormatFactory().getFormat(Integer.class).format(new Integer(123));
    
    assertEquals("123", output);
  }
  
  /**
   * Tests parse delegation by throwing in a type not recognized by
   * CustomFormatFactory. Due to delegation this should still work.
   */
  public void testDelegateParse()
  {
    Integer output = AbstractFormatFactory.getFormatFactory().getFormat(Integer.class).parse("123");
    
    assertEquals(new Integer(123), output);
  }
  
  /**
   * Disables delegation and runs a test which should fail because CustomFormatFactory can't
   * format the given value.
   */
  public void testDisableDelegation()
  {
    try
    {
      AbstractFormatFactory.setDelegating(false);
      AbstractFormatFactory.setFormatFactory(new CustomFormatFactory());

      // Integers are not handled by CustomFormatFactory so the call should return null
      Format<Integer> format = AbstractFormatFactory.getFormatFactory().getFormat(Integer.class);
      assertNull(format);
    }
    finally
    {
      // reset for other tests
      classSetUp();
    }
  }

  /**
   * CustomFormatFactory cannot accept null values without throwing a NullPointerException.
   * But if we are delegating correctly the exception will be handled and the value will be formatted.
   */
  public void testDelegateFormatOnError()
  {
    try
    {
      // prove the CustomFormatFactory can't handle nulls
      new CustomFormatFactory().getFormat(String.class).format(null);
      fail("CustomFormatFactory should have thrown an NPE.");
    }
    catch(NullPointerException e)
    {
      // expected
    }
    
    // now handle the null by delegation
    String value = AbstractFormatFactory.getFormatFactory().getFormat(String.class).format(null);
    assertNull(value);
  }
  
  /**
   * CustomFormatFactory cannot accept null values without throwing a NullPointerException.
   * But if we are delegating correctly the exception will be handled and the value will be displayed.
   */
  public void testDelegateDisplayOnError()
  {
    try
    {
      // prove the CustomFormatFactory can't handle nulls
      new CustomFormatFactory().getFormat(String.class).display(null);
      fail("CustomFormatFactory should have thrown an NPE.");
    }
    catch(NullPointerException e)
    {
      // expected
    }
    
    // now handle the null by delegation
    String value = AbstractFormatFactory.getFormatFactory().getFormat(String.class).display(null);
    assertNull(value);
  }
  
  /**
   * CustomFormatFactory cannot accept null values without throwing a NullPointerException.
   * But if we are delegating correctly the exception will be handled and the value will be parsed.
   */
  public void testDelegateParseOnError()
  {
    try
    {
      // prove the CustomFormatFactory can't handle nulls
      new CustomFormatFactory().getFormat(String.class).parse(null);
      fail("CustomFormatFactory should have thrown an NPE.");
    }
    catch(NullPointerException e)
    {
      // expected
    }
    
    // now handle the null by delegation
    String value = AbstractFormatFactory.getFormatFactory().getFormat(String.class).parse(null);
    assertNull(value);
  }
}
