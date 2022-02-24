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
/**
 * Created on Aug 2, 2005
 *
 */
package com.runwaysdk.dataaccess.cache;

import com.runwaysdk.generation.loader.LockHolder;

/**
 * @author nathan
 *
 * TODO To change the template for this generated comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public privileged aspect ObjectCacheFacade_ThreadSafety 
{
  protected pointcut protectedMethods() :
    (execution (public * ObjectCache.*(..)) ||  execution(protected * ObjectCache.*(..))   )  && 
    !execution (* ObjectCache.init()) &&
    !execution (* ObjectCache.addListener(..)) &&
    !execution (* ObjectCache.removeListener(..)) &&
    !execution (* ObjectCache.init()) &&
    !execution (* ObjectCache.refreshCache()) &&
    !execution (* ObjectCache.flushCache()) &&
    !execution (* ObjectCache.shutdownGlobalCache()) &&
    !execution (* ObjectCache.beginGlobalCacheTransaction()) &&
    !execution (* ObjectCache.commitGlobalCacheTransaction()) &&
    !execution (* ObjectCache.rollbackGlobalCacheTransaction());
  
  protected pointcut initCollections() 
    : protectedMethods() &&
    !cflowbelow(execution (* ObjectCache.init()));
    
    before() : initCollections()  
    {
      try
      {
        LockHolder.lockCache(this);
        if (ObjectCache.initialized == false)
        {
          ObjectCache.init();
        }
      }
      finally
      {
        LockHolder.unlockCache();
      }
    }
}
