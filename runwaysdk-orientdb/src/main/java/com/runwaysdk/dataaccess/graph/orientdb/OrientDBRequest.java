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

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBRequest implements GraphRequest
{
  private ODatabaseSession dbSession;

  private boolean          isDDLRequest;

  protected OrientDBRequest(ODatabaseSession dbSession)
  {
    this.dbSession = dbSession;
    this.isDDLRequest = false;
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
      // ODatabaseDocument database = (ODatabaseDocument)
      // ODatabaseRecordThreadLocal.instance().get();
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

    // this.dbSession.begin();
  }

  @Override
  public void commit()
  {
    if (!this.dbSession.isActiveOnCurrentThread())
    {
      // ODatabaseDocument database = (ODatabaseDocument)
      // ODatabaseRecordThreadLocal.instance().get();
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
    //
    // System.out.println("Heads up: Graph: Commit GraphRequest:
    // "+this.isDDLRequest+" "+dbSession);
    // this.dbSession.commit();
    // System.out.println("COMPLETED");
  }
  // ODatabaseRecordThreadLocal.instance().set(db)
  // com.orientechnologies.orient.core.exception.ODatabaseException: The
  // database instance is not set in the current thread. Be sure to set it with:
  // ODatabaseRecordThreadLocal.instance().set(db);

  @Override
  public void rollback()
  {
    if (!this.dbSession.isActiveOnCurrentThread())
    {
      // ODatabaseDocument database = (ODatabaseDocument)
      // ODatabaseRecordThreadLocal.instance().get();
      ODatabaseDocument database = (ODatabaseDocument) ODatabaseRecordThreadLocal.instance().getIfDefined();
      //
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
    //
    // System.out.println("Heads up: Graph: Rollback GraphRequest:
    // "+this.isDDLRequest+" "+dbSession);
    // this.dbSession.rollback();
    // System.out.println("COMPLETED");
  }

  @Override
  public void close()
  {
    if (!this.dbSession.isClosed())
    {
      if (!this.dbSession.isActiveOnCurrentThread())
      {
        // ODatabaseDocument database = (ODatabaseDocument)
        // ODatabaseRecordThreadLocal.instance().get();
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

  @Override
  public boolean getIsDDLRequest()
  {
    return this.isDDLRequest;
  }

  @Override
  public void setIsDDLRequest()
  {
    this.isDDLRequest = true;
  }

}
