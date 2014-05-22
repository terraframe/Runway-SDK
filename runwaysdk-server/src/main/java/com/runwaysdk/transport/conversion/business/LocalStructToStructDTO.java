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

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class LocalStructToStructDTO extends StructToStructDTO
{
  public LocalStructToStructDTO(String sessionId, LocalStruct localStruct, boolean readAccess, boolean convertMetaData)
  {
    super(sessionId, localStruct, readAccess, convertMetaData);
  }
  
  public LocalStructToStructDTO(String sessionId, LocalStruct localStruct, boolean convertMetaData)
  {
    super(sessionId, localStruct, convertMetaData);
  }
  
  @Override
  public LocalStructDTO populate()
  {
    LocalStructDTO populate = (LocalStructDTO)super.populate();
    BusinessFacade.setLocalStructDTODefaultValue(populate, getComponentIF().getValue());
    return populate;
  }
  
  @Override
  protected LocalStruct getComponentIF()
  {
    return (LocalStruct)super.getComponentIF();
  }
  
  @Override
  protected LocalStructDTO factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance,
      boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildLocalStructDTO(null, this.getComponentIF().getType(), attributeMap,
        newInstance, readable, writable, modified, this.getComponentIF().toString());
  }
}
