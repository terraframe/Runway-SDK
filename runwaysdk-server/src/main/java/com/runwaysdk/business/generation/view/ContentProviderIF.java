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
package com.runwaysdk.business.generation.view;

import java.util.List;

/**
 * Manages generating and deleting the content provided by all of the registered
 * {@link ContentListener}s.
 * 
 * @author Justin Smethie
 */
public interface ContentProviderIF
{
  /**
   * Registers a {@link ContentListener} to the this {@link ContentProviderIF}
   * 
   * @param listener
   *          The {@link ContentListener} to register
   */
  public void registerContentListener(ContentListener listener);

  /**
   * Unregisters a {@link ContentListener} from the this
   * {@link ContentProviderIF}
   * 
   * @param listener
   *          The {@link ContentListener} to unregister
   */
  public void unregisterContentListener(ContentListener listener);

  /**
   * Generates the content of all of the registered {@link ContentListener}s.
   * However, if content already exists then it is not overwritten.
   */
  public void generateContent();

  /**
   * Generates the content of all of the registered {@link ContentListener}s.
   * However, if content already exists then it is not overwritten unless the
   * force regenerate flage is set to <code>true</code>.
   * 
   * @param forceRegenerate
   *          Flag denoting if existing content should be overwritten
   */
  public void generateContent(boolean forceRegenerate);

  /**
   * Deletes the content of all of the registered {@link ContentListener}s.
   */
  public void deleteContent();

  /**
   * @return A list of the fully qualified paths for the content generated by
   *         all of the registered {@link ContentListener}'s.
   */
  public List<String> getGeneratedFilePaths();

  /**
   * @return Makes a backup of all the files generated by this
   *         {@link ContentProviderIF}
   */
  public void backupContent();

  /**
   * Reloads the content of the files from the backup files
   */
  public void reloadContent();

  /**
   * Deletes all of the backup files generated by this {@link ContentProviderIF}
   */
  public void deleteBackup();
}
