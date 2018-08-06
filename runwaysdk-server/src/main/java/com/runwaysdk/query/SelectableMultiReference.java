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
/**
 * 
 */
package com.runwaysdk.query;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public interface SelectableMultiReference extends Selectable
{
  /**
   * Compares the oid of a component for equality.
   * 
   * @param oid
   *          oid of the object to compare.
   * @return Basic Condition object
   */
  public BasicCondition EQ(String oid);

  // Any
  /**
   * Checks if the enumeration attribute contains a mapping with one of the
   * enumeration items with the given oid.
   * 
   * @param itemIds
   *          ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsAny(String... itemIds);

  // Not Any
  /**
   * Checks if the enumeration attribute does not contain a mapping with one of
   * the enumeration items with the given oid.
   * 
   * @param itemIds
   *          ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition notContainsAny(String... itemIds);

  // All
  /**
   * Checks if the enumeration attribute contains a mapping with all of the
   * enumeration items with the given oid.
   * 
   * @param itemIds
   *          ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsAll(String... itemIds);

  // NOT All
  /**
   * Checks if the enumeration attribute does not contain a mapping with all of
   * the enumeration items with the given oid.
   * 
   * @param itemIds
   *          ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition notContainsAll(String... itemIds);

  // Exactly
  /**
   * Checks if the enumeration attribute contains a mapping with exactly the
   * given set of the enumeration items with the given oid.
   * 
   * @param itemIds
   *          ID of an enumeration.
   * @return Condition representing the query constraint.
   */
  public Condition containsExactly(String... itemIds);

}
