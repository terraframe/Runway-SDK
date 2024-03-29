/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.localization;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.SupportedLocale;
import com.runwaysdk.system.metadata.SupportedLocaleQuery;

public class SupportedLocaleCache
{
  private static Lock refreshLock = new ReentrantLock();
  
  private static Set<SupportedLocaleIF> supportedLocales = null;
  
  private static Set<Locale> locales = null;

  public static Set<SupportedLocaleIF> getSupportedLocales()
  {
    refreshLock.lock();
    
    try
    {
      if (supportedLocales == null)
      {
        internalRefresh();
      }
      
      // The person calling this method needs their own copy of our cache otherwise they're vulnerable to threading issues.
      Set<SupportedLocaleIF> clone = new HashSet<SupportedLocaleIF>();
      for (SupportedLocaleIF locale : supportedLocales)
      {
        clone.add(locale);
      }
  
      return clone;
    }
    finally
    {
      refreshLock.unlock();
    }
  }
  
  public static Set<Locale> getInstalledLocales()
  {
    refreshLock.lock();
    
    try
    {
      if (locales == null)
      {
        internalRefresh();
      }
      
      // The person calling this method needs their own copy of our cache otherwise they're vulnerable to threading issues.
      Set<Locale> clone = new HashSet<Locale>();
      for (Locale locale : locales)
      {
        clone.add(locale);
      }
  
      return clone;
    }
    finally
    {
      refreshLock.unlock();
    }
  }
  
  public static void clear()
  {
    refreshLock.lock();
    
    try
    {
      supportedLocales = null;
      locales = null;
    }
    finally
    {
      refreshLock.unlock();
    }
  }
  
  private static void internalRefresh()
  {
    // SupportedLocale is already cached in Runway. It's cache algorithm strategy is set to "nothing", however
    // "nothing" in Runway actually functionally means inherit from parent. The parent strategy for enums is "hardcoded".
    
    supportedLocales = new HashSet<SupportedLocaleIF>();
    
    
    // TODO : Runway is NOT updating this cache correctly...
//    List<? extends EntityDAOIF> entityDAOs = ObjectCache.getCachedEntityDAOs(SupportedLocale.CLASS);
//    
//    for (EntityDAOIF entityDAOIF : entityDAOs)
//    {
//      if (entityDAOIF.getType().equals(SupportedLocale.CLASS)) // Even though SupportedLocale is cached, it's grouped in a cache with ALL enums.
//      {
//        SupportedLocale locale = (SupportedLocale) BusinessFacade.get(entityDAOIF);
//        
//        supportedLocales.add((SupportedLocaleIF) locale);
//      }
//    }
    
    SupportedLocaleQuery slq = new SupportedLocaleQuery(new QueryFactory());
    OIterator<? extends SupportedLocale> it = slq.getIterator();
    try
    {
      while (it.hasNext())
      {
        SupportedLocale locale = it.next();
        
        supportedLocales.add((SupportedLocaleIF) locale);
      }
    }
    finally
    {
      it.close();
    }
    
    locales = supportedLocales.stream().map(sup -> sup.getLocale()).collect(Collectors.toSet());
  }
}
