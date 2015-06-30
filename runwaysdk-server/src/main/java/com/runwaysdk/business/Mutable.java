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

import com.runwaysdk.ComponentIF;

public interface Mutable extends ComponentIF
{
  /**
   * When an object at the business layer is converted into a DTO, this method is invoked to
   * ensure there are not any READ violations that are enforced programmatically.  This method
   * should be overwritten in business classes if special programmatic READ permissions need to
   * be implemented.  This method should throw an exception if customized READ permissions are 
   * not adequate.
   */
  public void customReadCheck();

  /**
   * Returns the Universally Unique ID (UUID) for this entity.
   * 
   * @return <b>this</b> entity's UUID
   */
  public String getId();

  /**
   * Returns true if the object has an attribute with the given name, false
   * otherwise. It is case sensitive.
   * 
   * @param name
   *          name of the attribute.
   * @return true if the object has an attribute with the given name, false
   *         otherwise. It is case sensitive.
   */
  public boolean hasAttribute(String name);

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * Strings
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, String _value);

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * an Object.
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, Object object);

  /**
   * A generic, type-unsafe getter that takes a blob attribute name as a String,
   * and returns the value as a byte array
   * 
   * @param blobName
   *          Name of the blob attribute
   * @return byte array representing the blob
   */
  public void setBlob(String blobName, byte[] value);

  /**
   * A generic, type-unsafe method for adding an item to an enumerated attribute
   * that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void addEnumItem(String name, String item);

  /**
   * Replaces the items of an enumerated attribute. If the attribute does not
   * allow multiplicity, then the {@code values} collection must contain only
   * one item.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param values
   *          Collection of enumerated item ids
   */
  public void replaceEnumItems(String name, Collection<String> values);

  /**
   * A generic, type-unsafe method for removing an item from an enumerated
   * attribute that takes the attribute name and enumeration item as Strings
   * 
   * @param name
   *          String name of the enumerated attribute
   * @param item
   *          String representation of the enumeration item
   */
  public void removeEnumItem(String name, String item);

  /**
   * A generic method for clearing out all selected items on an enumerated
   * attribute.
   * 
   * @param name
   *          String name of the enumerated attribute
   */
  public void clearEnum(String name);

  /**
   * Returns if an attribute of the Mutable has been modified from its orginal
   * value loaded from the database.
   * 
   * @param name
   *          The name of the attribute
   * @return
   */
  public boolean isModified(String name);

  /**
   * Persists this entity and all changes to the database.
   * <code><b>new</b></code> instances of Entity are <i>not</i> persisted until
   * <code>apply()</code> is called. Similarly, changes made to instances
   * through the generated java classes are not persisted until
   * <code>apply()</code> is called.
   * 
   * <b>Precondtion:</b> Session user has a lock on this object, assuming this
   * object has a ComponentIF.LOCKED_BY field.
   */
  public void apply();

  /**
   * Deletes this object. If it is an entity then it is also removed from the
   * database. Any attempt to {@link Entity#apply()} this entity will throw an
   * exception, so it is the responsibility of the developer to remove
   * references to deleted instances of Entity.
   */
  public void delete();

  public void addMultiItem(String name, String itemId);

  public void replaceMultiItems(String name, Collection<String> itemIds);

  public void removeMultiItem(String name, String item);

  public void clearMultiItems(String name);
}
