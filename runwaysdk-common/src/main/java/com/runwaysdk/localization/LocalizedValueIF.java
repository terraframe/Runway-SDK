package com.runwaysdk.localization;

import java.util.Locale;
import java.util.Map;

public interface LocalizedValueIF
{
  public String getValue();
  
  public String getValue(Locale locale);

  public void setValue(Locale locale, String value);

  public void setValue(String value);
  
  /**
   * Returns a map where the key is locale.toString() and the value is the label for that
   * locale.
   */
  public Map<String, String> getLocaleMap();
  
  /**
   * Sets the localized values where the key is locale.toString()  and the value is the label for that
   * locale.
   */
  public void setLocaleMap(Map<String, String> map);

  /**
   * Gets the value of the default locale.
   */
  public String getDefaultValue();
  
  /**
   * Sets the value of the default locale.
   */
  public void setDefaultValue(String value);
}
