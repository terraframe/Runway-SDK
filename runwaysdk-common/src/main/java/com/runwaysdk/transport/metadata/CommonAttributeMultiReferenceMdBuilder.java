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
package com.runwaysdk.transport.metadata;

/**
 * Builds the metadata for an attribute enumeration.
 */
public class CommonAttributeMultiReferenceMdBuilder extends CommonAttributeMdBuilder
{
  /**
   * The MdBusiness type the attribute references.
   */
  private String referencedMdBusiness;

  private String referencedDisplayLabel;

  /**
   * Constructor to set the source and destination AttributeMultiReferenceMdDTO
   * 
   * @param source
   * @param dest
   */
  protected CommonAttributeMultiReferenceMdBuilder(AttributeMultiReferenceMdDTO source, AttributeMultiReferenceMdDTO dest)
  {
    super(source, dest);

    referencedMdBusiness = source.getReferencedMdBusiness();
    referencedDisplayLabel = source.getReferencedDisplayLabel();
  }

  /**
   * Builds the metadata.
   */
  protected void build()
  {
    super.build();

    AttributeMultiReferenceMdDTO destSafe = (AttributeMultiReferenceMdDTO) dest;

    destSafe.setReferencedMdBusiness(referencedMdBusiness);
    destSafe.setReferencedDisplayLabel(referencedDisplayLabel);
  }
}
