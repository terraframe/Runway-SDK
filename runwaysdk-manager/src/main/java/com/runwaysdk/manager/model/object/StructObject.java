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
package com.runwaysdk.manager.model.object;

import java.util.HashMap;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.manager.model.attributes.SimpleAttribute;

public class StructObject extends EntityObject
{

  public StructObject(StructDAOIF structDAO)
  {
    super(structDAO);
  }

  public StructObject(MdStructDAOIF mdStruct)
  {
    super(mdStruct);
  }

  public StructObject(MdStructDAOIF mdStruct, HashMap<String, SimpleAttribute> attributes)
  {
    super(mdStruct, attributes);
  }

  @Override
  public EntityDAO instance()
  {
    if (this.isNew() && !this.isAppliedToDB())
    {
      return PersistanceFacade.newInstance(this.getType());
    }

    return PersistanceFacade.get(this.getId()).getEntityDAO();
  }
}
