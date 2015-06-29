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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdFormInfo;

public interface MdFormDAOIF extends MdTypeDAOIF
{
  public static String       CLASS = MdFormInfo.CLASS;

  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE = "md_form";

  /**
   * Returns the name of the form.
   * 
   * @return
   */
  public String getFormName();

  /**
   * Returns the MdClass that this MdForm represents.
   * 
   * @return
   */
  public MdClassDAOIF getFormMdClass();

  /**
   * Returns the immediate children of this MdForm, including groups.
   * 
   * @return
   */
  public List<? extends MdFieldDAOIF> getAllMdFields();

  public List<? extends MdFieldDAOIF> getAllMdFieldsForDelete();

  /**
   * Returns all fields, recursively, in order. Groups are also included.
   * 
   * @return
   */
  public List<? extends MdFieldDAOIF> getOrderedMdFields();

  /**
   * Returns the MdFieldDAOIF with the given name defined by this Form.
   * 
   * @return
   */
  public MdFieldDAOIF getMdField(String fieldName);

  public Map<String, ? extends MdFieldDAOIF> getMdFieldsByName();
}
