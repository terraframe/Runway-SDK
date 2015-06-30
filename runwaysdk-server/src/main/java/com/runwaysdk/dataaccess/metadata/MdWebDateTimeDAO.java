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

import com.runwaysdk.constants.MdWebDateTimeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdWebDateTimeDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebDateTimeDAO extends MdWebMomentDAO implements MdWebDateTimeDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 283682901132359174L;

  public MdWebDateTimeDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebDateTimeDAO()
  {
    super();
  }

  @Override
  public MdWebDateTimeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebDateTimeDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeDateTimeDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeDateTimeDAOIF) super.getDefiningMdAttribute();
  }

  public static MdWebDateTimeDAO newInstance()
  {
    return (MdWebDateTimeDAO) BusinessDAO.newInstance(MdWebDateTimeInfo.CLASS);
  }

}
