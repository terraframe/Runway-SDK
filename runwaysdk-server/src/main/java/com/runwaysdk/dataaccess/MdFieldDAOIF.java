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

import java.util.List;
import java.util.Locale;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;

public interface MdFieldDAOIF extends MetadataDAOIF
{
  public static String       CLASS = MdFieldInfo.CLASS;

  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE = "md_field";

  public String getFieldName();

  public String getFieldOrder();

  public String getDisplayLabel(Locale locale);

  public String getDescription(Locale locale);

  /**
   * Returns the MdForm that defines this MdField.
   * 
   * @return
   */
  public MdFormDAOIF getMdForm();

  /**
   * Returns the oid of the MdForm that defines this MdField.
   * 
   * @return
   */
  public String getMdFormId();

  /**
   * Returns the underlying BusinessDAO of this MdFieldDAO.
   * 
   * @return
   */
  public MdFieldDAO getBusinessDAO();

  public String isRequired();

  public List<FieldConditionDAOIF> getConditions();
}
