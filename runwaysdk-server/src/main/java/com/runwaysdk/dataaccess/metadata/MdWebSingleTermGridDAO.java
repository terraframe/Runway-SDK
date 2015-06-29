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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebSingleTermGridDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;

public class MdWebSingleTermGridDAO extends MdWebAttributeDAO implements MdWebSingleTermGridDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 2688027572137526191L;

  public MdWebSingleTermGridDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebSingleTermGridDAO()
  {
    super();
  }

  @Override
  public MdWebSingleTermGridDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebSingleTermGridDAO(attributeMap, classType);
  }

  public static MdWebSingleTermGridDAO newInstance()
  {
    return (MdWebSingleTermGridDAO) BusinessDAO.newInstance(MdWebSingleTermGridInfo.CLASS);
  }

  /**
   * @param name
   * @return
   */
  public MdFieldDAOIF getMdField(String name)
  {
    try
    {
      String key = MdFieldDAO.buildKey(this.getKey(), name);

      return (MdFieldDAOIF) MdFieldDAO.get(MdFieldInfo.CLASS, key);
    }
    catch (DataNotFoundException e)
    {
      return null;
    }
  }

}
