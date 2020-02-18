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
 * Created on Aug 15, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.util.Locale;

import com.runwaysdk.constants.IndexTypes;


/**
 * @author nathan
 *
 */
public interface MdAttributeConcreteDAOIF extends MdAttributeDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_concrete";
  /**
   * Column Name of the attribute that specifies the name for this attribute.
   */
  public static final String NAME_COLUMN              = "attribute_name";
  /**
   * Column name of the attribute that indicates what kind of index this attribute has.
   */
  public static final String INDEX_TYPE_COLUMN        = "index_type";
  /**
   * Column name of the attribute that has the name of the index in the database.
   */
  public static final String INDEX_TYPE_NAME        = "index_name";
  /**
   * Column Name of the attribute that specifies the database column name.
   */
  public static final String COLUMN_NAME_COLUMN       = "column_name";
  /**
   * Column name of the attribute that specifies which site has write abilities
   */
  public static final String SITE_MASTER_COLUMN       = "site_master";
  /**
   * Column name of the attribute that references the meta data type to which this
   * attribute belongs.
   */
  public static final String DEFINING_MD_CLASS_COLUMN = "defining_md_class";
  /**
   * The visibility of the setter methods for this attribute.
   */
  public static final String SETTER_VISIBILITY_COLUMN = "setter_visibility";
  /**
   * The visibility of the getter methods for this attribute.
   */
  public static final String GETTER_VISIBILITY_COLUMN = "getter_visibility";
  /**
   * The enum cache of the attribute that indicates what kind of index this attribute has.
   */
  public static final String INDEX_TYPE_CACHE         = MdAttributeConcreteDAOIF.INDEX_TYPE_COLUMN+MdAttributeEnumerationDAOIF.CACHE_COLUMN_DELIMITER;
  /**
   * The enum cache of the attribute that specifies visibility of the setter methods for this attribute.
   */
  public static final String SETTER_VISIBILITY_CACHE  = MdAttributeConcreteDAOIF.SETTER_VISIBILITY_COLUMN+MdAttributeEnumerationDAOIF.CACHE_COLUMN_DELIMITER;
  /**
   * The enum cache of the attribute that specifies visibility of the getter methods for this attribute.
   */
  public static final String GETTER_VISIBILITY_CACHE  = MdAttributeConcreteDAOIF.GETTER_VISIBILITY_COLUMN+MdAttributeEnumerationDAOIF.CACHE_COLUMN_DELIMITER;
  
  /**
   * Returns the name of the column in the database.
   *
   * @return the name of the column in the database.
   */
  public String getColumnName();

  /**
   * Returns the name of the column in the database as it is in this metadata.
   *
   * @return name of the column in the database as it is in this metadata.
   */
  public String getDefinedColumnName();

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale);
  
  /**
   * Returns true if the attribute should be unique, false otherwise.
   *
   * @return true if the attribute should be unique, false otherwise.
   */
  public boolean isUnique();

  /**
   * Returns true if the attribute participates in an individual non-unique column index, false otherwise.
   *
   * @return true if the attribute participates in an individual non-unique column index, false otherwise.
   */
  public boolean hasNonUniqueIndex();

  /**
   * Returns true if the attribute participates in an individual column index, false otherwise.
   *
   * @return true if the attribute participates in an individual column index, false otherwise.
   */
  public boolean hasIndividualIndex();

  /**
   * Returns true if the attribute is part of a group of indexed attributes, false otherwise.
   * @return true if the attribute is part of a group of indexed attributes, false otherwise.
   */
  public boolean isPartOfIndexedAttributeGroup();

  /**
   * Used for data generation.  Returns a random valid value for this Attribute type.
   * @param object The entity to set a random value on
   */
  public void setRandomValue(EntityDAO object);

}
