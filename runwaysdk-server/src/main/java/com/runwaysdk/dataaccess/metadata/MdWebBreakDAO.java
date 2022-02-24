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

import com.runwaysdk.constants.MdWebBreakInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebBreakDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebBreakDAO extends MdWebFieldDAO implements MdWebBreakDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -2275607538051181875L;

  public MdWebBreakDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebBreakDAO()
  {
    super();
  }
  
  public static MdWebBreakDAO newInstance()
  {
    return (MdWebBreakDAO) BusinessDAO.newInstance(MdWebBreakInfo.CLASS);
  }
  
  @Override
  public MdWebBreakDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebBreakDAO(attributeMap, classType);
  }
}
