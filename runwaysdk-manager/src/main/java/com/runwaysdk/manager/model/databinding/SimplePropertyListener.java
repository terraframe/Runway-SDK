/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.model.databinding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.IDiff;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.eclipse.core.internal.databinding.beans.BeanPropertyListenerSupport;

public abstract class SimplePropertyListener extends NativePropertyListener implements PropertyChangeListener
{
  private final String propertyName;

  protected SimplePropertyListener(IProperty property, ISimplePropertyListener listener, String propertyName)
  {
    super(property, listener);
    
    this.propertyName = propertyName;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName() == null || propertyName.equals(evt.getPropertyName()))
    {
      Object oldValue = evt.getOldValue();
      Object newValue = evt.getNewValue();
      IDiff diff = null;
            
      if (evt.getPropertyName() != null && oldValue != null && newValue != null)
      {
        diff = computeDiff(oldValue, newValue);
      }
      
      fireChange(evt.getSource(), diff);
    }
  }

  protected abstract IDiff computeDiff(Object oldValue, Object newValue);

  protected void doAddTo(Object source)
  {
    BeanPropertyListenerSupport.hookListener(source, propertyName, this);
  }

  protected void doRemoveFrom(Object source)
  {
    BeanPropertyListenerSupport.unhookListener(source, propertyName, this);
  }
}
