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

/**
 * Builds the metadata for an attribute dec.
 */
public class CommonAttributeDecMdBuilder extends CommonAttributeNumberMdBuilder
{
  /**
   * The total length of the attribute.
   */
  private int totalLength;

  /**
   * The decimal length of the attribute.
   */
  private int decimalLength;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeDecMdBuilder(AttributeDecMdDTO source, AttributeDecMdDTO dest)
  {
    super(source, dest);

    this.totalLength = source.getTotalLength();
    this.decimalLength = source.getDecimalLength();
  }

  /**
   * Builds the metadata.
   */
  protected void build()
  {
    super.build();

    AttributeDecMdDTO destSafe = (AttributeDecMdDTO) dest;

    destSafe.setTotalLength(totalLength);
    destSafe.setDecimalLength(decimalLength);
  }

}
