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

import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.transport.attributes.AttributeDTO;

/**
 * Populates an StructDTO provided a Struct that represents.
 *
 */
public class StructToStructDTO extends EntityToEntityDTO
{
  private boolean readAccess = false;

  public StructToStructDTO(String sessionId, Struct struct, boolean readAccess, boolean convertMetaData)
  {
    this(sessionId, struct, convertMetaData);
    this.readAccess = readAccess;
  }

  public StructToStructDTO(String sessionId, Struct struct, boolean convertMetaData)
  {
    super(sessionId, struct, convertMetaData);
  }

  /**
   * Creates and populates an StructDTO based on the provided ComponentIF
   * when this object was constructed. The created StructDTO is returned.
   *
   * @return
   */
  public StructDTO populate()
  {
    return (StructDTO)super.populate();
  }

  /**
   * Instantiates StructDTO (not type safe)
   * @param sessionId
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return BusinessDTO (not type safe)
   */
  protected StructDTO factoryMethod(Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildStructDTO(null, this.getComponentIF().getType(), attributeMap,
        newInstance, readable, writable, modified, this.getComponentIF().toString());
  }

  /**
   * Returns true if the user bound to the session has permission to read this object, false otherwise.
   * @return false if the user bound to the session has permission to read this object, false otherwise.
   */
  protected boolean checkReadAccess()
  {
    if (this.readAccess)
    {
      return true;
    }
    else
    {
      return PermissionFacade.checkReadAccess(this.getSessionId(), this.getComponentIF());
    }
  }

  /**
   * Checks permissions on an attribute to see if it can be read or not.
   *
   * @param sessionId
   * @param mdAttribute
   *            The MdAttributeIF object to check for write permission on.
   * @return true if the attribute can be read, false otherwise.
   */
  protected boolean hasAttributeReadAccess(MdAttributeDAOIF mdAttribute)
  {
    if (this.readAccess)
    {
      return true;
    }
    else
    {
      return PermissionFacade.checkAttributeReadAccess(this.getSessionId(), this.getComponentIF(), mdAttribute);
    }
  }
}
