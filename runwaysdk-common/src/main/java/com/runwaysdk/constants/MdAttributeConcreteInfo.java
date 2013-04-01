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
package com.runwaysdk.constants;


public interface MdAttributeConcreteInfo extends MdAttributeInfo
{

  /**
   * Class.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdAttributeConcrete";
  /**
   * ID.
   */
  public static final String ID_VALUE  = "0000000000000000000000000000000200000000000000000000000000000001";
  /**
   * Name of the attribute that references the meta data type to which this
   * attribute belongs.
   */
  public static final String DEFINING_MD_CLASS         = "definingMdClass";
  /**
   * Name of the attribute that specifies the name for this attribute.
   */
  public static final String NAME                      = "attributeName";
  /**
   * Name of the attribute that specifies the database column name.
   */
  public static final String COLUMN_NAME               = "columnName";
  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL             = "displayLabel";
  /**
   * Name of the attribute that defines the default value for instances of this attribute.
   */
  public static final String DEFAULT_VALUE             = "defaultValue";
  /**
   * Name of the attribute that indicates if the attribute requires a value.
   */
  public static final String REQUIRED                  = "required";
  /**
   * Name of the attribute that indicates if the attribute value can only be modified
   * internally.
   */
  public static final String SYSTEM                    = "system";
  /**
   * Name of the attribute that indicates if the attribute value be modified once it has
   * received an initial value.
   */
  public static final String IMMUTABLE                 = "immutable";
  /**
   * The name of the attribute that indicates what kind of index this attribute has.
   */
  public static final String INDEX_TYPE                = "indexType";
  /**
   * The name of the index in the database.
   */
  public static final String INDEX_NAME                = "indexName";
  /**
   * The visibility of the setter methods for this attribute.
   */
  public static final String SETTER_VISIBILITY         = "setterVisibility";
  /**
   * The visibility of the getter methods for this attribute.
   */
  public static final String GETTER_VISIBILITY         = "getterVisibility";
}
