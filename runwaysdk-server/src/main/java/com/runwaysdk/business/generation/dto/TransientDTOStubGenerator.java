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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdTransientDAOIF;

public abstract class TransientDTOStubGenerator extends ClassDTOStubGenerator
{

  public TransientDTOStubGenerator(MdTransientDAOIF mdTransientIF)
  {
    super(mdTransientIF);
  }

  /**
   * The general algorithm used to generate the DTOs
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    { 
      return;
    }
    
    // Do not generate a DTO if this class is not published.
    if (!getMdTypeDAOIF().isPublished())
    {
      return;
    }
    else
    {
      super.go(forceRegeneration);
    }
  }
}
