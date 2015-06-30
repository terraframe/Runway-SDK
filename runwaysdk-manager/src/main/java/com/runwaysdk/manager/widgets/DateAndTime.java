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
package com.runwaysdk.manager.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.runwaysdk.manager.general.Localizer;

public class DateAndTime extends Composite implements IDateTime
{
  private DateTime date;

  private DateTime time;

  private Button   empty;

  public DateAndTime(Composite parent, int style, boolean required)
  {
    super(parent, style);

    this.setLayout(new FillLayout());

    this.date = new DateTime(this, SWT.DATE);
    this.time = new DateTime(this, SWT.TIME);

    if (!required)
    {
      this.empty = new Button(this, SWT.CHECK);
      this.empty.setText(Localizer.getMessage("NO_VALUE"));
      this.empty.setSelection(false);
    }

    this.pack();
  }

  public int getDay()
  {
    return date.getDay();
  }

  public void setDay(int day)
  {
    date.setDay(day);
  }

  public int getMonth()
  {
    return date.getMonth();
  }

  public void setMonth(int month)
  {
    date.setMonth(month);
  }

  public int getYear()
  {
    return date.getYear();
  }

  public void setYear(int year)
  {
    date.setYear(year);
  }

  public int getHours()
  {
    return time.getHours();
  }

  public void setHours(int hours)
  {
    time.setHours(hours);
  }

  public int getMinutes()
  {
    return time.getMinutes();
  }

  public void setMinutes(int minutes)
  {
    time.setMinutes(minutes);
  }

  public int getSeconds()
  {
    return time.getSeconds();
  }

  public void setSeconds(int seconds)
  {
    time.setSeconds(seconds);
  }

  public Widget getWidget()
  {
    return this;
  }

  public void addComponentListener(int eventType, Listener listener)
  {
    date.addListener(eventType, listener);
    time.addListener(eventType, listener);
    empty.addListener(eventType, listener);
  }
  
  public boolean isEmpty()
  {
    if(empty == null)
    {
      return false;
    }
    
    return empty.getSelection();
  }

  @Override
  public void setEmpty(boolean isEmpty)
  {
    empty.setSelection(isEmpty);
  }
}
