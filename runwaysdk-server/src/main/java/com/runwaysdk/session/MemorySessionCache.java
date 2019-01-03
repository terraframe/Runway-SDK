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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.system.metadata.MdDimension;

/**
 * Concrete implementation of {@link ManagedUserSessionCache}. The
 * {@link Session}s in this {@link SessionCache} are stored in memory. In
 * addition, this cache spawns a thread which automatically checks and removes
 * expired {@link Session}s from the cache.
 * 
 * @author Justin Smethie
 */
public class MemorySessionCache extends ManagedUserSessionCache implements Runnable
{
  /**
   * The default amount of time to sleep between expire session checks
   */
  private static final int       DEFAULT_PERIOD = 5000;

  /**
   * The default number of sessions the cache is limited too.
   */
  private static final int       DEFAULT_LIMIT  = 50000;

  /**
   * A priority heap with the session closest to expiring on top
   */
  private PriorityQueue<Session> expireHeap;

  /**
   * A cache of sessions, the mapping between the session oid and the Session
   */
  private Map<String, Session>   sessions;

  /**
   * The thread to check for expired sessions
   */
  private Thread                 sessionChecker;

  /**
   * Limit on the number of sessions in the cache. A limit of -1 means that the
   * cache does not have a limit on the number of sessions.
   */
  private final int              limit;

  /**
   * The amount of time between session expiration checks (in miliseconds)
   */
  private final int              period;

  /**
   * The public Session
   */
  private Session                publicSession;

  /**
   * Creates a new {@link MemorySessionCache} with a default limit of 50,000
   * {@link Session}s objects and a limit of 10,000 unique {@link UserDAO}s. The
   * {@link Session}s of the cache are preserved in memory and automatically
   * checked for expiration every 5 seconds.
   */
  protected MemorySessionCache()
  {
    this(DEFAULT_LIMIT, DEFAULT_PERIOD, 10000);
  }

  /**
   * Creates a new {@link MemorySessionCache} with the limit of the given,
   * 'limit', size on session objects. The sessions of the cache are preserved
   * in memory and automatically checked for expiration every 'period' seconds.
   * 
   * @param limit
   *          Limit to the number of {@link Session}s in the cache
   * @param period
   *          The period between checks of the cache (in miliseconds)
   * @param usersLimit
   *          Limit to the number of unique {@link UserDAO}s allowed to be
   *          logged in at any given time.
   */
  @Inject
  protected MemorySessionCache(@Named("limit")
  int limit, @Named("period")
  int period, @Named("usersLimit")
  int usersLimit)
  {
    super(usersLimit);

    this.limit = limit;
    this.period = period;

    this.sessions = new HashMap<String, Session>();
    this.expireHeap = new PriorityQueue<Session>();

    this.publicSession = new Session(CommonProperties.getDefaultLocale());
    this.publicSession.setExpirationTime(-1);
    this.sessions.put(this.publicSession.getOid(), this.publicSession);
    this.expireHeap.offer(this.publicSession);

    sessionChecker = new Thread(this, "Session Cleanup");
    sessionChecker.setDaemon(true);
    sessionChecker.start();
  }

