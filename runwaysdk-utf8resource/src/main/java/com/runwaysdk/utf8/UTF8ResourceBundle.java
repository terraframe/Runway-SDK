package com.runwaysdk.utf8;

import java.util.Locale;
import java.util.ResourceBundle;

public class UTF8ResourceBundle {
  public static ResourceBundle getBundle(String baseName) {
    return ResourceBundle.getBundle(baseName, new UTF8Control());
  }
  
  public static ResourceBundle getBundle(String baseName, Locale locale) {
    return ResourceBundle.getBundle(baseName, locale, new UTF8Control());
  }
  
  public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
    return ResourceBundle.getBundle(baseName, locale, loader, new UTF8Control());
  }
}