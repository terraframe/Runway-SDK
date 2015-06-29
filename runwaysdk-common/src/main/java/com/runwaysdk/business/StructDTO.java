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
package com.runwaysdk.business;

import java.util.Map;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.StructDTOInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class StructDTO extends EntityDTO
{

  /**
   *
   */
  private static final long serialVersionUID = 5897017509618641800L;

  public final static String CLASS = StructDTOInfo.CLASS;

  /**
   * Generic business object.  The boolean parameter is a hack to prevent infinite recursion.  Shrug.
   *
   * @param clientRequest
   */
  protected StructDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   *
   * @param clientRequest
   */
  protected StructDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);

    String _type = this.getDeclaredType();

    if(!_type.equals(StructDTO.class.getName()) &&
       !_type.equals(LocalStructDTO.class.getName()) &&
       clientRequest != null)
    {
      StructDTO dto = clientRequest.newGenericStruct(_type);
      // Not bothering to clone the map, since the source dto is gargage collected anyway.
      this.copyProperties(dto, dto.attributeMap);
    }
  }

  protected StructDTO(StructDTO structDTO, ClientRequestIF clientRequest)
  {
    super(clientRequest);

    if(!this.getDeclaredType().equals(structDTO.getType()))
    {
      String msg = "Cannot instaniate [" + this.getDeclaredType() +
                   "] with a generic DTO of [" + structDTO.getType() + "]";

      CommonExceptionProcessor.processException(
          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), msg);
    }

    this.copyProperties(structDTO, structDTO.attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  public StructDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }




  @Override
  protected String getDeclaredType()
  {
    return StructDTO.class.getName();
  }

}