  @Override
  protected void changeLogin(String username, String password, String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      if (sessionId.equals(publicSession.getOid()))
      {
        String msg = "Users are not permitted to log into the public session [" + sessionId + "]";
        throw new InvalidLoginException(msg);
      }

      // Get the session
      Session session = this.getSession(sessionId);

      super.changeLogIn(username, password, session);
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
      if (sessionId.equals(publicSession.getOid()))
      {
        String msg = "Users are not permitted to log into the public session [" + sessionId + "]";
        throw new InvalidLoginException(msg);
      }

      // Get the session
      Session session = this.getSession(sessionId);

      super.changeLogIn(user, session);
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
      if (sessionId.equals(publicSession.getOid()))
      {
        String msg = "Users are not permitted to change the dimension of the public session [" + sessionId + "]";
        throw new InvalidLoginException(msg);
      }

      // Get the session
      Session session = this.getSession(sessionId);
      session.setDimension(dimensionKey);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  public void run()
  {
    while (true)
    {
      cleanUp();

      try
      {
        Thread.sleep(period);
      }
      catch (Exception e)
      {
        String errMsg = e.getMessage();
        throw new ProgrammingErrorException(errMsg);
      }
    }
  }

  @Override
  protected void cleanUp()
  {
    sessionCacheLock.lock();
    try
    {
      long currentTime = System.currentTimeMillis();
      Session session = expireHeap.peek();

      while (session != null && session.isExpired(currentTime))
      {
        // The session on the top of the heat has expirted
        close(session);

        // Check if the next session has also expired
        session = expireHeap.peek();
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void closeSession(String sessionId)
  {
    sessionCacheLock.lock();
    try
    {
      // Do nothing if the session is the public session
      if (!sessionId.equals(publicSession.getOid()))
      {
        Session session = getSession(sessionId);
        close(session);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Removes a {@link Session} from the {@link SessionCache}. In addition, if
   * the {@link Session} has a {@link UserDAO} then this method decrements the
   * session count of the {@link UserDAO}.
   * 
   * @param session
   *          The {@link Session} to remove.
   */
  private void close(Session session)
  {
    sessionCacheLock.lock();
    try
    {
      // Remove the session from the heap
      expireHeap.remove(session);

      // Remove the session from the sessions cache
      sessions.remove(session.getOid());

      this.decrementUserLoginCount(session);

      // Ensure that the heap size and the session cache are synchronized
      if (expireHeap.size() != sessions.size())
      {
        String error = "Session [" + session.getOid() + "]'s cache is corrupt";
        throw new ProgrammingErrorException(error);
      }
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
      super.clearSessions();
      expireHeap.clear();
      sessions.clear();

      // Put the public session back onto the heap and mapping
      sessions.put(publicSession.getOid(), publicSession);
      expireHeap.offer(publicSession);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void renewSession(String sessionId)
  {
    sessionCacheLock.lock();
    try
    {
      Session session = getSession(sessionId);

      session.renew();
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected Session getSession(String sessionId)
  {
    sessionCacheLock.lock();
    try
    {
      if (sessions.containsKey(sessionId))
      {
        return sessions.get(sessionId);
      }

      String error = "Session [" + sessionId + "] does not exist or has expired.";
      throw new InvalidSessionException(error);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected boolean containsSession(String sessionId)
  {
    sessionCacheLock.lock();
    try
    {
      return sessions.containsKey(sessionId);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void addSession(Session session)
  {
    sessionCacheLock.lock();
    try
    {
      if (!sessions.containsKey(session.getOid()) && this.full())
      {
        String error = "Session Cache is full.";
        throw new ProgrammingErrorException(error);
      }

      // Add the session to the Session Cache.
      if (!sessions.containsKey(session.getOid()))
      {
        super.addSession(session);
        expireHeap.offer(session);
      }
      sessions.put(session.getOid(), session);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected boolean full()
  {
    return ( limit != -1 && sessions.size() >= limit );
  }

  @Override
  protected Session makeRoom()
  {
    // Get the session closet to expiring
    Session session = expireHeap.peek();

    // Remove the session from the cache
    this.close(session);

    return session;
  }

  @Override
  protected Session getPublicSession()
  {
    return publicSession;
  }

  @Override
  protected Session getSessionForRequest(String sessionId)
  {
    return this.getSession(sessionId);
  }
  
  /**
   * @see com.runwaysdk.session.SessionCache#getIterator()
   */
  public SessionIterator getIterator()
  {
    return new MemorySessionIterator();
  }
  private class MemorySessionIterator implements SessionIterator
  {
    Iterator<Session> iterator;
    
    Session current;
    Session next;
    
    private MemorySessionIterator()
    {
      iterator = sessions.values().iterator();
      
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
    
    private Session privateNext(boolean isConstructor)
    {
      if (next == null && !isConstructor) { throw new NoSuchElementException(); }
      
      current = next;
      
      while (true)
      {
        if (iterator.hasNext())
        {
          next = iterator.next();
          
          // Skip the public session.
          if (UserDAO.getPublicUser().getOid().equals(next.getUser().getOid()))
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
      
      return current;
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#remove()
     */
    @Override
    public void remove()
    {
      MemorySessionCache.this.closeSession(current.getOid());
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
      // Do nothing.
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#getAll()
     */
    @Override
    public Collection<SessionIF> getAll()
    {
      Collection<SessionIF> sesses = new ArrayList<SessionIF>();
      
      while (hasNext())
      {
        sesses.add(next());
      }
      
      return sesses;
    }
  }
}
