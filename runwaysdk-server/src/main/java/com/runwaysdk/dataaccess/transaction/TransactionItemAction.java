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
/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.runwaysdk.dataaccess.transaction;

/**
 * This class is used to keep track if the given TransactionItem has been created,
 *  updated, or deleted during this transaction.  This information is used to update
 *  the cache when a transaction is completed.  TransactionItems that were created
 *  or updated are added to the cache.  TransactionItems that were deleted are
 *  removed from the cache.
 *
 * @author nathan
 *
 */
public abstract class TransactionItemAction
{
  private ActionEnumDAO action;

  /**
   *Constructor.
   * <br/><b>Precondition:</b>  (action == TransactionItemAction ||
   *                             action == TransactionItemAction)
   *
   * @param action 
   */
  protected TransactionItemAction(ActionEnumDAO action)
  {
    this.action = action;
  }

  /**
   * Returns the action performed on this transaction item
   * @return action performed on this transaction item
   */
  protected ActionEnumDAO getAction()
  {
    return this.action;
  }
}
