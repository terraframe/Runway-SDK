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

import java.util.Iterator;

/**
 * Interface denoting an abstract "file", which may in practice exist on the classpath, in the vault, as a file on the filesystem, or potentially even as a remote resource (like on S3).
 * These "files" are allowed to participate in tree structures and may or may not actually have a data stream which can be consumed, if for example they are a directory and not a file.
 * If you attempt to open a data stream on a purely pathing construct (i.e. a directory) a ResourceException will be thrown. You can invoke 'hasDataStream' to test whether or not this is the case.
 * 
 * @author rrowlands
 */
public interface ApplicationTreeResource extends ApplicationResource
{
  /**
   * Can be thought of as similarly to the 'isDirectory' method on Java's File construct. This method will tell you whether or not you can open an input stream
   * on this resource and start reading data from it.
   */
  public boolean hasDataStream();
  
  /**
   * Gets all children of this tree resource. If this resource does not have any children this method will return an iterator whereby 'hasNext' will return false.
   */
  public Iterator<ApplicationTreeResource> getChildren();
  
  /**
   * Gets the parent of this resource. If this resource does not have a parent this method will return null.
   */
  public ApplicationTreeResource getParent();
}
