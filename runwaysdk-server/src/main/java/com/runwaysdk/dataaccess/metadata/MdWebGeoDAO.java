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

import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebGeoDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebGeoDAO extends MdWebAttributeDAO implements MdWebGeoDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 7911996721267754728L;

  public MdWebGeoDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebGeoDAO()
  {
    super();
  }
  
  @Override
  public MdWebGeoDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebGeoDAO(attributeMap, classType);
  }
  
  public static MdWebGeoDAO newInstance()
  {
    return (MdWebGeoDAO) BusinessDAO.newInstance(MdWebGeoInfo.CLASS);
  }
}
