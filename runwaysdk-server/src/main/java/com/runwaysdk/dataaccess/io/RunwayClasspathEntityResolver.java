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
package com.runwaysdk.dataaccess.io;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This entity resolver allows us to specify schemaLocations with a preceding 'classpath:'.
 * 
 * @author Richard Rowlands
 */
public class RunwayClasspathEntityResolver implements EntityResolver
{
  private Log log = LogFactory.getLog(RunwayClasspathEntityResolver.class);

  public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
  {
    if (systemId.startsWith("classpath:"))
    {
      systemId = systemId.replaceFirst("classpath:", "");
    }
    else
    {
      return null;
    }
    
    // We're fetching from the root classloader, which means the equivalent of having a / at the beginning of the path
    // For some reason, when you do put a preceeding / the root classloader won't find the resource. This is dumb so I'm
    // going to remove preceeding slashes to stop it from not finding a valid resource.
    if (systemId.startsWith("/")) {
      systemId = systemId.replaceFirst("/", "");
    }

    InputSource inputSource = null;

    try
    {
      URL url = this.getClass().getClassLoader().getResource(systemId);
      
      if (url == null)
      {
        throw new Exception("ClassLoader.getResource returned null.");
      }
      
      inputSource = new InputSource(url.toURI().toString());

      if (inputSource.getSystemId() == null)
      {
        throw new Exception("ClassLoader.getResource returned a url, but inputSource.getSystemId returned null.");
      }

      log.info("Runway SAX parser successfully resolved resource on classpath [" + systemId + "] to [" + inputSource.getSystemId() + "].");

      return inputSource;
    }
    catch (Exception e)
    {
      log.fatal("Runway SAX parser unable to resolve resource on classpath [" + systemId + "].", e);
      return null;
    }
  }
}
