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
import java.io.InputStream;

/**
 * Interface denoting an abstract "file", which may in practice exist on the classpath, in the vault, as a file on the filesystem, or potentially even as a remote resource (like on S3)
 * 
 * @author rrowlands
 */
public interface ApplicationResource extends AutoCloseable
{
  /**
   * Opens a new stream to access the underlying resource. The returned resource should be cleaned up after you are done
   * using it by invoking 'close' on it.
   */
  public InputStream openNewStream();
  
  /**
   * Opens a new connection to the resource as a file. Depending on the implementation, a new temporary file may be created
   * to fulfill the request, which may involve expensive copying of files. If this is not what you want, use 'getUnderlyingFile'
   * instead, which throws an exception if it cannot invoke performantly. For this reason you should always invoke "close" on the
   * ApplicationResource when you are done using the file returned by this method.
   */
  public CloseableFile openNewFile();
  
  /**
   * If the underlying implementation supports direct file access (vault, or file) a direct reference to the underlying
   * file will be returned. If this is not supported an UnsupportedOperationException will be thrown. In the case of vault
   * files a file will be returned, however the filename will be a randomized string with no extension. This method promises
   * to do no file copying or otherwise expensive operations, unlike 'openNewFile'.
   */
  public File getUnderlyingFile();
  
  /**
   * Returns the name of the resource, which is the base name plus the extension (if onee exists). This method will not return
   * any sort of pathing information.
   */
  public String getName();
  
  /**
   * Returns the name of the resource, without any filename extensions. This method will not return any sort of pathing information.
   */
  public String getBaseName();
  
  /**
   * Gets the extension of the resource.

      This method returns the textual part of the fileName after the last dot. There must be no directory separator after the dot.
      
       foo.txt      --> "txt"
       a/b/c.jpg    --> "jpg"
       a/b.txt/c    --> ""
       a/b/c        --> ""
   */
  public String getNameExtension();
  
  /**
   * Returns true if and only if the resource exists on some remote server. Returns false if the resource exists locally.
   */
  public boolean isRemote();
  
  /**
   * Closes the underlying resource, freeing any OS resources. Depending on the underlying implementation of this resource,
   * some methods on this interface may no longer function properly after invoking this method.
   */
  public void close();
  
  /**
   * Attempts to delete the resource, if possible. Not all resources can be deleted, for example input streams. Classpath resources
   * can be deleted only if they do not exist inside of a jar.
   */
  public void delete();
}
