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

import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdWebIntegerDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebIntegerDAO extends MdWebNumberDAO implements MdWebIntegerDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -1210104181542887010L;


  public MdWebIntegerDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebIntegerDAO()
  {
    super();
  }
  
  @Override
  public MdWebIntegerDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebIntegerDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeIntegerDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeIntegerDAOIF) super.getDefiningMdAttribute();
  }
  
  public static MdWebIntegerDAO newInstance()
  {
    return (MdWebIntegerDAO) BusinessDAO.newInstance(MdWebIntegerInfo.CLASS);
  }
}
