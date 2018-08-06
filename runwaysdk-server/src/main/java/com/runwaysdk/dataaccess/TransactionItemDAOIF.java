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
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.transaction.ActionEnumDAO;
import com.runwaysdk.dataaccess.transaction.LocalTransactionArtifact;
import com.runwaysdk.dataaccess.transaction.TransactionItemDAO;

public interface TransactionItemDAOIF extends BusinessDAOIF, LocalTransactionArtifact
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE = "transaction_item";

  /**
   * Returns the <code>TransactionRecordDAOIF</code> that is the transaction
   * that this item belongs to.
   * 
   * @return <code>TransactionRecordDAOIF</code> that is the transaction that
   *         this item belongs to.
   */
  public TransactionRecordDAOIF getTransactionRecordDAOIF();

  public Boolean getIgnoreSequenceNumber();

  /**
   * Returns the metadata that defines component in the transaction.
   * 
   * @return metadata that defines component in the transaction.
   */
  public MdClassDAOIF getComponentMdClassDAOIF();

  /**
   * Returns the oid of the component.
   * 
   * @return oid of the component.
   */
  public String getComponentId();

  /**
   * Returns the seq of the component.
   * 
   * @return seq of the component.
   */
  public long getComponentSeq();

  /**
   * Returns the site master of the component.
   * 
   * @return site master of the component.
   */
  public String getComponentSiteMaster();

  /**
   * Returns the action on of this transaction item.
   * 
   * @return action on of this transaction item.
   */
  public ActionEnumDAO getItemAction();

  /**
   * Returns the XML that is the serialized actions that were performed on this
   * object.
   * 
   * @return XML that is the serialized actions that were performed on this
   *         object.
   */
  public String getXMLRecord();

  /**
   * 
   * @return
   */
  public TransactionItemDAO getBusinessDAO();
}
