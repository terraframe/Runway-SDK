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
package com.runwaysdk.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;

public class MultipartFileParameter implements ParameterValue
{

  private final FileItem fileItem;

  private final long     size;

  public MultipartFileParameter(FileItem fileItem)
  {
    this.fileItem = fileItem;
    this.size = this.fileItem.getSize();
  }

  public final FileItem getFileItem()
  {
    return this.fileItem;
  }

  public String getFieldName()
  {
    return this.fileItem.getFieldName();
  }

  public String getFilename()
  {
    return this.fileItem.getName();
  }

  public String getContentType()
  {
    return this.fileItem.getContentType();
  }

  public boolean isEmpty()
  {
    return ( this.size == 0 );
  }

  public long getSize()
  {
    return this.size;
  }

  public InputStream getInputStream() throws IOException
  {
    if (!this.isAvailable())
    {
      // TODO change this message
      throw new IllegalStateException("File has been moved - cannot be read again");
    }

    InputStream inputStream = this.fileItem.getInputStream();

    return ( inputStream != null ? inputStream : new ByteArrayInputStream(new byte[0]) );
  }

  /**
   * Determine whether the multipart content is still available. If a temporary
   * file has been moved, the content is no longer available.
   */
  protected boolean isAvailable()
  {
    // If in memory, it's available.
    if (this.fileItem.isInMemory())
    {
      return true;
    }

    // Check actual existence of temporary file.
    if (this.fileItem instanceof DiskFileItem)
    {
      return ( (DiskFileItem) this.fileItem ).getStoreLocation().exists();
    }

    // Check whether current file size is different than original one.
    return ( this.fileItem.getSize() == this.size );
  }

  @Override
  public String getSingleValue()
  {
    // TODO Change exception type
    throw new RuntimeException("Parameter type [" + this.getClass().getName() + "] does not support a single value");
  }

  @Override
  public String[] getValuesAsArray()
  {
    // TODO Change exception type
    throw new RuntimeException("Parameter type [" + this.getClass().getName() + "] does not support a single value");
  }

}
