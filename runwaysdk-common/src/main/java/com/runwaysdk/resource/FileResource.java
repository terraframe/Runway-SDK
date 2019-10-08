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
