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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.SupportedLocale;

/**
 * Default implementation of the {@link com.runwaysdk.RunwayLocalizationProviderIF} interface. 
 * 
 * @author rrowlands
 */
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
  public LocalizedValueIF localizeAll(String key)
  {
    return LocalizedValueStore.localizeAll(key);
  }

  @Override
  @Request
  public Map<String, String> getAll()
  {
    return LocalizedValueStore.getAll();
  }

  @Override
  @Request
  public SupportedLocaleIF install(Locale locale)
  {
    SupportedLocaleIF supportedLocale = this.installInTrans(locale);
    
    SupportedLocaleCache.clear();
    
    return supportedLocale;
  }
  
  @Transaction
  private SupportedLocaleIF installInTrans(Locale locale)
  {
    SupportedLocale sl = new SupportedLocale();
    
    sl.setEnumName(locale.toLanguageTag().replace("-", "_"));
    
    sl.setLocaleLabel(locale.getDisplayName(CommonProperties.getDefaultLocale())); // I don't think this attribute is actually used anywhere
    
    sl.apply();
    
    
    sl.appLock();
    for (Locale installedLocale : this.getInstalledLocales())
    {
      sl.getDisplayLabel().setValue(installedLocale, locale.getDisplayName(installedLocale));
    }
    sl.getDisplayLabel().setDefaultValue(locale.getDisplayName(CommonProperties.getDefaultLocale()));
    sl.apply();
    
    
    return sl;
  }
  
  @Override
  @Request
  public void uninstall(Locale locale)
  {
    this.uninstallInTrans(locale);
    
    SupportedLocaleCache.clear();
  }
  
  @Transaction
  private void uninstallInTrans(Locale locale)
  {
    SupportedLocale supportedLocale = (SupportedLocale) this.getSupportedLocale(locale);
    
    supportedLocale.delete();
  }
  
  @Override
  @Request
  public SupportedLocaleIF getSupportedLocale(Locale locale)
  {
    String enumName = locale.toLanguageTag().replace("-", "_");
    
    Set<SupportedLocaleIF> supportedLocales = this.getSupportedLocales();
    
    for (SupportedLocaleIF supportedLocale : supportedLocales)
    {
      if (supportedLocale.getName().equals(enumName))
      {
        return supportedLocale;
      }
    }
    
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(SupportedLocale.CLASS);
    throw new DataNotFoundException("Could not find an installed locale [" + locale.toLanguageTag() + "].", mdClassIF);
  }
  
  /**
   * Returns the list of installed locales in the system.
   */
  @Request
  public Set<Locale> getInstalledLocales()
  {
    return SupportedLocaleCache.getInstalledLocales();
  }
  
  @Request
  public Set<SupportedLocaleIF> getSupportedLocales()
  {
    return SupportedLocaleCache.getSupportedLocales();
  }

}
