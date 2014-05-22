/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;

public class SMCreateElementHandler extends UnKeyedElementHandler
{

  public SMCreateElementHandler(MergeSchema schema, XMLReader reader, AbstractSchemaHandler previousHandler)
  {
    super(schema, reader, previousHandler, (String) null, (Attributes) null);

    schema().enterCreateState();
  }

  @Override
  protected void initRootElement(Attributes attributes, String tag)
  {
    SchemaElementIF parent = ( (AbstractSchemaHandler) parentHandler ).builtElement();

    // IMPORTANT: if this CREATE element is inside of another CREATE elment then
    // do not add the CREATE element to the model.
    if (SchemaElement.getAncestor(parent, CreateElement.class) == null)
    {
      root = new CreateElement(parent);
    }
  }
  
  @Override
  public SchemaElementIF builtElement()
  {
    return ( root != null ) ? root : ( (AbstractSchemaHandler) parentHandler ).builtElement();
  }

  @Override
  protected void obtainChildElement()
  {
    if (childHandler != null)
    {
      SchemaElementIF element = ( root != null ) ? root : ( (AbstractSchemaHandler) parentHandler ).builtElement();

      // Add the most recently created child to the children set of this element
      SchemaElement childElement = (SchemaElement) childHandler.builtElement();
      element.addChild(childElement);
    }
  }

  protected String terminalTag()
  {
    return SMXMLTags.CREATE_TAG;
  }
}
