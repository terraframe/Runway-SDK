/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.resolver;

import java.util.Collection;

import com.runwaysdk.CompositeException;
import com.runwaysdk.RunwayException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.io.instance.ImportAction;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;

public abstract class PersistanceTemplate implements Runnable
{
  private ThreadTransactionState state;

  private EntityDAO              entityDAO;

  private IConflictResolver      resolver;

  private boolean                isNew;

  private boolean                appliedToDB;

  private Collection<Throwable>  attributeExceptions;

  public PersistanceTemplate(IConflictResolver resolver, ThreadTransactionState state, EntityDAO entityDAO, Collection<Throwable> attributeExceptions)
  {
    this.resolver = resolver;
    this.state = state;
    this.entityDAO = entityDAO;

    this.isNew = entityDAO.isNew();
    this.appliedToDB = entityDAO.isAppliedToDB();
    this.attributeExceptions = attributeExceptions;
  }

  protected EntityDAO getEntityDAO()
  {
    return this.entityDAO;
  }

  @Override
  public void run()
  {
    if (this.attributeExceptions.size() > 0)
    {
      String msg = "Exceptions have occured while importing attribute values";
      
      this.handleException(new CompositeException(msg, this.attributeExceptions));
    }
    else
    {
      try
      {
        SharedRequestExecutor<Void> executor = new SharedRequestExecutor<Void>(state)
        {
          @Override
          protected Void run()
          {
            execute();

            return null;
          }
        };

        executor.executeWithinTransaction();
      }
      catch (RunwayException ex)
      {
        this.handleException(ex);
      }
    }
  }

  private void handleException(RunwayException ex)
  {
    ImportAction action = this.getAction();

    entityDAO.rollbackState(isNew, appliedToDB);

    resolver.setPersistanceStrategy(new SharedRequestPersistanceStrategy(state));
    resolver.resolve(new ImportConflict(entityDAO, ex, action));
  }

  public abstract void execute();

  public abstract ImportAction getAction();
}
