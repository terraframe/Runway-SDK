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

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.google.inject.ImplementedBy;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.system.metadata.MdDimension;

/**
 * Abstract class which outlines the functionality that all concrete
 * {@link SessionCache}s must implement.
 * 
 * @author Justin Smethie
 */
@ImplementedBy(MemorySessionCache.class)
public abstract class SessionCache
{
  /**
   * Guards to ensure that invariants between multiple state fields hold.
   */
  protected final ReentrantLock sessionCacheLock = new ReentrantLock();

  /**
   * Logs a {@link UserDAO} into a new {@link Session} and loads the permissions of
   * the {@link UserDAO}. In addition, this method ensures that the {@link UserDAO}
   * is not at it's {@link Session} limit.
   * 
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param locales locale of the user.
   * 
   * @param oid of the newly created {@link Session}.
   */
  protected abstract String logIn(String username, String password, Locale[] locales);
  
  /**
   * Logs a {@link UserDAO} into a new {@link Session} and loads the permissions of
   * the {@link UserDAO} and sets the dimesion for the session. In addition, this method ensures that the {@link UserDAO}
   * is not at it's {@link Session} limit.
   * 
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param dimensionKey The dimension key of the dimension set to this session.
   * @param locales locale of the user.
   * 
   * @param oid of the newly created {@link Session}.
   */
  protected abstract String logIn(String username, String password, String dimensionKey, Locale[] locales);

  /**
   * Logs a {@link SingleActorDAOIF} into a new {@link Session} and loads the permissions of
   * the {@link SingleActorDAOIF}. In addition, this method ensures that the {@link SingleActorDAOIF}
   * is not at it's {@link Session} limit.
   * 
   * @param username Reference to the {@link SingleActorDAOIF} which is being logged in
   * @param locales locale of the user.
   * 
   * @param oid of the newly created {@link Session}.
   */
  protected abstract String logIn(SingleActorDAOIF user, Locale[] locales);
  
  /**
   * Logs a {@link UserDAO} into a new {@link Session} and loads the permissions of
   * the {@link UserDAO} and sets the dimesion for the session. In addition, this method ensures that the {@link UserDAO}
   * is not at it's {@link Session} limit.
   * 
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param dimensionKey The dimension key of the dimension set to this session.
   * @param locales locale of the user.
   * 
   * @param oid of the newly created {@link Session}.
   */
  protected abstract String logIn(SingleActorDAOIF user, String dimensionKey, Locale[] locales);
  
  /**
   * Changes the {@link UserDAO} of an existing {@link Session} and loads the
   * permissions of the new {@link UserDAO}. In addition, this method ensures that
   * the {@link UserDAO} is not at it's {@link Session} limit. Furthermore, it
   * decrements the session count of the previous user.
   * 
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param sessionId The oid of the {@link Session}
   */
  protected abstract void changeLogin(String username, String password, String sessionId);
  
  /**
   * Changes the {@link UserDAO} of an existing {@link Session} and loads the
   * permissions of the new {@link UserDAO}. In addition, this method ensures that
   * the {@link UserDAO} is not at it's {@link Session} limit. Furthermore, it
   * decrements the session count of the previous user.
   * 
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param sessionId The oid of the {@link Session}
   */
  protected abstract void changeLogin(SingleActorDAOIF user, String sessionId);

  /**
   * Sets the dimension of an existing {@link Session}.
   * @param sessionId The oid of the {@link Session}.
   * @param dimensionKey key of a {@link MdDimension}.
   */
  protected abstract void setDimension(String sessionId, String dimensionKey);
  
  /**
   * Forces this {@link SessionCache} to check for and remove expired
   * {@link Session}s
   */
  protected abstract void cleanUp();

  /**
   * Closes a {@link Session} in the {@link SessionCache}. If the
   * {@link Session} does not exist then this method does nothing.
   * 
   * @param sessionId The oid of a {@link Session}.
   */
  protected abstract void closeSession(String sessionId);

  /**
   * Removes all {@link Session}s from this {@link SessionCache}.
   */
  protected abstract void clearSessions();

  /**
   * Renews the amount of time a {@link Session} has till it expires.
   * 
   * @param sessionId The oid of the {@link Session} to renew
   */
  protected abstract void renewSession(String sessionId);

