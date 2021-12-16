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

import com.runwaysdk.localization.LocalizationFacade;

public class GISCommonExceptionMessageLocalizer
{
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
    return LocalizationFacade.getMessage(locale, "AttributePointParseException", "The value [{0}] on attribute [{1}] does not represent a valid point.", invalidValue, attributeDisplayLabel);
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
    return LocalizationFacade.getMessage(locale, "AttributeLineStringParseException", "The value [{0}] on attribute [{1}] does not represent a valid line.", invalidValue, attributeDisplayLabel);
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
    return LocalizationFacade.getMessage(locale, "AttributePolygonParseException", "The value [{0}] on attribute [{1}] does not represent a valid polygon.", invalidValue, attributeDisplayLabel);
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
    return LocalizationFacade.getMessage(locale, "AttributeMultiPointParseException", "The value [{0}] on attribute [{1}] does not represent a valid multi-point.", invalidValue, attributeDisplayLabel);
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
    return LocalizationFacade.getMessage(locale, "AttributeMultiLineStringParseException", "The value [{0}] on attribute [{1}] does not represent a valid multi-line.", invalidValue, attributeDisplayLabel);
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
    return LocalizationFacade.getMessage(locale, "AttributeMultiPolygonParseException", "The value [{0}] on attribute [{1}] does not represent a valid multi-polygon.", invalidValue, attributeDisplayLabel);
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
    return LocalizationFacade.getMessage(locale, "InvalidDimensionException", "Dimension value [{0}] on attribute [{1}] is invalid.  Please specify a value between 0 and 4.", invalidDimension, attributeDisplayLabel);
  }
}
