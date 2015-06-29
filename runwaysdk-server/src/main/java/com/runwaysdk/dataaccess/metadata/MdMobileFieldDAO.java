/**
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
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.MdMobileFieldDAOIF;
import com.runwaysdk.dataaccess.MdMobileGroupDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;

public abstract class MdMobileFieldDAO extends MdFieldDAO implements MdMobileFieldDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 5592904488942430845L;

  public MdMobileFieldDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdMobileFieldDAO()
  {
    super();
  }

  @Override
  public String isRequired()
  {
    return null;
  }

  @Override
  public String getDisplayLabel(Locale locale)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getFieldName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getFieldOrder()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MdFormDAOIF getMdForm()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getMdFormId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, String> getDisplayLabels()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public MdMobileGroupDAOIF getContainingGroup()
  {
    QueryFactory factory = new QueryFactory();
    RelationshipDAOQuery query = factory.relationshipDAOQuery(RelationshipTypes.MOBILE_GROUP_FIELD.getType());

    query.WHERE(query.childId().EQ(this.getId()));

    OIterator<RelationshipDAOIF> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        RelationshipDAOIF relationship = it.next();

        return (MdMobileGroupDAOIF) relationship.getParent();
      }

      return null;
    }
    finally
    {
      it.close();
    }
  }

  @Override
  public List<FieldConditionDAOIF> getConditions()
  {
    List<FieldConditionDAOIF> conditions = super.getConditions();

    MdMobileGroupDAOIF group = this.getContainingGroup();

    if (group != null)
    {
      conditions.addAll(group.getConditions());
    }

    return conditions;
  }

}
