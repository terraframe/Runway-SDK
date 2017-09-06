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
package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.metadata.MdTableDAO;

public interface MdTableDAOIF extends MdTableClassIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_table";
  
  /**
   * {@link MdTableDAOIF} does not currently support inheritance.
   */
  @Override
  public List<? extends MdTableDAOIF> getSubClasses();
  
  /**
   * {@link MdTableDAOIF} does not currently support inheritance.
   */
  @Override
  public MdTableDAOIF getSuperClass();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdTableDAO getBusinessDAO();
  
  /**
   * Returns the name of the table that this metadata represents.
   * @return name of the table that this metadata represents.
   */
  public String getTableName();
}
