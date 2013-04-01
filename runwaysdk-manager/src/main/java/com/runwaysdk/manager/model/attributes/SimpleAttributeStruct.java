/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.model.attributes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.dataaccess.EnumerationItemDAOIF;

public class SimpleAttributeStruct extends SimpleAttribute
{
  private HashMap<String, SimpleAttribute> attributes;

  public SimpleAttributeStruct()
  {
    super();

    this.attributes = new HashMap<String, SimpleAttribute>();
  }

  public Set<String> getAttributeNames()
  {
    return this.attributes.keySet();
  }

  public String getValue(String attributeName)
  {
    SimpleAttribute attribute = attributes.get(attributeName);

    if (attribute != null)
    {
      return attribute.getValue();
    }

    return null;
  }

  public void setValue(String key, String value)
  {
    attributes.put(key, new SimpleAttribute(value));
  }

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

  public void setItems(String attributeName, Set<String> ids)
  {
    this.attributes.put(attributeName, new SimpleAttributeEnumeration(ids));
  }

  public HashMap<String, SimpleAttribute> getAttributes()
  {
    return attributes;
  }
}
