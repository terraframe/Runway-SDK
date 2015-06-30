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

import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdWebDoubleDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebDoubleDAO extends MdWebDecDAO implements MdWebDoubleDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 1278922477459389227L;

  public MdWebDoubleDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebDoubleDAO()
  {
    super();
  }
  
  @Override
  public MdWebDoubleDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebDoubleDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeDoubleDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeDoubleDAOIF) super.getDefiningMdAttribute();
  }

  public static MdWebDoubleDAO newInstance()
  {
    return (MdWebDoubleDAO) BusinessDAO.newInstance(MdWebDoubleInfo.CLASS);
  }
}
