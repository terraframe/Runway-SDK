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

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import com.runwaysdk.query.OIterator;

/**
 * A resource which is ultimately backed by a file on the file-system, but may be wrapped in abstractions which provide additional information (such as a vault file).
 * 
 * @author rrowlands
 */
public interface ApplicationFileResource extends ApplicationTreeResource
{
  public void forAllFileChildren(Consumer<ApplicationFileResource> action);
  
  public File getUnderlyingFile();
  
  public boolean isDirectory();
  
  /**
   * Convenience method.
   * 
   * @see getChildren()
   */
  public OIterator<ApplicationFileResource> getChildrenFiles();
  
  /**
   * Convenience method.
   * 
   * @see getChild()
   */
  public Optional<ApplicationFileResource> getChildFile(String path);
  
  /**
   * Convenience method.
   * 
   * @see getParent()
   */
  public Optional<ApplicationFileResource> getParentFile();
}
