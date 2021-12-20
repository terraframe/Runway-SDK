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
package com.runwaysdk.generation.loader;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockHolder
{
  private static final ReentrantLock loaderLock = new ReentrantLock();

  private static final ReentrantLock cacheLock  = new ReentrantLock();

  static
  {
    Logger log = LoggerFactory.getLogger(LockHolder.class);

    log.info("New LockHolder created.  Loaded by " + LockHolder.class.getClassLoader() + " at " + System.currentTimeMillis() + " with lock " + loaderLock);
  }

  public static void lock(Object caller)
  {
    int wait = 100;
    while (true)
    {
      if (loaderLock.tryLock())
      {
        break;
      }
      try
      {
        // Don't try to wait an object we don't own. That causes an
        // IllegalMonitorStateException
        if (Thread.holdsLock(caller))
        {
          caller.wait(wait);
        }

        Thread.sleep(wait);
      }
      catch (InterruptedException e)
      {
      }

      // Exponential backoff
      if (wait < 3200)
      {
        wait *= 2;
      }
    }
  }

  public static void unlock()
  {
    loaderLock.unlock();
  }

  public static void lockCache(Object caller)
  {
    cacheLock.lock();
  }

  public static void unlockCache()
  {
    cacheLock.unlock();
  }
}
