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

import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;

public interface MdLocalStructDAOIF extends MdStructDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                     = "md_local_struct";
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdLocalStructDAO getBusinessDAO();
  
  /**
   * Returns all MdAttributeStruct that reference this class.
   * @return all MdAttributeStruct that reference this class.
   */
  public List<? extends MdAttributeLocalCharacterDAOIF> getMdAttributeStruct();
  
  /**
   * Returns the reference to this object, as an MdStruct is always the root 
   * of its own hierarchy.
   * 
   * @return reference to this object, as an MdStruct is always the root 
   *         of its own hierarchy.
   */
  public MdLocalStructDAOIF getRootMdClassDAO();
  
  public MdAttributeDAOIF getLocale(MdDimensionDAOIF mdDimensionDAO, Locale locale);
  
  public MdAttributeDAOIF getLocale(Locale locale);
  
  public MdAttributeDAOIF getDefaultLocale(MdDimensionDAOIF mdDimensionDAO);
}
