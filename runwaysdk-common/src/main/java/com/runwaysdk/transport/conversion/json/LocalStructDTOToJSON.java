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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.LocalStructDTOInfo;

public class LocalStructDTOToJSON extends StructDTOToJSON
{
  /**
   * Constructor to set the source StructDTO
   * 
   * @param StructDTO
   */
  protected LocalStructDTOToJSON(LocalStructDTO structDTO)
  {
    super(structDTO);
  }

  /**
   * Constructor to use when jsonObject represents an attribute that is already
   * in construction. That way, the attribute properties will remain in tact but
   * the properties added from EntityDTOIFToJSON will be added as well.
   * 
   * @param jsonObject
   * @param attributeStructDTO
   */
  protected LocalStructDTOToJSON(LocalStructDTO structDTO, JSONObject jsonObject)
  {
    super(structDTO, jsonObject);
  }

  @Override
  protected String getDTOType()
  {
    return LocalStructDTOInfo.CLASS;
  }

  @Override
  protected LocalStructDTO getComponentDTO()
  {
    return (LocalStructDTO) super.getComponentDTO();
  }

  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();

    JSONObject json = this.getJSON();
    LocalStructDTO component = this.getComponentDTO();

    // localized value
    json.put(JSON.LOCALIZED_VALE.getLabel(), component.getValue());
  }
}
