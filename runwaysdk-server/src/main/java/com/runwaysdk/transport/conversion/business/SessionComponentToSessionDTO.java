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
package com.runwaysdk.transport.conversion.business;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.SessionComponent;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;

public abstract class SessionComponentToSessionDTO extends TransientToTransientDTO
{

  /**
   * Constructor to use when the Session parameter is to be converted into an
   * DTO. 
   * 
   * @param sessionId
   * @param tranzient
   * @param convertMetaData
   */
  public SessionComponentToSessionDTO(String sessionId, SessionComponent session, boolean convertMetaData)
  {
    super(sessionId, session, convertMetaData);
    
    if (!GenerationUtil.isReservedType(this.getMdTypeIF()))
    {
      this.setTypeSafe(true);
      // Refresh the Class.  If the transaction created a new class loader instance (as the result of 
      // defining/modifying metadata) then this Class is tied to the old class loader.  We need to
      // reinstantiate it, otherwise reflection will cause an error later on, as the new class loader
      // will load up a new Class vs. the one that this object is an instance of.
      TransientDAO transientDAO = BusinessFacade.getTransientDAO(session);
      this.setComponetIF(BusinessFacade.get(transientDAO));
    }
  }

  /**
   * Returns the session that is being converted into a DTO.
   * @return session that is being converted into a DTO.
   */
  protected SessionComponent getComponentIF()
  {
    return (SessionComponent)super.getComponentIF();
  }

  
  @Override
  protected void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    BusinessFacade.getAttributeEnumerationNames(this.getComponentIF(), mdAttributeIF.definesAttribute(), attributeEnumerationDTO);
  }
  
}
