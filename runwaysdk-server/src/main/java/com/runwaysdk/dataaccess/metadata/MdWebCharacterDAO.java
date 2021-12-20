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

import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdCharacterFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebCharacterDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebCharacterDAO extends MdWebPrimitiveDAO implements MdWebCharacterDAOIF, MdCharacterFieldDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -4658057616355383416L;

  public MdWebCharacterDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebCharacterDAO()
  {
    super();
  }

  @Override
  public String getDisplayLength()
  {
    return this.getAttribute(MdWebCharacterInfo.DISPLAY_LENGTH).getValue();
  }

  @Override
  public String getMaxLength()
  {
    return this.getAttribute(MdWebCharacterInfo.MAX_LENGTH).getValue();
  }

  public static MdWebCharacterDAO newInstance()
  {
    return (MdWebCharacterDAO) BusinessDAO.newInstance(MdWebCharacterInfo.CLASS);
  }

  @Override
  public MdWebCharacterDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebCharacterDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeCharacterDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeCharacterDAOIF) super.getDefiningMdAttribute();
  }
}
