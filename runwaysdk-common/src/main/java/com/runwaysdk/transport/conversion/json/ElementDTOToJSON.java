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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.constants.JSON;

public abstract class ElementDTOToJSON extends EntityDTOToJSON
{
  /**
   * Constructor
   * 
   * @param entityDTO
   */
  protected ElementDTOToJSON(ElementDTO elementDTO)
  {
    super(elementDTO);
  }
  
  /**
   * Returns the souce ComponentDTO downcast as an ElementDTO
   */
  @Override
  protected ElementDTO getComponentDTO()
  {
    return (ElementDTO) super.getComponentDTO();
  }

  /**
   * Sets the basic entity info on a JSON string
   */
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    JSONObject json = getJSON();
    ElementDTO elementDTO = getComponentDTO();
    
    // locked by user
    json.put(JSON.ENTITY_DTO_LOCKED_BY_CURRENT_USER.getLabel(), elementDTO.isLockedByCurrentUser());
  }
}
