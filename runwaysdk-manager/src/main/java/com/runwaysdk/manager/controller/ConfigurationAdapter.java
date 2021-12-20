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
package com.runwaysdk.manager.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.general.MainWindow;

public class ConfigurationAdapter implements IConfiguration
{
  private MainWindow window;

  @Override
  public boolean getImportApplicationFiles()
  {
    return false;
  }

  @Override
  public boolean getExportStoredApplicationFiles()
  {
    return false;
  }

  @Override
  public List<String> getExportApplicationFiles()
  {
    return new LinkedList<String>();
  }

  @Override
  public Collection<String> getFilesToDeleteOnImport()
  {
    return new LinkedList<String>();
  }

  @Override
  public String getShellText()
  {
    return Localizer.getMessage("APPLICATION_NAME");
  }

  @Override
  public void handleError(Throwable t)
  {
    this.window.error(t);
  }

  @Override
  public void register(MainWindow window)
  {
    this.window = window;
  }

  protected MainWindow getWindow()
  {
    return window;
  }
}
