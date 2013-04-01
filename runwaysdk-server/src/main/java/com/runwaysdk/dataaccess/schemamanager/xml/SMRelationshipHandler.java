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

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaRelationship;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaRelationshipParticipant;

public class SMRelationshipHandler extends AbstractSchemaHandler
{

  public SMRelationshipHandler(Attributes attributes, XMLReader reader, AbstractSchemaHandler previousHandler, MergeSchema schema, String tag)
  {
    super(schema, reader, previousHandler, tag, attributes);

  }

  private SchemaRelationshipParticipant importParticipant(Attributes attributes, String tag)
  {
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    // IMPORTANT: The participant type may not be defined in the merged schema
    return schema.createRelationshipParticipant(attributes, tag, type, relationship());
  }

  public SchemaRelationship relationship()
  {
    return (SchemaRelationship) root;
  }

  @Override
  protected void dispatchChildHandler(Attributes attributes, String localName)
  {
    if (localName.equals(XMLTags.PARENT_TAG))
    {
      relationship().setParent(importParticipant(attributes, localName));
    }
    else if (localName.equals(XMLTags.CHILD_TAG))
    {
      relationship().setChild(importParticipant(attributes, localName));
    }
    else if (localName.equals(XMLTags.ATTRIBUTES_TAG) || localName.equals(XMLTags.MD_METHOD_TAG) ||  localName.equals(XMLTags.CREATE_TAG))
    {
      childHandler = SMHandlerFactory.getHandler(schema, reader, this, localName, attributes);
      reader.setContentHandler(childHandler);
      reader.setErrorHandler(childHandler);
    }
  }

  @Override
  protected void initRootElement(Attributes attributes, String localName)
  {
    root = schema().createSchemaRelationship(attributes, localName);
  }

  @Override
  protected void returnToParentHandler(String localName)
  {
    schema.notifyObserversRelationshipCreated((SchemaRelationship) root);
    super.returnToParentHandler(localName);
  }

}
