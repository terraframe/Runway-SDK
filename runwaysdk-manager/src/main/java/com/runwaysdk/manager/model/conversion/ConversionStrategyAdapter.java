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
package com.runwaysdk.manager.model.conversion;

import java.util.List;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.manager.model.object.IEntityObject;

public class ConversionStrategyAdapter implements IConversionStrategy
{

  @Override
  public void populate(EntityDAOIF source, IEntityObject dest)
  {
    for (AttributeIF attribute : source.getAttributeArrayIF())
    {
      if (attribute instanceof AttributeStructIF)
      {
        AttributeStructIF attributeStruct = (AttributeStructIF) attribute;
        String structAttributeName = attributeStruct.getName();

        for (AttributeIF structAttribute : attributeStruct.getAttributeArrayIF())
        {
          dest.setStructValue(structAttributeName, structAttribute.getName(), structAttribute.getValue());
        }
      }
      else if (attribute instanceof AttributeEnumerationIF)
      {
        AttributeEnumerationIF attributeEnumeration = (AttributeEnumerationIF) attribute;

        Set<String> items = attributeEnumeration.getCachedEnumItemIdSet();

        dest.setItems(attribute.getName(), items);
      }
      else
      {
        dest.setValue(attribute.getName(), attribute.getValue());
      }
    }
  }

  @Override
  public EntityDAO populate(IEntityObject source)
  {
    EntityDAO entity = source.instance();

    this.populate(source, entity);

    return entity;
  }

  @Override
  public void populate(IEntityObject source, EntityDAO destination)
  {
    Attribute[] attributes = destination.getAttributeArray();

    for (Attribute attribute : attributes)
    {
      if (!attribute.getName().equals(EntityInfo.ID))
      {
        if (attribute instanceof AttributeStruct)
        {
          AttributeStruct attributeStruct = (AttributeStruct) attribute;

          Attribute[] structAttributes = (Attribute[]) attributeStruct.getAttributeArrayIF();

          for (Attribute structAttribute : structAttributes)
          {
            String value = source.getStructValue(attribute.getName(), structAttribute.getName());

            destination.setStructValue(attribute.getName(), structAttribute.getName(), value);
          }
        }
        else if (attribute instanceof AttributeEnumerationIF)
        {
          List<EnumerationItemDAOIF> items = source.getItems(attribute.getName());

          for (EnumerationItemDAOIF item : items)
          {
            destination.addItem(attribute.getName(), item.getId());
          }
        }
        else
        {
          String value = source.getValue(attribute.getName());

          destination.setValue(attribute.getName(), value);
        }
      }
    }
  }

}
