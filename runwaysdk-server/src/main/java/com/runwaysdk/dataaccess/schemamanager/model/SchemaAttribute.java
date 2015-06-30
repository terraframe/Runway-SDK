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

import java.util.Map;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;

/**
 * 
 * @author Aritra
 * 
 */
public class SchemaAttribute extends KeyedElement implements ElementListener
{
  public SchemaAttribute(Attributes attributeMap, String tag, SchemaElementIF parent)
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
    if (obj instanceof SchemaAttribute)
    {
      SchemaAttribute otherAttribute = (SchemaAttribute) obj;
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
    String type = this.getXMLAttributeValue(SMXMLTags.TYPE_ATTRIBUTE);

    if (type != null && type.length() > 0)
    {
      return new String[] { type };
    }

    return new String[] {};
  }

  @Override
  public void addAttributesWithReplacement(Attributes attributes)
  {
    super.addAttributesWithReplacement(attributes);

    this.checkRename();
  }

  @Override
  public void addAttributesWithReplacement(SchemaElementIF element)
  {
    super.addAttributesWithReplacement(element);

    this.checkRename();
  }

  private void checkRename()
  {
    String newName = this.getXMLAttributeValue(XMLTags.RENAME_ATTRIBUTE);
    String name = this.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE);

    if (newName != null && !newName.equals(name))
    {
      String oldKey = this.getKey();

      Map<String, String> _attributes = this.getAttributes();

      _attributes.put(XMLTags.NAME_ATTRIBUTE, newName);
      _attributes.remove(XMLTags.RENAME_ATTRIBUTE);

      String newKey = this.getKey();

      this.fireEvent(new ChangeKeyEvent(this, oldKey, newKey));
    }
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      this.remove();
    }
  }

  public static SchemaAttribute newInstance(Attributes attributes, String tag, SchemaElementIF parent)
  {
    return (SchemaAttribute) SMFactory.newInstance(attributes, tag, parent);
  }
}
