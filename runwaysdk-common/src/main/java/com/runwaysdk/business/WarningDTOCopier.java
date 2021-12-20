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
package com.runwaysdk.business;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class WarningDTOCopier extends LocalizableDTOCopier
{
  /**
   * 
   * @clientRequest clientRequest
   * @param source
   * @param dest
   * @param typeSafeObject
   * @param typeSafeAttributes
   */
  protected WarningDTOCopier(ClientRequestIF clientRequest, WarningDTO source, boolean typeSafe, boolean typeSafeAttributes)
  {
    this(clientRequest, source, null, typeSafe, typeSafeAttributes);
    
    if (typeSafe)
    {
      this.dest = ConversionFacade.createDynamicWarningDTO(null, source.getType());
      this.dest.setClientRequest(clientRequest);
    }
    else
    {
      this.dest = ComponentDTOFacade.buildWarningDTO(clientRequest, source.getType());
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
  protected WarningDTOCopier(ClientRequestIF clientRequest, WarningDTO source, WarningDTO dest, boolean typeSafeObject, boolean typeSafeAttributes)
  {
    super(clientRequest, source, dest, typeSafeObject, typeSafeAttributes);
  }
}
