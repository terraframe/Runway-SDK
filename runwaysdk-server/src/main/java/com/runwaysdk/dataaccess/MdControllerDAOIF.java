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

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;

public interface MdControllerDAOIF extends MdTypeDAOIF
{
  public static String CLASS = Constants.METADATA_PACKAGE + ".MdController";
  
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_controller";
  
  public static final String       STUB_SOURCE_COLUMN = "stub_source";

  public static final String       STUB_CLASS_COLUMN  = "stub_class";

  public List<MdActionDAOIF> getMdActionDAOs();
  
  public List<MdActionDAOIF> getMdActionDAOsOrdered();
  
  public MdEntityDAOIF getMdEntity();
  
  public MdActionDAOIF definesMdAction(String actionName);
  
  /**
   *
   * @return
   */
  public MdControllerDAO getBusinessDAO();
 
}
