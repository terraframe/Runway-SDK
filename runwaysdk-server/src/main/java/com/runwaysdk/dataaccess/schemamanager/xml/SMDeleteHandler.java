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

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaObject;

/**
 * Handles delete tag
 * 
 * @author Aritra
 * 
 */
public class SMDeleteHandler extends AbstractSchemaHandler
{
  protected UnKeyedElement rootElement;

  public SMDeleteHandler(XMLReader reader, DefaultHandler previousHandler, MergeSchema schema)
  {
    super(schema, reader, previousHandler, null, null);

    schema().enterDeleteState();

    rootElement = schema().addDeleteGroup();
  }

  @Override
  protected void obtainChildElement()
  {
    if (childHandler != null)
    {
      SchemaObject element = (SchemaObject) childHandler.builtElement();
      String key = element.getKey();

      // If the instance element refers to some existing element in the model,
      // delete it. Otherwise include it in the instance element group.
      if (schema.containsElement(key))
      {
        schema.getElement(key).remove();
      }
      else if (schema.containsSchemaObjects(key))
      {
        List<SchemaObject> objects = schema.getSchemaObjects(key);
        
        for(SchemaObject object : objects)
        {
          object.remove();
        }
      }
      else if (!schema.wasDeleted(element))
      {
        schema.registerDependency(element);

        rootElement.addChild(element);
      }
    }

  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    childHandler = new InstanceElementHandler(attributes, reader, this, schema(), tag);

    reader.setErrorHandler(childHandler);
    reader.setContentHandler(childHandler);
  }

  @Override
  protected void initRootElement(Attributes attributes, String localName)
  {
    // TODO Auto-generated method stub

  }

  @Override
  protected String terminalTag()
  {
    return XMLTags.DELETE_TAG;
  }

}
