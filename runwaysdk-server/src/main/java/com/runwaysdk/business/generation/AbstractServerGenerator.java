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
package com.runwaysdk.business.generation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;


/**
 * Abstract generator that generates a file in the Server directory
 * 
 * @author Justin Smethie
 */
public abstract class AbstractServerGenerator extends AbstractGenerator implements ServerMarker
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }
  
  /**
   * Generates a file in the Server directory with the given package and file name
   * 
   * @param mdTypeDAOIF Type for which this generator will generate code artifacts.
   * @param fileName The name of the file to generate
   */
  public AbstractServerGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName)
  {
    super(mdTypeDAOIF, fileName);
  }
    
  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.AbstractGenerator#getSourceDirectory()
   */
  protected String getRootSourceDirectory()
  {
    return getRootServerBaseDirectory(this.getPackage());
  }
  
  @Override
  protected String getRootClassDirectory()
  {
    return getRootServerBinDirectory(getPackage());
  }
  
  @Override
  public String getClassAttribute()
  {
    return null;
  }
  
  @Override
  public String getSourceAttribute()
  {
    return null;
  }

  /**
   * Returns the directory where the class files are located on the server for the given package.
   * @param fileSystemPackage package as it appears on the file system.
   * @return directory where the class files are located on the server for the given package.
   */
  protected static String getRootServerBinDirectory(String fileSystemPackage)
  {
    String directory = null;
    
    for (PluginIF pluginIF : pluginMap.values())
    {
      directory = pluginIF.getClassDirectory(fileSystemPackage);
      
      if (directory != null)
      {
        break;
      }
    }
    
    if (directory != null)
    {
      return directory;
    }
    else
    {
      if (LocalProperties.isRunwayEnvironment() && MdTypeDAO.isSystemPackageFileSystem(fileSystemPackage))
      {      
        return RunwayProperties.getRunwayServerBin()+"/";
      }
      else
      {
        return CLASS_DIRECTORY;
      }
    }
  }
  
  /**
   * Returns the directory where the java base source is located on the server for the given package.
   * @param fileSystemPackage package as it appears on the file system.
   * @return directory where the java source is located on the server for the given package.
   */
  protected static String getRootServerBaseDirectory(String fileSystemPackage)
  {   
    String directory = null;
    
    for (PluginIF pluginIF : pluginMap.values())
    {
      directory = pluginIF.getBaseDirectory(fileSystemPackage);
      
      if (directory != null)
      {
        break;
      }
    }
    
    if (directory != null)
    {
      return directory;
    }
    else
    {
      if (LocalProperties.isRunwayEnvironment() && MdTypeDAO.isSystemPackageFileSystem(fileSystemPackage))
      {      
        return RunwayProperties.getServerSrc()+"/";
      }
      else
      {
        return BASE_DIRECTORY;
      }
    }
  }
  
  /**
   * Returns the directory where the java stub source is located on the server for the given package.
   * @param fileSystemPackage package as it appears on the file system.
   * @return directory where the java source is located on the server for the given package.
   */
  protected static String getRootServerStubDirectory(String fileSystemPackage)
  {
    String directory = null;
    
    for (PluginIF pluginIF : pluginMap.values())
    {
      directory = pluginIF.getSourceDirectory(fileSystemPackage);
      
      if (directory != null)
      {
        break;
      }
    }
    
    if (directory != null)
    {
      return directory;
    }
    else
    {
      if (LocalProperties.isRunwayEnvironment() && MdTypeDAO.isSystemPackageFileSystem(fileSystemPackage))
      {      
        return RunwayProperties.getServerSrc()+"/";
      }
      else
      {
        return SOURCE_DIRECTORY;
      }
    }
  }
 
  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public String getClassDirectory(String fileSystemPackage);
    
    public String getBaseDirectory(String fileSystemPackage);

    public String getSourceDirectory(String fileSystemPackage);
  }
}
