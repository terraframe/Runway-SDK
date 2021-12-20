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
package com.runwaysdk.dataaccess.metadata;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.MdMobileHeaderDAOIF;

public class MdMobileHeaderDAO extends MdMobileFieldDAO implements MdMobileHeaderDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -6150215746104186727L;

  @Override
  public String getDisplayLabel(Locale locale)
  {
    return null;
  }

  @Override
  public String getFieldName()
  {
    return null;
  }

  @Override
  public String getFieldOrder()
  {
    return null;
  }

  @Override
  public MdFormDAOIF getMdForm()
  {
    return null;
  }

  @Override
  public String getMdFormId()
  {
    return null;
  }

  @Override
  public Map<String, String> getDisplayLabels()
  {
    return null;
  }

}
