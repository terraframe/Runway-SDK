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

import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebReferenceDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebReferenceDAO extends MdWebAttributeDAO implements MdWebReferenceDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -4379494834886737785L;

  public MdWebReferenceDAO()
  {
    super();
  }

  public MdWebReferenceDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public MdWebReferenceDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebReferenceDAO(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdWebReferenceDAO getBusinessDAO()
  {
    return (MdWebReferenceDAO) super.getBusinessDAO();
  }

  public static MdWebReferenceDAO newInstance()
  {
    return (MdWebReferenceDAO) BusinessDAO.newInstance(MdWebReferenceInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdWebReferenceDAOIF get(String id)
  {
    return (MdWebReferenceDAOIF) BusinessDAO.get(id);
  }
}
