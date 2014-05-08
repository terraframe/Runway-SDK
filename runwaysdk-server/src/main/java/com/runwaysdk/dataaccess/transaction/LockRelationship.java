/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.transaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.LockException;

public class LockRelationship
{
  private Map<String, Thread> lockedIDsMap;

  private static LockRelationship lockRelationship;

  private LockRelationship()
  {
    this.lockedIDsMap = new HashMap<String, Thread>();
  }

  public synchronized static LockRelationship getLockRelationship()
  {
    if (lockRelationship == null)
    {
      lockRelationship = new LockRelationship();
      return lockRelationship;
    }
    else
    {
      return lockRelationship;
    }
  }

  /**
   * Tries to attain a relationship lock on both the parent and the child objects.
   * Goes to sleep if either object is locked.
   *
   * @param id of the parent object in a relationship.
   * @param id of the child object in a relationship.
   */
  public void relLock(String parentId, String childId)
  {
    while (true)
    {
      boolean breakLoop = tryRelLock(parentId, childId);

      if (breakLoop == true)
      {
        break;
      }
    } // while (true)
  }

  /**
   *
   * @param parentId
   * @param childId
   * @return
   */
  private synchronized boolean tryRelLock(String parentId, String childId)
  {
    if (! (this.lockedIDsMap.containsKey(parentId) &&
           this.lockedIDsMap.containsKey(childId)))
    {
      this.recordRelLock(parentId, childId);

      return true;
    }


    // block if the id is in the lockedIDsMap
    if ( (!LockObject.isLockedByThread(this.lockedIDsMap.get(parentId))) &&
         (!LockObject.isLockedByThread(this.lockedIDsMap.get(childId)))  )
    {
      try
      {
        this.wait();
      }
      catch (Exception ex)
      {
        String error = "An error occured during the cache database synchronization routine for ids ["
            + parentId + "] and ["+childId+"]";
        throw new LockException(error);
      }
    }
    else
    {
      return true;
    }

    return false;
  }

  // Hook method for an aspect
  private void recordRelLock(String parentId, String childId)
  {
    this.lockedIDsMap.put(parentId, LockObject.getCurrentThread());
    this.lockedIDsMap.put(childId, LockObject.getCurrentThread());
  }


  /**
   * Releases a relationship lock on the objects with the given ids, but only if the
   * current thread is the thread that holds the lock.
   * @param parentId id of the parent object in the relationship.
   * @param childId id of the child object in the relationship.
   */
  public synchronized void releaseRelLock(String parentId, String childId)
  {
    if( ( this.lockedIDsMap.containsKey(parentId) && (LockObject.isLockedByThread(this.lockedIDsMap.get(parentId))) )
        &&
        ( this.lockedIDsMap.containsKey(childId) && (LockObject.isLockedByThread(this.lockedIDsMap.get(childId))) )
      )
    {
      this.lockedIDsMap.remove(parentId);
      this.lockedIDsMap.remove(childId);
    }
    this.notifyAll();
  }

  /**
   * Releases application locks on all objects in the given set, but only if the current thread
   * owns the lock on the object.
   * @param unSetJavaLocksSet set of object ids.
   */
  public synchronized void releaseRelLocks(Set<String> unSetJavaLocksSet)
  {
    Iterator<String> setIterator = unSetJavaLocksSet.iterator();

    while(setIterator.hasNext())
    {
      String id = (String)setIterator.next();

      if(this.lockedIDsMap.containsKey(id) &&
          (LockObject.isLockedByThread(this.lockedIDsMap.get(id))))
      {
        this.lockedIDsMap.remove(id);
      }
    }

    this.notifyAll();
  }

}
