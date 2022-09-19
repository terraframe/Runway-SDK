package com.runwaysdk.session;

import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;

public class ThreadRequestAspectState extends AbstractRequestAspectState
{
  public ThreadRequestAspectState()
  {
    super();
  }
  
  public void beforeThread(ThreadTransactionState threadTransactionState)
  {
    this.requestState = threadTransactionState.getRequestState();
  }


  public void afterReturning()
  {
    // do not close the session.
    // do not close the database object.

    ( LockObject.getLockObject() ).releaseAppLocks(setAppLocksSet);
    setAppLocksSet.clear();

    this.idMap.clear();
  }

  public void afterThrowing()
  {
    // do not close the session.
    // do not close the database object.

    ( LockObject.getLockObject() ).releaseAppLocks(setAppLocksSet);
    setAppLocksSet.clear();

    this.idMap.clear();
  }

}
