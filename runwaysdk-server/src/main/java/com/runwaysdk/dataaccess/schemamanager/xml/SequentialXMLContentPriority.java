/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

/**
 * 
 * The default implementation of the {@link ContentPriorityIF}. It assumes that
 * the elements added later have lower priority.
 * 
 * @author Aritra
 * 
 */
public class SequentialXMLContentPriority implements ContentPriorityIF
{

  private Map<String, Integer> attributePriorityMap;

  private Map<String, Integer> childElementPriorityMap;

  public SequentialXMLContentPriority()
  {
    attributePriorityMap = new HashMap<String, Integer>();
    childElementPriorityMap = new HashMap<String, Integer>();
    curAttributePriority = 0;
    curChildPriority = 0;
  }

  public int attributePriority(String attributeName)
  {
    return attributePriorityMap.get(attributeName);
  }

  public int childElementPriority(String childElementTagName)
  {
    Integer childPriorityInteger = childElementPriorityMap.get(childElementTagName);

    if (childPriorityInteger != null)
    {
      return childPriorityInteger.intValue();
    }
    else
    {
      throw new ProgrammingErrorException("Unable to find XSD priority for tag [" + childElementTagName + "]");
    }
  }

  private int curAttributePriority;

  public void addAttribute(String attributeName)
  {
    if (!attributePriorityMap.containsKey(attributeName))
    {
      attributePriorityMap.put(attributeName, curAttributePriority++);
    }

  }

  private int curChildPriority;

  public void addChildTag(String childTag)
  {
    if (!childElementPriorityMap.containsKey(childTag))
    {
      childElementPriorityMap.put(childTag, curChildPriority++);
    }

  }

  public Map<String, Integer> attributePriorityMap()
  {
    // TODO Auto-generated method stub
    return attributePriorityMap;
  }

  public Map<String, Integer> childPriorityMap()
  {
    // TODO Auto-generated method stub
    return childElementPriorityMap;
  }

}
