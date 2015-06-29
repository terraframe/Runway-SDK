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

import java.util.LinkedList;
import java.util.List;

public abstract class ElementObservable implements SchemaElementIF
{
  private List<ElementListener> listeners;

  public ElementObservable()
  {
    this.listeners = new LinkedList<ElementListener>();
  }

  public void registerListener(ElementListener listener)
  {
    listeners.add(listener);
  }

  public void unregisterListener(ElementListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Registers all of this elements listeners to the passed in ElementObservable
   * 
   * @param observable
   */
  protected void registerListeners(SchemaElementIF observable)
  {
    for (ElementListener listener : listeners)
    {
      observable.registerListener(listener);
    }
  }

  protected void fireEvent(ElementEvent event)
  {
    for (ElementListener listener : listeners)
    {
      listener.handleEvent(event);
    }
  }
}
