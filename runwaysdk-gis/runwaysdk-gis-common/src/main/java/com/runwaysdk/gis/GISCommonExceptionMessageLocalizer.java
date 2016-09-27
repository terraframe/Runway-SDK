/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis;

import java.util.Locale;
import java.util.ResourceBundle;

import com.runwaysdk.ExceptionMessageLocalizer;

public class GISCommonExceptionMessageLocalizer extends ExceptionMessageLocalizer
{
  private static final String BUNDLE = "gisCommonExceptions";

  /**
   * Fetches the parameterized, localized error message template for the given exception.
   * The variable String arguments represent the parameters in the template string. For
   * example, given the template "The {0} in the {1}." and arguments "cat" and "hat", the
   * final String will be "The cat in the hat."
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
    String hashkey = BUNDLE + "-" + locale.toString();

    if (!props.containsKey(hashkey))
      props.put(hashkey, ResourceBundle.getBundle(BUNDLE, locale));

    String template = props.get(hashkey).getString(key);

    return parseMessage(template, params);
  }

  /**
   * Gets the localized AttributePointParseException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String attributePointParseException(Locale locale, String attributeDisplayLabel, String invalidValue)
  {
    return getMessage(locale, "AttributePointParseException", invalidValue, attributeDisplayLabel);
  }

  /**
   * Gets the localized AttributeLineStringParseException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String attributeLineStringParseException(Locale locale, String attributeDisplayLabel, String invalidValue)
  {
    return getMessage(locale, "AttributeLineStringParseException", invalidValue, attributeDisplayLabel);
  }

  /**
   * Gets the localized AttributePolygonParseException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String attributePolygonParseException(Locale locale, String attributeDisplayLabel, String invalidValue)
  {
    return getMessage(locale, "AttributePolygonParseException", invalidValue, attributeDisplayLabel);
  }

  /**
   * Gets the localized AttributeMultiPointParseException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String attributeMultiPointParseException(Locale locale, String attributeDisplayLabel, String invalidValue)
  {
    return getMessage(locale, "AttributeMultiPointParseException", invalidValue, attributeDisplayLabel);
  }

  /**
   * Gets the localized AttributeMultiLineStringParseException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String attributeMultiLineStringParseException(Locale locale, String attributeDisplayLabel, String invalidValue)
  {
    return getMessage(locale, "AttributeMultiLineStringParseException", invalidValue, attributeDisplayLabel);
  }


  /**
   * Gets the localized AttributeMultiPolygonParseException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String attributeMultiPolygonParseException(Locale locale, String attributeDisplayLabel, String invalidValue)
  {
    return getMessage(locale, "AttributeMultiPolygonParseException", invalidValue, attributeDisplayLabel);
  }

  /**
   * Gets the localized InvalidDimensionException message, which indicates an
   * invalid Point.
   *
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   * @param invalidValue
   * @return The localized error message
   */
  public static String invalidDimensionException(Locale locale, String invalidDimension,  String attributeDisplayLabel)
  {
    return getMessage(locale, "InvalidDimensionException", invalidDimension, attributeDisplayLabel);
  }
}
