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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.rbac.SingleActorDAO;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;

/**
 * An abstract {@link SessionCache} which tracks the number of sessions
 * a user has logged into.  The session count of each user is stored
 * in memory.
 *
 * @author Justin Smethie
 */
public abstract class ManagedUserSessionCache extends SessionCache
{
  /**
   * A mapping between a {@link SingleActorDAO} and the total number of {@link Session}s
   * the {@link SingleActorDAO} is currently logged in to.
   */
  private Map<String, Integer> userSessions;

  /**
   * Limit on the number of unique {@link SingleActorDAO}s who can be logged into this
   * {@link SessionCache} at a given time.
   */
  private int usersLimit;

  /**
   * Constructs a new {@link ManagedUserSessionCache}. Upon construction the
   * mapping between {@link SingleActorDAO} and {@link Session} count is empty.  Additionally,
   * the userLimit is set to 10,000.
   */
  public ManagedUserSessionCache()
  {
    this(10000);
  }

  /**
   * Constructs a new {@link ManagedUserSessionCache}. Upon construction the
   * mapping between {@link SingleActorDAO} and {@link Session} count is empty.  Additionally,
   * the userLimit is set to given value.
   *
   * @param userLimit Limit on the number of unique {@link SingleActorDAO}s who can be logged into
   *                  this {@link SessionCache} at any given time.
   */
  public ManagedUserSessionCache(int usersLimit)
  {
    this.userSessions = new HashMap<String, Integer>();
    this.usersLimit = usersLimit;
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
    Session session = this.logInCommon(user, locales);
    return session.getOid();
  }
  
  @Override
  protected String logIn(SingleActorDAOIF user, String dimensionKey, Locale[] locales)
  {
    Session session = this.logInCommon(user, locales);
    session.setDimension(dimensionKey);
    
    return session.getOid();
  }

