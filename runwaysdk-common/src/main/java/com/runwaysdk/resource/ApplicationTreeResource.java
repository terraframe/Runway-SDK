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
 * An application resource that has relationships with children and parents.
 * 
 * @author rrowlands
 */
public interface ApplicationTreeResource extends ApplicationResource
{
  /**
   * Gets all children of this tree resource. If this resource does not have any children this method will return an iterator whereby 'hasNext' will return false.
   */
  public Iterator<ApplicationTreeResource> getChildren();
  
  /**
   * Returns a new reference of a specific child of this tree resource by path. There is no guarantee that the child actually exists, for that you may call the 'exists'
   * method. Some implementations may support navigating multiple layers deep depending on how the pathing structure is specified.
   * 
   * @return
   */
  public ApplicationTreeResource getChild(String path);
  
  /**
   * Gets the parent of this resource. If this resource does not have a parent this method will return null.
   */
  public ApplicationTreeResource getParent();
}
