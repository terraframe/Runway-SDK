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
   * @param oid of the parent object in a relationship.
   * @param oid of the child object in a relationship.
   */
  public void relLock(String parentOid, String childOid)
  {
    while (true)
    {
      boolean breakLoop = tryRelLock(parentOid, childOid);

      if (breakLoop == true)
      {
        break;
      }
    } // while (true)
  }

  /**
   *
   * @param parentOid
   * @param childOid
   * @return
   */
  private synchronized boolean tryRelLock(String parentOid, String childOid)
  {
    if (! (this.lockedIDsMap.containsKey(parentOid) &&
           this.lockedIDsMap.containsKey(childOid)))
    {
      this.recordRelLock(parentOid, childOid);

      return true;
    }


    // block if the oid is in the lockedIDsMap
    if ( (!LockObject.isLockedByThread(this.lockedIDsMap.get(parentOid))) &&
         (!LockObject.isLockedByThread(this.lockedIDsMap.get(childOid)))  )
    {
      try
      {
        this.wait();
      }
      catch (Exception ex)
      {
        String error = "An error occured during the cache database synchronization routine for ids ["
            + parentOid + "] and ["+childOid+"]";
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
  private void recordRelLock(String parentOid, String childOid)
  {
    this.lockedIDsMap.put(parentOid, LockObject.getCurrentThread());
    this.lockedIDsMap.put(childOid, LockObject.getCurrentThread());
  }


  /**
   * Releases a relationship lock on the objects with the given ids, but only if the
   * current thread is the thread that holds the lock.
   * @param parentOid oid of the parent object in the relationship.
   * @param childOid oid of the child object in the relationship.
   */
  public synchronized void releaseRelLock(String parentOid, String childOid)
  {
    if( ( this.lockedIDsMap.containsKey(parentOid) && (LockObject.isLockedByThread(this.lockedIDsMap.get(parentOid))) )
        &&
        ( this.lockedIDsMap.containsKey(childOid) && (LockObject.isLockedByThread(this.lockedIDsMap.get(childOid))) )
      )
    {
      this.lockedIDsMap.remove(parentOid);
      this.lockedIDsMap.remove(childOid);
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
      String oid = (String)setIterator.next();

      if(this.lockedIDsMap.containsKey(oid) &&
          (LockObject.isLockedByThread(this.lockedIDsMap.get(oid))))
      {
        this.lockedIDsMap.remove(oid);
      }
    }

    this.notifyAll();
  }

}
