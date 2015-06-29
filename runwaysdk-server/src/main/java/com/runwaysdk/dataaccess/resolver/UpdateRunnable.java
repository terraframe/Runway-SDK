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

import java.util.Collection;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.io.instance.ImportAction;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;

public class UpdateRunnable extends PersistanceTemplate
{
  public UpdateRunnable(IConflictResolver resolver, ThreadTransactionState state, EntityDAO entityDAO, Collection<Throwable> attributeExceptions)
  {
    super(resolver, state, entityDAO, attributeExceptions);
  }

  @Override
  public void execute()
  {
    this.getEntityDAO().importSave();

    if (this.getEntityDAO() instanceof MdTypeDAO)
    {
      MdTypeDAO mdType = (MdTypeDAO) this.getEntityDAO();

      if (mdType.isNew())
      {
        mdType.markToWriteNewArtifact();
      }
    }
  }

  @Override
  public ImportAction getAction()
  {
    return ImportAction.APPLY;
  }

}
