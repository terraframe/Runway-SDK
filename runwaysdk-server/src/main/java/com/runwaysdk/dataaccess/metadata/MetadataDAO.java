/**
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
 */
/**
 * Created on Aug 12, 2005
 * 
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;

/**
 * @author nathan
 * 
 */
public abstract class MetadataDAO extends BusinessDAO implements MetadataDAOIF, SpecializedDAOImplementationIF
{
  /**
   *
   */
  private static final long serialVersionUID = 973510008626606314L;

  /**
   * The default constructor, does not set any attributes
   */
  public MetadataDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given Map of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   * 
   * @param attributeMap
   * @param type
   */
  protected MetadataDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * Returns true if the metadata can be removed, false otherwise.
   * 
   * @return true if the metadata can be removed, false otherwise.
   */
  public boolean isRemovable()
  {
    AttributeBooleanIF attributeBoolean = (AttributeBooleanIF) this.getAttributeIF(MetadataInfo.REMOVE);
    return AttributeBoolean.getBooleanValue(attributeBoolean);
  }

  /**
   * Deletes this class. Consequently all instances of this class are deleted as
   * well. Deletes all parent and child relationships with the every instance of
   * this class. The BusinessDAOs and relationships are removed from the
   * database and from the cache. <b>All subclasses and instances of subclasses
   * are likewise deleted</b>
   * 
   * <br/>
   * <b>Postcondition:</b> Table that defines this className (and tables that
   * define all classes) are dropped from the database. For all sub className
   * tables.
   * 
   * <br/>
   * <b>Postcondition:</b> child relationships are removed
   * (RelationshipFactory.getChildren(this.getId(), "")).length == 0 <br/>
   * <b>Postcondition:</b> parent relationships are removed
   * (RelationshipFactory.getParents(this.getId(), "")).length == 0
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  @Override
  public void delete(boolean businessContext)
  {
    if (!this.isRemovable() && !this.isImport())
    {
      String error = "Metadata [" + this.getId() + "] is not allowed to be deleted.";
      throw new MetadataCannotBeDeletedException(error, this);
    }

    super.delete(businessContext);
  }

  /**
   * Returns a description of this metadata;
   * 
   * @param SupportedLocale
   *          locale
   * 
   * @return a description of this metadata;
   */
  public String getDescription(Locale locale)
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MetadataInfo.DESCRIPTION) ).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDescriptions()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MetadataInfo.DESCRIPTION) ).getLocalValues();
  }

  /**
   * Throws {@link InvalidIdentifierException} exception if the given name is
   * not a valid Java classifier name or attribute.
   * 
   * @param validateName
   *          name to validate
   * @throws {@link InvalidIdentifierException} exception if the given name is
   *         not a valid Java classifier name or attribute.
   */
  public static void validateName(String validateName)
  {
    if (!isJavaIdentifier(validateName))
    {
      String error = "The name [" + validateName + "] is not a valid identifier.";
      throw new InvalidIdentifierException(error, validateName);
    }
  }

  /**
   * Returns true if the given string represents a valid java identifier, false
   * otherwise.
   * 
   * @param identifier
   * @return true if the given string represents a valid java identifier, false
   *         otherwise.
   */
  public static boolean isJavaIdentifier(String identifier)
  {
    if (identifier.length() == 0 || !Character.isJavaIdentifierStart(identifier.charAt(0)))
    {
      return false;
    }

    for (int i = 1; i < identifier.length(); i++)
    {
      if (!Character.isJavaIdentifierPart(identifier.charAt(i)))
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Converts the given name from camel case to underscore case and converts the
   * entire result string to lower case.
   * 
   * @param name
   *          name to convert
   * @return given name from camel case to underscore case.
   */
  public static String convertCamelCaseToUnderscore(String name)
  {
    if (name == null || name.length() == 0)
    {
      return name;
    }
    else if (name.length() == 1)
    {
      return name.toLowerCase();
    }

    // Length > 1
    StringBuffer result = new StringBuffer();

    char previousChar = name.charAt(0);
    result.append(previousChar);

    for (int i = 1; i < name.length(); i++)
    {
      char currentChar = name.charAt(i);

      if (currentChar == '_')
      {
        result.append(currentChar);
      }
      else if (!Character.isLetter(previousChar) && previousChar != '_' && Character.isLetter(currentChar))
      {
        result.append('_');
        result.append(currentChar);
      }
      else if (i == 1 && Character.isLetter(previousChar) && Character.isLetter(currentChar) && Character.isUpperCase(previousChar) && Character.isLowerCase(currentChar))
      {
        result.append(currentChar);
      }
      else if (i < 2 && Character.isLetter(previousChar) && Character.isLetter(currentChar) && !casesMatch(previousChar, currentChar))
      {
        /*
         * Insert space before start of word if camel case
         */
        result.append('_');
        result.append(currentChar);
      }
      else if (i >= 2 && Character.isLetter(previousChar) && Character.isLetter(currentChar) && !casesMatch(previousChar, currentChar) && ( casesMatch(name.charAt(i - 2), previousChar) || ( Character.isUpperCase(name.charAt(i - 2)) && Character.isLetter(name.charAt(i - 2)) && Character.isLowerCase(previousChar) && Character.isUpperCase(currentChar) ) ))
      {
        result.append('_');
        result.append(currentChar);
      }
      else
      {
        result.append(currentChar);
      }

      previousChar = currentChar;
    }

    return result.toString().trim().toLowerCase();
  }

  /**
   * Returns true if the cases of the given characters match, false otherwise.
   * 
   * @param char1
   * @param char2
   * 
   * @return true if the cases match, false otherwise.
   */
  private static boolean casesMatch(char char1, char char2)
  {
    if ( ( Character.isUpperCase(char1) && Character.isUpperCase(char2) ) || ( Character.isLowerCase(char1) && Character.isLowerCase(char2) ) || 
            (!Character.isUpperCase(char1) && !Character.isLowerCase(char1) && !Character.isUpperCase(char2) && !Character.isLowerCase(char2) ))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Converts the given name from underscore case and converts to camel case.
   * 
   * @param name
   *          name to convert
   * @return given name from underscore case and converts to camel case.
   */
  public static String convertUnderscoreToCamelCase(String name)
  {
    if (name == null || name.length() == 0)
    {
      return name;
    }

    StringBuffer result = new StringBuffer();

    boolean upperCaseNextChar = false;
    for (int i = 0; i < name.length(); i++)
    {
      char currentChar = name.charAt(i);

      if (currentChar != '_')
      {
        if (upperCaseNextChar)
        {
          currentChar = Character.toUpperCase(currentChar);
          upperCaseNextChar = false;
        }

        result.append(currentChar);
      }
      else
      {
        upperCaseNextChar = true;
      }
    }

    return result.toString().trim().toLowerCase();
  }

  public String getPermissionKey()
  {
    return this.getId();
  }
}
