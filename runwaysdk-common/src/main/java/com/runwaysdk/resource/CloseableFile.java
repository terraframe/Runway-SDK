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
import java.net.URI;

import org.apache.commons.io.FileUtils;

/**
 * A file that may be used within the Java AutoCloseable paradigm. This file includes within it
 * a "isTemp" flag, which defaults to false. If it is set to true, then when close is called
 * the file will be deleted.
 * 
 * @author rrowlands
 */
public class CloseableFile extends File implements AutoCloseable
{

  private static final long serialVersionUID = -5033938374526120632L;
  
  private boolean isTemp = false;
  
  public CloseableFile(File file)
  {
    super(file.toURI());
  }
  
  public CloseableFile(File file, boolean isTemp)
  {
    super(file.toURI());
    this.isTemp = isTemp;
  }
  
  public CloseableFile(String pathname)
  {
    super(pathname);
  }
  
  public CloseableFile(String pathname, boolean isTemp)
  {
    super(pathname);
    this.isTemp = isTemp;
  }
  
  public CloseableFile(URI uri, boolean isTemp)
  {
    super(uri);
    this.isTemp = isTemp;
  }
  
  public CloseableFile(File parent, String child)
  {
    super(parent, child);
  }
  
  public CloseableFile(File parent, String child, boolean isTemp)
  {
    super(parent, child);
    this.isTemp = isTemp;
  }
  
  public CloseableFile(String parent, String child)
  {
    super(parent, child);
  }
  
  public CloseableFile(String parent, String child, boolean isTemp)
  {
    super(parent, child);
    this.isTemp = isTemp;
  }
  
  public void setIsTemporary(boolean isTemp)
  {
    this.isTemp = isTemp;
  }
  
  public boolean isTemporary()
  {
    return this.isTemp;
  }
  
  public void close()
  {
    if (this.isTemporary())
    {
      FileUtils.deleteQuietly(this);
    }
  }
  
}
