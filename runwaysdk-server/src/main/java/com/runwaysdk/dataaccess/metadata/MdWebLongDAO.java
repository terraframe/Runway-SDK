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

import java.util.Map;

import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdWebLongDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebLongDAO extends MdWebNumberDAO implements MdWebLongDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 8838184885510827391L;

  public MdWebLongDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebLongDAO()
  {
    super();
  }

  @Override
  public MdWebLongDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebLongDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeLongDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeLongDAOIF) super.getDefiningMdAttribute();
  }

  public static MdWebLongDAO newInstance()
  {
    return (MdWebLongDAO) BusinessDAO.newInstance(MdWebLongInfo.CLASS);
  }
}