  private Session logInCommon(String username, String password, Locale[] locales)
  {
    Session session = new Session(locales);

    //Update the session on the cache with the user logged in
    this.addSession(session);

    try
    {
      //Log the user into the session
      this.changeLogIn(username, password, session);
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
    
    //Update the session on the cache with the user logged in
    this.addSession(session);
    
    try
    {
      //Log the user into the session
      this.changeLogIn(user, session);
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
  
  /**
   * Logs a {@link UserDAO} into an existing {@link Session} of this {@link SessionCache}.
   * Additionally, the permissions of the {@link UserDAO} are loaded into the {@link Session}.
   * During log in the session count for the previous {@link UserDAO} of the {@link Session} is
   * decremented and the session count for the new {@link UserDAO} of the {@link Session} is
   * incremented.
   *
   * @param username The name of the user
   * @param password The password of the user
   * @param session The {@link Session} to log into.
   *
   * @return The oid of the {@link Session} which was logged into.
   */
  protected void changeLogIn(String username, String password, Session session)
  {
    sessionCacheLock.lock();

    try
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

      String userId = user.getOid();
      int sessionLimit = user.getSessionLimit();
      int currentAmount = getUserSessionCount(userId);
      UserDAOIF publicUser = UserDAO.getPublicUser();
      SingleActorDAOIF userOld = session.getUser();

      if(!userSessions.containsKey(userId) && userSessions.size() >= usersLimit)
      {
        String msg = "Too many users are currently logged into the system.";
        throw new ProgrammingErrorException(msg);
      }

      // If the session already has a user, we need to decrement the session
      // count.
      if (!user.equals(publicUser) && currentAmount >= sessionLimit)
      {
        String devMessage = "The user [" + username + "] already has the maximum number sessions opened";
        throw new MaximumSessionsException(devMessage, user);
      }

      if (userOld != null && !userOld.equals(publicUser))
      {
        this.decrementUserLoginCount(userOld);
      }

      //Increment the users session count
      if(!user.equals(publicUser))
      {
        userSessions.put(user.getOid(), new Integer(currentAmount + 1));
      }

      session.setUser(user);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Logs a {@link UserDAO} into an existing {@link Session} of this {@link SessionCache}.
   * Additionally, the permissions of the {@link UserDAO} are loaded into the {@link Session}.
   * During log in the session count for the previous {@link UserDAO} of the {@link Session} is
   * decremented and the session count for the new {@link UserDAO} of the {@link Session} is
   * incremented.
   *
   * @param username The name of the user
   * @param password The password of the user
   * @param session The {@link Session} to log into.
   *
   * @return The oid of the {@link Session} which was logged into.
   */
  protected void changeLogIn(SingleActorDAOIF user, Session session)
  {
    sessionCacheLock.lock();
    
    try
    {                  
      String userId = user.getOid();
      
      int sessionLimit = user.getSessionLimit();
      int currentAmount = getUserSessionCount(userId);
      UserDAOIF publicUser = UserDAO.getPublicUser();
      SingleActorDAOIF userOld = session.getUser();
      
      if(!userSessions.containsKey(userId) && userSessions.size() >= usersLimit)
      {
        String msg = "Too many users are currently logged into the system.";
        throw new ProgrammingErrorException(msg);
      }
      
      // If the session already has a user, we need to decrement the session
      // count.
      if (!user.isLoginSupported())
      {
        String devMessage = "The class [" + user.getType() + "] does not support logging in";
        throw new LoginNotSupportedException(devMessage, user);
      }
      
      // If the session already has a user, we need to decrement the session
      // count.
      if (!user.equals(publicUser) && sessionLimit != -1 && currentAmount >= sessionLimit)
      {
        String devMessage = "The user [" + user.getSingleActorName() + "] already has the maximum number sessions opened";
        throw new MaximumSessionsException(devMessage, user);
      }
      
      if (userOld != null && !userOld.equals(publicUser))
      {
        this.decrementUserLoginCount(userOld);
      }
      
      //Increment the users session count
      if(!user.equals(publicUser))
      {
        userSessions.put(user.getOid(), new Integer(currentAmount + 1));
      }
      
      session.setUser(user);
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
      userSessions.clear();
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Decrements the session count by one for the given {@link UserDAOIF}.
   *
   * @param user The {@link UserDAOIF} in which to decrement the session count.
   */
  private void decrementUserLoginCount(SingleActorDAOIF user)
  {
    sessionCacheLock.lock();
    try
    {
      int currentAmount = getUserSessionCount(user.getOid());

      currentAmount--;

      // Remove from the map
      if (currentAmount <= 0)
      {
        userSessions.remove(user.getOid());
      }
      else
      {
        userSessions.put(user.getOid(), currentAmount);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Decrements the session count by one for the {@link UserDAOIF} of the given
   * {@link Session}.  If the {@link Session} does not have a {@link UserDAOIF},
   * or is the public user, then session count is not modified.
   *
   * @param session A {@link Session}.
   */
  void decrementUserLoginCount(Session session)
  {
    // Remove one from the total amount of sessions allocated
    // to a user if the user is not anonymous
    SingleActorDAOIF user = session.getUser();
    UserDAOIF publicUser = UserDAO.getPublicUser();

    if (!user.getOid().equals(publicUser.getOid()))
    {
      decrementUserLoginCount(user);
    }
  }

  @Override
  int getUserSessionCount(String userId)
  {
    sessionCacheLock.lock();
    try
    {
      if (isLoggedIn(userId))
      {
        return userSessions.get(userId).intValue();
      }
      return 0;
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  boolean isLoggedIn(String userId)
  {
    if (userSessions.containsKey(userId))
    {
      return true;
    }

    return false;
  }

  @Override
  protected void addSession(Session session)
  {
    //If a user is logged into the session then the session count
    //of the user needs to be incremented.

    sessionCacheLock.lock();
    try
    {
      UserDAOIF publicUser = UserDAO.getPublicUser();
      SingleActorDAOIF user = session.getUser();

      if (!user.equals(publicUser))
      {
        int currentAmount = this.getUserSessionCount(user.getOid());

        // Add 1 to the session count for the user
        userSessions.put(user.getOid(), new Integer(currentAmount + 1));
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void setUser(String sessionId, UserDAOIF user)
  {
    sessionCacheLock.lock();

    try
    {
      // Ensure the user is active
      if (user.getInactive())
      {
        String devMessage = "The user [" + user.getUsername() + "] is inactive.";
        throw new InactiveUserException(devMessage, user);
      }

      String userId = user.getOid();
      int sessionLimit = user.getSessionLimit();
      int currentAmount = getUserSessionCount(userId);
      UserDAOIF publicUser = UserDAO.getPublicUser();
      Session session = this.getSession(sessionId);
      SingleActorDAOIF userOld = session.getUser();

      if(!userSessions.containsKey(userId) && userSessions.size() >= usersLimit)
      {
        String msg = "Too many users are currently logged into the system.";
        throw new ProgrammingErrorException(msg);
      }

      if (!user.equals(publicUser) && currentAmount >= sessionLimit)
      {
        String devMessage = "The user [" + user.getUsername() + "] already has the maximum number sessions opened";
        throw new MaximumSessionsException(devMessage, user);
      }

      // If the session already has a user, we need to decrement the session count.
      if (userOld != null && !userOld.equals(publicUser))
      {
        this.decrementUserLoginCount(userOld);
      }

      //Increment the users session count
      if(!user.equals(publicUser))
      {
        userSessions.put(user.getOid(), new Integer(currentAmount + 1));
      }

      session.setUser(user);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }
}
