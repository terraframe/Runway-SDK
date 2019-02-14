/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.localization.configuration;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.dataaccess.EntityDAOIF;

public class AttributeLocalQueryCriteria
{
  private List<String> entityKeyMustInclude = new ArrayList<String>();
  
  private List<String> definingTypeMustNotInclude = new ArrayList<String>();
  
  public AttributeLocalQueryCriteria()
  {
    
  }
  
  public void definingTypeMustNotInclude(String badWord)
  {
    this.definingTypeMustNotInclude.add(badWord);
  }
  
  public void entityKeyMustInclude(String required)
  {
    this.entityKeyMustInclude.add(required);
  }
  
  public boolean shouldExportDefiningType(String definingType)
  {
    return !this.definingTypeMustNotInclude.contains(definingType);
  }
  
  public boolean shouldExportEntity(EntityDAOIF entity)
  {
    if (this.entityKeyMustInclude.size() == 0)
    {
      return true;
    }
    
    for (String mustInclude : this.entityKeyMustInclude)
    {
      if (entity.getKey().contains(mustInclude))
      {
        return true;
      }
    }
    
    return false;
  }
}
