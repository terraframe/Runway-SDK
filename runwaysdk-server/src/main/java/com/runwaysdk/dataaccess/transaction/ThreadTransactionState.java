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
package com.runwaysdk.dataaccess.transaction;

import com.runwaysdk.session.RequestState;

/**
 * A wrapper class that contains the state of the main request
 * and the main transaction.
 *
 * @author nathan
 *
 */
public class ThreadTransactionState
{
  private RequestState requestState;
  private TransactionState transactionState;

  public ThreadTransactionState(RequestState requestState, TransactionState transactionState)
  {
    this.requestState = requestState;
    this.transactionState = transactionState;
  }

  /**
   * Returns the request state of the main thread.
   *
   * @return request state of the main thread.
   */
  public RequestState getRequestState()
  {
    return this.requestState;
  }

  /**
   * Returns the transaction state of the main thread.
   *
   * @return transaction state of the main thread.
   */
  public TransactionState getTransactionState()
  {
    return this.transactionState;
  }

  public static ThreadTransactionState getCurrentThreadTransactionState()
  {
    RequestState requestState = RequestState.getCurrentRequestState();
    TransactionState transactionState = TransactionState.getCurrentTransactionState();

    return new ThreadTransactionState(requestState, transactionState);
  }
}
