package com.runwaysdk.resource;

import java.io.File;
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
      Path path = Files.createTempFile(this.getBaseName(), this.getNameExtension());
      
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
  public File getUnderlyingFile()
  {
    throw new UnsupportedOperationException();
  }
  
}
