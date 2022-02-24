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
package com.runwaysdk.manager.model.databinding;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.PersistanceFacade;

public class ComponentValueProperty extends SimpleValueProperty
{
  private String attributeName;

  public ComponentValueProperty(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public Object getValueType()
  {
    return ComponentIF.class;
  }

  protected Object doGetValue(Object source)
  {
    if (source instanceof IComponentObject)
    {
      IComponentObject decorator = (IComponentObject) source;

      String value = decorator.getValue(attributeName);

      if (value != null && value.length() > 0)
      {
        return PersistanceFacade.get(value);
      }
      else
      {
        return null;
      }
    }

    throw new RuntimeException(Localizer.getMessage("INVALID_SOURCE") + source);
  }

  protected void doSetValue(Object source, Object value)
  {
    if (source instanceof IComponentObject)
    {
      IComponentObject decorator = (IComponentObject) source;
      ComponentIF component = (ComponentIF) value;

      if (component != null)
      {
        decorator.setValue(attributeName, component.getOid());
      }
      else
      {
        decorator.setValue(attributeName, "");
      }
    }
    else
    {
      throw new RuntimeException("Invalid Source: " + source);
    }
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
