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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.constants.AndFieldConditionInfo;
import com.runwaysdk.dataaccess.AndFieldConditionDAOIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.session.Session;

public class AndFieldConditionDAO extends CompositeFieldConditionDAO implements AndFieldConditionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -7235272213669409053L;

  /**
   * The default constructor, does not set any attributes
   */
  public AndFieldConditionDAO()
  {
    super();
  }

  public AndFieldConditionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public AndFieldConditionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new AndFieldConditionDAO(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public AndFieldConditionDAO getBusinessDAO()
  {
    return (AndFieldConditionDAO) super.getBusinessDAO();
  }

  public static AndFieldConditionDAO newInstance()
  {
    return (AndFieldConditionDAO) BusinessDAO.newInstance(AndFieldConditionInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static AndFieldConditionDAOIF get(String oid)
  {
    return (AndFieldConditionDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public boolean evaluate(Mutable instance, HashMap<String, List<Entity>> extraEntities)
  {
    FieldConditionDAOIF firstCondition = this.getFirstCondition();
    FieldConditionDAOIF secondCondition = this.getSecondCondition();

    return firstCondition.evaluate(instance, extraEntities) && secondCondition.evaluate(instance, extraEntities);
  }

  @Override
  public String getFormattedString()
  {
    FieldConditionDAOIF firstCondition = this.getFirstCondition();
    FieldConditionDAOIF secondCondition = this.getSecondCondition();

    return ServerExceptionMessageLocalizer.andCondition(Session.getCurrentLocale(), firstCondition.getFormattedString(), secondCondition.getFormattedString());
  }
}
