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
package com.runwaysdk.query;

import com.runwaysdk.query.EXTRACT.Unit;


public class F
{
  /**
   *
   * @param attributePrimitive Attribute that is a parameter to the function.
   */
  public static AVG AVG(EntityQuery entityQuery)
  {
    return new AVG(entityQuery);
  }

  /**
   *
   * @param attributePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static AVG AVG(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new AVG(entityQuery, userDefinedAlias, null);
  }

  /**
  *
  * @param attributePrimitive Attribute that is a parameter to the function.
  * @param userDefinedAlias
  * @param userDefinedDisplayLabel
  */
 public static AVG AVG(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
 {
   return new AVG(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
 }

  /**
   *
   * @param selectable
   */
  public static AVG AVG(Selectable selectable)
  {
    return new AVG(selectable);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static AVG AVG(Selectable selectable, String userDefinedAlias)
  {
    return new AVG(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static AVG AVG(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new AVG(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  
  /**
   * Coalesce function
   *
   * @param selectable  need at least one selectable
   * @param optionalSelectableArray optional additional selectables
   */
  public static Coalesce COALESCE(SelectableSingle selectable, SelectableSingle...optionalSelectableArray)
  {
    return new Coalesce(selectable, optionalSelectableArray);
  }
  
  /**
   * Coalesce function
   *
   * @param selectable  need at least one selectable
   * @param optionalSelectableArray optional additional selectables
   */
  public static Coalesce COALESCE(SelectableSingle selectable, String other, SelectableSingle...optionalSelectableArray)
  {
    return new Coalesce(selectable, other, optionalSelectableArray);
  }

  /**
   * Coalesce function
   * 
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @param selectable  need at least one selectable
   * @param optionalSelectableArray optional additional selectables
   */
  public static Coalesce COALESCE(String userDefinedAlias, String userDefinedDisplayLabel, SelectableSingle selectable, SelectableSingle...optionalSelectableArray)
  {
    return new Coalesce(userDefinedAlias, userDefinedDisplayLabel, selectable, optionalSelectableArray);
  }
  
  /**
   * String concatenate function.  Concatentates the result of the function with the given attribute.
   *
   * @param selectable1
   * @param selectable2
   */
  public static CONCAT CONCAT(Selectable selectable1, Selectable selectable2)
  {
    return new CONCAT(selectable1, selectable2);
  }

  /**
   * String concatenate function.  Concatentates the two attribute.
   *
   * @param selectable1 attribute to concatenate.
   * @param selectable2 attribute to concatenate.
   * @param userDefinedAlias
   */
  public static CONCAT CONCAT(Selectable selectable1, Selectable selectable2, String userDefinedAlias)
  {
    return new CONCAT(selectable1, selectable2, userDefinedAlias, null);
  }

  /**
   * String concatenate function.  Concatentates the two attribute.
   *
   * @param selectable1 attribute to concatenate.
   * @param selectable2 attribute to concatenate.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static CONCAT CONCAT(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new CONCAT(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param function tring to append
   * @param stringToAppend tring to append
   */
  public static CONCAT CONCAT(Selectable selectable, String stringToAppend)
  {
    return new CONCAT(selectable, stringToAppend);
  }

  /**
   * String concatenate selectable.  Concatentates the result of the function with the given string.
   *
   * @param selectable tring to append
   * @param stringToAppend tring to append
   * @param userDefinedAlias
   */
  public static CONCAT CONCAT(Selectable selectable, String stringToAppend, String userDefinedAlias)
  {
    return new CONCAT(selectable, stringToAppend, userDefinedAlias, null);
  }

  /**
   * String concatenate selectable.  Concatentates the result of the function with the given string.
   *
   * @param selectable tring to append
   * @param stringToAppend tring to append
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static CONCAT CONCAT(Selectable selectable, String stringToAppend, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new CONCAT(selectable, stringToAppend, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param stringToAppend tring to append
   * @param selectable tring to append
   */
  public static CONCAT CONCAT(String stringToAppend, Selectable selectable)
  {
    return new CONCAT(stringToAppend, selectable);
  }

  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param selectable tring to append
   * @param stringToAppend tring to append
   * @param userDefinedAlias
   */
  public static CONCAT CONCAT(String stringToAppend, Selectable selectable, String userDefinedAlias)
  {
    return new CONCAT(stringToAppend, selectable, userDefinedAlias, null);
  }


  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param selectable tring to append
   * @param stringToAppend tring to append
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static CONCAT CONCAT(String stringToAppend, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new CONCAT(stringToAppend, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   */
  public static COUNT COUNT(EntityQuery entityQuery)
  {
    return new COUNT(entityQuery);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static COUNT COUNT(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new COUNT(entityQuery, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static COUNT COUNT(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new COUNT(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  public static COUNT COUNT(Selectable selectable)
  {
    return new COUNT(selectable);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static COUNT COUNT(Selectable selectable, String userDefinedAlias)
  {
    return new COUNT(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static COUNT COUNT(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new COUNT(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * String LENGTH function.  Returns the length of the given column in the database.  For binary columns,
   * it returns the number of bits.  Otherwise, it returns the length of the number of characters in the column.
   *
   * @param function that returns a character.
   */
  public static LENGTH LENGTH(Selectable selectable)
  {
    return new LENGTH(selectable);
  }

  /**
   * String LENGTH function.  Returns the length of the given column in the database.  For binary columns,
   * it returns the number of bits.  Otherwise, it returns the length of the number of characters in the column.
   *
   * @param function that returns a character.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static LENGTH LENGTH(Selectable selectable, String userDefinedAlias)
  {
    return new LENGTH(selectable, userDefinedAlias, null);
  }

  /**
   * String LENGTH function.  Returns the length of the given column in the database.  For binary columns,
   * it returns the number of bits.  Otherwise, it returns the length of the number of characters in the column.
   *
   * @param function that returns a character.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static LENGTH LENGTH(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new LENGTH(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Lowercase function.  Converts the values of the character column to lowercase.
   *
   * @param selectable selectable result to trim.
   */
  public static LOWER LOWER(Selectable selectable)
  {
    return new LOWER(selectable);
  }

  /**
   * Lowercase selectable.  Converts the values of the character column to lowercase.
   *
   * @param selectable selectable result to trim.
   * @param userDefinedAlias
   */
  public static LOWER LOWER(Selectable selectable, String userDefinedAlias)
  {
    return new LOWER(selectable, userDefinedAlias, null);
  }

  /**
   * Lowercase selectable.  Converts the values of the character column to lowercase.
   *
   * @param selectable selectable result to trim.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static LOWER LOWER(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new LOWER(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }


  /**
   * Left Trim selectable.  Whitespace to the left of function result are trimed.
   *
   * @param selectable function result to trim.
   */
  public static LTRIM LTRIM(Selectable selectable)
  {
    return new LTRIM(selectable);
  }

  /**
   * Left Trim function.  Whitespace to the left of function result are trimed.
   *
   * @param function function result to trim.
   * @param userDefinedAlias
   */
  public static LTRIM LTRIM(Selectable selectable, String userDefinedAlias)
  {
    return new LTRIM(selectable, userDefinedAlias, null);
  }

  /**
   * Left Trim function.  Whitespace to the left of function result are trimed.
   *
   * @param function function result to trim.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static LTRIM LTRIM(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new LTRIM(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   */
  public static MAX MAX(EntityQuery entityQuery)
  {
    return new MAX(entityQuery);
  }

  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static MAX MAX(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new MAX(entityQuery, userDefinedAlias, null);
  }

  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MAX MAX(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MAX(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  public static MAX MAX(Selectable selectable)
  {
    return new MAX(selectable);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static MAX MAX(Selectable selectable, String userDefinedAlias)
  {
    return new MAX(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static MAX MAX(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MAX(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   */
  public static MIN MIN(EntityQuery entityQuery)
  {
    return new MIN(entityQuery);
  }

  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static MIN MIN(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new MIN(entityQuery, userDefinedAlias, null);
  }

  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MIN MIN(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MIN(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   *
   * @param selectable
   */
  public static MIN MIN(Selectable selectable)
  {
    return new MIN(selectable);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static MIN MIN(Selectable selectable, String userDefinedAlias)
  {
    return new MIN(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MIN MIN(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MIN(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * String position function.  Returns the first index that the given String appears in the character attribute.
   *
   * @param stringToSearch string to look for.
   * @param selectable that returns a character.
   */
  public static POSITION POSITION(String stringToSearch, Selectable selectable)
  {
    return new POSITION(stringToSearch, selectable);
  }

  /**
   * String position function.  Returns the first index that the given String appears in the character attribute.
   *
   * @param stringToSearch string to look for.
   * @param selectable that returns a character.
   * @param userDefinedAlias
   */
  public static POSITION POSITION(String stringToSearch, Selectable selectable, String userDefinedAlias)
  {
    return new POSITION(stringToSearch, selectable, userDefinedAlias, null);
  }


  /**
   * String position function.  Returns the first index that the given String appears in the character attribute.
   *
   * @param stringToSearch string to look for.
   * @param selectable that returns a character.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static POSITION POSITION(String stringToSearch, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new POSITION(stringToSearch, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Right Trim selectable.  Whitespace to the left of function result are trimed.
   *
   * @param selectable function result to trim.
   */
  public static RTRIM RTRIM(Selectable selectable)
  {
    return new RTRIM(selectable);
  }

  /**
   * Right Trim selectable.  Whitespace to the left of function result are trimed.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   */
  public static RTRIM RTRIM(Selectable selectable, String userDefinedAlias)
  {
    return new RTRIM(selectable, userDefinedAlias, null);
  }

  /**
   * Right Trim selectable.  Whitespace to the left of function result are trimed.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static RTRIM RTRIM(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new RTRIM(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * 
   * @param _delimiter
   *          Attribute that is a parameter to the function.
   */
  public static STRING_AGG STRING_AGG(EntityQuery _entityQuery, String _delimiter)
  {
    return new STRING_AGG(_entityQuery, _delimiter);
  }

  /**
   * 
   * @param attributePrimitive
   *          Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static STRING_AGG STRING_AGG(EntityQuery _entityQuery, String _delimiter, String _userDefinedAlias)
  {
    return new STRING_AGG(_entityQuery, _delimiter, _userDefinedAlias, null);
  }

  /**
   * 
   * @param attributePrimitive
   *          Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static STRING_AGG STRING_AGG(EntityQuery _entityQuery, String _delimiter, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    return new STRING_AGG(_entityQuery, _delimiter, _userDefinedAlias, _userDefinedDisplayLabel);
  }

  /**
   * 
   * @param selectable
   */
  public static STRING_AGG STRING_AGG(Selectable _selectable, String _delimiter)
  {
    return new STRING_AGG(_selectable, _delimiter);
  }

  /**
   * 
   * @param selectable
   * @param userDefinedAlias
   */
  public static STRING_AGG STRING_AGG(Selectable _selectable, String _delimiter, String _userDefinedAlias)
  {
    return new STRING_AGG(_selectable, _delimiter, _userDefinedAlias, null);
  }

  /**
   * 
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static STRING_AGG STRING_AGG(Selectable _selectable, String _delimiter, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    return new STRING_AGG(_selectable, _delimiter, _userDefinedAlias, _userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   */
  public static STDDEV STDDEV(EntityQuery entityQuery)
  {
    return new STDDEV(entityQuery);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static STDDEV STDDEV(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new STDDEV(entityQuery, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static STDDEV STDDEV(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new STDDEV(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   *
   * @param selectable
   */
  public static STDDEV STDDEV(Selectable selectable)
  {
    return new STDDEV(selectable);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static STDDEV STDDEV(Selectable selectable, String userDefinedAlias)
  {
    return new STDDEV(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static STDDEV STDDEV(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new STDDEV(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Substring selectable
   *
   * @param selectable Selectable that returns a String.
   * @param startIndex Starting string index.  First character is at index 1 (like in SQL).
   * @param numOfCharacters Number of characters starting with the startIndex.
   */
  public static SUBSTR SUBSTR(Selectable selectable,  int startIndex, int numOfCharacters)
  {
    return new SUBSTR(selectable, startIndex, numOfCharacters);
  }

  /**
   * Substring function
   *
   * @param selectable Selectable that returns a String.
   * @param startIndex Starting string index.  First character is at index 1 (like in SQL).
   * @param numOfCharacters Number of characters starting with the startIndex.
   * @param userDefinedAlias
   */
  public static SUBSTR SUBSTR(Selectable selectable,  int startIndex, int numOfCharacters, String userDefinedAlias)
  {
    return new SUBSTR(selectable,  startIndex, numOfCharacters, userDefinedAlias, null);
  }


  /**
   * Substring function
   *
   * @param selectable Selectable that returns a String.
   * @param startIndex Starting string index.  First character is at index 1 (like in SQL).
   * @param numOfCharacters Number of characters starting with the startIndex.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUBSTR SUBSTR(Selectable selectable,  int startIndex, int numOfCharacters, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUBSTR(selectable,  startIndex, numOfCharacters, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   */
  public static SUM SUM(EntityQuery entityQuery)
  {
    return new SUM(entityQuery);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static SUM SUM(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new SUM(entityQuery, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUM SUM(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUM(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  public static SUM SUM(Selectable selectable)
  {
    return new SUM(selectable);
  }

  /**
   *
   * @param function
   * @param userDefinedAlias
   */
  public static SUM SUM(Selectable selectable, String userDefinedAlias)
  {
    return new SUM(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param function
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUM SUM(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUM(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * Uppercase selectable.  Converts the values of the character column to uppercase.
   *
   * @param selectable function result to trim.
   */
  public static UPPER UPPER(Selectable selectable)
  {
    return new UPPER(selectable);
  }

  /**
   * Uppercase selectable.  Converts the values of the character column to uppercase.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   */
  public static UPPER UPPER(Selectable selectable, String userDefinedAlias)
  {
    return new UPPER(selectable, userDefinedAlias, null);
  }

  /**
   * Uppercase selectable.  Converts the values of the character column to uppercase.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static UPPER UPPER(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new UPPER(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   */
  public static VARIANCE VARIANCE(EntityQuery entityQuery)
  {
    return new VARIANCE(entityQuery);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   */
  public static VARIANCE VARIANCE(EntityQuery entityQuery, String userDefinedAlias)
  {
    return new VARIANCE(entityQuery, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static VARIANCE VARIANCE(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new VARIANCE(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  public static VARIANCE VARIANCE(Selectable selectable)
  {
    return new VARIANCE(selectable);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public static VARIANCE VARIANCE(Selectable selectable, String userDefinedAlias)
  {
    return new VARIANCE(selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static VARIANCE VARIANCE(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new VARIANCE(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Trim selectable.  Whitespace to the left and to the right of function result are trimed.
   *
   * @param selectable function result to trim.
   */
  public static TRIM TRIM(Selectable selectable)
  {
    return new TRIM(selectable);
  }

  /**
   * Trim selectable.  Whitespace to the left and to the right of function result are trimed.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   */
  public static TRIM TRIM(Selectable selectable, String userDefinedAlias)
  {
    return new TRIM(selectable, userDefinedAlias, null);
  }


  /**
   * Trim selectable.  Whitespace to the left and to the right of function result are trimed.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static TRIM TRIM(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new TRIM(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  public static DIV DIV(Selectable selectable1, Selectable selectable2)
  {
    return new DIV(selectable1, selectable2);
  }

  /**
   * DIV function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   */
  public static DIV DIV(Selectable selectable1, Selectable selectable2, String userDefinedAlias)
  {
    return new DIV(selectable1, selectable2, userDefinedAlias, null);
  }

  /**
   * DIV function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(int intValue, Selectable selectable)
  {
    return new DIV(intValue, selectable);
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(int intValue, Selectable selectable, String userDefinedAlias)
  {
    return new DIV(intValue, selectable, userDefinedAlias, null);
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   */
  public static DIV DIV(Selectable selectable, int intValue)
  {
    return new DIV(selectable, intValue);
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   */
  public static DIV DIV(Selectable selectable, int intValue, String userDefinedAlias)
  {
    return new DIV(selectable, intValue, userDefinedAlias, null);
  }


  /**
   * DIV function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(long longValue, Selectable selectable)
  {
    return new DIV(longValue, selectable);
  }

  /**
   * DIV function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(long longValue, Selectable selectable, String userDefinedAlias)
  {
    return new DIV(longValue, selectable, userDefinedAlias, null);
  }


  /**
   * DIV function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param longValue
   * @param selectable
   */
  public static DIV DIV(Selectable selectable, long longValue)
  {
    return new DIV(selectable, longValue);
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   */
  public static DIV DIV(Selectable selectable, long longValue, String userDefinedAlias)
  {
    return new DIV(selectable, longValue, userDefinedAlias, null);
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(float floatValue, Selectable selectable)
  {
    return new DIV(floatValue, selectable);
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(float floatValue, Selectable selectable, String userDefinedAlias)
  {
    return new DIV(floatValue, selectable, userDefinedAlias, null);
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   */
  public static DIV DIV(Selectable selectable, float floatValue)
  {
    return new DIV(selectable, floatValue);
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   */
  public static DIV DIV(Selectable selectable, float floatValue, String userDefinedAlias)
  {
    return new DIV(selectable, floatValue, userDefinedAlias, null);
  }


  /**
   * DIV function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(double doubleValue, Selectable selectable)
  {
    return new DIV(doubleValue, selectable);
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static DIV DIV(double doubleValue, Selectable selectable, String userDefinedAlias)
  {
    return new DIV(doubleValue, selectable, userDefinedAlias, null);
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   */
  public static DIV DIV(Selectable selectable, double doubleValue)
  {
    return new DIV(selectable, doubleValue);
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   */
  public static DIV DIV(Selectable selectable, double doubleValue, String userDefinedAlias)
  {
    return new DIV(selectable, doubleValue, userDefinedAlias, null);
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static DIV DIV(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new DIV(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  public static MULTI MULTI(Selectable selectable1, Selectable selectable2)
  {
    return new MULTI(selectable1, selectable2);
  }

  /**
   * MULTI function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   */
  public static MULTI MULTI(Selectable selectable1, Selectable selectable2, String userDefinedAlias)
  {
    return new MULTI(selectable1, selectable2, userDefinedAlias, null);
  }

  /**
   * MULTI function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(int intValue, Selectable selectable)
  {
    return new MULTI(intValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(int intValue, Selectable selectable, String userDefinedAlias)
  {
    return new MULTI(intValue, selectable, userDefinedAlias, null);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   */
  public static MULTI MULTI(Selectable selectable, int intValue)
  {
    return new MULTI(selectable, intValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   */
  public static MULTI MULTI(Selectable selectable, int intValue, String userDefinedAlias)
  {
    return new MULTI(selectable, intValue, userDefinedAlias, null);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(long longValue, Selectable selectable)
  {
    return new MULTI(longValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(long longValue, Selectable selectable, String userDefinedAlias)
  {
    return new MULTI(longValue, selectable, userDefinedAlias, null);
  }

  /**
   * MULTI function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param longValue
   * @param selectable
   */
  public static MULTI MULTI(Selectable selectable, long longValue)
  {
    return new MULTI(selectable, longValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   */
  public static MULTI MULTI(Selectable selectable, long longValue, String userDefinedAlias)
  {
    return new MULTI(selectable, longValue, userDefinedAlias, null);
  }


  /**
   * MULTI function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(float floatValue, Selectable selectable)
  {
    return new MULTI(floatValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(float floatValue, Selectable selectable, String userDefinedAlias)
  {
    return new MULTI(floatValue, selectable, userDefinedAlias, null);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   */
  public static MULTI MULTI(Selectable selectable, float floatValue)
  {
    return new MULTI(selectable, floatValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   */
  public static MULTI MULTI(Selectable selectable, float floatValue, String userDefinedAlias)
  {
    return new MULTI(selectable, floatValue, userDefinedAlias, null);
  }


  /**
   * MULTI function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(double doubleValue, Selectable selectable)
  {
    return new MULTI(doubleValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MULTI MULTI(double doubleValue, Selectable selectable, String userDefinedAlias)
  {
    return new MULTI(doubleValue, selectable, userDefinedAlias, null);
  }


  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   */
  public static MULTI MULTI(Selectable selectable, double doubleValue)
  {
    return new MULTI(selectable, doubleValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   */
  public static MULTI MULTI(Selectable selectable, double doubleValue, String userDefinedAlias)
  {
    return new MULTI(selectable, doubleValue, userDefinedAlias, null);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MULTI MULTI(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MULTI(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  public static ADD ADD(Selectable selectable1, Selectable selectable2)
  {
    return new ADD(selectable1, selectable2);
  }

  /**
   * ADD function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   */
  public static ADD ADD(Selectable selectable1, Selectable selectable2, String userDefinedAlias)
  {
    return new ADD(selectable1, selectable2, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(int intValue, Selectable selectable)
  {
    return new ADD(intValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(int intValue, Selectable selectable, String userDefinedAlias)
  {
    return new ADD(intValue, selectable, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   */
  public static ADD ADD(Selectable selectable, int intValue)
  {
    return new ADD(selectable, intValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   */
  public static ADD ADD(Selectable selectable, int intValue, String userDefinedAlias)
  {
    return new ADD(selectable, intValue, userDefinedAlias, null);
  }


  /**
   * ADD function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(long longValue, Selectable selectable)
  {
    return new ADD(longValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(long longValue, Selectable selectable, String userDefinedAlias)
  {
    return new ADD(longValue, selectable, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * ADD function.
   *
   * @param longValue
   * @param selectable
   */
  public static ADD ADD(Selectable selectable, long longValue)
  {
    return new ADD(selectable, longValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   */
  public static ADD ADD(Selectable selectable, long longValue, String userDefinedAlias)
  {
    return new ADD(selectable, longValue, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(float floatValue, Selectable selectable)
  {
    return new ADD(floatValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(float floatValue, Selectable selectable, String userDefinedAlias)
  {
    return new ADD(floatValue, selectable, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   */
  public static ADD ADD(Selectable selectable, float floatValue)
  {
    return new ADD(selectable, floatValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   */
  public static ADD ADD(Selectable selectable, float floatValue, String userDefinedAlias)
  {
    return new ADD(selectable, floatValue, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(double doubleValue, Selectable selectable)
  {
    return new ADD(doubleValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static ADD ADD(double doubleValue, Selectable selectable, String userDefinedAlias)
  {
    return new ADD(doubleValue, selectable, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   */
  public static ADD ADD(Selectable selectable, double doubleValue)
  {
    return new ADD(selectable, doubleValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   */
  public static ADD ADD(Selectable selectable, double doubleValue, String userDefinedAlias)
  {
    return new ADD(selectable, doubleValue, userDefinedAlias, null);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static ADD ADD(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new ADD(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }
  /**
   * SUB function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  public static SUB SUB(Selectable selectable1, Selectable selectable2)
  {
    return new SUB(selectable1, selectable2);
  }

  /**
   * SUB function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   */
  public static SUB SUB(Selectable selectable1, Selectable selectable2, String userDefinedAlias)
  {
    return new SUB(selectable1, selectable2, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(int intValue, Selectable selectable)
  {
    return new SUB(intValue, selectable);
  }

  /**
   * SUB function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(int intValue, Selectable selectable, String userDefinedAlias)
  {
    return new SUB(intValue, selectable, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param intValue
   * @param selectable
   */
  public static SUB SUB(Selectable selectable, int intValue)
  {
    return new SUB(selectable, intValue);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   */
  public static SUB SUB(Selectable selectable, int intValue, String userDefinedAlias)
  {
    return new SUB(selectable, intValue, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(long longValue, Selectable selectable)
  {
    return new SUB(longValue, selectable);
  }

  /**
   * SUB function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(long longValue, Selectable selectable, String userDefinedAlias)
  {
    return new SUB(longValue, selectable, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param longValue
   * @param selectable
   */
  public static SUB SUB(Selectable selectable, long longValue)
  {
    return new SUB(selectable, longValue);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   */
  public static SUB SUB(Selectable selectable, long longValue, String userDefinedAlias)
  {
    return new SUB(selectable, longValue, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(float floatValue, Selectable selectable)
  {
    return new SUB(floatValue, selectable);
  }

  /**
   * SUB function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(float floatValue, Selectable selectable, String userDefinedAlias)
  {
    return new SUB(floatValue, selectable, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param floatValue
   * @param selectable
   */
  public static SUB SUB(Selectable selectable, float floatValue)
  {
    return new SUB(selectable, floatValue);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   */
  public static SUB SUB(Selectable selectable, float floatValue, String userDefinedAlias)
  {
    return new SUB(selectable, floatValue, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(double doubleValue, Selectable selectable)
  {
    return new SUB(doubleValue, selectable);
  }

  /**
   * SUB function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static SUB SUB(double doubleValue, Selectable selectable, String userDefinedAlias)
  {
    return new SUB(doubleValue, selectable, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * SUB function.
   *
   * @param doubleValue
   * @param selectable
   */
  public static SUB SUB(Selectable selectable, double doubleValue)
  {
    return new SUB(selectable, doubleValue);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   */
  public static SUB SUB(Selectable selectable, double doubleValue, String userDefinedAlias)
  {
    return new SUB(selectable, doubleValue, userDefinedAlias, null);
  }

  /**
   * SUB function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static SUB SUB(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SUB(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  public static MOD MOD(Selectable selectable1, Selectable selectable2)
  {
    return new MOD(selectable1, selectable2);
  }

  /**
   * MOD function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   */
  public static MOD MOD(Selectable selectable1, Selectable selectable2, String userDefinedAlias)
  {
    return new MOD(selectable1, selectable2, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(int intValue, Selectable selectable)
  {
    return new MOD(intValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(int intValue, Selectable selectable, String userDefinedAlias)
  {
    return new MOD(intValue, selectable, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   */
  public static MOD MOD(Selectable selectable, int intValue)
  {
    return new MOD(selectable, intValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   */
  public static MOD MOD(Selectable selectable, int intValue, String userDefinedAlias)
  {
    return new MOD(selectable, intValue, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(long longValue, Selectable selectable)
  {
    return new MOD(longValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(long longValue, Selectable selectable, String userDefinedAlias)
  {
    return new MOD(longValue, selectable, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param longValue
   * @param selectable
   */
  public static MOD MOD(Selectable selectable, long longValue)
  {
    return new MOD(selectable, longValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   */
  public static MOD MOD(Selectable selectable, long longValue, String userDefinedAlias)
  {
    return new MOD(selectable, longValue, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(float floatValue, Selectable selectable)
  {
    return new MOD(floatValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(float floatValue, Selectable selectable, String userDefinedAlias)
  {
    return new MOD(floatValue, selectable, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   */
  public static MOD MOD(Selectable selectable, float floatValue)
  {
    return new MOD(selectable, floatValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   */
  public static MOD MOD(Selectable selectable, float floatValue, String userDefinedAlias)
  {
    return new MOD(selectable, floatValue, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(double doubleValue, Selectable selectable)
  {
    return new MOD(doubleValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  public static MOD MOD(double doubleValue, Selectable selectable, String userDefinedAlias)
  {
    return new MOD(doubleValue, selectable, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static MOD MOD(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   */
  public static MOD MOD(Selectable selectable, double doubleValue)
  {
    return new MOD(selectable, doubleValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   */
  public static MOD MOD(Selectable selectable, double doubleValue, String userDefinedAlias)
  {
    return new MOD(selectable, doubleValue, userDefinedAlias, null);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   */
  public static MOD MOD(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new MOD(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   */
  public static EXTRACT EXTRACT(Unit unit, Selectable selectable)
  {
    return new EXTRACT(unit, selectable);
  }

  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   */
  public static EXTRACT EXTRACT(Unit unit, Selectable selectable, String userDefinedAlias)
  {
    return new EXTRACT(unit, selectable, userDefinedAlias, null);
  }

  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public static EXTRACT EXTRACT(Unit unit, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new EXTRACT(unit, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }
  
  
  /**
   * 
   * @param _selectables
   * @return
   */
  public static PartitionBy PARTITION_BY(Selectable... _selectables)
  {
    return new PartitionBy(_selectables);
  }
}
