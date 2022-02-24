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

import org.eclipse.core.runtime.IProgressMonitor;

import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.manager.general.Localizer;

public class TransactionTaskListener implements ITaskListener
{
  private int                previous;

  protected IProgressMonitor monitor;

  public TransactionTaskListener(IProgressMonitor monitor)
  {
    this.monitor = monitor;
    this.previous = 0;
  }

  @Override
  public void start()
  {
    // Do nothing
  }

  public IProgressMonitor getMonitor()
  {
    return monitor;
  }

  @Override
  public void taskStart(String name, int amount)
  {    
    getMonitor().beginTask(Localizer.getMessage(name), ( amount != -1 ) ? amount : IProgressMonitor.UNKNOWN);

    previous = 0;
  }

  @Override
  public void taskProgress(int percent)
  {
    if (this.previous != percent)
    {
      getMonitor().worked(percent - previous);

      previous = percent;
    }
  }

  @Override
  public void done(boolean success)
  {
    getMonitor().done();
  }
}
