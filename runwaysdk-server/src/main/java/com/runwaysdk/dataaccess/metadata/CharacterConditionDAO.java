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
import java.util.TreeSet;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.CharacterConditionDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.MdWebMultipleTermDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;

public class CharacterConditionDAO extends BasicConditionDAO implements CharacterConditionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 5903201176720607502L;

  enum Operation {
    EQ, NEQ;

    public boolean evaluate(String actual, String expected)
    {
      if (this.equals(EQ))
      {
        return actual.equals(expected);
      }

      return !actual.equals(expected);
    }
  }

  /**
   * The default constructor, does not set any attributes
   */
  public CharacterConditionDAO()
  {
    super();
  }

  public CharacterConditionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public CharacterConditionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new CharacterConditionDAO(attributeMap, classType);
  }

  public MdFieldDAOIF getDefiningMdFieldDAO()
  {
    return MdFieldDAO.get(this.getAttribute(CharacterConditionInfo.DEFINING_MD_FIELD).getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public CharacterConditionDAO getBusinessDAO()
  {
    return (CharacterConditionDAO) super.getBusinessDAO();
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

      if (mdField instanceof MdWebMultipleTermDAOIF)
      {
        List<Entity> entities = extraEntities.get(mdField.getOid());

        if (entities != null)
        {
          return this.evaluate(entities);
        }
      }
      else
      {
        String actual = instance.getValue(attributeName);

        if (actual != null && actual.length() > 0)
        {
          return this.evaluate(actual);
        }
      }
    }

    return false;
  }

  /**
   * IMPORTANT: I am making the assumption that multi-term values are stored as
   * relationships, and that the list of entities associated with this field is
   * actually a list of relationships where the child oid is the value which
   * should be checked for condition validation. Additionally, the equals
   * condition is treated as a contains condition and not equals is treated as
   * not-contains.
   * 
   * @param entities
   * @return
   */
  private boolean evaluate(List<Entity> entities)
  {
    String expected = this.getAttribute(CharacterConditionInfo.VALUE).getValue();

    Set<String> values = new TreeSet<String>();

    for (Entity entity : entities)
    {
      Relationship relationship = (Relationship) entity;
      values.add(relationship.getChildOid());
    }

    Operation operation = this.getOperation();

    if (operation != null && operation.equals(Operation.EQ))
    {
      return values.contains(expected);
    }
    else if (operation != null && operation.equals(Operation.NEQ))
    {
      return !values.contains(expected);
    }

    return false;
  }

  private boolean evaluate(String value)
  {
    String expected = this.getAttribute(CharacterConditionInfo.VALUE).getValue();

    Operation operation = getOperation();

    if (operation != null)
    {
      return operation.evaluate(value, expected);
    }

    return false;
  }

  private Operation getOperation()
  {
    AttributeEnumeration attribute = (AttributeEnumeration) this.getAttribute(CharacterConditionInfo.OPERATION);
    Set<String> itemIds = attribute.getEnumItemIdList();

    for (String itemId : itemIds)
    {
      EnumerationItemDAOIF item = EnumerationItemDAO.get(itemId);

      return Operation.valueOf(item.getName());
    }

    return null;
  }

  /**
   * @return
   */
  public static CharacterConditionDAO newInstance()
  {
    return (CharacterConditionDAO) BusinessDAO.newInstance(CharacterConditionInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static CharacterConditionDAOIF get(String oid)
  {
    return (CharacterConditionDAOIF) BusinessDAO.get(oid);
  }
}
