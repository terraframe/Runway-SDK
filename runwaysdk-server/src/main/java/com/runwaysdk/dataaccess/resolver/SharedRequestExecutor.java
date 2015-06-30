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
package com.runwaysdk.dataaccess.resolver;

import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionType;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

public abstract class SharedRequestExecutor<E>
{
  private ThreadTransactionState state;
  
  public SharedRequestExecutor(ThreadTransactionState state)
  {
    this.state = state;
  }

  public E executeWithinRequest()
  {
    return executeWithinRequest(state);
  }
  
  @Request(RequestType.THREAD)
  private E executeWithinRequest(ThreadTransactionState state)
  {
    return this.run();
  }  
  
  public E executeWithinTransaction()
  {
    return request(state);
  }
  
  @Request(RequestType.THREAD)
  private E request(ThreadTransactionState state)
  {
    return this.transaction(state);
  }

  @Transaction(TransactionType.THREAD)
  private E transaction(ThreadTransactionState state)
  {
    return this.run();
  }
  
  protected abstract E run();  
}
