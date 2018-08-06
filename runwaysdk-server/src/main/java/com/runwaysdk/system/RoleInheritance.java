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
package com.runwaysdk.system;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;

public class RoleInheritance extends RoleInheritanceBase
{
  private static final long serialVersionUID = 1229405888043L;
  
  public RoleInheritance(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public RoleInheritance(com.runwaysdk.system.Roles parent, com.runwaysdk.system.Roles child)
  {
    this(parent.getOid(), child.getOid());
  }
 
  public void apply()
  {
    this.setKeyName(RoleDAO.buildInheritanceKey((RelationshipDAO)BusinessFacade.getEntityDAO(this)));
    
    super.apply();
  }
  
}
