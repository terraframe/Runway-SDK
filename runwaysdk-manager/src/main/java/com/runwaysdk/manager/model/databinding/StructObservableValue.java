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

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.widgets.Widget;

import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.view.StructView;

public class StructObservableValue extends AbstractObservableValue implements ISWTObservableValue
{
  private StructView        view;

  private IComponentObject     currentValue;

  private boolean           updating = false;

  public StructObservableValue(Realm realm, StructView view)
  {
    super(realm);

    this.view = view;
    this.currentValue = view.getEntity();
    this.currentValue.addPropertyChangeListener(null, new PropertyChangeListener()
    {      
      @Override
      public void propertyChange(PropertyChangeEvent evt)
      {
        if (updating)
        {
          return;
        }

        notifyIfChanged(currentValue, currentValue = getCurrentValue());
      }
    });
  }

  @Override
  protected Object doGetValue()
  {
    return view.getEntity();
  }

  @Override
  protected void doSetValue(Object value)
  {
    updating = true;

    try
    {
      this.setCurrentValue((IComponentObject) value);      
    }
    finally
    {
      updating = false;
    }
    
    notifyIfChanged(currentValue, currentValue = this.getCurrentValue());
  }

  protected void notifyIfChanged(Object oldValue, Object newValue)
  {
    if (!Util.equals(oldValue, newValue))
    {
      fireValueChange(Diffs.createValueDiff(oldValue, newValue));
    }
  }

  public Object getValueType()
  {
    return IComponentObject.class;
  }

  public IComponentObject getCurrentValue()
  {
    return view.getEntity();
  }

  public void setCurrentValue(IComponentObject entity)
  {
    view.setEntity(entity);
  }

  @Override
  public Widget getWidget()
  {
    return view.getWidget();
  }
}
