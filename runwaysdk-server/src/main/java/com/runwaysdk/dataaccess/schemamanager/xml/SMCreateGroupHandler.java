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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaObject;

public class SMCreateGroupHandler extends AbstractSchemaHandler
{
  private CreateElement rootElement;

  public SMCreateGroupHandler(XMLReader reader, DefaultHandler previousHandler, MergeSchema schema)
  {
    super(schema, reader, previousHandler, null, null);
    rootElement = schema().addCreateGroup();
    schema.enterCreateState();
  }

  /**
   * Takes actions based on the type of the element obtained from the child
   * handler.
   * 
   */
  private void resolveChildElementAction()
  {
    SchemaElement childElement = (SchemaElement) childHandler.builtElement();

    if (childElement instanceof SchemaObject)
    {
      SchemaObject schemaObject = (SchemaObject) childElement;
      rootElement.addChild(schemaObject);
      schema.addSchemaObject(schemaObject);
    }
    else
    {
      schema().addToCreateElement(childElement);
    }

  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    childHandler = new SMCreateHandlerFactory().getHandler(tag, attributes, reader, this, schema);
    
    if (childHandler != null)
    {
      reader.setContentHandler(childHandler);
      reader.setErrorHandler(childHandler);
    }

  }

  @Override
  protected void initRootElement(Attributes attributes, String localName)
  {
    // TODO Auto-generated method stub

  }

  @Override
  protected void obtainChildElement()
  {
    if (childHandler != null)
    {
      // If the childHandler is not null, it means that
      // this handler had passed controlled to another handler before
      // The model element built by the child handler must be added to the
      // create element of the schema
      resolveChildElementAction();
    }

  }

  @Override
  protected String terminalTag()
  {
    return XMLTags.CREATE_TAG;

  }

}
