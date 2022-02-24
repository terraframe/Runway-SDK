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

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdWebGroupInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebGroupDAOIF;
import com.runwaysdk.dataaccess.TreeDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.system.metadata.MdWebField;
import com.runwaysdk.system.metadata.WebGroupField;

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
  public static MdWebGroupDAOIF get(String oid)
  {
    return (MdWebGroupDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public TreeDAO addField(MdWebFieldDAOIF mdWebField)
  {
    return TreeDAO.newInstance(this.getOid(), mdWebField.getOid(), RelationshipTypes.WEB_GROUP_FIELD.getType());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.MdWebGroupDAOIF#removeField(com.runwaysdk.dataaccess
   * .MdWebFieldDAOIF)
   */
  @Override
  public void removeField(MdWebFieldDAOIF mdWebField)
  {
    this.removeAllChildren(mdWebField, RelationshipTypes.WEB_GROUP_FIELD.getType());
  }

  /**
   * Returns all fields in order for the MdWebGroup.
   * 
   * @param groupId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdWebFieldDAO> getGroupFields()
  {
    QueryFactory f = new QueryFactory();

    BusinessDAOQuery q = f.businessDAOQuery(MdWebField.CLASS);
    RelationshipDAOQuery relQ = f.relationshipDAOQuery(WebGroupField.CLASS);

    relQ.WHERE(relQ.parentOid().EQ(this.getOid()));
    q.WHERE(q.isChildIn(relQ));

    q.ORDER_BY_ASC(q.aInteger(MdWebField.FIELDORDER));

    OIterator<? extends BusinessDAOIF> iterator = q.getIterator();

    try
    {
      return (List<? extends MdWebFieldDAO>) iterator.getAll();
    }
    finally
    {
      iterator.close();
    }
  }

}
