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
 * Describes the metadata for an attribute struct.
 */
public class AttributeStructMdDTO extends AttributeMdDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 5413324354038885547L;
  
  /**
   * The MdStruct that defines the struct.
   */
  private String definingMdStruct;
  
  /**
   * Default constructor.
   *
   */
  protected AttributeStructMdDTO()
  {
    super();
    definingMdStruct = "";
  }
  
  /**
   * Returns the type of MdStruct that defines this Struct.
   * 
   * @return
   */
  public String getDefiningMdStruct()
  {
    return definingMdStruct;
  }
  
  /**
   * Sets the type of MdStruct that defines this Struct.
   * 
   * @param definingMdStruct
   */
  protected void setDefiningMdStruct(String definingMdStruct)
  {
    this.definingMdStruct = definingMdStruct;
  }

  @Override
  public Class<?> getJavaType()
  {
    return LoaderDecorator.load(definingMdStruct + TypeGeneratorInfo.DTO_SUFFIX);
  }
}
