/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc.conversion;

import org.json.JSONException;

import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.mvc.JsonConfiguration;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class EntityDTOToBasicJSON extends MutableDTOToBasicJSON
{

  /**
   * 
   * @param entityDTO
   * @param configuration
   */
  protected EntityDTOToBasicJSON(EntityDTO entityDTO, JsonConfiguration configuration)
  {
    super(entityDTO, configuration);
  }

  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();

    if (this.getComponentDTO().isNewInstance())
    {
      this.getJSON().remove(ComponentInfo.ID);
    }
  }

  @Override
  public boolean isValid(AttributeDTO attributeDTO)
  {
    String name = attributeDTO.getName();

    return super.isValid(attributeDTO) && ! ( name.equals(EntityInfo.SITE_MASTER) );
  }

}
