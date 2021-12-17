package com.runwaysdk.utf8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTF8Control extends ResourceBundle.Control {
  public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
    String bundleName = toBundleName(baseName, locale);
    ResourceBundle bundle = null;
    if (format.equals("java.class")) {
      try {
        Class<? extends ResourceBundle> bundleClass = (Class)loader
          .loadClass(bundleName);
        if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
          bundle = bundleClass.newInstance();
        } else {
          throw new ClassCastException(String.valueOf(bundleClass.getName()) + 
              " cannot be cast to ResourceBundle");
        } 
      } catch (ClassNotFoundException classNotFoundException) {}
    } else if (format.equals("java.properties")) {
      final String resourceName = toResourceName(bundleName, "properties");
      final ClassLoader classLoader = loader;
      final boolean reloadFlag = reload;
      InputStream stream = null;
      try {
        stream = 
          AccessController.<InputStream>doPrivileged(new PrivilegedExceptionAction<InputStream>() {
              public InputStream run() throws IOException {
                InputStream is = null;
                if (reloadFlag) {
                  URL url = classLoader.getResource(resourceName);
                  if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                      connection.setUseCaches(false);
                      is = connection.getInputStream();
                    } 
                  } 
                } else {
                  is = classLoader.getResourceAsStream(resourceName);
                } 
                return is;
              }
            });
      } catch (PrivilegedActionException e) {
        throw (IOException)e.getException();
      } 
      if (stream != null)
        try {
          bundle = new PropertyResourceBundle(new InputStreamReader(stream, 
                "UTF-8"));
        } finally {
          stream.close();
        }  
    } else {
      throw new IllegalArgumentException("unknown format: " + format);
    } 
    return bundle;
  }
}