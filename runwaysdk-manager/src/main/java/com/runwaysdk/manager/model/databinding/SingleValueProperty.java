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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.IDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.view.LabelValuePair;

public class SingleValueProperty extends SimpleValueProperty
{
  private String attributeName;

  public SingleValueProperty(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public Object getValueType()
  {
    return LabelValuePair.class;
  }

  protected Object doGetValue(Object source)
  {
    if (source instanceof IComponentObject)
    {
      IComponentObject object = (IComponentObject) source;

      List<EnumerationItemDAOIF> items = object.getItems(attributeName);

      if (items != null)
      {
        TreeSet<LabelValuePair> set = new TreeSet<LabelValuePair>();

        for (EnumerationItemDAOIF item : items)
        {
          String itemLabel = item.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, Localizer.DEFAULT_LOCALE);
          String id = item.getId();

          set.add(new LabelValuePair(itemLabel, id));
        }

        if (!set.isEmpty())
        {
          return set.first();
        }
      }
    }

    return null;
  }

  protected void doSetValue(Object source, Object value)
  {
    if (source instanceof IComponentObject)
    {
      IComponentObject decorator = (IComponentObject) source;

      LabelValuePair item = (LabelValuePair) value;

      Set<String> items = new TreeSet<String>();
      items.add(item.getValue());

      decorator.setItems(attributeName, items);
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
