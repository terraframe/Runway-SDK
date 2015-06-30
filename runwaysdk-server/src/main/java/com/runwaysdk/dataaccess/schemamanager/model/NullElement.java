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
import java.util.Set;

import org.xml.sax.Attributes;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;

/**
 * This is a placeholder element in which to register listeners in case an
 * actual element of this key is added.
 * 
 * @author jsmethie
 */
public class NullElement extends ElementObservable implements KeyedElementIF
{
  private String key;

  public NullElement(String key)
  {
    this.key = key;
  }

  @Override
  public void addAttributesWithReplacement(Attributes attributes)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public void addAttributesWithReplacement(SchemaElementIF element)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public boolean addChild(SchemaElementIF child)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public void addChildren(Set<SchemaElementIF> children)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public Set<SchemaElementIF> children()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public Map<String, String> getAttributes()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public SchemaElementIF getChild(SchemaElementIF child)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public SchemaElementIF getChild(Attributes attributes, String tag)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public SchemaElementIF getChild(String tag, String key)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public SchemaElementIF getChild(String tag)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public String[] getElementsToObserve()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public String getKey()
  {
    return key;
  }

  @Override
  public SchemaElementIF getParent()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public String getTag()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public String getXMLAttributeValue(String localName)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public XSType getXSType(XSSchemaSet schemaSet)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public boolean hasChildren()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public boolean isEmpty()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public boolean isMerged()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public void remove()
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public void removeChild(SchemaElementIF child)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public void setParent(SchemaElementIF parent)
  {
    throw new RuntimeException("Method should never be invoked");
  }

  @Override
  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    // Do nothing
  }
}
