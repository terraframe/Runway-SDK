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
package com.runwaysdk.manager.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.dataaccess.transaction.TransactionExportManager;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.view.IAdminModule;
import com.runwaysdk.session.Request;

public class ExportWorker implements IRunnableWithProgress
{
  private File            location;

  private IExportStrategy strategy;

  private IConfiguration  configuration;

  private IAdminModule    module;

  public ExportWorker(File location, IAdminModule module, IExportStrategy strategy, IConfiguration configuration)
  {
    this.location = location;
    this.strategy = strategy;
    this.configuration = configuration;
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

  public void setStrategy(IExportStrategy strategy)
  {
    this.strategy = strategy;
  }

  @Override
  @Request
  public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
  {
    StringTokenizer toke = new StringTokenizer(location.getName(), ".");

    String path = location.getParent();
    String fileName = toke.nextToken();

    List<String> files = configuration.getExportApplicationFiles();

    TransactionExportManager manager = new TransactionExportManager(files, CommonProperties.getTransactionRecordXMLschemaLocation(), fileName, path);
    manager.addListener(new TransactionTaskListener(monitor));
    
    List<ITaskListener> listeners = module.getExportListeners();

    for(ITaskListener listener : listeners)
    {
      manager.addListener(listener);
    }
    
    HashMap<String, String> properties = module.getExportProperties();
    
    for(String name : properties.keySet())
    {
      String value = properties.get(name);
      manager.addProperty(name, value);
    }
    
    manager.setExportStoredApplicationFiles(configuration.getExportStoredApplicationFiles());
    strategy.execute(manager);
    
    module.message(Localizer.getMessage("EXPORT_FINISHED"));
  }
  
}
