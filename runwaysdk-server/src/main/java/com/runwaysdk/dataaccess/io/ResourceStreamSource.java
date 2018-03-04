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
package com.runwaysdk.dataaccess.io;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.runwaysdk.ClasspathResource;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ResourceStreamSource implements StreamSource
{
  private String name;

  /**
   * 
   */
  public ResourceStreamSource(String name)
  {
    this.name = name;
  }
  
  public ResourceStreamSource(ClasspathResource res)
  {
    this.name = res.getAbsolutePath();
    
    if (this.name.startsWith("/"))
    {
      this.name = this.name.substring(1);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.StreamSource#getInputStream()
   */
  @Override
  public InputStream getInputStream()
  {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    return classLoader.getResourceAsStream(this.name);
  }
  
  @Override
  public String getToString()
  {
    return name;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.io.StreamSource#toURI()
   */
  @Override
  public URI toURI()
  {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    
    try
    {
      return classLoader.getResource(this.name).toURI();
    }
    catch (URISyntaxException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
