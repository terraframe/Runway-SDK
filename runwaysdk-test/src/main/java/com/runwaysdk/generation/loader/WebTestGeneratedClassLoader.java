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
package com.runwaysdk.generation.loader;

import java.io.File;
import java.net.MalformedURLException;

import com.runwaysdk.constants.TestProperties;

/**
 * Class loader that adds directories used by some web services tests.
 */
public class WebTestGeneratedClassLoader
{
  private static boolean init = false;
  
  private static void initURLs() throws MalformedURLException
  { 
//    File commonTestDirectory = new File(TestProperties.getWebTestCommonBin());
//    
//    File severTestDirectory = new File(TestProperties.getWebTestServerBin());
//    
//    File clientTestDirectory = new File(TestProperties.getWebTestClientBin());
//    
//    RunwayClassLoader.binDirs.add(commonTestDirectory);
//    RunwayClassLoader.binDirs.add(severTestDirectory);
//    RunwayClassLoader.binDirs.add(clientTestDirectory);
    
    init = true;
  }
  
  /**
   * Gets the class that represents the specified type. Will load the class if it isn't in
   * the JVM already.
   * 
   * @param type
   *          Fully qualified type to load
   * @return Class specified by the type name
   * @throws ClassNotFoundException 
   */
  @SuppressWarnings("unchecked")
  public synchronized static Class load(String type) throws ClassNotFoundException, MalformedURLException
  {
    if(!init)
    {
      LoaderDecorator.reload(); // ensure a loader is available
//      initURLs();
    }

    return LoaderDecorator.load(type);
  }
}
