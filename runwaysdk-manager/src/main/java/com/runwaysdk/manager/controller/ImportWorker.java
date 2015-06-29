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

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import com.runwaysdk.dataaccess.resolver.IConflictResolver;
import com.runwaysdk.dataaccess.transaction.IPropertyListener;
import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.view.IAdminModule;

public class ImportWorker implements UncaughtExceptionHandler
{
  private static final String TASK_NAME = "IMPORT";

  private File                location;

  private IConfiguration      configuration;

  private IConflictResolver   resolver;

  private IAdminModule        module;

  public ImportWorker(File location, IConfiguration configuration, IConflictResolver resolver, IAdminModule module)
  {
    this.location = location;
    this.configuration = configuration;
    this.resolver = resolver;
    this.module = module;
  }

  public File getLocation()
  {
    return location;
  }

  public void setLocation(File location)
  {
    this.location = location;
  }

  public void start()
  {
    Thread thread = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        if (location.exists())
        {
          TransactionImportManager manager = new TransactionImportManager(location.getAbsolutePath(), resolver);

          for (ITaskListener listener : module.getImportListeners())
          {
            manager.addTaskListener(listener);
          }

          for (IPropertyListener listener : module.getImportPropertyListeners())
          {
            manager.addPropertyListener(listener);
          }
          
          for(String path : configuration.getFilesToDeleteOnImport())
          {
            manager.addFileToDelete(path);
          }

          manager.setImportApplicationFiles(configuration.getImportApplicationFiles());
          manager.importTransactions();
        }
        else
        {
          String msg = Localizer.getMessage("MISSING_FILE") + location.getPath();
          throw new RuntimeException(msg);
        }
      }
    });

    thread.setUncaughtExceptionHandler(this);
    thread.setDaemon(true);
    thread.start();
  }

  public String getTaskName()
  {
    return TASK_NAME;
  }

  @Override
  public void uncaughtException(Thread thread, Throwable throwable)
  {
    module.error(throwable);
  }
}
