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

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.runwaysdk.manager.widgets.IDateTime;

public class IDateObservableValue extends AbstractObservableValue implements ISWTObservableValue
{
  private IDateTime dateTime;

  private Date      currentValue;

  private Calendar  calendar;

  private Listener  listener;

  private boolean   updating = false;

  public IDateObservableValue(Realm realm, IDateTime dateTime)
  {
    super(realm);

    this.dateTime = dateTime;
    this.calendar = Calendar.getInstance();
    this.currentValue = this.getCurrentValue();

    listener = new Listener()
    {
      public void handleEvent(Event event)
      {
        if (updating)
        {
          return;
        }

        notifyIfChanged(currentValue, currentValue = getCurrentValue());
      }
    };

    this.dateTime.addComponentListener(SWT.Selection, listener);
  }

  @Override
  protected Object doGetValue()
  {
    return currentValue;
  }

  @Override
  protected void doSetValue(Object value)
  {
    updating = true;

    try
    {
      this.setCurrentValue((Date) value);
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
    return Date.class;
  }

  public Date getCurrentValue()
  {
    if (dateTime.isEmpty())
    {
      return null;
    }

    calendar.clear();

    calendar.set(Calendar.YEAR, dateTime.getYear());
    calendar.set(Calendar.MONTH, dateTime.getMonth());
    calendar.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
    calendar.set(Calendar.HOUR_OF_DAY, dateTime.getHours());
    calendar.set(Calendar.MINUTE, dateTime.getMinutes());
    calendar.set(Calendar.SECOND, dateTime.getSeconds());

    return calendar.getTime();
  }

  public void setCurrentValue(Date date)
  {
    if (date != null)
    {
      calendar.setTime(date);

      dateTime.setYear(calendar.get(Calendar.YEAR));
      dateTime.setMonth(calendar.get(Calendar.MONTH));
      dateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
      dateTime.setHours(calendar.get(Calendar.HOUR_OF_DAY));
      dateTime.setMinutes(calendar.get(Calendar.MINUTE));
      dateTime.setSeconds(calendar.get(Calendar.SECOND));
    }
    else
    {
      dateTime.setEmpty(true);
    }
  }

  @Override
  public Widget getWidget()
  {
    return dateTime.getWidget();
  }
}
