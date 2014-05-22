/**
 * 
 */
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class FileStreamSource implements StreamSource
{
  private File file;

  /**
   * 
   */
  public FileStreamSource(File file)
  {
    this.file = file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.util.StreamSource#getInputStream()
   */
  @Override
  public InputStream getInputStream()
  {
    try
    {
      return new FileInputStream(file);
    }
    catch (FileNotFoundException e)
    {
      throw new FileReadException(file, e);
    }
  }

}
