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
package com.runwaysdk.transport.conversion.json;

import org.json.JSONObject;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.TransientDTO;

public abstract class TransientDTOToJSON extends MutableDTOToJSON
{
  /**
   * Constructor to set the source TransientDTO.
   * @param componentDTO
   */
  protected TransientDTOToJSON(TransientDTO transientDTO)
  {
    super(transientDTO);
  }

  /**
   * Constructor to set the source TransientDTO and destination JSONObject.
   * 
   * @param transientDTO
   * @param json
   */
  protected TransientDTOToJSON(TransientDTO transientDTO, JSONObject json)
  {
    super(transientDTO, json);
  }
  
  /**
   * Returns the source ComponentDTO downcasted as a MutableDTO.
   */
  @Override
  protected MutableDTO getComponentDTO()
  {
    return (MutableDTO) super.getComponentDTO();
  }

}
