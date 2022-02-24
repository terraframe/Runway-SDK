/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.dataaccess.transaction.LocalTransactionArtifact;
import com.runwaysdk.query.OIterator;

public interface TransactionRecordDAOIF extends BusinessDAOIF, LocalTransactionArtifact
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                     = "transaction_record";

  /**
   * Returns the export sequence of the transaction.
   * 
   * @return export sequence of the transaction.
   */
  public Long getExportSequence();

  /**
   * Returns the file path for the xml file that contains the transaction items.
   *
   * @return file path for the xml file that contains the transaction items.
   */
  public String getFilePath();

  /**
   * Returns transaction items.  Client must close the iterator when finished.
   * 
   * @return transaction items.
   */
  public OIterator<BusinessDAOIF> getTransactionItems();
}
