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

public class SmartExceptionDTOCopier extends ComponentDTOIFCopier
{
  /**
   * 
   * @clientRequest clientRequest
   * @param source
   * @param dest
   * @param typeSafeObject
   * @param typeSafeAttributes
   */
  protected SmartExceptionDTOCopier(ClientRequestIF clientRequest, SmartExceptionDTOIF source, boolean typeSafeObject, boolean typeSafeAttributes)
  {
    super(clientRequest, source, null, typeSafeObject, typeSafeAttributes);
    
    if (typeSafeObject)
    {
      this.dest = ConversionFacade.createDynamicSmartExceptionDTO(null, source.getType());
      this.dest.setClientRequest(clientRequest);
    }
    else
    {
      this.dest = ComponentDTOFacade.buildSmartExceptionDTO(clientRequest);
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
  protected SmartExceptionDTOCopier(ClientRequestIF clientRequest, SmartExceptionDTOIF source, SmartExceptionDTOIF dest, boolean typeSafeObject, boolean typeSafeAttributes)
  {
    super(clientRequest, source, dest, typeSafeObject, typeSafeAttributes);
  }
  
  /**
   * Copies the values from the source ComponentDTOIF into the destination ComponentDTOIF
   * and returns the destination ComponentDTOIF;
   * 
   * @return
   */
  protected ComponentDTOIF copy()
  {
    super.copy();
    
    ExceptionDTO sourceExceptionDTO;
    
    if (source instanceof ExceptionDTO)
    {
      sourceExceptionDTO = (ExceptionDTO)source;
    }
    else
    {
      sourceExceptionDTO = SmartExceptionDTO.getExceptionDTO((SmartExceptionDTO) source);
    }
    
    SmartExceptionDTO smartDest =(SmartExceptionDTO) dest;
    SmartExceptionDTO.getExceptionDTO(smartDest).setLocalizedMessage(sourceExceptionDTO.getLocalizedMessage());
    SmartExceptionDTO.getExceptionDTO(smartDest).setDeveloperMessage(sourceExceptionDTO.getDeveloperMessage());
    
    return dest;
  }
}
