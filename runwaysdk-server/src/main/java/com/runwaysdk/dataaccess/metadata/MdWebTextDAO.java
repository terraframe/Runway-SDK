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

import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdWebTextDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebTextDAO extends MdWebPrimitiveDAO implements MdWebTextDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 3395616645991178999L;

  public MdWebTextDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebTextDAO()
  {
    super();
  }

  @Override
  public MdWebTextDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebTextDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeTextDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeTextDAOIF) super.getDefiningMdAttribute();
  }

  public static MdWebTextDAO newInstance()
  {
    return (MdWebTextDAO) BusinessDAO.newInstance(MdWebTextInfo.CLASS);
  }

  @Override
  public String getHeight()
  {
    return this.getAttribute(MdWebTextInfo.HEIGHT).getValue();
  }

  @Override
  public String getWidth()
  {
    return this.getAttribute(MdWebTextInfo.WIDTH).getValue();
  }

}
