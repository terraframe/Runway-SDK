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
package com.runwaysdk.localization;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.session.Request;

public class LocalizationFacadeTest
{
  @Test
  @Request
  public void testGetLocalizationProvider()
  {
    RunwayLocalizationProviderIF provider = LocalizationFacade.getLocalizationProvider();
    
    Assert.assertTrue(provider instanceof RunwayServerLocalizationProvider);
  }
  
  @Test
  @Request
  public void testInstallAndUninstallLocale()
  {
    Locale installLocale = Locale.CANADA;
    
    SupportedLocaleIF supLocale = LocalizationFacade.install(installLocale);
    
    try
    {
      Assert.assertEquals(installLocale, supLocale.getLocale());
      
      Assert.assertEquals(Locale.CANADA.getDisplayName(CommonProperties.getDefaultLocale()), supLocale.getDisplayLabel().getDefaultValue());
      Assert.assertEquals(Locale.CANADA.getDisplayName(Locale.CANADA), supLocale.getDisplayLabel().getValue(Locale.CANADA));
      
      Assert.assertTrue(LocalizationFacade.getSupportedLocales().contains(supLocale));
      
      Assert.assertTrue(LocalizationFacade.getInstalledLocales().contains(installLocale));
    }
    finally
    {
      LocalizationFacade.uninstall(installLocale);
    }
    
    Assert.assertFalse(LocalizationFacade.getSupportedLocales().contains(supLocale));
    
    Assert.assertFalse(LocalizationFacade.getInstalledLocales().contains(installLocale));
  }
}
