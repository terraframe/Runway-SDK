/**
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
 */
/**
 * 
 */
package com.runwaysdk.generation.loader;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public interface ReloadableClassLoaderIF
{

  /**
   * @param newParent
   */
  public void setParent(ClassLoader newParent);

  /**
   * Create a new loader
   */
  public void newLoader();

  /**
   * 
   */
  public void notifyListeners();

  /**
   * @param listener
   */
  public void addListener(Object listener);

  /**
   * @param listener
   */
  public void removeListener(Object listener);

  /**
   * Loads a class from the underlying ClassLoader
   * 
   * @param type
   *          Fully qualified name of the class to load
   * @param useExProcessor
   *          Flag denoting if the loader should try to process any underlying
   *          exception or not
   * @return
   * 
   * @throws ClassNotFoundException
   */
  public Class<?> load(String type, boolean useExProcessor) throws ClassNotFoundException;

  /**
   * Gets the class that represents the specified type. Loads directly from the
   * underlying ClassLoader bypassing any sort of managed delegation.
   * 
   * @param type
   *          Fully qualified type to load
   * @return Class specified by the type name
   * @throws ClassNotFoundException
   */
  public Class<?> loadClass(String type) throws ClassNotFoundException;

  /**
   * Gets the class that represents the specified type. Loads directly from the
   * underlying ClassLoader bypassing any sort of managed delegation.
   * 
   * @param type
   *          Fully qualified type to load
   * @return Class specified by the type name
   * @throws ClassNotFoundException
   */
  public Class<?> loadClass(String type, boolean resolve) throws ClassNotFoundException;

}
