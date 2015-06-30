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
package com.runwaysdk.dataaccess.schemamanager.model;

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;
import com.runwaysdk.dataaccess.schemamanager.xml.UnKeyedElement;

/**
 * 
 * Represents the mdIndex tag
 * 
 * @author Aritra
 * 
 */
public class SchemaIndex extends UnKeyedElement implements ElementListener
{
  private HashSet<IndexAttribute> indexAttributes;

  public SchemaIndex(Attributes map, String tag, SchemaElementIF parent)
  {
    super(map, tag, parent);

    indexAttributes = new HashSet<IndexAttribute>();
  }

  public Set<IndexAttribute> indexAttributes()
  {
    return indexAttributes;
  }

  public void addIndexAttribute(IndexAttribute attribute)
  {
    indexAttributes.add(attribute);
  }

  public boolean containsIndexAttribute(String attrName)
  {
    return indexAttributes.contains(attrName);
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  public String[] indexAttributesNamesArray()
  {
    String[] attributeNames = new String[indexAttributes.size()];
    int i = 0;
    for (IndexAttribute attribute : indexAttributes)
    {
      attributeNames[i++] = attribute.getXMLAttributeValue(XMLTags.INDEX_NAME_ATTRIBUTE);
    }
    return attributeNames;
  }

  public String[] getElementsToObserve()
  {
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
  
  @Override
  public boolean equals(Object obj)
  {
    return false;
  }
}
