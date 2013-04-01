/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdWebTimeDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebTimeDAO extends MdWebMomentDAO implements MdWebTimeDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -754287804473762954L;

  public MdWebTimeDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebTimeDAO()
  {
    super();
  }
  
  @Override
  public MdWebTimeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebTimeDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeTimeDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeTimeDAOIF) super.getDefiningMdAttribute();
  }
}
