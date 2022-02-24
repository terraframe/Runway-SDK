/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.gis.constants.MdWebPointInfo;
import com.runwaysdk.gis.dataaccess.MdWebPointDAOIF;

public class MdWebPointDAO extends MdWebGeometryDAO implements MdWebPointDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 4636342852312950231L;

  public MdWebPointDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebPointDAO()
  {
    super();
  }
  
  @Override
  public MdWebPointDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebPointDAO(attributeMap, classType);
  }
  
  public static MdWebPointDAO newInstance()
  {
    return (MdWebPointDAO) BusinessDAO.newInstance(MdWebPointInfo.CLASS);
  }
}
