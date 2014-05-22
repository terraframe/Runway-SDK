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
package com.runwaysdk.session;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class LocaleManagerTest extends TestCase
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(LocaleManagerTest.class);

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

  
  protected static void classSetUp()
  {
  }

  protected static void classTearDown()
  {
  }

  private Locale[] getLocales()
  {
    return new Locale[]{this.getSouthernLocale(), Locale.US, Locale.ENGLISH, Locale.CANADA_FRENCH, Locale.GERMANY};
  }

  private Locale getSouthernLocale()
  {
    return new Locale("en", "us", "south");
  }
  
  public void testBestFit()
  {
    Locale southern = this.getSouthernLocale();
    Locale[] locales = this.getLocales();
    Collection<Locale> set = new LinkedList<Locale>();
    
    set.add(southern);
    set.add(Locale.US);
    set.add(Locale.ENGLISH);
    
    Locale locale = new LocaleManager(locales, set).getBestFitLocale();
    
    assertEquals(southern, locale);
  }
  
  public void testTie()
  {
    Locale[] locales = this.getLocales();
    Collection<Locale> set = new LinkedList<Locale>();
    
    set.add(Locale.GERMANY);
    set.add(Locale.CANADA_FRENCH);
    
    Locale locale = new LocaleManager(locales, set).getBestFitLocale();
    
    assertEquals(Locale.CANADA_FRENCH, locale);    
  }

  public void testNoFit()
  {
    Locale[] locales = this.getLocales();
    Collection<Locale> set = new LinkedList<Locale>();
    
    set.add(Locale.CHINESE);
    set.add(Locale.JAPANESE);
    
    Locale locale = new LocaleManager(locales, set).getBestFitLocale();
    
    assertEquals(this.getSouthernLocale(), locale);    
  }
  
  public void testDefault()
  {
    Locale[] locales = this.getLocales();
    
    Locale locale = new LocaleManager(locales).getBestFitLocale();
    
    assertEquals(this.getSouthernLocale(), locale);    
  }
  
  public void testCompleteMatch()
  {
    Locale en = Locale.ENGLISH;
    Locale en_US = Locale.US;
    Locale en_GB = new Locale("en", "GB");
    Locale nl_BE = new Locale("nl", "BE");

    Locale[] locales = new Locale[]{en_US, en, nl_BE, en_GB};
    Collection<Locale> supported = new LinkedList<Locale>();    
    supported.add(en);
    supported.add(en_GB);
    supported.add(nl_BE);
    
    Locale locale = new LocaleManager(locales, supported).getBestFitLocale();
    
    assertEquals(en, locale);
  }
}
