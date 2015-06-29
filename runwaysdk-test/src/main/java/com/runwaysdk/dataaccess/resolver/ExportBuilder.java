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
package com.runwaysdk.dataaccess.resolver;

import java.io.File;
import java.util.LinkedList;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.constants.TransactionRecordInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionEventChangeListener;
import com.runwaysdk.dataaccess.transaction.TransactionExportManager;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public abstract class ExportBuilder<E>
{
  private String exportSequence;

  private String sourceSite;

  public E generate(String site, File file)
  {
    this.sourceSite = site;

    TestFixtureFactory.setDomain(site);

    ServerProperties.setLogTransactions(false);

    setup();

    ServerProperties.setLogTransactions(true);

    E value = this.doIt();

    TransactionExportManager.export(new LinkedList<String>(), CommonProperties.getTransactionXMLschemaLocation(), file, new TransactionEventChangeListener());

    this.saveExportSequence();

    this.undoIt();

    resetTransactions();

    return value;
  }

  private void saveExportSequence()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(TransactionRecordInfo.CLASS);
    query.ORDER_BY_ASC(query.getPrimitive(TransactionRecordInfo.EXPORT_SEQUENCE));

    OIterator<BusinessDAOIF> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        BusinessDAOIF record = it.next();

        this.exportSequence = record.getValue(TransactionRecordInfo.EXPORT_SEQUENCE);
      }
    }
    finally
    {
      it.close();
    }

  }

  protected void setup()
  {

  }

  protected abstract E doIt();

  protected abstract void undoIt();

  @Transaction
  public static void resetTransactions()
  {
    MdBusinessDAOIF mdBusTransactionItem = MdBusinessDAO.getMdBusinessDAO(TransactionItemInfo.CLASS);
    mdBusTransactionItem.getBusinessDAO().deleteAllRecords();

    MdBusinessDAOIF mdBusTransactionRecord = MdBusinessDAO.getMdBusinessDAO(TransactionRecordInfo.CLASS);
    mdBusTransactionRecord.getBusinessDAO().deleteAllRecords();

    QueryFactory qf = new QueryFactory();
    BusinessDAOQuery importLogQ = qf.businessDAOQuery(ImportLogInfo.CLASS);
    OIterator<BusinessDAOIF> i = importLogQ.getIterator();

    for (BusinessDAOIF importLogDAOIF : i)
    {
      importLogDAOIF.getBusinessDAO().delete();
    }

    Database.resetTransactionSequence();
  }

  public String getExportSequence()
  {
    return exportSequence;
  }

  public String getSourceSite()
  {
    return sourceSite;
  }

}
