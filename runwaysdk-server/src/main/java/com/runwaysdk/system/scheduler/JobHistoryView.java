package com.runwaysdk.system.scheduler;

import com.runwaysdk.query.QueryFactory;

public class JobHistoryView extends JobHistoryViewBase
{
  private static final long serialVersionUID = -2067034857;
  
  public JobHistoryView()
  {
    super();
  }
  
  public static JobHistoryViewQuery getJobHistories(String sortAttribute, Boolean isAscending, Integer pageSize, Integer pageNumber)
  {
    QueryFactory f = new QueryFactory();
    
    JobHistoryViewQuery query = new JobHistoryViewQuery(f, sortAttribute, isAscending, pageSize, pageNumber);
    
    return query;
  }
  
}
