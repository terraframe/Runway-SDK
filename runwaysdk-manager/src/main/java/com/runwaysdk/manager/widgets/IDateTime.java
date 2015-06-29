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
package com.runwaysdk.manager.widgets;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public interface IDateTime 
{
  public int getDay();
  
  public void setDay(int day);
  
  public int getMonth();
  
  public void setMonth(int month);

  public int getYear();

  public void setYear(int year);
  
  public int getHours();
  
  public void setHours(int hours);
  
  public int getMinutes();
  
  public void setMinutes(int minutes);
  
  public int getSeconds();
  
  public void setSeconds(int seconds);

  public void addComponentListener(int selection, Listener listener);

  public Widget getWidget();
  
  public boolean isEmpty();

  public void setEmpty(boolean empty);
}
