/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.generation.loader;


/**
 * Marker interface, extended by all reloadable generated content. The custom
 * {@link RunwayClassLoader} will ignore any content that does not implement this
 * interface. When {@link RunwayClassLoader} is set as the default loader, dynamic
 * content can reside on the classpath with static content. This is useful for deployed
 * environments, where jsp complication may require access to both static and dynmaic
 * classes.
 * 
 * @author Eric
 */
public interface Reloadable
{  
  /**
   * A String representing the java code to implement this interface:<br />
   * implements <code>com.runwaysdk.generation.loader.Reloadable</code>
   */
  public static final String IMPLEMENTS = " implements " + Reloadable.class.getName();
}
