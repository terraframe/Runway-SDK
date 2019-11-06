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
package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public abstract class OrientDBDDLAction implements GraphDDLCommandAction
{
  private GraphRequest graphRequest;

  private GraphRequest ddlGraphDBRequest;
  
  

  /**
   * @param graphRequest
   * @param ddlGraphDBRequest
   */
  public OrientDBDDLAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest)
  {
    super();
    this.graphRequest = graphRequest;
    this.ddlGraphDBRequest = ddlGraphDBRequest;
  }

  public void execute()
  {
    try
    {
      OrientDBRequest ddlOrientDBRequest = (OrientDBRequest) this.ddlGraphDBRequest;
      ODatabaseSession db = ddlOrientDBRequest.getODatabaseSession();

      // make sure the DDL graph request is current on the active thread.
      db.activateOnCurrentThread();

      this.executeDDL(db);
    }
    finally
    {
      // make sure the DML graph request is current on the active thread.
      OrientDBRequest orientDBRequest = (OrientDBRequest) graphRequest;
      ODatabaseSession db = orientDBRequest.getODatabaseSession();
      db.activateOnCurrentThread();
    }
  }

  protected abstract void executeDDL(ODatabaseSession db);
}
