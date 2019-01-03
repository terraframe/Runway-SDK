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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.LongConditionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;

public class LongConditionDAO extends BasicConditionDAO implements LongConditionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 8899227144282310796L;

  enum Operation {
    EQ, NEQ, GT, GTE, LT, LTE;

    public boolean evaluate(double actual, double expected)
    {
      if (this.equals(EQ))
      {
        return ( actual == expected );
      }
      else if (this.equals(NEQ))
      {
        return ( actual != expected );
      }
      else if (this.equals(GT))
      {
        return ( actual > expected );
      }
      else if (this.equals(GTE))
      {
        return ( actual >= expected );
      }
      else if (this.equals(LT))
      {
        return ( actual < expected );
      }

      return ( actual <= expected );
    }
  }

  /**
   * The default constructor, does not set any attributes
   */
  public LongConditionDAO()
  {
    super();
  }

  public LongConditionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public LongConditionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new LongConditionDAO(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public LongConditionDAO getBusinessDAO()
  {
    return (LongConditionDAO) super.getBusinessDAO();
  }

  public static LongConditionDAO newInstance()
  {
    return (LongConditionDAO) BusinessDAO.newInstance(LongConditionInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static LongConditionDAOIF get(String oid)
  {
    return (LongConditionDAOIF) BusinessDAO.get(oid);
  }

  public MdFieldDAOIF getDefiningMdFieldDAO()
  {
    return MdFieldDAO.get(this.getAttribute(CharacterConditionInfo.DEFINING_MD_FIELD).getValue());
  }

  @Override
  public boolean evaluate(Mutable instance, HashMap<String, List<Entity>> extraEntities)
  {
    MdFieldDAOIF mdField = this.getDefiningMdFieldDAO();

    if (mdField instanceof MdWebAttributeDAOIF)
    {
      MdAttributeDAOIF mdAttribute = ( (MdWebAttributeDAOIF) mdField ).getDefiningMdAttribute();
      MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();
      String attributeName = mdAttributeConcrete.getValue(MdAttributeConcreteInfo.NAME);

      String actualValue = instance.getValue(attributeName);

      if (actualValue != null && actualValue.length() > 0)
      {
        String expectedValue = this.getAttribute(LongConditionInfo.VALUE).getValue();

        double actual = Long.parseLong(actualValue);
        double expected = Long.parseLong(expectedValue);

        AttributeEnumeration attribute = (AttributeEnumeration) this.getAttribute(LongConditionInfo.OPERATION);
        Set<String> itemIds = attribute.getEnumItemIdList();

        for (String itemId : itemIds)
        {
          EnumerationItemDAOIF item = EnumerationItemDAO.get(itemId);
          Operation operation = Operation.valueOf(item.getName());

          return operation.evaluate(actual, expected);
        }
      }
    }

    return false;
  }
}
