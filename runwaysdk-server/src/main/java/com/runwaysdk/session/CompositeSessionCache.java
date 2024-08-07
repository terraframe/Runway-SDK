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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.system.metadata.MdDimension;

/**
 * An abstract {@link SessionCache} which uses the composite pattern. This cache
 * is the composition of two different session caches. Concrete implementations
 * of this class must provide logic to determine when to use each session cache.
 * 
 * @author Justin Smethie
 */
public abstract class CompositeSessionCache extends SessionCache
{
  /**
   * The first {@link SessionCache} of the composition
   */
  protected SessionCache firstCache;

  /**
   * The second {@link SessionCache} of the composition
   */
  protected SessionCache secondCache;

  /**
   * Constructs a new {@link CompositeSessionCache}.
   * 
   * @param firstCache
   *          A {@link SessionCache} to use in the composition.
   * @param secondCache
   *          A {@link SessionCache} to use in the composition.
   * 
   * @pre firstCache != null
   * @pre secondCache != null
   * @pre !firstCache.equals(secondCache)
   */
  protected CompositeSessionCache(SessionCache firstCache, SessionCache secondCache)
  {
    this.firstCache = firstCache;
    this.secondCache = secondCache;
  }

  @Override
  protected void closeSession(String sessionId)
  {
    try
    {
      SessionCache cache = this.getCache(sessionId);
      cache.closeSession(sessionId);
    }
    catch (InvalidSessionException ex)
    {
      // If the {@link Session} is not valid then go ahead and ignore it.
    }
  }

  @Override
  protected String logIn(String username, String password, Locale[] locales)
  {
    Session session = createSession(username, password, locales, null);

    return session.getOid();
  }

  @Override
  protected String logIn(String username, String password, String dimensionKey, Locale[] locales)
  {
    Session session = createSession(username, password, locales, dimensionKey);

    return session.getOid();
  }

  @Override
  protected String logIn(SingleActorDAOIF user, Locale[] locales)
  {
    Session session = createSession(user, locales, null);

    return session.getOid();
  }

  @Override
  protected String logIn(SingleActorDAOIF user, String dimensionKey, Locale[] locales)
  {
    Session session = createSession(user, locales, dimensionKey);

    return session.getOid();
  }

  private Session createSession(String username, String password, Locale[] locales, String dimensionKey)
  {
    UserDAOIF user = null;

    // Get the user associated with the username
    try
    {
      user = UserDAO.findUser(username);
    }
    catch (DataNotFoundException e)
    {
      String devMessage = "Invalid username/password combination.";
      throw new InvalidLoginException(devMessage);
    }

    // Ensure the user is active
    if (user.getInactive())
    {
      String devMessage = "The user [" + username + "] is inactive.";
      throw new InactiveUserException(devMessage, user);
    }

    // Ensure the password is correct
    if (!user.compareToPassword(password))
    {
      String devMessage = "Invalid username/password combination.";
      throw new InvalidLoginException(devMessage);
    }

    return this.createSession(user, locales, dimensionKey);
  }

