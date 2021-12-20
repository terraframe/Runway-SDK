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
package com.runwaysdk.transport.conversion.business;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.Warning;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class WarningToWarningDTO extends MessageToMessageDTO
{

  /**
   * Constructor to use when the Session parameter is to be converted into a
   * DTO. 
   * 
   * @param sessionId
   * @param warning
   * @param convertMetaData
   */
  public WarningToWarningDTO(String sessionId, Warning warning, boolean convertMetaData)
  {
    super(sessionId, warning, convertMetaData);

    if (!GenerationUtil.isSkipCompileAndCodeGeneration(this.getMdTypeIF()))
    {
      this.setTypeSafe(true);
      // Refresh the Class.  If the transaction created a new class loader instance (as the result of 
      // defining/modifying metadata) then this Class is tied to the old class loader.  We need to
      // reinstantiate it, otherwise reflection will cause an error later on, as the new class loader
      // will load up a new Class vs. the one that this object is an instance of.
//      TransientDAO transientDAO = BusinessFacade.getTransientDAO(warning);
//      this.setComponetIF(BusinessFacade.get(transientDAO));
    }
  }  

  /**
   * Returns the component that is being converted into a DTO.
   * @return component that is being converted into a DTO.
   */
  protected Warning getComponentIF()
  {
    return (Warning)super.getComponentIF();
  }

  /**
   * Creates and populates an WarningDTO based on the provided ComponentIF
   * when this object was constructed. The created WarningDTO is returned.
   * 
   * @return
   */
  public WarningDTO populate()
  {
    return (WarningDTO)super.populate();
  }
  
  @Override
  protected ComponentDTOIF factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance,
      boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildWarningDTO( null, this.getComponentIF().getType(), attributeMap, 
        newInstance, readable, writable, modified, this.getComponentIF().toString());
  }
}
