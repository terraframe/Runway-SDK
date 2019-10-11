package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBRequest implements GraphRequest
{
  private ODatabaseSession dbSession;
  
  protected OrientDBRequest(ODatabaseSession dbSession)
  {
    this.dbSession = dbSession;
  }
  
  public ODatabaseSession getODatabaseSession()
  {
    return dbSession;
  }
  
  @Override
  public void beginTransaction()
  {
    if (!this.dbSession.isActiveOnCurrentThread())
    {
//      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().get();
      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().getIfDefined();

      this.dbSession.activateOnCurrentThread();
      
      this.dbSession.begin();

      if (database != null)
      {
        database.activateOnCurrentThread();
      }
    }
    else
    {
      this.dbSession.begin();
    }
  }
  
  @Override
  public void commit()
  {
    if (!this.dbSession.isActiveOnCurrentThread())
    {
//      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().get();
      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().getIfDefined();

      this.dbSession.activateOnCurrentThread();
      
      this.dbSession.commit();

      if (database != null)
      {
        database.activateOnCurrentThread();
      }
    }
    else
    {
      this.dbSession.commit();
    }
  }
  // ODatabaseRecordThreadLocal.instance().set(db)
//com.orientechnologies.orient.core.exception.ODatabaseException: The database instance is not set in the current thread. Be sure to set it with: ODatabaseRecordThreadLocal.instance().set(db);

  @Override
  public void rollback()
  {
    if (!this.dbSession.isActiveOnCurrentThread())
    { 
//      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().get();
      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().getIfDefined();
      
      this.dbSession.activateOnCurrentThread();
      
      this.dbSession.rollback();

      if (database != null)
      {
        database.activateOnCurrentThread();
      }
    }
    else
    {
      this.dbSession.rollback();
    }
  }
    

  @Override
  public void close()
  {
    if (!this.dbSession.isClosed())
    {
      if (!this.dbSession.isActiveOnCurrentThread())
      {
 //       ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().get();
        ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().getIfDefined();

        this.dbSession.activateOnCurrentThread();
        
        this.dbSession.close();

        if (database != null)
        {
          database.activateOnCurrentThread();
        }
      }
      else
      {
        this.dbSession.close();
      }
    }
  }
}
