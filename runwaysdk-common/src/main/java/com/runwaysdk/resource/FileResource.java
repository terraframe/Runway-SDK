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
package com.runwaysdk.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FileResource implements ApplicationTreeResource
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
    if (this.file instanceof CloseableFile)
    {
      ( (CloseableFile) this.file ).close();
    }
  }

  @Override
  public void delete()
  {
    FileUtils.deleteQuietly(this.file);
  }

  @Override
  public CloseableFile openNewFile()
  {
    if (this.file instanceof CloseableFile)
    {
      return (CloseableFile) this.file;
    }
    else
    {
      return new CloseableFile(this.file, false);
    }
  }
  
  @Override
  public File getUnderlyingFile()
  {
    return this.file;
  }

  @Override
  public boolean hasDataStream()
  {
    return this.file.isDirectory();
  }

  @Override
  public Iterator<ApplicationTreeResource> getChildren()
  {
    ArrayList<ApplicationTreeResource> children = new ArrayList<ApplicationTreeResource>();
    
    for (File file : this.file.listFiles())
    {
      children.add(new FileResource(file));
    }
    
    return children.iterator();
  }

  @Override
  public ApplicationTreeResource getParent()
  {
    return new FileResource(this.file.getParentFile());
  }

  @Override
  public boolean exists()
  {
    return this.file.exists();
  }

  @Override
  public ApplicationTreeResource getChild(String path)
  {
    return new FileResource(new File(this.file.getAbsolutePath() + File.pathSeparator + path));
  }
}
