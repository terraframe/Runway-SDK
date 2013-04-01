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
package com.runwaysdk.manager.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.runwaysdk.manager.general.Localizer;

public class CDateTime extends Composite implements IDateTime
{
  private DateTime dateTime;

  private Button   empty;

  public CDateTime(Composite parent, int style, boolean required)
  {
    super(parent, SWT.BORDER);

    this.setLayout(new FillLayout());

    this.dateTime = new DateTime(this, style);

    if (!required)
    {
      this.empty = new Button(this, SWT.CHECK);
      this.empty.setText(Localizer.getMessage("NO_VALUE"));
    }

    this.pack();
  }

  public int getDay()
  {
    return dateTime.getDay();
  }

  public void setDay(int day)
  {
    dateTime.setDay(day);
  }

  public int getMonth()
  {
    return dateTime.getMonth();
  }

  public void setMonth(int month)
  {
    dateTime.setMonth(month);
  }

  public int getYear()
  {
    return dateTime.getYear();
  }

  public void setYear(int year)
  {
    dateTime.setYear(year);
  }

  public int getHours()
  {
    return dateTime.getHours();
  }

  public void setHours(int hours)
  {
    dateTime.setHours(hours);
  }

  public int getMinutes()
  {
    return dateTime.getMinutes();
  }

  public void setMinutes(int minutes)
  {
    dateTime.setMinutes(minutes);
  }

  public int getSeconds()
  {
    return dateTime.getSeconds();
  }

  public void setSeconds(int seconds)
  {
    dateTime.setSeconds(seconds);
  }

  public Widget getWidget()
  {
    return this;
  }

  public void addComponentListener(int eventType, Listener listener)
  {
    dateTime.addListener(eventType, listener);
    empty.addListener(eventType, listener);
  }

  public boolean isEmpty()
  {
    if (empty == null)
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
