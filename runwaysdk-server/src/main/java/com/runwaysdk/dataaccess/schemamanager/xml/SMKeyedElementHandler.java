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

import com.runwaysdk.dataaccess.schemamanager.model.KeyedElement;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;

/**
 * 
 * Handles {@link KeyedElement}s in the xml schemas
 * 
 * @author Aritra
 * 
 */
public class SMKeyedElementHandler extends AbstractSchemaHandler
{

  public SMKeyedElementHandler(MergeSchema schema, XMLReader reader, AbstractSchemaHandler previousHandler, String tag, Attributes attributes)
  {
    super(schema, reader, previousHandler, tag, attributes);
  }

  public SMKeyedElementHandler(MergeSchema schema, XMLReader reader, AbstractSchemaHandler previousHandler, SchemaElementIF rootElement)
  {
    super(schema, reader, previousHandler, rootElement);
  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    childHandler = SMHandlerFactory.getHandler(schema, reader, this, tag, attributes);
    reader.setContentHandler(childHandler);
    reader.setErrorHandler(childHandler);
  }

  /**
   * Initializes the root model element being processed by this handler.
   * Depending on the state of the import either it creates a new element or it
   * retrieves the existing element.
   * 
   */
  @Override
  protected void initRootElement(Attributes attributes, String tag)
  {
    SchemaElementIF parentElement = ( (AbstractSchemaHandler) parentHandler ).builtElement();

    KeyedElement element = KeyedElement.newInstance(attributes, tag, parentElement);

    // If the parsing is within a top level create tag, and not within a create
    // tag that appears inside another update, then the model element never
    // existed before, and therefore has to be created.

    if (schema().isTopLevelCreate())
    {
      root = schema.createKeyedElement(element);
    }

    // If the parsing is within update tag or within a create tag inside another
    // update, or with permission tag then there is a possibility that the
    // element existed before.
    else if (schema().isUpdateState() || schema().isCreateWithinUpdate() || schema().isPermissionState())
    {
      root = schema.createKeyedElement(element);
    }
    else
    {
      root = null;
    }
  }
}
