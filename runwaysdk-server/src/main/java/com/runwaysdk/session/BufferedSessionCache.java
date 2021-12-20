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

import com.google.inject.Inject;
import com.google.inject.name.Named;


/**
 * Concrete implementation of {@link CompositeSessionCache}.  The first
 * {@link SessionCache} is used as a pre cache for the main cache, the second {@link SessionCache}.
 * A {@link Session} is first added to the pre cache, if the {@link Session} is then
 * retrieved it is removed from the pre cache and added to the main cache.
 * 
 * @author Justin Smethie
 */
public class BufferedSessionCache extends CompositeSessionCache
{
  /**
   * Constructs a new {@link BufferedSessionCache} with the given pre cache
   * and main cache.
   *
   * @param preCache {@link SessionCache} to use as a the pre cache.
   * @param mainCache {@link SessionCache} to use as the main cache.
   * 
   * @pre preCache != null
   * @pre mainCache != null
   * @pre !preCache.equals(mainCache)
   */
   @Inject
  protected BufferedSessionCache(@Named("preCache") SessionCache preCache, @Named("mainCache") SessionCache mainCache)
  {
    super(preCache, mainCache);
  }

  @Override
  protected SessionCache getCache(String sessionId)
  {
    if(!secondCache.containsSession(sessionId))
    {
      if(!firstCache.containsSession(sessionId))
      {
        String error = "Session [" + sessionId + "] does not exist or has expired.";
        throw new InvalidSessionException(error);
      }
      
      //If the {@link Session} is in the preCache then remove it
      //from the preCache and add it to the main cache.
      Session session = firstCache.getSession(sessionId);
      firstCache.closeSession(sessionId);
      secondCache.addSession(session);      
    }
    
    return secondCache;
  }
  
  @Override
  protected void addSession(Session session)
  {
    if(!secondCache.containsSession(session.getOid()) &&
       !firstCache.containsSession(session.getOid()))
    {      
      firstCache.addSession(session);
    }
  }

  @Override
  protected boolean full()
  {
    return firstCache.full() || secondCache.full();
  }

  @Override
  protected Session makeRoom()
  {
    return secondCache.makeRoom();
  }
  
  @Override
  protected boolean containsSession(String sessionId)
  {
    boolean contains = secondCache.containsSession(sessionId);

    if (!contains)
    {
      contains = firstCache.containsSession(sessionId);
    }

    return contains;
  }

  @Override
  protected Session getPublicSession()
  {
    return secondCache.getPublicSession();
  }

  @Override
  protected Session getSessionForRequest(String sessionId)
  {
    return this.getSession(sessionId);
  }
}
