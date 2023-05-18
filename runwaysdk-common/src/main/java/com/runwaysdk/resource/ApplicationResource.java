/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.io.InputStream;

/**
 * Interface denoting an abstract resource, which may in practice exist on the classpath, in the vault, as a file on the filesystem, or potentially even as a remote resource (like on S3)
 * 
 * @author rrowlands
 */
public interface ApplicationResource extends AutoCloseable
{
  /**
   * Returns the absolute path to the resource. Depending upon the underlying implementation this path can vary wildly
   * from a path to a file on a filesystem to the path to a file on S3.
   */
  public String getAbsolutePath();
  
  /**
   * Opens a new stream to access the underlying resource. The returned resource should be cleaned up after you are done
   * using it by invoking 'close' on it.
   */
  public InputStream openNewStream();
  
  /**
   * Opens a new connection to the resource as a file. Depending on the implementation, a new temporary file may be created
   * to fulfill the request, which may involve expensive copying of files. For this reason you should always invoke "close"
   * on the returned CloseableFile when you are done. If this is not what you want, consider using the ApplicationFileResource
   * interface which provides 'getUnderlyingFile'.
   */
  public CloseableFile openNewFile();
  
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
   * Returns true if the resource exists.
   */
  public boolean exists();
  
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
