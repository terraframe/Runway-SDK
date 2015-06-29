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

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.Problem;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;

public class ProblemToProblemDTO extends NotificationToNotificationDTO
{
  /**
   * Constructor to use when the Session parameter is to be converted into an
   * DTO. 
   * 
   * @param sessionId
   * @param problem
   * @param convertMetaData
   */
  public ProblemToProblemDTO(String sessionId, Problem problem, boolean convertMetaData)
  {
    super(sessionId, problem, convertMetaData);

    if (!GenerationUtil.isReservedType(this.getMdTypeIF()))
    {
      this.setTypeSafe(true);
      // Refresh the Class.  If the transaction created a new class loader instance (as the result of 
      // defining/modifying metadata) then this Class is tied to the old class loader.  We need to
      // reinstantiate it, otherwise reflection will cause an error later on, as the new class loader
      // will load up a new Class vs. the one that this object is an instance of.
//      TransientDAO transientDAO = BusinessFacade.getTransientDAO(problem);
//      this.setComponetIF(BusinessFacade.get(transientDAO));
    }
  }

  /**
   * Returns the component that is being converted into a DTO.
   * @return component that is being converted into a DTO.
   */
  protected Problem getComponentIF()
  {
    return (Problem)super.getComponentIF();
  }

  /**
   * Creates and populates an ProblemDTO based on the provided ComponentIF
   * when this object was constructed. The created ProblemDTO is returned.
   * 
   * @return
   */
  public ProblemDTO populate()
  {
    ProblemDTO populate = (ProblemDTO)super.populate();
    
    if (this.getComponentIF().getLocale() == null)
    {
      populate.setLocalizedMessage("");
      populate.setDeveloperMessage("");
    }
    else
    {
      populate.setLocalizedMessage(this.getComponentIF().getLocalizedMessage());
      populate.setDeveloperMessage(this.getComponentIF().getDeveloperMessage());
    }
    return populate;
  }

  @Override
  protected void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    BusinessFacade.getAttributeEnumerationNames(this.getComponentIF(), mdAttributeIF.definesAttribute(), attributeEnumerationDTO);
  }

  @Override
  protected ComponentDTOIF factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance,
      boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildProblemDTO( null, this.getComponentIF().getType(), attributeMap, 
        newInstance, readable, writable, modified, this.getComponentIF().toString());
  }
  
}
