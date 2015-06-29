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
package com.runwaysdk.session;

import java.io.File;
import java.io.IOException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.util.FileIO;

public class IntegratedOverflowTest extends IntegratedSessionTest
{
  private static String directory = LocalProperties.getSessionCacheDirectory();
  
  /**
   * A suite() takes <b>this </b> <code>AttributeTest.class</code> and wraps
   * it in <code>MasterTestSetup</code>. The returned class is a suite of all
   * the tests in <code>AttributeTest</code>, with the global setUp() and
   * tearDown() methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    
    suite.addTestSuite(IntegratedOverflowTest.class);
    
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
  
  /**
   * The setup done before the test suite is run
   */
  public static void classSetUp()
  {
    SessionCacheInjector.createInjector(new Module()
    {
      public void configure(Binder binder)
      {
        OverflowSessionCache sessionCache = new OverflowSessionCache(
            new MemorySessionCache(3, 5000, 100),
            new FileSessionCache(LocalProperties.getSessionCacheDirectory()));
        
        binder.bind(SessionCache.class).toInstance(sessionCache);
      }
    });
    
    
    
    SessionFacade.reloadCache();
    
    IntegratedSessionTest.classSetUp();
  }
  
  public static void classTearDown()
  {
    IntegratedSessionTest.classTearDown();
    
    SessionFacade.clearSessions();
    
    try
    {
      FileIO.deleteDirectory(new File(directory));
    }
    catch (IOException e)
    {
      throw new FileReadException(new File(directory), e);
    }
    
    //Return the facade that the cache uses to its original form
    SessionCacheInjector.reloadInjector();
    SessionFacade.reloadCache();

  }
}
