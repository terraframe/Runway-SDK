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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdEntityDAOIF;

public abstract class EntityQueryDTOGenerator extends ComponentQueryDTOGenerator
{
  /**
   * 
   * @param mdEntityIF
   */
  public EntityQueryDTOGenerator(MdEntityDAOIF mdEntityIF)
  {   
    super(mdEntityIF);
  }

  /**
   * Returns the reference to the MdEntityIF object that defines the entity type
   * for which this object generates a query API object for.
   * @return reference to the MdEntityIF object that defines the entity type
   * for which this object generates a query API object for.
   */
  protected MdEntityDAOIF getMdClassIF()
  {
    return (MdEntityDAOIF)super.getMdClassIF();
  }
  
  public void go(boolean forceRegeneration)
  {   
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    { 
      return;
    }
    
    //  Check our special cases
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this.getMdClassIF()))
    {
      return;
    }
    else
    {
      super.go(forceRegeneration);
    }
  }
  
}
