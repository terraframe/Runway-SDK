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

import com.runwaysdk.constants.CompositeFieldConditionInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.CompositeFieldConditionDAOIF;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.system.metadata.FieldConditionDAO;

public abstract class CompositeFieldConditionDAO extends FieldConditionDAO implements CompositeFieldConditionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -8568975834312012046L;

  /**
   * The default constructor, does not set any attributes
   */
  public CompositeFieldConditionDAO()
  {
    super();
  }

  public CompositeFieldConditionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public CompositeFieldConditionDAO getBusinessDAO()
  {
    return (CompositeFieldConditionDAO) super.getBusinessDAO();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static CompositeFieldConditionDAOIF get(String id)
  {
    return (CompositeFieldConditionDAOIF) BusinessDAO.get(id);
  }

  public FieldConditionDAOIF getFirstCondition()
  {
    return FieldConditionDAO.get(this.getAttribute(CompositeFieldConditionInfo.FIRST_CONDITION).getValue());
  }

  public FieldConditionDAOIF getSecondCondition()
  {
    return FieldConditionDAO.get(this.getAttribute(CompositeFieldConditionInfo.SECOND_CONDITION).getValue());
  }

  @Override
  public void delete(boolean businessContext)
  {
    String firstConditionId = this.getAttribute(CompositeFieldConditionInfo.FIRST_CONDITION).getValue();
    String secondConditionId = this.getAttribute(CompositeFieldConditionInfo.SECOND_CONDITION).getValue();

    super.delete(businessContext);

    if (firstConditionId != null && firstConditionId.length() > 0)
    {
      FieldConditionDAO.get(firstConditionId).getBusinessDAO().delete();
    }

    if (secondConditionId != null && secondConditionId.length() > 0)
    {
      FieldConditionDAO.get(secondConditionId).getBusinessDAO().delete();
    }
  }

}
