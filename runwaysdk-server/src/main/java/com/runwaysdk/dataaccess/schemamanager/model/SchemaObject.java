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
package com.runwaysdk.dataaccess.schemamanager.model;

import org.xml.sax.Attributes;

import com.runwaysdk.business.generation.view.AbstractViewGenerator;
import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;

/**
 * 
 * Represents elements described by object tag or the relationship tag
 * 
 * @author Aritra
 * 
 */
public class SchemaObject extends KeyedElement implements ElementListener
{
  public SchemaObject(Attributes attributes, String tag)
  {
    super(attributes, tag);
  }

  @Override
  public String toString()
  {
    return this.getKey();
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SchemaObject)
    {
      SchemaObject other = (SchemaObject) obj;
      
      return this.getKey().equals(other.getKey());
    }
    return false;
  }

  public String[] getElementsToObserve()
  {
    String key = this.getKey();

    // IMPORTANT: If this node represesents a controller, then the node is
    // dependent upon the controllers defining MdEntity
    if (key.contains(AbstractViewGenerator.CONTROLLER_SUFFIX))
    {
      return new String[] { this.getXMLAttributeValue(SMXMLTags.TYPE_ATTRIBUTE), key.replace(AbstractViewGenerator.CONTROLLER_SUFFIX, "") };
    }

    return new String[] { this.getXMLAttributeValue(SMXMLTags.TYPE_ATTRIBUTE) };
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      this.remove();
    }
  }
}
