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
package com.runwaysdk.business.generation;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.generation.CommonMarker;


/**
 * Abstract generator that generates a file in the Common directory
 * 
 * @author Justin Smethie
 */
public abstract class AbstractCommonGenerator extends AbstractGenerator implements CommonMarker
{ 
  /**
   * Generates a file in the Common directory with the given package and file name
   * 
   * @param mdTypeDAOIF Type for which this generator will generate code artifacts.
   * @param fileName The name of the file to generate
   */
  public AbstractCommonGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName)
  {
    super(mdTypeDAOIF, fileName);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.AbstractGenerator#getSourceDirectory()
   */
  protected String getRootSourceDirectory()
  {
    return getRootCommonBaseDirectory(this.getPackage());
  }
  
  @Override
  protected String getRootClassDirectory()
  {
    return getRootCommonBinDirectory(this.getPackage());
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
   * Returns the directory where the class files are located in common for the given package.
   * @param fileSystemPackage package as it appears on the file system.
   * @return directory where the class files are located in common for the given package.
   */
  protected static String getRootCommonBinDirectory(String fileSystemPackage)
  {   
    if (LocalProperties.isRunwayEnvironment() && MdTypeDAO.isSystemPackageFileSystem(fileSystemPackage))
    {      
      return RunwayProperties.getRunwayCommonBin()+"/";
    }
    else
    {
      return CLASS_DIRECTORY;
    }
  }
  
  /**
   * Returns the directory where the java base source is located in common for the given package.
   * @param fileSystemPackage package as it appears on the file system.
   * @return directory where the java source is located in common for the given package.
   */
  protected static String getRootCommonBaseDirectory(String fileSystemPackage)
  {   
    if (LocalProperties.isRunwayEnvironment() && MdTypeDAO.isSystemPackageFileSystem(fileSystemPackage))
    {      
      return RunwayProperties.getCommonSrc()+"/";
    }
    else
    {
      return BASE_DIRECTORY;
    }
  }

  /**
   * Returns the directory where the java stub source is located in common for the given package.
   * @param fileSystemPackage package as it appears on the file system.
   * @return directory where the java source is located in common for the given package.
   */
  public static String getRootCommonStubDirectory(String fileSystemPackage)
  {   
    if (LocalProperties.isRunwayEnvironment() && MdTypeDAO.isSystemPackageFileSystem(fileSystemPackage))
    {      
      return RunwayProperties.getClientSrc()+"/";
    }
    else
    {
      return SOURCE_DIRECTORY;
    }
  }
  
}
