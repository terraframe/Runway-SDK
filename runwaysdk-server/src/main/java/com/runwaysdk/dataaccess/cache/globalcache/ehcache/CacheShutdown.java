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
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;

public class CacheShutdown
{
  public static synchronized void shutdown()
  {
    ObjectCache.shutdownGlobalCache();

    List<CacheManager> knownCacheManagers = CacheManager.ALL_CACHE_MANAGERS;

    while (!knownCacheManagers.isEmpty())
    {
      CacheManager manager = (CacheManager) CacheManager.ALL_CACHE_MANAGERS.get(0);

      if (ServerProperties.getGlobalCacheStats())
      {
        String[] names = manager.getCacheNames();

        for (String name : names)
        {
          Cache cache = manager.getCache(name);
          
          if(cache.isStatisticsEnabled())
          {
            RunwayLogUtil.logToLevel(LogLevel.INFO, cache.getStatistics().toString());
          }
        }
      }

      manager.shutdown();
    }
  }
}
