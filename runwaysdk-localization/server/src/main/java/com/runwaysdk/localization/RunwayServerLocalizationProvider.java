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
