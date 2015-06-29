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

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;

public interface MdActionDAOIF extends ParameterMarker
{
  public static String CLASS = Constants.METADATA_PACKAGE + ".MdAction";

  public static String TABLE = "md_action";

  public MdActionDAO getBusinessDAO();

  public List<MdParameterDAOIF> getMdParameterDAOs();

  /**
   * True if this action is a post, false if otherwise.
   * 
   * @return true if this action is a post, false if otherwise.
   */
  public boolean isPost();
  
  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels();
}
