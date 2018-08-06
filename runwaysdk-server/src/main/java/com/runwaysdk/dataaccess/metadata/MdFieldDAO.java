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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.system.metadata.FieldConditionDAO;

public abstract class MdFieldDAO extends MetadataDAO implements MdFieldDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -2802299321530775547L;

  public MdFieldDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdFieldDAO()
  {
    super();
  }

  @Override
  public String apply()
  {
    if (this.getMdForm() != null)
    {
      String key = MdFieldDAO.buildKey(this.getMdForm().getKey(), this.getAttribute(MdFieldInfo.FIELD_NAME).getValue());
      this.setKey(key);
    }

    return super.apply();
  }

  @Override
  public String getSignature()
  {
    return "Name: [" + this.getFieldName() + "] Form: [" + this.getMdForm().getSignature() + "]";
  }

  @Override
  public MdFieldDAO getBusinessDAO()
  {
    return (MdFieldDAO) super.getBusinessDAO();
  }

  @Override
  public void delete(boolean businessContext)
  {
    this.delete(businessContext, true);
  }

  public void delete(boolean businessContext, boolean enforceRemovable)
  {
    String conditionId = this.getAttribute(MdFieldInfo.FIELD_CONDITION).getValue();
    
    super.delete(businessContext, enforceRemovable);
    
    // If there is a condition on the field then delete the condition also
    if (conditionId != null && conditionId.length() > 0)
    {
      FieldConditionDAO.get(conditionId).getBusinessDAO().delete();
    }
    
  }
  
  public List<FieldConditionDAOIF> getConditions()
  {
    List<FieldConditionDAOIF> conditions = new LinkedList<FieldConditionDAOIF>();

    String conditionId = this.getAttribute(MdFieldInfo.FIELD_CONDITION).getValue();

    // If there is a condition on the field then delete the condition also
    if (conditionId != null && conditionId.length() > 0)
    {
      conditions.add(FieldConditionDAO.get(conditionId));
    }

    return conditions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdFieldDAOIF get(String oid)
  {
    return (MdFieldDAOIF) BusinessDAO.get(oid);
  }

  public static String buildKey(String formKey, String fieldName)
  {
    return formKey + "." + fieldName;
  }
}
