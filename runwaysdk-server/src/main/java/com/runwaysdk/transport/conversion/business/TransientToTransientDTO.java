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

import com.runwaysdk.business.Transient;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.session.SessionFacade;

public abstract class TransientToTransientDTO extends MutableToMutableDTO
{
  /**
   * Constructor to use when the Session parameter is to be converted into an
   * DTO.
   *
   * @param sessionId
   * @param component
   * @param convertMetaData
   */
  public TransientToTransientDTO(String sessionId, Transient tranzient, boolean convertMetaData)
  {
    super(sessionId, tranzient, convertMetaData);
  }


  /**
   * Returns the Transient that is being converted into a DTO.
   * @return Transient that is being converted into a DTO.
   */
  protected Transient getComponentIF()
  {
    return (Transient)super.getComponentIF();
  }

  /**
   * Returns true if the user bound to the session has permission to read this object, false otherwise.
   * @return false if the user bound to the session has permission to read this object, false otherwise.
   */
  protected boolean checkReadAccess()
  {
    return PermissionFacade.checkReadAccess(this.getSessionId(), this.getComponentIF());
  }


  /**
   * Returns true if the user bound to the session has permission to write to this object, false otherwise.
   * @return false if the user bound to the session has permission to write to this object, false otherwise.
   */
  protected boolean checkWriteAccess()
  {
    return SessionFacade.checkAccess(this.getSessionId(), Operation.WRITE, this.getComponentIF());
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
    return PermissionFacade.checkAttributeReadAccess(this.getSessionId(), this.getComponentIF(), mdAttribute);
  }

  /**
   * Checks permissions on an attribute to see if it can be edited or not.
   *
   * @param mdAttribute
   *            The MdAttributeIF object to check for write permission on.
   * @return true if attribute can be edited, false otherwise.
   */
  protected boolean hasAttributeWriteAccess(MdAttributeDAOIF mdAttribute)
  {
    return SessionFacade.checkAttributeAccess(this.getSessionId(), Operation.WRITE, this.getComponentIF(), mdAttribute);
  }


}
