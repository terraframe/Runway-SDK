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
package com.runwaysdk.generation.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

import com.runwaysdk.constants.LocalProperties;

public class GeneratedLoader extends URLClassLoader
{
  private String name;

  /**
   * @param urls
   * @param parent
   * @param factory
   */
  public GeneratedLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
  {
    super(urls, parent, factory);
  }

  /**
   * @param urls
   * @param parent
   */
  public GeneratedLoader(URL[] urls, ClassLoader parent)
  {
    super(urls, parent);
  }

  /**
   * @param urls
   */
  public GeneratedLoader(URL[] urls)
  {
    super(urls);
  }
  
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    return super.loadClass(name);
  }
  
  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    return super.loadClass(name, resolve);
  }    
  
  public static GeneratedLoader createClassLoader() throws MalformedURLException
  {
    return createClassLoader(Thread.currentThread().getContextClassLoader());
  }

  public static GeneratedLoader createClassLoader(ClassLoader parent) throws MalformedURLException
  {
    File common = new File(LocalProperties.getCommonGenBin() + "/");
    File client = new File(LocalProperties.getClientGenBin() + "/");
    File server = new File(LocalProperties.getServerGenBin() + "/");
    
    common.mkdirs();
    client.mkdirs();
    server.mkdirs();
    
    return new GeneratedLoader(new URL[] {
        common.toURI().toURL(),
        client.toURI().toURL(),
        server.toURI().toURL()
    }, parent);
  }
  
  public static GeneratedLoader isolatedClassLoader() throws MalformedURLException
  {
    File common = new File(LocalProperties.getCommonGenBin() + "/");
    File client = new File(LocalProperties.getClientGenBin() + "/");
    File server = new File(LocalProperties.getServerGenBin() + "/");
    
    common.mkdirs();
    client.mkdirs();
    server.mkdirs();
    
    return new GeneratedLoader(new URL[] {
        common.toURI().toURL(),
        client.toURI().toURL(),
        server.toURI().toURL()
    });
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }
  
  @Override
  public String toString()
  {
    if(name != null)
    {
      return super.toString() + " - " + name;
    }
    
    return super.toString();
  }
}