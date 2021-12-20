/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.web.applet;

import javax.swing.JProgressBar;

public class UnlimitedProgressBar extends JProgressBar
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public UnlimitedProgressBar()
  {
    super();
  }
  
  public void setMaximum(long amount)
  {
    if(amount > Integer.MAX_VALUE)
    {
      super.setMaximum(Integer.MAX_VALUE);
    }
    else
    {
      super.setMaximum((int) amount);
    }
  }
  
  public void setValue(long amount)
  {
    if(amount > Integer.MAX_VALUE)
    {
      super.setValue(Integer.MAX_VALUE);
    }
    else
    {
      super.setValue((int) amount);
    }
  }
}
