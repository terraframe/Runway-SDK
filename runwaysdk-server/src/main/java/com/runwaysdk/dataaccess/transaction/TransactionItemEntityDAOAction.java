/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.transaction;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;

public class TransactionItemEntityDAOAction extends TransactionItemAction
{
  private String entityDAOid;
  
  private TransactionCacheIF transactionCacheIF;
  
  protected TransactionItemEntityDAOAction(ActionEnumDAO _action, EntityDAOIF _entityDAOIF, TransactionCacheIF _transactionCacheIF)
  {
    super(_action);
    this.entityDAOid = _entityDAOIF.getId();
    this.transactionCacheIF = _transactionCacheIF;
  }
  
  /**
   * Creates a new {@link TransactionItemEntityDAOAction} and copies the {@link EntityDAOIF}
   * from the given {@link TransactionItemEntityDAOAction} but with the given {@link ActionEnumDAO}
   * 
   * @return new {@link TransactionItemEntityDAOAction} 
   */
  protected static TransactionItemEntityDAOAction copy(TransactionItemEntityDAOAction _entityItemAction, ActionEnumDAO _action)
  {
    return factory(_action, _entityItemAction.getEntityDAO(), _entityItemAction.transactionCacheIF);
  }
  
  protected static TransactionItemEntityDAOAction factory(ActionEnumDAO _action, EntityDAOIF _entityDAOIF, TransactionCacheIF _transactionCacheIF)
  {
    if (_entityDAOIF instanceof BusinessDAO)
    {
      return new TransactionItemBusinessDAOAction(_action, _entityDAOIF, _transactionCacheIF);
    }
    else
    {
      return new TransactionItemEntityDAOAction(_action, _entityDAOIF, _transactionCacheIF);
    }
  }
  
  protected EntityDAOIF getEntityDAO()
  {
// Heads up: test pinch
// Original
//    return this.transactionCacheIF.getEntityDAO(this.entityDAOid);
// New
    return this.transactionCacheIF.getEntityDAOIFfromCache(this.entityDAOid);
  }
  
  protected String getEntityDAOid()
  {
    return this.entityDAOid;
  }
}
