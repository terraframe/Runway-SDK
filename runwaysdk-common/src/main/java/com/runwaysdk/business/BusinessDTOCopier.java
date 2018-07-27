/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class BusinessDTOCopier extends ElementDTOCopier
{

  /**
   * 
   * @clientRequest clientRequest
   * @param source
   * @param dest
   * @param typeSafeObject
   * @param typeSafeAttributes
   */
  protected BusinessDTOCopier(ClientRequestIF clientRequest, BusinessDTO source, boolean typeSafe, boolean typeSafeAttributes)
  {
    this(clientRequest, source, null, typeSafe, typeSafeAttributes);
    
    if (typeSafe && source.getMd().isGenerateSource())
    {
      this.dest = ConversionFacade.createDynamicBusinessDTO(null, source.getType());
      this.dest.setClientRequest(clientRequest);
    }
    else
    {
      this.dest = ComponentDTOFacade.buildBusinessDTO(clientRequest, source.getType());
    }
  }
  
  /**
   * 
   * @clientRequest clientRequest
   * @param source
   * @param dest
   * @param typeSafeObject
   * @param typeSafeAttributes
   */
  protected BusinessDTOCopier(ClientRequestIF clientRequest, BusinessDTO source, BusinessDTO dest, boolean typeSafe, boolean typeSafeAttributes)
  {
    super(clientRequest, source, dest, typeSafe, typeSafeAttributes);
  }

  /**
   * 
   * @return
   */
  protected BusinessDTO getSource()
  {
    return (BusinessDTO)this.source;
  }
  
  /**
   * 
   * @return
   */
  protected BusinessDTO getDest()
  {
    return (BusinessDTO)this.dest;
  }

}
