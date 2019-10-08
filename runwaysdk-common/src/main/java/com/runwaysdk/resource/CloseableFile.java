package com.runwaysdk.resource;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FileUtils;

public class CloseableFile extends File implements AutoCloseable
{

  private static final long serialVersionUID = -5033938374526120632L;
  
  private boolean isTemp = false;
  
  public CloseableFile(File file)
  {
    super(file.toURI());
  }
  
  public CloseableFile(String pathname)
  {
    super(pathname);
  }
  
  public CloseableFile(URI uri, boolean isTemp)
  {
    super(uri);
  }
  
  public CloseableFile(File parent, String child)
  {
    super(parent, child);
  }
  
  public CloseableFile(String parent, String child)
  {
    super(parent, child);
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
