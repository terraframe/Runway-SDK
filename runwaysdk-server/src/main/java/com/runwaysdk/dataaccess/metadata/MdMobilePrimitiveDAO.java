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

import com.runwaysdk.constants.MdMobilePrimitiveInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdMobilePrimitiveDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;


public abstract class MdMobilePrimitiveDAO extends MdMobileFieldDAO implements MdMobilePrimitiveDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 8720704250106685425L;

  public MdMobilePrimitiveDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdMobilePrimitiveDAO()
  {
    super();
  }
  
  @Override
  public MdAttributeDAOIF getDefiningMdAttribute()
  {
    return (MdAttributeDAOIF) this.getAttribute(MdMobilePrimitiveInfo.DEFINING_MD_ATTRIBUTE);
  }


}
