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
package com.runwaysdk.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.NoSuchElementException;

import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
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
    Session session = logInCommon(username, password, locales);

    return session.getOid();
  }

  @Override
  protected String logIn(String username, String password, String dimensionKey, Locale[] locales)
  {
    Session session = logInCommon(username, password, locales);

    session.setDimension(dimensionKey);

    return session.getOid();
  }
  
  @Override
  protected String logIn(SingleActorDAOIF user, Locale[] locales)
  {
    Session session = logInCommon(user, locales);

    return session.getOid();
  }
  
  @Override
  protected String logIn(SingleActorDAOIF user, String dimensionKey, Locale[] locales)
  {
    Session session = logInCommon(user, locales);

    session.setDimension(dimensionKey);

    return session.getOid();
  }

  private Session logInCommon(String username, String password, Locale[] locales)
  {
    Session session = new Session(locales);

    try
    {
      this.addSession(session);

      this.changeLogin(username, password, session.getOid());
    }
    catch (InvalidLoginException e)
    {
      this.closeSession(session.getOid());
      throw e;
    }
    catch (InactiveUserException e)
    {
      this.closeSession(session.getOid());
      throw e;
    }
    catch (MaximumSessionsException e)
    {
      this.closeSession(session.getOid());
      throw e;
    }
    return session;
  }

  private Session logInCommon(SingleActorDAOIF user, Locale[] locales)
  {
    Session session = new Session(locales);
    
    try
    {
      this.addSession(session);
      
      this.changeLogin(user, session.getOid());
    }
    catch (InvalidLoginException e)
    {
      this.closeSession(session.getOid());
      throw e;
    }
    catch (InactiveUserException e)
    {
      this.closeSession(session.getOid());
      throw e;
    }
    catch (MaximumSessionsException e)
    {
      this.closeSession(session.getOid());
      throw e;
    }
    return session;
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
      UserDAOIF publicUser = UserDAO.getPublicUser();

      if (!user.equals(publicUser) && currentAmount >= sessionLimit)
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
      UserDAOIF publicUser = UserDAO.getPublicUser();

      // If the session already has a user, we need to decrement the session
      // count.
      if (!user.equals(publicUser) && currentAmount >= sessionLimit)
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

  /**
   * @see com.runwaysdk.session.SessionCache#getIterator()
   */
  public SessionIterator getIterator()
  {
    return new CompositeSessionIterator();
  }

  private class CompositeSessionIterator implements SessionIterator
  {
    SessionIterator currentIt;

    SessionIterator firstIt;

    SessionIterator secondIt;

    SessionIF       next;

    private CompositeSessionIterator()
    {
      firstIt = firstCache.getIterator();
      secondIt = secondCache.getIterator();
      currentIt = firstIt;

      privateNext(true);
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#next()
     */
    @Override
    public SessionIF next()
    {
      return privateNext(false);
    }

    private SessionIF privateNext(boolean isConstructor)
    {
      if (next == null && !isConstructor)
      {
        throw new NoSuchElementException();
      }

      SessionIF current = next;

      if (firstIt.hasNext())
      {
        next = firstIt.next();
      }
      else
      {
        currentIt = secondIt;

        while (true)
        {
          if (currentIt.hasNext())
          {
            next = currentIt.next();

            // Skip the session if its in the first cache since we've already
            // returned it.
            if (firstCache.containsSession(next.getOid()))
            {
              continue;
            }
            else
            {
              break;
            }
          }
          else
          {
            next = null;
            break;
          }
        }
      }

      return current;
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#remove()
     */
    @Override
    public void remove()
    {
      currentIt.remove();
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
      return next != null;
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#close()
     */
    @Override
    public void close()
    {
      firstIt.close();
      secondIt.close();
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#getAll()
     */
    @Override
    public Collection<SessionIF> getAll()
    {
      ArrayList<SessionIF> all = new ArrayList<SessionIF>();

      all.addAll(firstIt.getAll());
      all.addAll(secondIt.getAll());

      return all;
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
