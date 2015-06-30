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
package com.runwaysdk.dataaccess.resolver;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.system.Roles;

public class RoleResolver extends ConflictAdapter
{

  @Override
  public void resolve(final ImportConflict conflict)
  {
    new SynchronusExecutor(new Runnable()
    {
      @Override
      public void run()
      {
        EntityDAO entityDAO = conflict.getEntityDAO();
        
        if (entityDAO instanceof RoleDAO)
        {
          Attribute roleName = entityDAO.getAttribute(Roles.ROLENAME);

          String updateRoleName = roleName.getValue() + "_update";
          roleName.setValue(updateRoleName);
        }
        else
        {
          entityDAO.setKey(entityDAO.getAttribute(ComponentInfo.KEY).getValue()+"_update");          
        }

        strategy.apply(entityDAO);
      }
    }).start();
  }

}
