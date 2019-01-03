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
package com.runwaysdk.transport.metadata;

import java.util.Map;

/**
 * Builds the metadata for an attribute enumeration.
 */
public class CommonAttributeEnumerationMdBuilder extends CommonAttributeMdBuilder
{

  /**
   * Flag denoting if this enumeration supports multiple item select or not.
   */
  private boolean selectMultiple;

  /**
   * Map of enum names and display labels.
   * Key: name of the enum item
   * Value: display label of the item.
   */
  private Map<String, String> enumNameMap;

  private String referencedMdEnumeration;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeEnumerationMdBuilder(AttributeEnumerationMdDTO source, AttributeEnumerationMdDTO dest)
  {
    super(source, dest);
    selectMultiple = source.selectMultiple();
    enumNameMap = source.getEnumItems();
    referencedMdEnumeration = source.getReferencedMdEnumeration();
  }

  /**
   * Builds the metadata.
   */
  protected void build()
  {
    super.build();

    AttributeEnumerationMdDTO destSafe = (AttributeEnumerationMdDTO) dest;

    destSafe.setSelectMultiple(selectMultiple);

    destSafe.setReferencedMdEnumeration(referencedMdEnumeration);

    destSafe.setEnumItems(enumNameMap);
  }

}
