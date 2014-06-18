/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk;

import java.util.Locale;

import com.terraframe.utf8.UTF8ResourceBundle;

public class ClientExceptionMessageLocalizer extends ExceptionMessageLocalizer
{
  private static final String BUNDLE = "clientExceptions";

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
   * @param params
   *          The array of parameters to plug into the template string
   */
  protected static String getMessage(Locale locale, String key, String... params)
  {
    String hashkey = BUNDLE;

    if (locale != null)
    {
      hashkey += "-" + locale.toString();

      if (!props.containsKey(hashkey))
      {
        props.put(hashkey, UTF8ResourceBundle.getBundle(BUNDLE, locale));
      }
    }
    else if (!props.containsKey(hashkey))
    {
      props.put(hashkey, UTF8ResourceBundle.getBundle(BUNDLE));
    }

    String template = props.get(hashkey).getString(key);

    return parseMessage(template, params);
  }

  /**
   * Gets the localized message, which indicates an unspecified error.
   * 
   * @param locale
   *          The desired locale
   * 
   * @return The localized error message
   */
  public static String unspecifiedException(Locale locale)
  {
    return getMessage(locale, "UnspecifiedException");
  }

  /**
   * Gets the localized message, which indicates an error with a ClientRequest.
   * 
   * @param locale
   *          The desired locale
   * 
   * @return The localized error message
   */
  public static String clientRequestException(Locale locale)
  {
    // This is not a cut and paste error. End users will not understand
    // clientRequest errors.
    return getMessage(locale, "UnspecifiedException");
  }

  /**
   * Gets the localized message, which indicates an error thrown from the server
   * side.
   * 
   * @param locale
   *          The desired locale
   * 
   * @return The localized error message
   */
  public static String serverSideException(Locale locale)
  {
    return getMessage(locale, "UnspecifiedException");
  }

  /**
   * Gets the localized message, which indicates an error thrown from the RMI
   * services.
   */
  public static String rmiClientRequestException(Locale locale)
  {
    return getMessage(locale, "UnspecifiedException");
  }

  /**
   * Gets the localized message, which indicates an error thrown from web
   * services.
   */
  public static String webServiceClientRequestException(Locale locale)
  {
    return getMessage(locale, "UnspecifiedException");
  }

  /**
   * Gets the localized message for an error thrown when an admin tries to
   * request a non-unique type name to view.
   * 
   * @param locale
   * @return
   */
  public static String shortHandTypeDuplicateException(Locale locale, String typeName)
  {
    return getMessage(locale, "shortHandTypeDuplicateException", typeName);
  }

  /**
   * Gets the localized message for an error thrown when an admin tries to
   * request an invalid type to view.
   * 
   * @param locale
   * @return
   */
  public static String shortHandTypeInvalidException(Locale locale, String typeName)
  {
    return getMessage(locale, "shortHandTypeInvalidException", typeName);
  }

  public static String unknownServletException(Locale locale, String controllerName)
  {
    return getMessage(locale, "UnknownServletException", controllerName);
  }

  public static String illegalURIMethodException(Locale locale, String uri)
  {
    return getMessage(locale, "IllegalURIMethodException", uri);
  }

  public static String nullClientRequestException(Locale locale)
  {
    return getMessage(locale, "NullClientRequestException");
  }

  public static String undefinedControllerAction(Locale locale, String action)
  {
    return getMessage(locale, "UndefinedControllerAction");
  }
}
