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
package com.runwaysdk.transport.conversion.dom;

import java.util.Map;

import org.w3c.dom.Element;

import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class DocToSessionComponentDTO extends DocToTransientDTO
{
  /**
   * Constructor.
   * 
   * @param clientRequest
   * @param root
   * @param convertMetaData
   */
  public DocToSessionComponentDTO(ClientRequestIF clientRequest, Element rootElement)
  {
    super(clientRequest, rootElement);
  }

  
  /**
   * Instantiates the proper sessionDTO class (not type safe)
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return proper sessionDTO class (not type safe)
   */
  protected abstract SessionDTO factoryMethod(String type, Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified);
}
