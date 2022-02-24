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

import java.util.Locale;

import org.json.JSONException;

import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;

public class BasicJSONToRelationshipDTO extends BasicJSONToElementDTO
{
  /**
   * 
   * @param json
   * @throws JSONException
   */
  protected BasicJSONToRelationshipDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);

  }

  protected RelationshipDTO factoryMethod(String type, boolean newInstance, String oid) throws JSONException
  {
    ClientRequestIF request = getClientRequest();

    if (newInstance)
    {
      return (RelationshipDTO) request.newMutable(type);
    }
    else
    {
      return (RelationshipDTO) request.get(oid);
    }
  }

  @Override
  public RelationshipDTO populate()
  {
    return (RelationshipDTO) super.populate();
  }
}
