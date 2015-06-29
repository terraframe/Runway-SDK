/**
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
 */
package com.runwaysdk.transport.conversion.business;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class UtilToUtilDTO extends SessionComponentToSessionDTO
{

  /**
   * Constructor to use when the Session parameter is to be converted into an
   * DTO. 
   * 
   * @param sessionId
   * @param componentIF
   * @param convertMetaData
   */
  public UtilToUtilDTO(String sessionId, Util util, boolean convertMetaData)
  {
    super(sessionId, util, convertMetaData);
  }

  /**
   * Returns the component that is being converted into a DTO.
   * @return component that is being converted into a DTO.
   */
  protected Util getComponentIF()
  {
    return (Util)super.getComponentIF();
  }


  /**
   * Creates and populates an UtilDTO based on the provided ComponentIF
   * when this object was constructed. The created UtilDTO is returned.
   * 
   * @return
   */
  public UtilDTO populate()
  {
    return (UtilDTO)super.populate();
  }
  
  @Override
  protected ComponentDTOIF factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance,
      boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildUtilDTO(null, this.getComponentIF().getType(), attributeMap, 
        newInstance, readable, writable, modified, this.getComponentIF().toString());
  }

}
