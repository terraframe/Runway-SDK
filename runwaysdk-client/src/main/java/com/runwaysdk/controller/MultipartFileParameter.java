package com.runwaysdk.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;

public class MultipartFileParameter implements Parameter
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
