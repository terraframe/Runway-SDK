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
package com.runwaysdk.dataaccess.resolver;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;

public class SynchronusExecutor implements UncaughtExceptionHandler
{
  private Runnable  runnable;

  private Throwable throwable;

  public SynchronusExecutor(Runnable runnable)
  {
    this.runnable = runnable;
  }

  public Runnable getRunnable()
  {
    return runnable;
  }

  public void start()
  {
    Thread thread = new Thread(runnable);
    thread.setDaemon(true);
    thread.setUncaughtExceptionHandler(this);
    thread.start();

    while (thread.isAlive())
    {
      try
      {
        Thread.sleep(100L);
      }
      catch (InterruptedException e)
      {
        // Do nothing: Just retest to ensure that the thread is alive
      }
    }

    if (throwable != null)
    {
      this.handleThrowable(throwable);
    }
  }

  @Override
  public void uncaughtException(Thread thread, Throwable throwable)
  {
    this.throwable = throwable;
  }

  private void handleThrowable(Throwable throwable)
  {
    if (throwable instanceof InvocationTargetException)
    {
      InvocationTargetException e = (InvocationTargetException) throwable;

      this.handleThrowable(e.getTargetException());
    }
    else if (throwable instanceof RuntimeException)
    {
      throw (RuntimeException) throwable;
    }
    else
    {
      throw new RuntimeException(throwable);
    }
  }
}
