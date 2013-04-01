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

import java.io.File;
import java.util.Locale;

import com.terraframe.utf8.UTF8ResourceBundle;

public class CommonExceptionMessageLocalizer extends ExceptionMessageLocalizer
{
  private static final String BUNDLE = "commonExceptions";

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
   * Gets the localized {@link RunwayExceptionIF} message.
   *
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String runwayException(Locale locale)
  {
    return getMessage(locale, "RunwayException");
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
    return getMessage(locale, "WritingFileMessage", filePathAndName);
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
    return getMessage(locale, "FileReadException", file.getPath());
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
    return getMessage(locale, "FileWriteException", file.getPath());
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
    return getMessage(locale, "AttributeBooleanParseException", value, name);
  }
  
  public static String textParseException(Locale locale, String name, String value)
  {
    return getMessage(locale, "AttributeTextParseException", value, name);
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
    return getMessage(locale, "AttributeIntegerParseException", value, displayLabel);
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
    return getMessage(locale, "DecimalParseException", value, name);
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
    return getMessage(locale, "AttributeCharacterParseException", value, name);
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
    return getMessage(locale, "AttributeReferenceParseExceptionWithValue", value, name, type);
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
    return getMessage(locale, "AttributeReferenceParseException", name, type);
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
    return getMessage(locale, "AttributeDateTimeParseException", value, attributeName, format);
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
    return getMessage(locale, "AttributeTimeParseException", value, attributeName, format);
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
    return getMessage(locale, "AttributeDateParseException", value, attributeName, format);
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
    return getMessage(locale, "ConfigurationException");
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
    return getMessage(locale, "ConversionException");
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
    return getMessage(locale, "LoaderDecoratorException");
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
    return getMessage(locale, "AttributeReadPermissionException", attributeDisplayLabel, classDisplayLabel);
  }
}
