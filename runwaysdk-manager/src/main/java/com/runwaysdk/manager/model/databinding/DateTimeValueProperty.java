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

import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;

import com.runwaysdk.manager.model.IComponentObject;

public class DateTimeValueProperty extends SimpleValueProperty
{
  private String                 attributeName;

  private IDateConversionCommand command;

  public DateTimeValueProperty(String attributeName, IDateConversionCommand command)
  {
    this.attributeName = attributeName;
    this.command = command;
  }

  public Object getValueType()
  {
    return Date.class;
  }

  protected Object doGetValue(Object source)
  {
    if (source instanceof IComponentObject)
    {
      IComponentObject decorator = (IComponentObject) source;

      return command.convert(decorator.getValue(attributeName));
    }

    throw new RuntimeException("Invalid Source: " + source);
  }

  protected void doSetValue(Object source, Object value)
  {
    if (source instanceof IComponentObject)
    {
      IComponentObject decorator = (IComponentObject) source;

      String dateTime = command.convert((Date) value); 

      decorator.setValue(attributeName, dateTime);
    }

    throw new RuntimeException("Invalid Source: " + source);
  }

  public INativePropertyListener adaptListener(final ISimplePropertyListener listener)
  {
    return new SimplePropertyListener(this, listener, attributeName)
    {
      protected IDiff computeDiff(Object oldValue, Object newValue)
      {
        return Diffs.createValueDiff(oldValue, newValue);
      }
    };
  }
}
