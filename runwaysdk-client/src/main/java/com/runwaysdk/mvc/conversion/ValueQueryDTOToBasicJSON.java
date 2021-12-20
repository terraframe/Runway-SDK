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
package com.runwaysdk.mvc.conversion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.mvc.JsonConfiguration;

public class ValueQueryDTOToBasicJSON extends ComponentQueryDTOToBasicJSON
{

  protected ValueQueryDTOToBasicJSON(ValueQueryDTO queryDTO, JsonConfiguration configuration)
  {
    super(queryDTO, configuration);
  }

  @Override
  protected ValueQueryDTO getComponentQueryDTO()
  {
    return (ValueQueryDTO) super.getComponentQueryDTO();
  }

  /**
   * Adds the result set to the json.
   */
  @Override
  protected void addResultSet() throws JSONException
  {
    ValueQueryDTO queryDTO = getComponentQueryDTO();

    JSONArray resultSet = new JSONArray();

    for (ValueObjectDTO valueObjectDTO : queryDTO.getResultSet())
    {
      ValueObjectDTOToBasicJSON valueObjectDTOToJSON = new ValueObjectDTOToBasicJSON(valueObjectDTO, this.getConfiguration());
      resultSet.put(valueObjectDTOToJSON.populate());
    }

    JSONObject json = getJSON();
    json.put(JSON.CLASS_QUERY_DTO_RESULT_SET.getLabel(), resultSet);
  }

}
