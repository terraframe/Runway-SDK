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
package com.runwaysdk.system.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public abstract class FieldConditionDAO extends BusinessDAO implements FieldConditionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -2331062368765847255L;

  /**
   * The default constructor, does not set any attributes
   */
  public FieldConditionDAO()
  {
    super();
  }

  public FieldConditionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public FieldConditionDAO getBusinessDAO()
  {
    return (FieldConditionDAO) super.getBusinessDAO();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static FieldConditionDAOIF get(String oid)
  {
    return (FieldConditionDAOIF) BusinessDAO.get(oid);
  }
  
  @Override
  public boolean evaluate(Mutable instance)
  {
    return this.evaluate(instance, new HashMap<String, List<Entity>>());
  }
}
