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
package com.runwaysdk.query;



public interface SelectableEnumeration extends Selectable
{
  /**
   * Compares the id of a component for equality.
   * @param id id of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(String id);

  // Any
  /**
   * Checks if the enumeration attribute contains a mapping with
   * one of the enumeration items with the given id.
   * @param enumIds ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsAny(String ... enumIds);

  // Not Any
  /**
   * Checks if the enumeration attribute does not contain a mapping with
   * one of the enumeration items with the given id.
   * @param enumIds ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition notContainsAny(String ... enumIds);

  // All
  /**
   * Checks if the enumeration attribute contains a mapping with
   * all of the enumeration items with the given id.
   * @param enumIds ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsAll(String ... enumIds);

  // NOT All
  /**
   * Checks if the enumeration attribute does not contain a mapping with
   * all of the enumeration items with the given id.
   * @param enumIds ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition notContainsAll(String ... enumIds);

  // Exactly
  /**
   * Checks if the enumeration attribute contains a mapping with
   * exactly the given set of the enumeration items with the given id.
   * @param enumIds ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsExactly(String ... enumIds);

  /**
   * Returns a {@link ColumnInfo} object that contains SQL specific attributes for the cached column.
   * 
   * @return a {@link ColumnInfo} object that contains SQL specific attributes for the cached column.
   */
  public ColumnInfo getCacheColumnInfo();
 
  /**
   * Returns the name of the database column that stores the cached enumeration references.
   * 
   * @return name of the database column that stores the cached enumeration references.
   */
  public String getCacheColumnName();
  
  /**
   * Returns the name of the database column alias that references the column that stores the cached enumeration references.
   * 
   * @return name of the database column alias that references the column that stores the cached enumeration references.
   */
  public String getCacheColumnAlias();
 
  
}
