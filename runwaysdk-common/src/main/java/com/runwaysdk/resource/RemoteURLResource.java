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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteURLResource implements ApplicationResource
{

  private static final Logger logger = LoggerFactory.getLogger(RemoteURLResource.class);
  
  protected URL url;
  
  protected String filename;
  
  public RemoteURLResource(URL url, String filename)
  {
    this.url = url;
    this.filename = filename;
  }

  @Override
  public InputStream openNewStream()
  {
    logger.info("Downloading remote resource from [" + url.toString() + "].");
    
    try
    {
      return new BufferedInputStream(url.openStream());
    }
    catch (IOException e)
    {
      throw new ResourceException(e);
    }
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
        try (InputStream is = this.openNewStream())
        {
          IOUtils.copy(is, fos);
        }
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
  public File getUnderlyingFile()
  {
    throw new UnsupportedOperationException();
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
    return true;
  }

  @Override
  public boolean exists()
  {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public void close()
  {
    
  }

  @Override
  public void delete()
  {
    
  }
  
}
