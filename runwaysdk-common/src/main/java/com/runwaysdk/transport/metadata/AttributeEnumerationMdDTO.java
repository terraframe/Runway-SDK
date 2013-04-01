/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.transport.metadata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class AttributeEnumerationMdDTO extends AttributeMdDTO
{
  /**
   *
   */
  private static final long serialVersionUID = -3002432864479344813L;

  private boolean selectMultiple;

  private String referencedMdEnumeration;

  /**
   * Map of enum names and display labels.
   * Key: name of the enum item
   * Value: display label of the item.
   */
  private Map<String, String> enumNameMap;

  protected AttributeEnumerationMdDTO()
  {
    super();
    selectMultiple = true;
    enumNameMap = new HashMap<String, String>();
  }

  /**
   * Returns the type of the Enumeration Master List.
   * @return the type of the Enumeration Master List.
   */
  public String getReferencedMdEnumeration()
  {
    return referencedMdEnumeration;
  }

  /**
   * Sets the type of the Enumeration Master List that this attribute references.
   * @param type of the Enumeration Master List that this attribute references.
   */
  protected void setReferencedMdEnumeration(String referencedMdEnumeration)
  {
    this.referencedMdEnumeration = referencedMdEnumeration;
  }

  /**
   * Checks if this enumeration can select multiple items.
   *
   * @return
   */
  public boolean selectMultiple()
  {
    return selectMultiple;
  }

  /**
   * Sets whether or not this enumeration can select multiple items
   *
   * @param true if this enumeration can select multiple items, false otherwise.
   */
  protected void setSelectMultiple(boolean selectMultiple)
  {
    this.selectMultiple = selectMultiple;
  }

  /**
   * Adds an enumeration
   *
   * @param enumName
   * @param enumDisplayLabel
   */
  protected void addEnumItem(String enumName, String enumDisplayLabel)
  {
    enumNameMap.put(enumName, enumDisplayLabel);
  }

  /**
   * Returns a list of the enum names.
   * @return list of the enum names.
   */
  public List<String> getEnumNames()
  {
    List<String> enumNameList = new LinkedList<String>();

    for (String enumName : enumNameMap.keySet())
    {
      enumNameList.add(enumName);
    }

    return enumNameList;
  }

  /**
   * Returns a list of the enum display labels.
   * @return list of the enum display labels.
   */
  public List<String> getEnumLabels()
  {
    List<String> enumDisplayLabels = new LinkedList<String>();

    for (String enumDisplayLabel : enumNameMap.values())
    {
      enumDisplayLabels.add(enumDisplayLabel);
    }

    return enumDisplayLabels;
  }

  /**
   * Returns the display label of the enumeration with the given name.
   * @param enumName
   * @return display label of the enumeration with the given name.
   */
  public String getEnumDisplayLabel(String enumName)
  {
    return enumNameMap.get(enumName);
  }

  /**
   * Returns a map where the key is the attribute name
   * and the value is the display label.
   * @return map where the key is the attribute name
   * and the value is the display label.
   */
  public Map<String, String> getEnumItems()
  {
    return new HashMap<String, String>(enumNameMap);
  }

  /**
   * Sets the enumerationMap.
   * @param enumNameMap
   */
  protected void setEnumItems(Map<String, String> enumNameMap)
  {
    this.enumNameMap = new HashMap<String, String>(enumNameMap);
  }

  @Override
  public Class<?> getJavaType()
  {
    //Get the DTO representation of the referenced MdEnumeration
    return LoaderDecorator.load(referencedMdEnumeration + TypeGeneratorInfo.DTO_SUFFIX);
  }
}
