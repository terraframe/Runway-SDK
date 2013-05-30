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

    InputSource inputSource = null;

    try
    {
      String uri = this.getClass().getClassLoader().getResource(systemId).toURI().toString();
      inputSource = new InputSource(uri);

      if (inputSource.getSystemId() == null)
      {
        throw new Exception("The classloader returned null.");
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
