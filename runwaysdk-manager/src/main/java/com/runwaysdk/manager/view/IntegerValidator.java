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
package com.runwaysdk.manager.view;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.fieldassist.ControlDecoration;

import com.runwaysdk.ConfigurationException;

public class IntegerValidator implements IValidator
{
  private String            message;

  private ControlDecoration decoration;

  public IntegerValidator(String message, ControlDecoration decoration)
  {
    this.message = message;
    this.decoration = decoration;
  }

  public IStatus validate(Object value)
  {
    if (value instanceof String)
    {
      String s = (String) value;
      
      try
      {
        Long.parseLong(s);

        decoration.hide();
        return Status.OK_STATUS;
      }
      catch(Exception e)
      {
        decoration.show();        
        
        return ValidationStatus.error(message);
      }
    }
    else
    {
      throw new ConfigurationException("Not supposed to be called for non-strings.");
    }
  }

}