  private Session createSession(SingleActorDAOIF user, Locale[] locales, String dimensionKey)
  {
    sessionCacheLock.lock();

    try
    {
      String userId = user.getOid();

      int sessionLimit = user.getSessionLimit();
      int currentAmount = getUserSessionCount(userId);

      // If the session already has a user, we need to decrement the session
      // count.
      if (!user.isLoginSupported())
      {
        String devMessage = "The class [" + user.getType() + "] does not support logging in";
        throw new LoginNotSupportedException(devMessage, user);
      }

      // If the session already has a user, we need to decrement the session
      // count.
      if (!userId.equals(UserDAOIF.PUBLIC_USER_ID) && sessionLimit != -1 && currentAmount >= sessionLimit)
      {
        String devMessage = "The user [" + user.getSingleActorName() + "] already has the maximum number sessions opened";
        throw new MaximumSessionsException(devMessage, user);
      }

      Session session = new Session(locales, user, dimensionKey);

      this.addSession(session);

      return session;
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void changeLogin(String username, String password, String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      SessionCache cache = this.getCache(sessionId);
      UserDAOIF user = null;

      // Get the user associated with the username
      try
      {
        user = UserDAO.findUser(username);
      }
      catch (DataNotFoundException e)
      {
        String devMessage = "Invalid username/password combination.";
        throw new InvalidLoginException(devMessage);
      }

      // Ensure the user is active
      if (user.getInactive())
      {
        String devMessage = "The user [" + username + "] is inactive.";
        throw new InactiveUserException(devMessage, user);
      }

      // Ensure the password is correct
      if (!user.compareToPassword(password))
      {
        String devMessage = "Invalid username/password combination.";
        throw new InvalidLoginException(devMessage);
      }

      String userId = user.getOid();
      int sessionLimit = user.getSessionLimit();
      int currentAmount = this.getUserSessionCount(userId);

      if (!userId.equals(UserDAOIF.PUBLIC_USER_ID) && currentAmount >= sessionLimit)
      {
        String devMessage = "The user [" + username + "] already has the maximum number sessions opened [" + sessionLimit + "].";
        throw new MaximumSessionsException(devMessage, user);
      }

      cache.changeLogin(username, password, sessionId);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void changeLogin(SingleActorDAOIF user, String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      SessionCache cache = this.getCache(sessionId);

      cache.changeLogin(user, sessionId);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Sets the dimension of an existing {@link Session}.
   * 
   * @param sessionId
   *          The oid of the {@link Session}.
   * @param dimensionKey
   *          key of a {@link MdDimension}.
   */
  @Override
  protected void setDimension(String sessionId, String dimensionKey)
  {
    sessionCacheLock.lock();

    try
    {
      SessionCache cache = this.getCache(sessionId);
      cache.setDimension(sessionId, dimensionKey);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void clearSessions()
  {
    sessionCacheLock.lock();
    try
    {
      firstCache.clearSessions();
      secondCache.clearSessions();
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void cleanUp()
  {
    firstCache.cleanUp();
    secondCache.cleanUp();
  }

  @Override
  protected void renewSession(String sessionId)
  {
    SessionCache cache = this.getCache(sessionId);
    cache.renewSession(sessionId);
  }

  @Override
  protected Session getSession(String sessionId)
  {
    SessionCache cache = this.getCache(sessionId);
    Session session = cache.getSession(sessionId);

    return session;
  }

  @Override
  int getUserSessionCount(String userId)
  {
    return firstCache.getUserSessionCount(userId) + secondCache.getUserSessionCount(userId);
  }

  @Override
  boolean isLoggedIn(String userId)
  {
    return firstCache.isLoggedIn(userId) || secondCache.isLoggedIn(userId);
  }

  @Override
  protected void setUser(String sessionId, UserDAOIF user)
  {
    sessionCacheLock.lock();

    try
    {
      SessionCache cache = this.getCache(sessionId);

      // Ensure the user is active
      if (user.getInactive())
      {
        String devMessage = "The user [" + user.getUsername() + "] is inactive.";
        throw new InactiveUserException(devMessage, user);
      }

      String userId = user.getOid();
      int sessionLimit = user.getSessionLimit();
      int currentAmount = this.getUserSessionCount(userId);

      // If the session already has a user, we need to decrement the session
      // count.
      if (!userId.equals(UserDAOIF.PUBLIC_USER_ID) && currentAmount >= sessionLimit)
      {
        String devMessage = "The user [" + user.getUsername() + "] already has the maximum number sessions opened";
        throw new MaximumSessionsException(devMessage, user);
      }

      cache.setUser(sessionId, user);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  public Map<String, SessionIF> getAllSessions()
  {
    sessionCacheLock.lock();

    try
    {
      HashMap<String, SessionIF> exitMap = new HashMap<String, SessionIF>();

      Map<String, SessionIF> firstMap = firstCache.getAllSessions();
      exitMap.putAll(firstMap);

      Map<String, SessionIF> secondMap = secondCache.getAllSessions();
      exitMap.putAll(secondMap);

      return exitMap;
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Returns the {@link SessionCache} which contains the {@link Session} with
   * the corresponding oid. This method also provides the logic to determine
   * which {@link SessionCache} to use for a given {@link Session}.
   * 
   * @param sessionId
   *          The session oid
   * 
   * @return The {@link SessionCache} which contains the {@link Session} with
   *         the given oid.
   */
  protected abstract SessionCache getCache(String sessionId);
}
