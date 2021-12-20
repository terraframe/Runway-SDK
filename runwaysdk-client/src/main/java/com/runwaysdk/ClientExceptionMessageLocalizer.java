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
package com.runwaysdk;

import java.util.Locale;

import com.runwaysdk.localization.LocalizationFacade;

public class ClientExceptionMessageLocalizer
{
  private static final String UnspecifiedExceptionMsg = "We are unable to complete your request at this time.  Try again later.  If the problem persists, contact your system administrator.";
  
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
    return LocalizationFacade.getMessage(locale, "UnspecifiedException", UnspecifiedExceptionMsg);
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
    return LocalizationFacade.getMessage(locale, "UnspecifiedException", UnspecifiedExceptionMsg);
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
    return LocalizationFacade.getMessage(locale, "UnspecifiedException", UnspecifiedExceptionMsg);
  }

  /**
   * Gets the localized message, which indicates an error thrown from the RMI
   * services.
   */
  public static String rmiClientRequestException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "UnspecifiedException", UnspecifiedExceptionMsg);
  }

  /**
   * Gets the localized message, which indicates an error thrown from web
   * services.
   */
  public static String webServiceClientRequestException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "UnspecifiedException", UnspecifiedExceptionMsg);
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
    return LocalizationFacade.getMessage(locale, "shortHandTypeDuplicateException", "The type name [{0}] defines more than one type. Please qualify the type name with a package.", typeName);
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
    return LocalizationFacade.getMessage(locale, "shortHandTypeInvalidException", "The type name [{0}] does not correspond to a valid type. Please retry the operation with a valid type name (or qualified type).", typeName);
  }

  public static String unknownServletException(Locale locale, String controllerName)
  {
    return LocalizationFacade.getMessage(locale, "UnknownServletException", "An action at the uri [{0}] does not exist.", controllerName);
  }

  public static String illegalURIMethodException(Locale locale, String uri)
  {
    return LocalizationFacade.getMessage(locale, "IllegalURIMethodException", "The uri [{0}] must be accessed in a POST method.", uri);
  }

  public static String nullClientRequestException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "NullClientRequestException", "Please log in.  If you are already logged in, please log off and back in.");
  }

  public static String undefinedControllerAction(Locale locale, String action)
  {
    return LocalizationFacade.getMessage(locale, "UndefinedControllerAction", "The action [{0}] has not been properly configured. Please alert your technical support team.");
  }
}
