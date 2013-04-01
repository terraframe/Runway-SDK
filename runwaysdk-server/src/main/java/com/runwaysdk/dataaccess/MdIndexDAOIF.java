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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Locale;

import com.runwaysdk.dataaccess.metadata.MdIndexDAO;

public interface MdIndexDAOIF extends MetadataDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_index";

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale);

  /**
   * Returns true if the index is unique, false otherwise.
   * @return true if the index is unique, false otherwise.
   */
  public boolean isUnique();
  /**
   * Returns <code>MdAttributeBooleanInfo.TRUE</code> if the index is unique, <code>MdAttributeBooleanInfo.FALSE</code> otherwise.
   * @return <code>MdAttributeBooleanInfo.TRUE</code> if the index is unique, <code>MdAttributeBooleanInfo.FALSE</code> otherwise.
   */
  public String getUniqueValue();

  /**
   * Returns the MdEntityIF that this index dfines an index on.
   */
  public MdEntityDAOIF definesIndexForEntity();

  /**
   * Returns the name the index that is defined in the database.
   */
  public String getIndexName();

  /**
   * Returns true if this index is applied to the database, false otherwise.
   * This is a boolean flag stored on this object.  It does not actually
   * check the database.
   * @return true if this index is applied to the database, false otherwise.
   */
  public boolean isActive();

  /**
   * Returns the metadata that defines each attribute in the index.
   * @return metadata that defines each attribute in the index.
   */
  public List<MdAttributeConcreteDAOIF> getIndexedAttributes();

  /**
   * MdIndex.
   * @return MdIndex.
   */
  public MdIndexDAO getBusinessDAO();

}
