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

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.constants.JSON;

public abstract class MutableDTOToJSON extends ComponentDTOIFToJSON
{

  /**
   * Constructor to set the source MutableDTO.
   * @param componentDTO
   */
  protected MutableDTOToJSON(MutableDTO mutableDTO)
  {
    super(mutableDTO);
  }

  /**
   * Constructor to set the source MutableDTO and destination JSONObject.
   * 
   * @param mutableDTO
   * @param json
   */
  protected MutableDTOToJSON(MutableDTO mutableDTO, JSONObject json)
  {
    super(mutableDTO, json);
  }
  
  /**
   * Returns the source ComponentDTO downcasted as a MutableDTO.
   */
  @Override
  protected MutableDTO getComponentDTO()
  {
    return (MutableDTO) super.getComponentDTO();
  }
  
  /**
   * Sets the MutableDTO properties on the json.
   * @throws JSONException 
   * 
   */
  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    JSONObject json = getJSON();
    MutableDTO mutableDTO = getComponentDTO();
    
    // new instance
    json.put(JSON.ENTITY_DTO_NEW_INSTANCE.getLabel(), mutableDTO.isNewInstance());
    
    // writable
    json.put(JSON.ENTITY_DTO_WRITABLE.getLabel(), mutableDTO.isWritable());
    
    // modified
    json.put(JSON.ENTITY_DTO_MODIFIED.getLabel(), mutableDTO.isModified());
  }

}
