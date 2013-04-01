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

import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebGroupDAOIF;
import com.runwaysdk.dataaccess.TreeDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebGroupDAO extends MdWebFieldDAO implements MdWebGroupDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 8871212150289442694L;

  public MdWebGroupDAO()
  {
    super();
  }

  public MdWebGroupDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public MdWebGroupDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebGroupDAO(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdWebGroupDAO getBusinessDAO()
  {
    return (MdWebGroupDAO) super.getBusinessDAO();
  }

  public static MdWebGroupDAO newInstance()
  {
    return (MdWebGroupDAO) BusinessDAO.newInstance(MdWebGroupInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdWebGroupDAOIF get(String id)
  {
    return (MdWebGroupDAOIF) BusinessDAO.get(id);
  }

  @Override
  public TreeDAO addField(MdWebFieldDAOIF mdWebField)
  {
    return TreeDAO.newInstance(this.getId(), mdWebField.getId(), RelationshipTypes.WEB_GROUP_FIELD.getType());
  }

}
