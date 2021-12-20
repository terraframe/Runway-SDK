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
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;

public interface MdAttributeLocalDAOIF extends MdAttributeStructDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_local";
  
  /**
   * Default locale column name.
   */
  public static final String DEFAULT_LOCALE = "default_locale";

  /**
   * Length of the metadata display label column.
   */
  public static final String METADATA_DISPLAY_LABEL_COLUMN_SIZE = "255";

  /**
   * The number of metadata display label and description columns.
   */
  public static final int NUMBER_OF_TEMP_COLUMNS = 64;

  /**
   * Temp column name root for the metadata display label and description to temporarily store
   * values for databases that need a separate DML and DDL connection.
   */
  public static final String METADATA_DISPLAY_LABEL_COLUMN_TEMP = "t";
  
  /**
   * Returns the MdStruct that defines the class used to store the values of the struct attribute.
   *
   * @return the MdStruct that defines the class used to store the values of the struct attribute.
   */
  public MdLocalStructDAOIF getMdStructDAOIF();
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLocalDAO getBusinessDAO();
}
