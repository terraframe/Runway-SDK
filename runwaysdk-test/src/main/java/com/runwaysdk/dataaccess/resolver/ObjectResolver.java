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
package com.runwaysdk.dataaccess.resolver;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;

public class ObjectResolver extends ConflictAdapter
{

  @Override
  public void resolve(final ImportConflict conflict)
  {
    try
    {
      new SynchronusExecutor(new Runnable()
      {
        @Override
        public void run()
        {
          EntityDAO entityDAO = conflict.getEntityDAO();

          Attribute key = entityDAO.getAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
          String updateKey = key.getValue() + "_update";
          key.setValue(updateKey);

          strategy.apply(entityDAO);
        }
      }).start();
    }
    catch (RuntimeException e)
    {
      throw e;
    }
  }

}
