package com.runwaysdk.resource;

import java.io.File;

/**
 * A resource which is ultimately backed by a file on the file-system, but may be wrapped in abstractions which provide additional information (such as a vault file).
 * 
 * @author rrowlands
 */
public interface ApplicationFileResource extends ApplicationResource
{
  public File getUnderlyingFile();
  
  public boolean isDirectory();
}
