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
package com.runwaysdk.transport.conversion.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.BusinessDTOInfo;
import com.runwaysdk.constants.JSON;

public class BusinessDTOToJSON extends ElementDTOToJSON
{
  /**
   * Constructor to set the source BusinessDTO
   * 
   * @param BusinessDTO
   */
  protected BusinessDTOToJSON(BusinessDTO businessDTO)
  {
    super(businessDTO);
  }
  
  /**
   * Returns the source ComponentDTO downcasted as a BusinessDTO.
   */
  @Override
  protected BusinessDTO getComponentDTO()
  {
    return (BusinessDTO) super.getComponentDTO();
  }
  
  /**
   * Sets the BusinessDTO properties on the json.
   */
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    JSONObject json = getJSON();
    BusinessDTO businessDTO = getComponentDTO();

    // add the state
    String state = businessDTO.getState();
    json.put(JSON.BUSINESS_DTO_STATE.getLabel(), state);
    
    // transitions
    JSONArray transitions = new JSONArray();
    for(String transition : businessDTO.getTransitions())
    {
      transitions.put(transition);
    }
    json.put(JSON.BUSINESS_DTO_TRANSITIONS.getLabel(), transitions);
  }

  @Override
  protected String getDTOType()
  {
    return BusinessDTOInfo.CLASS;
  }

}
