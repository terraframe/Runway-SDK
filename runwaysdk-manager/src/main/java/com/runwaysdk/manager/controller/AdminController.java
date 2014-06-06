/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.controller;

import java.io.File;

import com.runwaysdk.dataaccess.resolver.IConflictResolver;
import com.runwaysdk.dataaccess.resolver.IPersistanceStrategy;
import com.runwaysdk.dataaccess.resolver.ImportConflict;
import com.runwaysdk.manager.model.ExportBean;
import com.runwaysdk.manager.model.ImportBean;
import com.runwaysdk.manager.model.object.PersistanceFacade;
import com.runwaysdk.manager.view.IAdminModule;

public class AdminController implements IAdminModuleController, IConflictResolver
{
  private IAdminModule      module;

  private IConfiguration    configuration;

  private ImportWorker      worker;

  private volatile Conflict conflict;

  public AdminController(IConfiguration configuration)
  {
    this.configuration = configuration;
  }

  public AdminController(IConfiguration configuration, IAdminModule module)
  {
    this(configuration);

    this.module = module;
  }

  public void setModule(IAdminModule module)
  {
    this.module = module;
  }

  public void resolve(ImportConflict _conflict)
  {
    this.conflict = new Conflict(_conflict, module, worker);

    conflict.display();

    // Pause the import thread while waiting for the conflict to resolve.
    while (!conflict.isResolved())
    {
      try
      {
        Thread.sleep(1000L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();

        throw new RuntimeException(e);
      }
    }

    conflict = null;
    setPersistanceStrategy(new DefaultPersistanceStrategy());
    module.resume(worker.getTaskName());
  }

  @Override
  public void importTransaction(ImportBean bean)
  {
    synchronized (this)
    {
      File location = new File(bean.getLocation());

      worker = new ImportWorker(location, configuration, this, module);
      worker.start();
    }
  }

  @Override
  public void exportTransactions(ExportBean bean)
  {
    File location = new File(bean.getLocation());
    IExportStrategy strategy = bean.getExportStrategy();

    module.run(new ExportWorker(location, module, strategy, configuration));
  }

  public void resumeImport()
  {
    if (conflict != null)
    {
      conflict.resolved();
    }
  }

  @Override
  public void setPersistanceStrategy(IPersistanceStrategy strategy)
  {
    PersistanceFacade.setPersistanceStrategy(strategy);
  }
}
