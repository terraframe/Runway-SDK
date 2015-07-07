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
package com.runwaysdk.manager.model.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class ComboViewerObservableValue extends AbstractObservableValue implements ISelectionChangedListener
{
  private final ComboViewer viewer;

  private Object            selection = null;

  public ComboViewerObservableValue(Realm realm, ComboViewer viewer)
  {
    super(realm);

    this.viewer = viewer;

    this.viewer.addSelectionChangedListener(this);
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
    this.viewer.removeSelectionChangedListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.core.databinding.observable.value.AbstractObservableValue#
   * doSetValue(java.lang.Object)
   */
  protected void doSetValue(Object value)
  {
    this.viewer.setSelection((ISelection) value);
    
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
    ISelection current = this.viewer.getSelection();

    return current;
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
    return ISelection.class;
  }

  @Override
  public void selectionChanged(SelectionChangedEvent e)
  {
    final Object newSelection = this.viewer.getSelection();

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
