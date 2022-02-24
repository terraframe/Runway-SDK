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

import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.constants.EnumDTOInfo;
import com.runwaysdk.constants.JSON;

/**
 * Converts an EnumDTO into a JSON string.
 */
public class EnumDTOToJSON
{
  /**
   * The EnumDTO object we're going to convert
   */
  private EnumDTO enumDTO;
  
  /**
   * The converted EnumDTO in JSON form
   */
  private JSONObject json;

//  private String sessionId;
  
  /**
   * Constructor
   * 
   * @param json
   * @throws JSONException 
   */
  public EnumDTOToJSON(String sessionId, EnumDTO enumDTO) throws JSONException
  {
    this.enumDTO = enumDTO;
    this.json = new JSONObject();
//    this.sessionId = sessionId;
  }
  
  /**
   * Returns the generated JSONObject
   * 
   * @return
   */
  public JSONObject populate() throws JSONException
  {
    // null check
    if(enumDTO == null)
    {
      return null;
    }
    else
    {
      setProperties();
    
      return json;
    }
  }
  
  /**
   * @throws JSONException 
   * 
   *
   */
  private void setProperties() throws JSONException
  {
    // dto type
    json.put(JSON.DTO_TYPE.getLabel(), EnumDTOInfo.CLASS);
    
    // type
    json.put(JSON.ENUM_DTO_TYPE.getLabel(), enumDTO.getEnumType());
    
    // name
    json.put(JSON.ENUM_DTO_NAME.getLabel(), enumDTO.getEnumName());
    
    // dto
//    BusinessDTO businessDTO = EnumerationFacade.getBusinessDTOForEnumeration(sessionId, enumDTO.getEnumType(), enumDTO.getEnumName());
    
//    BusinessDTOToJSON businessDTOToJSON = new BusinessDTOToJSON(businessDTO);
    
//    json.put(JSON.ENUM_DTO_BUSINESS_DTO.getLabel(), businessDTOToJSON.populate());
  }
  
}
