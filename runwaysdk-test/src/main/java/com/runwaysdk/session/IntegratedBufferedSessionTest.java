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
package com.runwaysdk.session;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.util.FileIO;

@RunWith(ClasspathTestRunner.class)
public class IntegratedBufferedSessionTest extends IntegratedSessionTest
{
  private static String directory = LocalProperties.getSessionCacheDirectory();

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    SessionCacheInjector.createInjector(new Module()
    {
      public void configure(Binder binder)
      {
        binder.bind(SessionCache.class).toInstance(new BufferedSessionCache(new FileSessionCache(directory), new MemorySessionCache()));
      }
    });

    SessionFacade.reloadCache();

    IntegratedSessionTest.classSetUp();
  }

  @Request
  @AfterClass
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

    // Return the facade that the cache uses to its original form
    SessionCacheInjector.reloadInjector();
    SessionFacade.reloadCache();
  }
}
