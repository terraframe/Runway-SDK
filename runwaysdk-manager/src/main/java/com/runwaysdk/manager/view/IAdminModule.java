/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.manager.view;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.dataaccess.transaction.IPropertyListener;
import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.manager.controller.IViewStrategy;
import com.runwaysdk.manager.controller.ViewConfig;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.IEntityObject;

public interface IAdminModule
{
  public Display getDisplay();

  public void show(IViewStrategy strategy);

  public void closeTab(String key);

  public Shell getShell();

  public void message(String message);

  public void error(Throwable throwable);

  public void syncExec(Runnable runnable);

  public void run(IRunnableWithProgress worker);

  public void pause(String taskName);

  public void resume(String taskName);

  public void edit(IEntityObject entity, ViewConfig config);

  public void view(IComponentObject entity, boolean closable);

  public void search(IComponentObject entity, boolean closable);

  public List<ITaskListener> getImportListeners();

  public List<ITaskListener> getExportListeners();

  public List<IPropertyListener> getImportPropertyListeners();

  public HashMap<String, String> getExportProperties();
}
