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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.math.BigDecimal;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.io.ImportManager;

public class AttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public AttributeHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java
   * .lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    Object obj = context.getObject(EntityInfo.CLASS);

    if (obj instanceof EntityDAO)
    {
      EntityDAO entity = (EntityDAO) obj;

      String structAttributeName = (String) context.getObject(StructAttributeHandler.STRUCT_ATTRIBUTE_NAME);

      String attributeName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
      String value = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_VALUE_ATTRIBUTE);

      if (structAttributeName != null)
      {
        entity.setStructValue(structAttributeName, attributeName, value);
      }
      else
      {
        entity.setValue(attributeName, value);
      }
    }
    else if (obj instanceof GraphObjectDAO)
    {
      GraphObjectDAO entity = (GraphObjectDAO) obj;

      String structAttributeName = (String) context.getObject(StructAttributeHandler.STRUCT_ATTRIBUTE_NAME);

      String attributeName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
      String value = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_VALUE_ATTRIBUTE);

      if (structAttributeName != null)
      {
        GraphObjectDAO embeddedComponentDAO = (GraphObjectDAO) entity.getEmbeddedComponentDAO(structAttributeName);
        MdAttributeConcreteDAOIF mdAttribute = embeddedComponentDAO.getMdAttributeDAO(attributeName);

        embeddedComponentDAO.setValue(attributeName, this.convertToTypeSafeValue(value, mdAttribute));
      }
      else
      {
        MdAttributeConcreteDAOIF mdAttribute = entity.getMdAttributeDAO(attributeName);

        entity.setValue(attributeName, this.convertToTypeSafeValue(value, mdAttribute));
      }
    }
  }

  private Object convertToTypeSafeValue(String value, MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeFloatDAOIF)
    {
      return Float.valueOf(value);
    }
    else if (mdAttribute instanceof MdAttributeDoubleDAOIF)
    {
      return Double.valueOf(value);
    }
    else if (mdAttribute instanceof MdAttributeBooleanDAOIF)
    {
      return Boolean.valueOf(value);
    }
    else if (mdAttribute instanceof MdAttributeLongDAOIF)
    {
      return Long.valueOf(value);
    }
    else if (mdAttribute instanceof MdAttributeIntegerDAOIF)
    {
      return Integer.valueOf(value);
    }
    else if (mdAttribute instanceof MdAttributeDecimalDAOIF)
    {
      return new BigDecimal(value);
    }

    return value;
  }
}
