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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class StreamResource implements ApplicationResource
{

  InputStream stream;
  
  String filename;
  
  public StreamResource(InputStream stream, String filename)
  {
    this.stream = stream;
    this.filename = filename;
  }
  
  @Override
  public InputStream openNewStream()
  {
    return this.stream;
  }

  @Override
  public String getName()
  {
    return this.filename;
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
    try
    {
      this.stream.close();
    }
    catch (IOException e)
    {
      throw new ResourceException(e);
    }
  }

  @Override
  public void delete()
  {
  }

  @Override
  public CloseableFile openNewFile()
  {
    try
    {
      Path path = Files.createTempFile(this.getBaseName(), "." + this.getNameExtension());
      
      CloseableFile tempFile = new CloseableFile(path.toFile().toURI(), true);
      
      try(FileOutputStream fos = new FileOutputStream(tempFile))
      {
        IOUtils.copy(this.openNewStream(), fos);
      }
      
      tempFile.deleteOnExit();
      
      return tempFile;
    }
    catch (IOException e)
    {
      throw new ResourceException(e);
    }
  }

  @Override
  public boolean exists()
  {
    return this.stream == null;
  }

  @Override
  public String getAbsolutePath()
  {
    return this.stream.toString();
  }
  
}
