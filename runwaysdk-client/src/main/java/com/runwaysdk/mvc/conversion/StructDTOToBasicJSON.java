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
package com.runwaysdk.mvc.conversion;

import org.json.JSONObject;

import com.runwaysdk.business.StructDTO;

public class StructDTOToBasicJSON extends EntityDTOToBasicJSON
{
  /**
   * Constructor to set the source StructDTO
   *
   * @param StructDTO
   */
  protected StructDTOToBasicJSON(StructDTO structDTO)
  {
    super(structDTO);
  }

  /**
   * Constructor to use when jsonObject represents an attribute that is already in construction.
   * That way, the attribute properties will remain in tact but the properties added from EntityDTOIFToJSON
   * will be added as well.
   *
   * @param jsonObject
   * @param attributeStructDTO
   */
  protected StructDTOToBasicJSON(StructDTO structDTO, JSONObject jsonObject)
  {
    super(structDTO, jsonObject);
  }
}
