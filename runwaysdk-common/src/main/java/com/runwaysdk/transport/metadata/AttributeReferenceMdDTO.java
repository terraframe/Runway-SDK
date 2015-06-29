/**
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
 */
package com.runwaysdk.transport.metadata;

import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * Describes the metadata for an attribute reference
 */
public class AttributeReferenceMdDTO extends AttributeMdDTO
{
  /**
   * The MdBusiness type that this attribute references.
   */
  private String referencedMdBusiness;

  /**
   * The displayLabel of the MdBusiness that this attribute references
   */
  private String referencedDisplayLabel;

  /**
   *
   */
  private static final long serialVersionUID = 3297295010795017217L;

  /**
   * Default constructor.
   */
  protected AttributeReferenceMdDTO()
  {
    super();
    this.referencedMdBusiness = "";
    this.referencedDisplayLabel = "";
  }

  /**
   * Returns the MdBusiness type this attribute references.
   * @return
   */
  public String getReferencedMdBusiness()
  {
    return referencedMdBusiness;
  }

  /**
   * Sets the MdBusiness type this attribute references.
   *
   * @param referencedMdBusiness
   */
  protected void setReferencedMdBusiness(String referencedMdBusiness)
  {
    this.referencedMdBusiness = referencedMdBusiness;
  }

  public String getReferencedDisplayLabel()
  {
    return referencedDisplayLabel;
  }

  protected void setReferencedDisplayLabel(String referencedDisplayLabel)
  {
    this.referencedDisplayLabel = referencedDisplayLabel;
  }

  @Override
  public Class<?> getJavaType()
  {
    return LoaderDecorator.load(referencedMdBusiness + TypeGeneratorInfo.DTO_SUFFIX);
  }

}