  /**
   * Returns the session corresponding to a sessionId
   * 
   * @param sessionId The oid of the session
   * @return The session corresponding to the oid
   */
  protected abstract Session getSession(String sessionId);

  /**
   * Checks if a {@link Session} of the given oid exists in the
   * {@link SessionCache}.
   * 
   * @param sessionId The oid of the {@link Session}.
   * 
   * @return true if the {@link Session} exists, false otherwise.
   */
  protected abstract boolean containsSession(String sessionId);

  /**
   * Adds a {@link Session} to the {@link SessionCache}. If the {@link Session}
   * already exists in the {@link SessionCache} then given {@link Session}
   * overwrites the existing {@link Session}.
   * 
   * @param session The {@link Session} to add to the cache.
   */
  protected abstract void addSession(Session session);

  /**
   * @param userId The oid of the {@link UserDAO}.
   * 
   * @return Returns if the {@link UserDAO} is logged into at least one
   *         {@link Session}.
   */
  abstract boolean isLoggedIn(String userId);

  /**
   * @param userId The oid of the {@link UserDAO}.
   * 
   * @return Returns the number of open {@link Session}s associated with a
   *         {@link UserDAO}.
   */
  abstract int getUserSessionCount(String userId);

  /**
   * Indicates if the {@link SessionCache} is full. If the cache is full then it
   * will throw an exception if a new {@link Session} is added.
   * 
   * @return Returns true if the cache is full.
   */
  protected abstract boolean full();

  /**
   * Makes room in the {@link SessionCache}. If the {@link SessionCache} is not
   * full then it will return null. However, if the {@link SessionCache} is full
   * then this method will return the {@link Session} which was removed from the
   * {@link SessionCache} in order to make room.
   * 
   * @return null or the {@link Session} which was removed from the
   *         {@link SessionCache}
   */
  protected abstract Session makeRoom();

  /**
   * @return Returns the public {@link Session} of the {@link SessionCache}.
   *         The public {@link Session} never expires and it is the
   *         {@link Session} assigned to all initial connections with the
   *         server. As such the public {@link Session} cannot be closed,
   *         cleaned up, or logged into.
   */
  protected abstract Session getPublicSession();

  /**
   * Sets the user of a session
   * 
   * @param sessionId The oid of the session
   * @param user The user of the new session
   */
  protected abstract void setUser(String sessionId, UserDAOIF user);

  /**
   * Retrieves a {@link Session} which will be used in a request.
   * 
   * @param sessionId The oid of the session to retrieve
   * @return {@link Session} corresponding to the session oid
   */
  protected abstract Session getSessionForRequest(String sessionId);
  
  /**
   * Returns a map which represents all {@link Session}s in the system at the current
   * snapshot in time. The key is the sessionId and the value is the {@link Session}.
   * This map is not used internally and thus can be modified at will. 
   */
  protected abstract Map<String, SessionIF> getAllSessions();

  /**
   * Sets the flag denoting if the {@link Session} corresponding to the session oid
   * should close at the end of its current request.
   * 
   * @param sessionId Id of session
   * @param closeOnRequest Boolean flag
   */
  public void setCloseOnEndOfRequest(String sessionId, boolean closeOnRequest)
  {
    this.sessionCacheLock.lock();

    try
    {
      Session session = this.getSession(sessionId);
      session.setCloseOnEndOfRequest(closeOnRequest);

      this.addSession(session);
    }
    finally
    {
      this.sessionCacheLock.unlock();
    }
  }

  /**
   * Informs the {@link SessionCache} that a request
   * using the given {@link Session} is finished.
   * 
   * @param sessionId
   */
  protected void endOfRequest(String sessionId)
  {
    this.sessionCacheLock.lock();

    try
    {
      Session session = this.getSession(sessionId);

      if (session.closeOnEndOfRequest())
      {
        this.closeSession(sessionId);
      }
    }
    finally
    {
      this.sessionCacheLock.unlock();
    }
  }

  /**
   * Stores a {@link Mutable} object in the given
   * {@link Session}
   * 
   * @param sessionId Id of session
   * @param mutable {@link Mutable} object to store.
   */
  protected void put(String sessionId, Mutable mutable)
  {
    Session session = this.getSession(sessionId);

    session.put(mutable);
  }
}
