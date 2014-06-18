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

import java.util.Map;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class TermToTermDTO extends BusinessToBusinessDTO
{

  /**
   * @param sessionId
   * @param business
   * @param convertMetaData
   */
  public TermToTermDTO(String sessionId, Term term, boolean convertMetaData)
  {
    super(sessionId, term, convertMetaData);
  }
  
  /**
   * Instantiates TermDTO (not type safe)
   * @param sessionId
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return TermDTO (not type safe)
   */
  protected BusinessDTO factoryMethod(Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
//    return ComponentDTOFacade.buildBusinessDTO(null, this.getComponentIF().getType(), attributeMap,
//        newInstance, readable, writable, modified,  this.getComponentIF().toString(), this.getComponentIF().checkUserLock());
    
    TermDTO termDTO = new TermDTO(null, this.getComponentIF().getType(), attributeMap);
    ComponentDTOFacade.setCommonProperties(newInstance, readable, writable, modified, this.getComponentIF().toString(), (BusinessDTO) termDTO);
    termDTO.setLockedByCurrentUser(this.getComponentIF().checkUserLock());
    
    return termDTO;
  }
  
}
