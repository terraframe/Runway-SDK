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
package com.runwaysdk.session;

import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;

public aspect ThreadRequestManagement extends AbstractRequestManagement
{
  public pointcut request()
  : (threadRequest(Request) && !cflow(nonThreadRequest()));

//  public pointcut request(Request request)
//  : (threadRequest(Request) && !cflow(nonThreadRequest(Request)) && @annotation(request));

  public pointcut enterSession()
  : request();
//  : request(Request);

  protected pointcut rootThreadRequest(ThreadTransactionState threadTransactionState)
  : request() && !cflowbelow(threadRequest(Request)) && args(threadTransactionState, ..);

//  protected pointcut rootThreadRequest(ThreadTransactionState threadTransactionState)
//  : request(Request) && !cflowbelow(threadRequest(Request)) && args(threadTransactionState, ..);

  before(ThreadTransactionState threadTransactionState) :rootThreadRequest(threadTransactionState)
  {
    this.requestState = threadTransactionState.getRequestState();
  }
//  protected pointcut topLevelPermission(String _sessionId)
//  :  (sessionRequest(Request, String)
//     && !cflowbelow(sessionRequest(Request, String))
//     ) && args(_sessionId, ..);

  after() returning : topLevelSession()
  {
    // do not close the session.
    // do not close the database object.

    ( LockObject.getLockObject() ).releaseAppLocks(setAppLocksSet);
    setAppLocksSet.clear();

    this.idMap.clear();
  }

  after() throwing : topLevelSession()
  {
    // do not close the session.
    // do not close the database object.

    ( LockObject.getLockObject() ).releaseAppLocks(setAppLocksSet);
    setAppLocksSet.clear();

    this.idMap.clear();
  }

}
