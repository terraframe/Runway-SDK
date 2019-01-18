package com.runwaysdk;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface RunwayLocalizationProviderIF
{
  public String localize(String key);
  
  public String localize(String key, Locale locale);
  
  public Map<String, String> getAll();
  
  public Map<String, String> getAll(Locale locale);
  
  public void install(Locale locale);
  
  public List<Locale> getInstalledLocales();
}
