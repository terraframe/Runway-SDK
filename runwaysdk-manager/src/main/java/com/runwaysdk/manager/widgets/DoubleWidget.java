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

import org.eclipse.swt.widgets.Composite;

import com.runwaysdk.manager.general.Localizer;

public class DoubleWidget extends ValidateTextWidget
{

  public DoubleWidget(Composite parent, int style)
  {
    super(parent, style);
  }

  @Override
  public void validateValue(String value)
  {
    if(value.length() > 0)
    {
      Double.parseDouble(value);              
    }
  }

  @Override
  public String getMessage()
  {
    return Localizer.getMessage("INVALID_DOUBLE");
  }
}
