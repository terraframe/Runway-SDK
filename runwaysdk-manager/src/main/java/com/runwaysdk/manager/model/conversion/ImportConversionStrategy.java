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
package com.runwaysdk.manager.model.conversion;

import java.util.List;

import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.manager.model.object.IEntityObject;

public class ImportConversionStrategy extends ConversionStrategyAdapter implements IConversionStrategy
{
  @Override
  public void populate(IEntityObject source, EntityDAO destination)
  {
    Attribute[] attributes = destination.getAttributeArray();

    for (Attribute attribute : attributes)
    {
      if (attribute instanceof AttributeStruct)
      {
        AttributeStruct attributeStruct = (AttributeStruct) attribute;

        Attribute[] structAttributes = (Attribute[]) attributeStruct.getAttributeArrayIF();

        for (Attribute structAttribute : structAttributes)
        {
          String value = source.getStructValue(attribute.getName(), structAttribute.getName());

          structAttribute.importValue(value);
        }
      }
      else if (attribute instanceof AttributeEnumerationIF)
      {
        AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;

        List<EnumerationItemDAOIF> items = source.getItems(attribute.getName());

        for (EnumerationItemDAOIF item : items)
        {
          attributeEnumeration.addItem(item.getId());
        }
      }
      else
      {
        String value = source.getValue(attribute.getName());

        attribute.importValue(value);
      }

    }
  }
}
