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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.EnumItemModification;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.NullElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaEnumeration;

public class SMEnumerationHandler extends AbstractSchemaHandler
{

  public SMEnumerationHandler(Attributes attributes, XMLReader reader, AbstractSchemaHandler previousHandler, MergeSchema schema, String localName)
  {
    super(schema, reader, previousHandler, localName, attributes);

  }

  protected void initRootElement(Attributes attributes, String localName)
  {
    if (schema().isCreateState())
    {
      root = schema().createSchemaEnumeration(attributes, localName);
    }
    else if (schema().isUpdateState())
    {
      root = (SchemaEnumeration) schema().getElement(attributes.getValue(XMLTags.NAME_ATTRIBUTE));
      
      if (root != null || root instanceof NullElement)
      {
        root.addAttributesWithReplacement(attributes);
      }
      else
      {
        throw new SourceElementNotDeclaredException(new SchemaEnumeration(attributes, localName), new String());
      }
    }

  }

  @Override
  public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.ADD_ENUM_ITEM_TAG))
    {
      EnumItemModification item = new EnumItemModification(attributes, localName);
      enumeration().addEnumItemAddition(item);
    }

    else if (localName.equals(XMLTags.REMOVE_ENUM_ITEM_TAG))
    {
      EnumItemModification item = new EnumItemModification(attributes, localName);
      enumeration().addEnumItemDeletion(item);
    }

    else if (localName.equals(XMLTags.INCLUDEALL_TAG))
    {
      enumeration().setIncludeAll();
    }
  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    // TODO Auto-generated method stub

  }

  @Override
  protected void obtainChildElement()
  {
    // TODO Auto-generated method stub

  }

  public SchemaEnumeration enumeration()
  {
    return (SchemaEnumeration) root;
  }

}
