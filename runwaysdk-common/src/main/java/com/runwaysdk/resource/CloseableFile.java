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
 * a "deleteOnClose" flag, which defaults to true. If it is set to true, then when close is called
 * the file will be deleted. If deleteOnClose is set to true, this file will be registered with
 * the JVM's "deleteOnExit" feature, which will automatically delete this file when the JVM exits.
 * 
 * @author rrowlands
 */
public class CloseableFile extends File implements AutoCloseable
{

  private static final long serialVersionUID = -5033938374526120632L;
  
  private boolean deleteOnClose = true;
  
  public CloseableFile(File file)
  {
    super(file.toURI());
    this.setIsDeleteOnClose(this.deleteOnClose);
  }
  
  public CloseableFile(File file, boolean deleteOnClose)
  {
    super(file.toURI());
    this.setIsDeleteOnClose(deleteOnClose);
  }
  
  public CloseableFile(String pathname)
  {
    super(pathname);
    this.setIsDeleteOnClose(this.deleteOnClose);
  }
  
  public CloseableFile(String pathname, boolean deleteOnClose)
  {
    super(pathname);
    this.setIsDeleteOnClose(deleteOnClose);
  }
  
  public CloseableFile(URI uri, boolean deleteOnClose)
  {
    super(uri);
    this.setIsDeleteOnClose(deleteOnClose);
  }
  
  public CloseableFile(File parent, String child)
  {
    super(parent, child);
    this.setIsDeleteOnClose(this.deleteOnClose);
  }
  
  public CloseableFile(File parent, String child, boolean deleteOnClose)
  {
    super(parent, child);
    this.setIsDeleteOnClose(deleteOnClose);
  }
  
  public CloseableFile(String parent, String child)
  {
    super(parent, child);
    this.setIsDeleteOnClose(this.deleteOnClose);
  }
  
  public CloseableFile(String parent, String child, boolean deleteOnClose)
  {
    super(parent, child);
    this.setIsDeleteOnClose(deleteOnClose);
  }
  
  /**
   * Denotes whether or not this CloseableFile should delete itself when close is called.
   * This method is protected because setting "deleteOnClose" to true is an irreversable
   * operation, because it registers the delete with the JVM (which cannot be undone).
   */
  protected void setIsDeleteOnClose(boolean deleteOnClose)
  {
    this.deleteOnClose = deleteOnClose;
    
    if (this.deleteOnClose)
    {
      this.deleteOnExit();
    }
  }
  
  public boolean isDeleteOnClose()
  {
    return this.deleteOnClose;
  }
  
  public void close()
  {
    if (this.isDeleteOnClose())
    {
      FileUtils.deleteQuietly(this);
    }
  }
  
}
