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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.RunwayLocalizationProviderIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.SupportedLocale;
import com.runwaysdk.system.metadata.SupportedLocaleQuery;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class RunwayServerLocalizationProvider implements RunwayLocalizationProviderIF
{
  
  @Override
  @Request
  public String localize(String key, Locale locale)
  {
    return LocalizedValueStore.localize(key, locale);
  }

  @Override
  @Request
  public Map<String, String> getAll(Locale locale)
  {
    return LocalizedValueStore.getAll(locale);
  }

  @Override
  @Request
  public String localize(String key)
  {
    return LocalizedValueStore.localize(key);
  }

  @Override
  @Request
  public Map<String, String> getAll()
  {
    return LocalizedValueStore.getAll();
  }

  @Override
  @Transaction
  public void install(Locale locale)
  {
    String displayName = locale.getDisplayName(Locale.ENGLISH); // TODO : Really? We're hard coding Locale.ENGLISH ?

    SupportedLocale sl = new SupportedLocale();
    sl.setEnumName(locale.toLanguageTag().replace("-", "_"));
    sl.setLocaleLabel(displayName);
    sl.getDisplayLabel().setDefaultValue(displayName);
    sl.apply();
  }
  
  /**
   * Returns the list of installed locales in the system.
   */
  @Request
  public List<Locale> getInstalledLocales()
  {
    ArrayList<Locale> locales = new ArrayList<Locale>();
    
    SupportedLocaleQuery query = new SupportedLocaleQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getLocaleLabel());
    
    OIterator<? extends SupportedLocale> it = query.getIterator();
    
    try
    {
      while (it.hasNext())
      {
        Locale locale = ConversionFacade.getLocale(it.next().getEnumName());
        
        locales.add(locale);
      }
    }
    finally
    {
      it.close();
    }
    
    return locales;
  }

}
