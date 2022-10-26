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
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.util.HashMap;
import java.util.Map;

public class PriorityManager
{
  private Map<String, Integer> priorityMap;

  private int                  currentPriority;

  public PriorityManager()
  {
    priorityMap = new HashMap<String, Integer>();
  }

  public void put(String string)
  {
    if (!priorityMap.containsKey(string))
    {
      priorityMap.put(string, Integer.valueOf(currentPriority++));
    }
  }

  public int getPriority(String string)
  {
    if (priorityMap.containsKey(string))
    {
      return priorityMap.get(string).intValue();
    }
    else
      return -1;
  }
  
  public Map<String, Integer> priorityMap() {
    return priorityMap;
  }

}
