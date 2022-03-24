/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a clean abstraction for all localization related activities in Runway. These methods are implemented by a 
 * {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}, which is resolved at runtime using the java service loader paradigm.
 * The default implementation of these methods is found in the runwaysdk-localization-server package.
 * 
 * @seealso {@link com.runwaysdk.session.LocaleManager}
 * 
 * @author rrowlands
 */
public class LocalizationFacade
{

  private static Logger                       logger              = LoggerFactory.getLogger(LocalizationFacade.class);

  private static Boolean                      didLoadLocalization = false;

  private static RunwayLocalizationProviderIF localizer           = null;

  /**
   * Returns the underlying localization provider.
   */
  public static synchronized RunwayLocalizationProviderIF getLocalizationProvider()
  {
    if (didLoadLocalization)
    {
      return localizer;
    }

    try
    {
      didLoadLocalization = true;

      ServiceLoader<RunwayLocalizationProviderIF> loader = ServiceLoader.load(RunwayLocalizationProviderIF.class, Thread.currentThread().getContextClassLoader());

      Iterator<RunwayLocalizationProviderIF> it = loader.iterator();

      localizer = it.next();

      return localizer;
    }
    catch (Throwable ex)
    {
      logger.info("A Runway localization strategy has not been installed. Defaulting to English.");

      return null;
    }
  }

  /**
   * Returns all system installed locales, represented as {@link java.util.Locale}. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return an empty list. This method will NOT include the DEFAULT_LOCALE, which is a LocalStruct concept and has no
   * Java Locale representation. If you're using this method to loop through locales in a LocalStruct you must manually fetch that data.
   */
  public static Set<Locale> getInstalledLocales()
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.getInstalledLocales();
    }
    else
    {
      return new HashSet<Locale>(Arrays.asList(new Locale[] {}));
    }
  }
  
  /**
   * Returns all system installed locales, represented as {@link com.runwaysdk.localization.SupportedLocaleIF}. If a
   * {@link com.runwaysdk.localization.RunwayLocalizationProviderIF} is not installed, this method will return an empty list. This method will NOT include the
   * DEFAULT_LOCALE, which is a LocalStruct concept and has no Java Locale representation. If you're using this method to loop through locales in a LocalStruct
   * you must manually fetch that data.
   */
  public static Set<SupportedLocaleIF> getSupportedLocales()
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.getSupportedLocales();
    }
    else
    {
      return new HashSet<SupportedLocaleIF>();
    }
  }
  
  /**
   * Returns the equivalent {@link com.runwaysdk.localization.SupportedLocaleIF} for a given {@link java.util.Locale}. If the given locale
   * is not installed, a {@link com.runwaysdk.dataaccess.cache.DataNotFoundException} is thrown. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return null.
   */
  public static SupportedLocaleIF getSupportedLocale(Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.getSupportedLocale(locale);
    }
    else
    {
      return null;
    }
  }
  
  /**
   * Constructs a Java locale from the provided information.
   * 
   * @param language
   * @param country
   * @param variant
   * @return
   */
  public static Locale getLocale(String language, String country, String variant)
  {
    String localeString = language;
    
    if (country != null && country.length() > 0)
    {
      localeString += "_" + country;
      
      if (variant != null && variant.length() > 0)
      {
        localeString += "_" + variant;
      }
    }
    
    Locale locale = org.apache.commons.lang.LocaleUtils.toLocale(localeString);
    
    return locale;
  }

  /**
   * Localizes the given key to the user's current locale. If the key cannot be
   * localized null is returned. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return null. If the user does not have a locale
   * the (default) locale, fetched from common.properties, is used.
   */
  public static String localize(String key)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.localize(key);
    }
    else
    {
      return null;
    }
  }

  /**
   * Localizes the given key to the given locale. If the key cannot be localized
   * null is returned. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return null.
   */
  public static String localize(String key, Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.localize(key, locale);
    }
    else
    {
      return null;
    }
  }
  
  /**
   * Returns localized values for all installed locales for the given key. If the key cannot be localized
   * null is returned. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return null.
   * 
   * @param key
   * @return
   */
  public static LocalizedValueIF localizeAll(String key)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.localizeAll(key);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns all localized keys for the user's current locale. Returns null if
   * the keys cannot be fetched. If the user does not have a locale
   * the default locale is used. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return an empty map.
   */
  public static Map<String, String> getAll()
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.getAll();
    }
    else
    {
      return new HashMap<String, String>();
    }
  }

  /**
   * Returns all localized keys for the given locale. Returns null if the keys
   * cannot be fetched. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return an empty map.
   */
  public static Map<String, String> getAll(Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.getAll(locale);
    }
    else
    {
      return new HashMap<String, String>();
    }
  }

  /**
   * Fetches the parameterized, localized error message template for the given
   * exception. The variable String arguments represent the parameters in the
   * template string. For example, given the template "The {0} in the {1}." and
   * arguments "cat" and "hat", the final String will be "The cat in the hat."
   * If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will return the defaultValue, templatized with
   * the given params.
   * 
   * @param locale
   *          The desired locale of the message
   * @param key
   *          The name of the Exception whose message is being retrieved
   * @param defautValue
   *          The error message in English, which will be used if the key cannot
   *          be localized.
   * @param params
   *          The array of parameters to plug into the template string
   */
  public static String getMessage(Locale locale, String key, String defaultValue, String... params)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer == null)
    {
      return parseMessage(defaultValue, params);
    }

    String template = null;

    try
    {
      template = localizer.localize(key, locale);
    }
    catch (RuntimeException e)
    {
      logger.error("Unable to retrieve localized template", e);
    }

    if (template == null || template.length() == 0)
    {
      return parseMessage(defaultValue, params);
    }

    return parseMessage(template, params);
  }

  /**
   * Parses the parameterized, localized error message template for the given
   * exception. The variable String arguments represent the parameters in the
   * template string. For example, given the template "The {0} in the {1}." and
   * arguments "cat" and "hat", the final String will be "The cat in the hat."
   * 
   * @param key
   *          The name of the Exception whose message is being retrieved
   * @param params
   *          The array of parameters to plug into the template string
   */
  public static String parseMessage(String template, String... params)
  {
    // Sub in all the parameters
    for (int i = 0; i < params.length; i++)
    {
      // If the string is too long (more than 256 characters) shorten it.
      // We don't want a 3 page error message.
      if (params[i].length() > 256)
        params[i] = params[i].substring(0, 256) + "...";

      template = template.replace("{" + i + "}", params[i]);
    }

    return template;
  }

  /**
   * Installs the given locale into the system. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will do nothing and return null.
   * 
   * @param locale
   */
  public static SupportedLocaleIF install(Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      return localizer.install(locale);
    }
    else
    {
      return null;
    }
  }
  
  /**
   * Uninstalls the given locale into the system. If the locale does not exist
   * a DataNotFoundException will be thrown. If a {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
   * is not installed, this method will do nothing. 
   * 
   * @param locale
   */
  public static void uninstall(Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizationProvider();

    if (localizer != null)
    {
      localizer.uninstall(locale);
    }
  }
  
}
