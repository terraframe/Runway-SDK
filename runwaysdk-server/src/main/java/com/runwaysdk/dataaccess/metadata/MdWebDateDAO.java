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

import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdWebDateDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebDateDAO extends MdWebMomentDAO implements MdWebDateDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 7765378849680590466L;

  public MdWebDateDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebDateDAO()
  {
    super();
  }

  @Override
  public MdWebDateDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebDateDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeDateDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeDateDAOIF) super.getDefiningMdAttribute();
  }

  public static MdWebDateDAO newInstance()
  {
    return (MdWebDateDAO) BusinessDAO.newInstance(MdWebDateInfo.CLASS);
  }

}
