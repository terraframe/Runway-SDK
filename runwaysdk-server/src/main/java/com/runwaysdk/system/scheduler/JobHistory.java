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
package com.runwaysdk.system.scheduler;

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
   * @MdMethod
   * 
   * Used to clear all NON RUNNING job history.
   */
  @Transaction
  public static void clearHistory()
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
