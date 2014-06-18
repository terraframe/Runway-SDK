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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.RadioGroup;

public class RadioGroupObservableValue extends AbstractObservableValue implements SelectionListener
{
  private final RadioGroup group;

  private Object           selection = null;

  public RadioGroupObservableValue(Realm realm, RadioGroup group)
  {
    super(realm);

    this.group = group;

    group.addComponentSelectionListener(this);
  }

  
  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.databinding.observable.value.AbstractObservableValue#dispose
   * ()
   */
  public synchronized void dispose()
  {
    group.removeComponentSelectionListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.core.databinding.observable.value.AbstractObservableValue#
   * doSetValue(java.lang.Object)
   */
  protected void doSetValue(Object value)
  {
    group.setSelection(value);
    selection = value;
  }

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.core.databinding.observable.value.AbstractObservableValue#
   * doGetValue()
   */
  protected Object doGetValue()
  {
    return group.getSelection();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.databinding.observable.value.IObservableValue#getValueType
   * ()
   */
  public Object getValueType()
  {
    return String.class;
  }

  public void widgetDefaultSelected(SelectionEvent e)
  {
    widgetSelected(e);
  }

  public void widgetSelected(SelectionEvent e)
  {
    final Object newSelection = group.getSelection();

    fireValueChange(new ValueDiff()
    {
      public Object getNewValue()
      {
        return newSelection;
      }

      public Object getOldValue()
      {
        return selection;
      }
    });

    selection = newSelection;
  }

}
