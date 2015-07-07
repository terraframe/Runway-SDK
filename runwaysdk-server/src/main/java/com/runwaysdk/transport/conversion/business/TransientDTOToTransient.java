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
package com.runwaysdk.transport.conversion.business;

import com.runwaysdk.business.Transient;
import com.runwaysdk.business.TransientDTO;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class TransientDTOToTransient extends MutableDTOToMutable
{
  /**
   *
   * @param transientDTO
   * @param tranzient
   */
  public TransientDTOToTransient(String sessionId, TransientDTO transientDTO, Transient tranzient)
  {
    super(sessionId, transientDTO, tranzient);
  }

  public Transient populate()
  {
    return (Transient)super.populate();
  }

  /**
   * Returns true if the attribute should be copied from the DTO to the Business layer class,
   * false otherwise.
   * @param mdAttributeIF
   * @return true if the attribute should be copied from the DTO to the Business layer class,
   * false otherwise.
   */
  protected boolean copyAttribute(MdAttributeDAOIF mdAttributeIF)
  {
    if (!mdAttributeIF.isSystem() &&
        mdAttributeIF.getSetterVisibility() == VisibilityModifier.PUBLIC)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}
