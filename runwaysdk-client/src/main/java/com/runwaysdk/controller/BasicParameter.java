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
package com.runwaysdk.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BasicParameter implements ParameterValue
{
  private List<String> values;

  public BasicParameter()
  {
    this.values = new LinkedList<String>();
  }

  public BasicParameter(String[] values)
  {
    this();

    if (values != null)
    {
      this.values.addAll(Arrays.asList(values));
    }
  }

  public void add(String value)
  {
    this.values.add(value);
  }

  public List<String> getValues()
  {
    return values;
  }

  public String[] getValuesAsArray()
  {
    return this.values.toArray(new String[this.values.size()]);
  }

  public String getSingleValue()
  {
    if (this.values.size() > 0)
    {
      return this.values.get(0);
    }

    return null;
  }
}
