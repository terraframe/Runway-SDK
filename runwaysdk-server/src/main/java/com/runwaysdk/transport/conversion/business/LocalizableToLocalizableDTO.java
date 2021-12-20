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
package com.runwaysdk.transport.conversion.business;

import com.runwaysdk.business.LocalizableDTO;
import com.runwaysdk.business.LocalizableIF;
import com.runwaysdk.business.MutableDTO;

public abstract class LocalizableToLocalizableDTO extends TransientToTransientDTO
{
  /**
   * Constructor to use when the Session parameter is to be converted into a
   * DTO. 
   * 
   * @param sessionId
   * @param localizableIF
   * @param convertMetaData
   */
  public LocalizableToLocalizableDTO(String sessionId, LocalizableIF localizableIF, boolean convertMetaData)
  {
    super(sessionId, localizableIF, convertMetaData);
  }
  
  @Override
  public MutableDTO populate()
  {
    LocalizableDTO localizable = (LocalizableDTO) super.populate();
    
    return localizable;
  }
}
