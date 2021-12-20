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
package com.runwaysdk.manager.controller;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.io.instance.ImportAction;
import com.runwaysdk.dataaccess.resolver.ImportConflict;
import com.runwaysdk.manager.model.object.EntityObject;
import com.runwaysdk.manager.model.object.IEntityObject;
import com.runwaysdk.manager.model.object.PersistanceFacade;
import com.runwaysdk.manager.view.IAdminModule;

public class Conflict
{
  private ImportConflict conflict;

  private IAdminModule   module;

  private ImportWorker   worker;

  private boolean        resolved;

  public Conflict(ImportConflict conflict, IAdminModule module, ImportWorker worker)
  {
    this.resolved = false;

    this.module = module;
    this.conflict = conflict;
    this.worker = worker;
  }

  public void display()
  {
    Runnable runnable = new Runnable()
    {
      @Override
      public void run()
      {
        EntityDAO entityDAO = conflict.getEntityDAO();
        ImportAction action = conflict.getAction();

        module.pause(worker.getTaskName());

        String msg = PersistanceFacade.getLocalizedMessage(conflict.getException());

        ViewConfig config = new ViewConfig(false, true, msg);

        // If this is a conflict on a delete action occurs then we do not want
        // to allow them to apply the deleted object to resolve the conflict.
        if (action.equals(ImportAction.DELETE))
        {
          config.removeImportAction(ImportAction.APPLY);
        }
        // If this is a conflict on an apply then we only want to allow them to
        // delete the object if it is a relationship. This is required because
        // the conflict may be on the parent or child entity. In this situation
        // there is no way to resolve the conflict. As such the relationship
        // must be deleted and then a new one created with the appropriate new
        // parent and child
        else if (action.equals(ImportAction.APPLY) && ! ( entityDAO instanceof RelationshipDAO ))
        {
          config.removeImportAction(ImportAction.DELETE);
        }

        IEntityObject entityObject = EntityObject.get(entityDAO);
        entityObject.setImport(true);

        module.edit(entityObject, config);
      }
    };

    module.syncExec(runnable);
  }

  public void resolved()
  {
    resolved = true;
  }

  public boolean isResolved()
  {
    return resolved;
  }

}
