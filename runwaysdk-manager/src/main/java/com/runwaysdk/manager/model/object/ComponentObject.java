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
package com.runwaysdk.manager.model.object;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.manager.model.ComponentPropertyHolder;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.attributes.SimpleAttribute;
import com.runwaysdk.manager.model.attributes.SimpleAttributeEnumeration;
import com.runwaysdk.manager.model.attributes.SimpleAttributeStruct;
import com.runwaysdk.query.EntityQuery;

public abstract class ComponentObject extends ComponentPropertyHolder implements IComponentObject
{
  private MdEntityDAOIF                    mdEntity;

  private HashMap<String, SimpleAttribute> attributes;

  public ComponentObject(MdEntityDAOIF mdEntity)
  {
    this(mdEntity, new HashMap<String, SimpleAttribute>());
  }

  public ComponentObject(MdEntityDAOIF mdEntity, HashMap<String, SimpleAttribute> attributes)
  {
    this.mdEntity = mdEntity;
    this.attributes = attributes;
  }

  public Set<String> getAttributeNames()
  {
    return this.attributes.keySet();
  }

  @Override
  public MdAttributeDAOIF getMdAttributeDAO(String attributeName)
  {
    MdEntityDAOIF mdEntity = this.getMdClassDAO();

    return PersistanceFacade.getMdAttributeDAO(mdEntity, attributeName);
  }

  @Override
  public MdEntityDAOIF getMdClassDAO()
  {
    return mdEntity;
  }

  @Override
  public String getKey()
  {
    return this.getValue(EntityInfo.KEY);
  }

  @Override
  public String getId()
  {
    return this.getValue(EntityInfo.ID);
  }

  @Override
  public String getValue(String attributeName)
  {
    SimpleAttribute attribute = attributes.get(attributeName);

    if (attribute != null)
    {
      return attribute.getValue();
    }

    return null;
  }

  @Override
  public void setValue(String key, String value)
  {
    String oldValue = this.getValue(key);

    attributes.put(key, new SimpleAttribute(value));

    this.firePropertyChange(key, oldValue, value);
  }

  @Override
  public List<EnumerationItemDAOIF> getItems(String attributeName)
  {
    SimpleAttribute attribute = this.attributes.get(attributeName);

    if (attribute != null && attribute instanceof SimpleAttributeEnumeration)
    {
      SimpleAttributeEnumeration attributeEnumeration = (SimpleAttributeEnumeration) attribute;

      return attributeEnumeration.dereference();
    }

    return new LinkedList<EnumerationItemDAOIF>();
  }

  @Override
  public void setItems(String attributeName, Set<String> ids)
  {
    this.attributes.put(attributeName, new SimpleAttributeEnumeration(ids));
  }

  protected SimpleAttributeStruct getAttributeStruct(String structAttributeName)
  {
    if (!attributes.containsKey(structAttributeName))
    {
      attributes.put(structAttributeName, new SimpleAttributeStruct());
    }

    return (SimpleAttributeStruct) this.attributes.get(structAttributeName);
  }

  @Override
  public String getStructValue(String structAttributeName, String attributeName)
  {
    return this.getAttributeStruct(structAttributeName).getValue(attributeName);
  }

  @Override
  public void setStructItem(String structName, String attributeName, Set<String> ids)
  {
    this.getAttributeStruct(structName).setItems(attributeName, ids);
  }

  @Override
  public List<EnumerationItemDAOIF> getStructItems(String structName, String attributeName)
  {
    return this.getAttributeStruct(structName).getItems(attributeName);
  }

  @Override
  public void setStructValue(String structAttributeName, String attributeName, String value)
  {
    this.getAttributeStruct(structAttributeName).setValue(attributeName, value);
  }

  @Override
  public EntityQuery getQuery()
  {
    return mdEntity.getEntityQuery();
  }

  @Override
  public String getType()
  {
    return mdEntity.definesType();
  }

  @Override
  public IComponentObject getStruct(String attributeName)
  {
    MdAttributeStructDAOIF mdAttribute = (MdAttributeStructDAOIF) this.getMdAttributeDAO(attributeName);
    MdStructDAOIF mdStruct = mdAttribute.getMdStructDAOIF();

    SimpleAttributeStruct attributeStruct = this.getAttributeStruct(attributeName);

    return new StructObject(mdStruct, attributeStruct.getAttributes());
  }
}
