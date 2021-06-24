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
