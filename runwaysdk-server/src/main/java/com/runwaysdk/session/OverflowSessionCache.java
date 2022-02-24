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
 * {@link SessionCache} is used as the main cache.  When the first cache is
 * full then second {@link SessionCache} is used for the overflowing sessions.
 * When a {@link Session} is retrieved from the overflow {@link SessionCache} it is
 * transfered to the main cache.  In this sitution if the main cache is full then
 * a {@link Session} of the main cache is transfered to the overflow cache to make
 * room for the session from the overflow cache.
 * 
 * @author Justin Smethie
 */
public class OverflowSessionCache extends CompositeSessionCache
{
  /**
   * Constructs a new {@link OverflowSessionCache} with the given main cache
   * and overflow cache.
   *
   * @param mainCache {@link SessionCache} to use as a the main cache.
   * @param overflowCache {@link SessionCache} to use as the overflow cache.
   * 
   * @pre mainCache != null
   * @pre overflowCache != null
   * @pre !mainCache.equals(overflowCache)
   */
  @Inject
  protected OverflowSessionCache(@Named("mainCache") SessionCache mainCache, @Named("overflowCache") SessionCache overflowCache)
  {
    super(mainCache, overflowCache);    
  }
    
  @Override
  protected SessionCache getCache(String sessionId)
  {    
    if(!firstCache.containsSession(sessionId))
    {
      if(!secondCache.containsSession(sessionId))
      {
        String error = "Session [" + sessionId + "] does not exist or has expired.";
        throw new InvalidSessionException(error);
      }
       
      //Get the session from the overflow cache and put it into the main cache.

      //If the main cache is full then remove the oldest session from
      //it and add it to the overflow cache
      if(firstCache.full())
      {
        Session oldest = firstCache.makeRoom();
        
        if(oldest != null)
        {
          secondCache.addSession(oldest);
        }
      }
      
      //Move the session into the main cache
      Session session = secondCache.getSession(sessionId);
      secondCache.closeSession(sessionId);
      firstCache.addSession(session);
    }
    
    return firstCache;
  }
  
  @Override
  protected void addSession(Session session)
  {
    //If the main cache is full then add the session
    //to the overflow cache.
    if(!firstCache.full())
    {
      firstCache.addSession(session);
    }
    else
    {
      secondCache.addSession(session);
    }
  }

  @Override
  protected boolean full()
  {
    //Return if the overflow cache is full.
    return secondCache.full();
  }

  @Override
  protected Session makeRoom()
  {
    if(firstCache.full())
    {
      return secondCache.makeRoom();
    }
    
    return firstCache.makeRoom();
  }
  
  @Override
  protected boolean containsSession(String sessionId)
  {
    boolean contains = firstCache.containsSession(sessionId);

    if (!contains)
    {
      contains = secondCache.containsSession(sessionId);
    }

    return contains;
  }

  @Override
  protected Session getPublicSession()
  {
    return firstCache.getPublicSession();
  }

  @Override
  protected Session getSessionForRequest(String sessionId)
  {
    return this.getSession(sessionId);
  }
}
