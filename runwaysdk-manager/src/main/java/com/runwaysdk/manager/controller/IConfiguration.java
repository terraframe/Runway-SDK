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
package com.runwaysdk.manager.controller;

import java.util.Collection;
import java.util.List;

import com.runwaysdk.manager.general.MainWindow;

public interface IConfiguration
{
  public List<String> getExportApplicationFiles();

  public boolean getExportStoredApplicationFiles();

  public boolean getImportApplicationFiles();

  /**
   * Returns a collection of paths with relative pathing from the webapp root
   * directory for which we want to delete before restoring the web app
   * directory.
   * 
   * @return Collection of relative paths
   */
  public Collection<String> getFilesToDeleteOnImport();

  /**
   * @return Text of the shell
   */
  public String getShellText();

  /**
   * Allows for custom error code handling per specific application.
   * 
   * @param t
   */
  public void handleError(Throwable t);

  public void register(MainWindow window);
}
