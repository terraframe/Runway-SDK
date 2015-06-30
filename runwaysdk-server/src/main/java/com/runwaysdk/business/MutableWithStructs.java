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
package com.runwaysdk.business;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public interface MutableWithStructs extends Mutable
{
  
  /**
   * Returns the Struct associated with an AttributeStruct 
   * 
   * @param structName The name of the AttributeStruct
   * @return A Struct representation of the AttributeStruct
   */
  public Struct getStruct(String structName);
  
  /**
   * A generic, type-unsafe getter for struct attributes that takes the
   * attribute and struct names as Strings, and returns the value as a
   * String
   * 
   * @param structName String name of the struct
   * @param name String name of the desired attribute
   * @return String representation of the struct value
   */
  public String getStructValue(String structName, String attributeName);

  /**
   * Returns the value for the attribute that matches the given locale (or a best fit).
   * 
   * @param localAttributeName
   * @param local
   * @return the value of a local attribute
   */
  public String getLocalValue(String localAttributeName, Locale locale);
  
  /**
   * A generic, type-unsafe getter for struct blob attributes that takes the
   * attribute and struct names as Strings, and returns the value as a
   * byte array
   * 
   * @param structName String name of the struct
   * @param blobName String name of the desired blob attribute
   * @return byte[] representation of the struct value
   */
  public byte[] getStructBlob(String structName, String blobName);

  /**
   * Returns a list of selected values for the given enumerated attribute. The
   * declared type of the list is BusinessEnumeration, but each entry is
   * instantiated through reflection, which allows for accurate actual types.
   * 
   * @param name Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getStructEnumValues(String structName, String attributeName);
  
  /**
   * A generic, type-unsafe setter for struct attributes that takes the
   * attribute name, struct name, and value as Strings
   * 
   * @param structName String name of the struct
   * @param name String name of the desired attribute
   * @param vale  String representation of the struct value
   */
  public void setStructValue(String structName, String attributeName, String _value);

  /**
   * A generic, type-unsafe setter for struct attributes that takes the
   * attribute name, struct name, and value as Strings
   * 
   * @param structName String name of the struct
   * @param blobName String name of the desired attribute
   * @param vale  String representation of the struct value
   */
  public void setStructBlob(String structAttributeName, String blobName, byte[] value);

  /**
   * Adds an item to an enumerated struct attribute.
   * 
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   * @param value The value to set
   */
  public void addStructItem(String structName, String attributeName, String value);

  /**
   * Replaces the items of an enumerated struct attribute. If the attribute 
   * does not allow multiplicity, then the {@code values} collection must
   * contain only one item.
   * 
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   * @param values Collection of enumerated it ids
   */
  public void replaceStructItems(String structName, String attributeName, Collection<String> values);
  
  /**
   * Remove an item for an enumerated struct attribute.
   * 
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   * @param value The value to set
   */
  public void removeStructItem(String structName, String attributeName, String value);
  
  /**
   * Clears all the values of a struct enumeration attribute.
   * 
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   */
  public void clearStructItems(String structName, String attributeName);
}
