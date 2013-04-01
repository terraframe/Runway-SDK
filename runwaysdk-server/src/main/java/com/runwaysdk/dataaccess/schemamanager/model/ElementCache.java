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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ElementCache implements ElementListener
{
  /**
   * Key-Element pairings
   */
  private Map<String, SchemaElementIF>    elementCache;

  /**
   * Key-SchemaObject pairings. It is possible to have many SchemaObjects with
   * the same key.
   */
  private Map<String, List<SchemaObject>> objectCache;

  public ElementCache()
  {
    this.elementCache = new HashMap<String, SchemaElementIF>();
    this.objectCache = new HashMap<String, List<SchemaObject>>();
  }

  public boolean containsKey(String key)
  {
    SchemaElementIF element = elementCache.get(key);

    if (element == null || element instanceof NullElement)
    {
      return false;
    }

    return true;
  }

  public SchemaElementIF get(String key)
  {
    if (!elementCache.containsKey(key))
    {
      elementCache.put(key, new NullElement(key));
    }

    return elementCache.get(key);
  }

  public void remove(String key)
  {
    elementCache.remove(key);
  }

  public void put(String key, SchemaElementIF element)
  {
    SchemaElementIF _element = elementCache.get(key);

    if (_element != null && _element instanceof NullElement)
    {
      ( (NullElement) _element ).registerListeners(element);
    }

    element.registerListener(this);

    elementCache.put(key, element);
  }

  public void putSchemaObject(String key, SchemaObject object)
  {
    if(!objectCache.containsKey(key))
    {
      objectCache.put(key, new LinkedList<SchemaObject>());
    }
    
    object.registerListener(this);
    
    objectCache.get(key).add(object);
  }
  
  public List<SchemaObject> getSchemaObjects(String key)
  {
    return new LinkedList<SchemaObject>(objectCache.get(key));
  }
  
  public boolean containsSchemaObject(String key)
  {
    return objectCache.containsKey(key);
  }

  
  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      elementCache.remove(event.getSource().getKey());
      objectCache.remove(event.getSource().getKey());
    }
    else if (event instanceof ChangeKeyEvent)
    {
      ChangeKeyEvent keyEvent = (ChangeKeyEvent) event;

      elementCache.put(keyEvent.getNewKey(), keyEvent.getSource());
      elementCache.remove(keyEvent.getOldKey());
    }
  }

}
