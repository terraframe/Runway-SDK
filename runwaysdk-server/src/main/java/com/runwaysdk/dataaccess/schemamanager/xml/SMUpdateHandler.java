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
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaObject;

public class SMUpdateHandler extends AbstractSchemaHandler
{
  public SMUpdateHandler(XMLReader reader, DefaultHandler previousHandler, MergeSchema schema)
  {
    super(schema, reader, previousHandler);

    root = schema().addUpdateGroup();

    schema().enterUpdateState();
  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    try
    {
      childHandler = new SMUpdateHandlerFactory().getHandler(tag, attributes, reader, this, schema);

      if (childHandler != null)
      {
        reader.setContentHandler(childHandler);
        reader.setErrorHandler(childHandler);
      }
    }
    catch (SourceElementNotDeclaredException exception)
    {
      SchemaElementIF partialElement = schema().createPartialSchemaElement(exception.element());

      childHandler = new SMKeyedElementHandler(schema(), reader, this, partialElement);
      reader.setContentHandler(childHandler);
      reader.setErrorHandler(childHandler);
    }

  }

  @Override
  protected void initRootElement(Attributes attributes, String qName)
  {
    // TODO Auto-generated method stub

  }

  @Override
  protected void obtainChildElement()
  {
    if (childHandler != null)
    {
      resolveChildAction();
    }

  }

  private void resolveChildAction()
  {
    SchemaElement childElement = (SchemaElement) childHandler.builtElement();

    if (childElement instanceof SchemaObject)
    {
      SchemaObject schemaObject = (SchemaObject) childElement;
      root.addChild(schemaObject);
      schema.addSchemaObject(schemaObject);
    }
  }

  @Override
  protected String terminalTag()
  {
    return XMLTags.UPDATE_TAG;
  }

}
