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

import com.runwaysdk.constants.MdMobileGroupInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdMobileFieldDAOIF;
import com.runwaysdk.dataaccess.MdMobileGroupDAOIF;
import com.runwaysdk.dataaccess.TreeDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdMobileGroupDAO extends MdMobileFieldDAO implements MdMobileGroupDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -2605590505858368579L;

  public MdMobileGroupDAO()
  {
    super();
  }

  public MdMobileGroupDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public MdMobileGroupDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdMobileGroupDAO(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdMobileGroupDAO getBusinessDAO()
  {
    return (MdMobileGroupDAO) super.getBusinessDAO();
  }

  public static MdMobileGroupDAO newInstance()
  {
    return (MdMobileGroupDAO) BusinessDAO.newInstance(MdMobileGroupInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdMobileGroupDAOIF get(String id)
  {
    return (MdMobileGroupDAOIF) BusinessDAO.get(id);
  }

  @Override
  public TreeDAO addField(MdMobileFieldDAOIF mdMobileField)
  {
    return TreeDAO.newInstance(this.getId(), mdMobileField.getId(), RelationshipTypes.MOBILE_GROUP_FIELD.getType());
  }

}
