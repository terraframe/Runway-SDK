/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalizationFacade
{

  private static Logger                       logger              = LoggerFactory.getLogger(LocalizationFacade.class);

  private static Boolean                      didLoadLocalization = false;

  private static RunwayLocalizationProviderIF localizer           = null;

  private static synchronized RunwayLocalizationProviderIF getLocalizer()
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
   * Returns the list of installed locales in the system.
   */
  public static List<Locale> getInstalledLocales()
  {
    // SupportedLocaleQuery query = new SupportedLocaleQuery(new
    // QueryFactory());
    // query.ORDER_BY_ASC(query.getLocaleLabel());

    RunwayLocalizationProviderIF localizer = getLocalizer();

    if (localizer != null)
    {
      return localizer.getInstalledLocales();
    }
    else
    {
      return null;
    }
  }

  /**
   * Localizes the given key to the user's current locale. If the key cannot be
   * localized null is returned.
   * 
   * TODO : And if the user doesn't have a locale?
   */
  public static String localize(String key)
  {
    RunwayLocalizationProviderIF localizer = getLocalizer();

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
   * null is returned.
   */
  public static String localize(String key, Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizer();

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
   * Returns all localized keys for the user's current locale. Returns null if
   * the keys cannot be fetched.
   * 
   * TODO : And what if the user doesn't have a locale?
   * 
   * @return
   */
  public static Map<String, String> getAll()
  {
    RunwayLocalizationProviderIF localizer = getLocalizer();

    if (localizer != null)
    {
      return localizer.getAll();
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns all localized keys for the given locale. Returns null if the keys
   * cannot be fetched.
   * 
   * @param locale
   * @return
   */
  public static Map<String, String> getAll(Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizer();

    if (localizer != null)
    {
      return localizer.getAll(locale);
    }
    else
    {
      return null;
    }
  }

  /**
   * Fetches the parameterized, localized error message template for the given
   * exception. The variable String arguments represent the parameters in the
   * template string. For example, given the template "The {0} in the {1}." and
   * arguments "cat" and "hat", the final String will be "The cat in the hat."
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
    RunwayLocalizationProviderIF localizer = getLocalizer();

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
   * Installs the given locale into the system.
   * 
   * @param locale
   */
  public static void install(Locale locale)
  {
    RunwayLocalizationProviderIF localizer = getLocalizer();

    if (localizer != null)
    {
      localizer.install(locale);
    }
  }

}
