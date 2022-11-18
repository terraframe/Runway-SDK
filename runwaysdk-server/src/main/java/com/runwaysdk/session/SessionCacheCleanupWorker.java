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
package com.runwaysdk.session;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class SessionCacheCleanupWorker implements Runnable
{
  /**
   * The default amount of time to sleep between expire session checks
   */
  public static final int DEFAULT_PERIOD   = 8000;

  private static Integer  period           = DEFAULT_PERIOD;

  private static Thread   workerThread;

  private static Boolean  runCleanupThread = true;

  public synchronized static void startWorkerThread()
  {
    if (workerThread != null)
    {
      return;
    }

    workerThread = new Thread(new SessionCacheCleanupWorker(), "Runway SessionCache Cleanup Worker");
    workerThread.setDaemon(true);
    workerThread.start();

    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        runCleanupThread = false;
      }
    }, "Runway SessionCache shutdown thread"));
  }

  public synchronized static void setPeriod(Integer period)
  {
    SessionCacheCleanupWorker.period = period;
  }

  public void run()
  {
    while (runCleanupThread)
    {
      try
      {
        Thread.sleep(period);
      }
      catch (Exception e)
      {
        String errMsg = e.getMessage();
        throw new ProgrammingErrorException(errMsg);
      }

      doInRequest();
    }
  }

  @Request
  private void doInRequest()
  {
    SessionFacade.cleanUp();
  }
}