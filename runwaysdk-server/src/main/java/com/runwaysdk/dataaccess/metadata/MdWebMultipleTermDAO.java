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

import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebMultipleTermDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebMultipleTermDAO extends MdWebAttributeDAO implements MdWebMultipleTermDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -537361856527820340L;

  public MdWebMultipleTermDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebMultipleTermDAO()
  {
    super();
  }

  @Override
  public MdWebMultipleTermDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebMultipleTermDAO(attributeMap, classType);
  }
  
  public static MdWebMultipleTermDAO newInstance()
  {
    return (MdWebMultipleTermDAO) BusinessDAO.newInstance(MdWebMultipleTermInfo.CLASS);
  }
}
