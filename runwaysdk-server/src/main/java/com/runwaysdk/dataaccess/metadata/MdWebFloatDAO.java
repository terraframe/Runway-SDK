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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebFloatDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebFloatDAO extends MdWebDecDAO implements MdWebFloatDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -4871219878565403472L;

  public MdWebFloatDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebFloatDAO()
  {
    super();
  }

  @Override
  public MdWebFloatDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebFloatDAO(attributeMap, classType);
  }

  public static MdWebFloatDAO newInstance()
  {
    return (MdWebFloatDAO) BusinessDAO.newInstance(MdWebFloatInfo.CLASS);
  }
}
