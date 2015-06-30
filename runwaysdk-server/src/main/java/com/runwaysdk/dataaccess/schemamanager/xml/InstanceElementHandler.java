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
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaObject;

public class InstanceElementHandler extends AbstractSchemaHandler
{
  public InstanceElementHandler(Attributes attributes, XMLReader reader, AbstractSchemaHandler previousHandler, MergeSchema schema, String tag)
  {
    super(schema, reader, previousHandler, tag, attributes);
  }

  @Override
  protected void initRootElement(Attributes attributes, String tag)
  {
    root = new SchemaObject(attributes, tag);
    schema.registerDependency(root);
    schema.enterCreateState();
  }

  public SchemaObject instanceElement()
  {
    return (SchemaObject) root;
  }

  /**
   * IMPORTANT: All children of an instance element must be UnKeyedElements
   * otherwise they must be added to the UnKeyed tag in SMXMLTags.
   */
  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    childHandler = SMHandlerFactory.getUnKeyedElementHandler(schema, reader, this, tag, attributes);
    reader.setContentHandler(childHandler);
    reader.setErrorHandler(childHandler);
  }
}
