/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.IndexAttribute;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaIndex;

public class SchemaIndexHandler extends AbstractSchemaHandler
{

  public SchemaIndexHandler(Attributes attributes, XMLReader reader, DefaultHandler parentHandler, MergeSchema schema, String tag)
  {
    super(schema, reader, parentHandler, tag, attributes);
  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    if (tag.equals(XMLTags.INDEX_ATTRIBUTE_TAG))
    {
      SchemaIndex index = (SchemaIndex) root;
      index.addIndexAttribute(new IndexAttribute(attributes, tag));
    }
  }
  
  @Override
  protected void initRootElement(Attributes attributes, String localName)
  {
    root = schema.createSchemaIndex(attributes, localName);
  }

  @Override
  protected void obtainChildElement()
  {
    // TODO Auto-generated method stub

  }
}
