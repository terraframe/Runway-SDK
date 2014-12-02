package com.runwaysdk.system.scheduler;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class JobHistory extends JobHistoryBase
{
  private static final long serialVersionUID = -1321109726;
  
  public JobHistory()
  {
    super();
  }
  
  /**
   * MdMethod
   * 
   * Used to clear all NON RUNNING job history.
   */
  @Authenticate
  public static void clearHistory()
  {
    clearHistoryInTransaction();
  }
  @Transaction
  private static void clearHistoryInTransaction()
  {
    JobHistoryQuery query = new JobHistoryQuery(new QueryFactory());
    OIterator<? extends JobHistory> jhs = query.getIterator();
    
    while (jhs.hasNext())
    {
      JobHistory jh = jhs.next();
      if (!jh.getStatus().contains(AllJobStatus.RUNNING))
      {
        jh.delete();
      }
    }
  }
  
}
