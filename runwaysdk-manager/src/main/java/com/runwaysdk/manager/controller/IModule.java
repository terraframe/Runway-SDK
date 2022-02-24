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
import java.util.Map;

import org.eclipse.jface.action.StatusLineManager;

import com.runwaysdk.dataaccess.transaction.IPropertyListener;
import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.manager.general.IMenuManager;
import com.runwaysdk.manager.general.IWindow;

public interface IModule
{
  public void generateMenu(IMenuManager manager);

  public StatusLineManager createStatusLineManager();

  public boolean hasStatusLineManager();

  public void init(IWindow window);

  public void validateAction(String actionName);

  public ITaskListener getImportListener();

  public ITaskListener getExportListener();

  public Collection<IPropertyListener> getImportPropertyListeners();

  public Map<String, String> getExportProperties();

  public void shellCloseEvent();
}
