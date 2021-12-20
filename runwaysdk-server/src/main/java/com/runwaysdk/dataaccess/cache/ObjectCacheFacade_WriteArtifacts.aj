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
package com.runwaysdk.dataaccess.cache;

/**
 * Aspect is used to tell the {@link ObjectCache} not to write java source and class
 * files from the database and onto the file system when the cache initializes.
 */
privileged public aspect ObjectCacheFacade_WriteArtifacts
{

  protected pointcut cacheInit()
  : execution (* com.runwaysdk.util.UpdateDatabaseSourceAndClasses.storeSourceAndClassesInDatabase(..)) || execution (* com.runwaysdk.dataaccess.io.Versioning.main(..));

  void around() : cacheInit()
  {
    ObjectCache.extractDatabaseSourceAndClasses = false;

    proceed();

    ObjectCache.extractDatabaseSourceAndClasses = true;
  }

}
