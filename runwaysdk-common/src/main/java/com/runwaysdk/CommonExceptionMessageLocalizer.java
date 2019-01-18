/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.io.File;
import java.util.Locale;

public class CommonExceptionMessageLocalizer
{
  /**
   * Gets the localized {@link InvalidSessionException} message, which is thrown
   * when there is an error in the session layer
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String invalidSessionException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "InvalidSessionException", "Your session has expired.  Please log in again.");
  }

  /**
   * Gets the localized {@link RunwayExceptionIF} message.
   *
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String runwayException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "RunwayException", "An unspecified error has occurred.  Please try your operation again.  If the problem continues, alert your technical support staff.");
  }

  /**
   * Message stating that the given file is being written.
   *
   * @param locale
   * @param filePathAndName
   * @return localized message
   */
  public static String writingFileMessage(Locale locale, String filePathAndName)
  {
    return LocalizationFacade.getMessage(locale, "WritingFileMessage", "Writing file: [{0}]", filePathAndName);
  }

  /**
   * Gets the localized FileReadException message, which is thrown when
   * an error in encountered when trying to read from the filesystem.
   *
   * @param locale
   *          The desired locale
   * @param file
   *          File attempting to be read
   * @return The localized error message
   */
  public static String fileReadException(Locale locale, File file)
  {
    if(file != null)
    {
      return LocalizationFacade.getMessage(locale, "FileReadException", "An error occurred while trying to read file [{0}].  Please try your operation again.", file.getPath());
    }
    else
    {
      return LocalizationFacade.getMessage(locale, "FileReadException", "An error occurred while trying to read file [{0}].  Please try your operation again.", "stream");      
    }
  }

  /**
   * Gets the localized FileWriteException message, which is thrown when
   * an error in encountered when trying to write to the filesystem.
   *
   * @param locale
   *          The desired locale
   * @param file
   *          File attempting to be written
   * @return The localized error message
   */
  public static String fileWriteException(Locale locale, File file)
  {
    if(file != null)
    {
      return LocalizationFacade.getMessage(locale, "FileWriteException", "Could not write to file [{0}].  Please close an application that may be accessing the file and try your operation again.", file.getPath());
    }
    else
    {
      return LocalizationFacade.getMessage(locale, "FileWriteException", "Could not write to file [{0}].  Please close an application that may be accessing the file and try your operation again.", "stream");      
    }
  }

  /**
   * Gets the localized message for an error thrown when parsing boolean
   * attributes and parameters from a request.
   *
   * @param name
   *          Name of the attribute or parameter
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String booleanParseException(Locale locale, String name, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeBooleanParseException", "The value [{0}] on attribute [{1}] must be true or false.", value, name);
  }
  
  public static String textParseException(Locale locale, String name, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeTextParseException", "The value [{0}] on attribute [{1}] does not represent a valid text.", value, name);
  }

  /**
   * Gets the localized message for an error thrown when parsing number
   * attributes and parameters from a request.
   *
   * @param displayLabel
   *          Name of the attribute or parameter
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String integerParseException(Locale locale, String displayLabel, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeIntegerParseException", "The value [{0}] on attribute [{1}] does not represent a valid integer.", value, displayLabel);
  }

  /**
   * Gets the localized message for an error thrown when parsing number
   * attributes and parameters from a request.
   *
   * @param name
   *          Name of the attribute or parameter
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String decimalParseException(Locale locale, String name, String value)
  {
    return LocalizationFacade.getMessage(locale, "DecimalParseException", "The value [{0}] on attribute [{1}] does not represent a valid decimal number.", value, name);
  }

  /**
   * Gets the localized message for an error thrown when parsing character
   * attributes or parameters from a request.
   *
   * @param name
   *          Name of the attribute or parameters
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String characterParseException(Locale locale, String name, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeCharacterParseException", "The value [{0}] on attribute [{1}] does not represent a valid letter.", value, name);
  }

  /**
   * Gets the localized message for an error thrown when parsing character
   * attributes or parameters from a request.
   *
   * @param name
   *          Name of the attribute or parameters
   * @param value
   *          Value being parsed
   * @param type
   *          TODO
   *
   * @return
   */
  public static String referenceParseException(Locale locale, String name, String value, String type)
  {
    return LocalizationFacade.getMessage(locale, "AttributeReferenceParseExceptionWithValue", "The value [{0}] on attribute [{1}] does not represent a valid [{2}].", value, name, type);
  }

  /**
   * Gets the localized message for an error thrown when parsing character
   * attributes or parameters from a request.
   *
   * @param name
   *          Name of the attribute or parameters
   * @param type
   *          TODO
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String referenceParseException(Locale locale, String name, String type)
  {
    return LocalizationFacade.getMessage(locale, "AttributeReferenceParseException", "The value on attribute [{0}] does not represent a valid [{1}].", name, type);
  }

  /**
   * Gets the localized message for an error thrown when parsing date attributes
   * from a request.
   *
   * @param attributeName
   *          Name of the attribute
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String dateTimeParseException(Locale locale, String attributeName, String value, String format)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDateTimeParseException", "The value [{0}] on attribute [{1}] does not represent a valid date and time of the following format: [{2}].", value, attributeName, format);
  }

  /**
   * Gets the localized message for an error thrown when parsing date attributes
   * from a request.
   *
   * @param attributeName
   *          Name of the attribute
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String timeParseException(Locale locale, String attributeName, String value, String format)
  {
    return LocalizationFacade.getMessage(locale, "AttributeTimeParseException", "The value [{0}] on attribute [{1}] does not represent a valid time of the following format: [{2}].", value, attributeName, format);
  }

  /**
   * Gets the localized message for an error thrown when parsing date time
   * attributes from a request.
   *
   * @param attributeName
   *          Name of the attribute
   * @param value
   *          Value being parsed
   *
   * @return
   */
  public static String dateParseException(Locale locale, String attributeName, String value, String format)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDateParseException", "The value [{0}] on attribute [{1}] does not represent a valid date of the following format: [{2}].", value, attributeName, format);
  }

  /**
   * Gets the localized ConfigurationException message
   * when the application is not properly configured.
   *
   * @param locale
   *          The desired locale
   */
  public static String configurationException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "ConfigurationException", "You application is not properly configured.  Please alert your technical support team.");
  }


  /**
   * Gets the localized message, which indicates an error with
   * conversions (used with Web Services to translate between documents
   * and objects).
   *
   * @param locale
   *          The desired locale
   *
   * @return The localized error message
   */
  public static String conversionException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "ConversionException", "A data conversion error occurred.  Please alert your technical support team.");
  }


  /**
   * Gets the localized LoaderDecoratorException}message.
   *
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String loaderDecoratorException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "LoaderDecoratorException", "An error occurred with the class loader.  Please alert your technical support staff.");
  }

  /**
   * Gets the localized message for an error thrown when an accessor method is called when the
   * isReadable value returns false.
   *
   * @param locale
   * @param attributeDisplayLabel
   * @param classDisplayLabel
   *
   * @return localized message for an error thrown when an accessor method is called when the
   * isReadable value returns false.
   */
  public static String attributeReadPermissionException(Locale locale, String attributeDisplayLabel, String classDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "AttributeReadPermissionException", "You do not have permission to read attribute [{0}] on [{1}].", attributeDisplayLabel, classDisplayLabel);
  }
}
