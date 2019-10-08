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
package com.runwaysdk.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FileResource implements ApplicationResource
{
  File file;
  
  public FileResource(File file)
  {
    this.file = file;
  }

  @Override
  public InputStream openNewStream()
  {
    try
    {
      return new FileInputStream(this.file);
    }
    catch (FileNotFoundException e)
    {
      throw new ResourceException(e);
    }
  }

  @Override
  public String getName()
  {
    return this.file.getName();
  }

  @Override
  public String getBaseName()
  {
    return FilenameUtils.getBaseName(this.getName());
  }

  @Override
  public String getNameExtension()
  {
    return FilenameUtils.getExtension(this.getName());
  }

  @Override
  public boolean isRemote()
  {
    return false;
  }

  @Override
  public void close()
  {
    
  }

  @Override
  public void delete()
  {
    FileUtils.deleteQuietly(this.file);
  }

  @Override
  public CloseableFile openNewFile()
  {
    return new CloseableFile(this.file);
  }
  
  @Override
  public File getUnderlyingFile()
  {
    return this.file;
  }
}
