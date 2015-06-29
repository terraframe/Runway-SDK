/**
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
 */
package com.runwaysdk.dataaccess.schemamanager.model;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;

/**
 * Represents the mdEnumeration tag
 * 
 * @author runway
 * 
 */
public class SchemaEnumeration extends KeyedElement implements ElementListener
{
  private SchemaClass               master;

  private boolean                   doesIncludeAll = false;

  private Set<EnumItemModification> enumItemAdditions;

  private Set<EnumItemModification> enumItemDeletions;

  public SchemaEnumeration(Attributes attributes, String tag)
  {
    super(attributes, tag);

    enumItemAdditions = new LinkedHashSet<EnumItemModification>();
    enumItemDeletions = new LinkedHashSet<EnumItemModification>();
  }

  public void addEnumItemAddition(EnumItemModification item)
  {
    doesIncludeAll = false;
    boolean cancelled = false;

    Iterator<EnumItemModification> existinRemoveItemsIterator = enumItemDeletions.iterator();

    while (existinRemoveItemsIterator.hasNext())
    {
      if (existinRemoveItemsIterator.next().getKey().equals(item.getKey()))
      {
        existinRemoveItemsIterator.remove();
        cancelled = true;
        break;
      }
    }

    if (!cancelled)
    {
      enumItemAdditions.add(item);
    }
  }

  public void addEnumItemDeletion(EnumItemModification item)
  {
    doesIncludeAll = false;
    boolean cancelled = false;

    Iterator<EnumItemModification> existingAddItemsIterator = enumItemAdditions.iterator();

    while (existingAddItemsIterator.hasNext())
    {
      if (existingAddItemsIterator.next().getKey().equals(item.getKey()))
      {
        existingAddItemsIterator.remove();
        cancelled = true;
        break;
      }
    }

    if (!cancelled)
    {
      enumItemDeletions.add(item);
    }
  }

  public void setIncludeAll()
  {
    this.doesIncludeAll = true;
  }

  public SchemaClass master()
  {
    return master;
  }

  public boolean doesIncludeAll()
  {
    return doesIncludeAll;
  }

  public Set<EnumItemModification> enumItemAdditions()
  {
    return enumItemAdditions;
  }

  public Set<EnumItemModification> enumItemDeletions()
  {
    return enumItemDeletions;
  }

  public Set<EnumItemModification> enumItemChanges()
  {
    Set<EnumItemModification> items = new LinkedHashSet<EnumItemModification>();
    items.addAll(enumItemAdditions);
    items.addAll(enumItemDeletions);
    return items;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SchemaEnumeration)
    {
      SchemaEnumeration element = (SchemaEnumeration) obj;
      return this.getKey().equals(element.getKey());
    }
    return false;
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
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
}
