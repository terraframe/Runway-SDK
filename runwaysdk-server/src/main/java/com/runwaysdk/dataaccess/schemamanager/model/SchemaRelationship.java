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
package com.runwaysdk.dataaccess.schemamanager.model;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;

public class SchemaRelationship extends SchemaClass implements ElementListener
{
  private SchemaRelationshipParticipant parent;

  private SchemaRelationshipParticipant child;

  public SchemaRelationship(Attributes attributeMap, String tag)
  {
    super(attributeMap, tag);
  }

  public void setParent(SchemaRelationshipParticipant parent)
  {
    this.parent = parent;
  }

  public void setChild(SchemaRelationshipParticipant child)
  {
    this.child = child;
  }

  public String renderedName()
  {
    return getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE);
  }

  public SchemaRelationshipParticipant relationshipParent()
  {
    return parent;
  }

  public SchemaRelationshipParticipant relationshipChild()
  {
    return child;
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }
  
  public String[] getElementsToObserve()
  {    
    return new String[]{parent.getType(), child.getType()};
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if(event instanceof DeleteEvent)
    {
      this.remove();
    }
  }
}
