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
package com.runwaysdk.manager.widgets;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.manager.controller.IPauseListener;
import com.runwaysdk.manager.general.Localizer;

public class PausableTaskDialog implements ITaskListener, IPauseListener
{
  /**
   * Name of the task
   */
  private String             taskName;

  /**
   * Total amount of work which needs to be completed
   */
  private int                work;

  /**
   * Parent shell of the dialog.
   */
  private Shell              shell;

  /**
   * Amount of work already completed
   */
  private int                totalWorked;

  /**
   * Dialog
   */
  protected IProgressMonitor monitor;

  public PausableTaskDialog(Shell shell)
  {
    this.shell = shell;
    this.totalWorked = 0;
    this.monitor = null;
  }

  public synchronized ProgressMonitorDialog getMonitor()
  {
    if (monitor == null)
    {
      monitor = new ProgressMonitorDialog(shell);
    }

    return (ProgressMonitorDialog) monitor;
  }

  protected synchronized void setMonitor(ProgressMonitorDialog monitor)
  {
    this.monitor = monitor;
  }

  @Override
  public void taskProgress(final int percent)
  {
    if (this.totalWorked != percent)
    {
      this.execute(new Runnable()
      {
        @Override
        public void run()
        {
          getMonitor().worked(percent - totalWorked);

          totalWorked = percent;
        }
      });
    }
  }

  @Override
  public void taskStart(final String taskName, final int amount)
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {
        getMonitor().beginTask(Localizer.getMessage(taskName), ( amount != -1 ) ? amount : IProgressMonitor.UNKNOWN);
      }
    });

    this.work = amount;
    this.taskName = taskName;
    this.totalWorked = 0;
  }

  @Override
  public void start()
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {
        getMonitor().open();
      }
    });
  }

  @Override
  public void done(boolean status)
  {
    getMonitor().done();
  }

  public void pause()
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {
        getMonitor().done();

        setMonitor(null);
      }
    });
  }

  public void resume()
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {
        getMonitor().beginTask(taskName, work);
        getMonitor().worked(totalWorked);

        // getMonitor().open() performs a blocking open. As such the task and
        // current worked must be set before open is called.
        getMonitor().open();
      }
    });
  }

  private void execute(Runnable runnable)
  {
    this.shell.getDisplay().asyncExec(runnable);
  }

}
