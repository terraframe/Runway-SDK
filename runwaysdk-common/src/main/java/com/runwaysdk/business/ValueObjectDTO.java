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
package com.runwaysdk.business;

import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ValueObjectDTOInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;

public class ValueObjectDTO extends ComponentDTO
{
  public static final String CLASS = ValueObjectDTOInfo.CLASS;
  
  /**
   *
   */
  private static final long serialVersionUID = 880570331696562320L;

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ValueObjectDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ValueObjectDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  @Override
  protected String getDeclaredType()
  {
    return ValueObjectDTO.class.getName();
  }

  /**
   * Returns the value of an attribute.
   *
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public String getStructValue(String structAttributeName, String attributeName)
  {
    if (attributeMap.containsKey(structAttributeName))
    {
      AttributeDTO attribute = attributeMap.get(structAttributeName);

      this.checkAttributeReadPermission(attribute);

      if (attribute instanceof AttributeStructDTO)
      {
        AttributeStructDTO attributeStruct = (AttributeStructDTO)attribute;

        return attributeStruct.getValue(attributeName);
      }
      else
      {
        return ComponentDTOIF.EMPTY_VALUE;
      }
    }
    else
    {
      return ComponentDTOIF.EMPTY_VALUE;
    }
  }
}
