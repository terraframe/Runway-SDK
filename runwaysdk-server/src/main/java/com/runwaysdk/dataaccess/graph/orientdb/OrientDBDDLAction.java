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
