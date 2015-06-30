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

public interface SchemaElementIF extends SchemaVisitable, ElementListener
{
  public void addAttributesWithReplacement(Attributes attributes);

  public void addAttributesWithReplacement(SchemaElementIF element);

  public Map<String, String> getAttributes();

  public String getXMLAttributeValue(String localName);

  public Set<SchemaElementIF> children();

  public boolean addChild(SchemaElementIF child);

  public boolean isEmpty();

  public String getTag();

  public SchemaElementIF getChild(SchemaElementIF child);

  public SchemaElementIF getChild(Attributes attributes, String tag);

  public SchemaElementIF getChild(String tag, String key);

  public SchemaElementIF getChild(String tag);

  public void removeChild(SchemaElementIF child);

  public boolean hasChildren();

  public XSType getXSType(XSSchemaSet schemaSet);

  public void addChildren(Set<SchemaElementIF> children);

  public boolean isMerged();

  public void remove();

  /**
   * @return The key of which this object is interested in observing
   */
  public String[] getElementsToObserve();

  public void registerListener(ElementListener listener);

  public void unregisterListener(ElementListener listener);

  public SchemaElementIF getParent();

  public void setParent(SchemaElementIF parent);
}
