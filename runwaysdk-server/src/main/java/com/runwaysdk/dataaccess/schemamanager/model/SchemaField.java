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

import java.util.Map;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;

public class SchemaField extends KeyedElement implements ElementListener
{
  public SchemaField(Attributes attributeMap, String tag, SchemaElementIF parent)
  {
    super(attributeMap, tag, parent);
  }

  @Override
  public String toString()
  {
    return this.getKey();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SchemaField)
    {
      SchemaField otherAttribute = (SchemaField) obj;
      return this.getKey().equals(otherAttribute.getKey());
    }
    return false;
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  public String getKey()
  {
    return this.accept(new KeyResolver());
  }

  public String[] getElementsToObserve()
  {
    SchemaClass ancestor = SchemaElement.getAncestor(this, SchemaClass.class);

    String classType = ancestor.getXMLAttributeValue(SMXMLTags.FORM_MD_CLASS);
    String attributeName = this.getXMLAttributeValue(SMXMLTags.MD_ATTRIBUTE);

    return new String[] { MdAttributeConcreteDAO.buildKey(classType, attributeName) };
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      this.remove();
    }
    else if (event instanceof ChangeKeyEvent)
    {
      SchemaElementIF source = event.getSource();

      // Attribute has changed its name, this node must correspondling change
      // its reference to that mdAttribute
      if (source instanceof SchemaAttribute)
      {
        String newKey = ((ChangeKeyEvent)event).getNewKey();

        String[] split = newKey.split("\\.");
        String newName = split[split.length - 1];

        Map<String, String> attributes = this.getAttributes();

        attributes.put(XMLTags.MD_ATTRIBUTE, newName);
      }
    }
  }

  public static SchemaField newInstance(Attributes attributes, String tag, SchemaElementIF parent)
  {
    return (SchemaField) SMFactory.newInstance(attributes, tag, parent);
  }
}
