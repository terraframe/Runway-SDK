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

import java.io.File;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.session.Request;

public class ReferenceExportBuilder extends ExportBuilder<Void>
{
  private File          file;

  private MdBusinessDAO mdBusiness;

  private MdBusinessDAO referenceBusiness;

  public ReferenceExportBuilder(File file, MdBusinessDAO mdBusiness, MdBusinessDAO referenceBusiness)
  {
    super();
    this.file = file;
    this.mdBusiness = mdBusiness;
    this.referenceBusiness = referenceBusiness;
  }

  @Override
  protected void setup()
  {
    new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
  }

  @Request
  protected Void doIt()
  {
    createObjectsTransaction();

    return null;
  }

  @Transaction
  private void createObjectsTransaction()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "test");
    businessDAO.apply();

    BusinessDAO referenceDAO = BusinessDAO.newInstance(referenceBusiness.definesType());
    referenceDAO.setValue("testReference", businessDAO.getId());
    referenceDAO.apply();

    businessDAO.setValue("testReference", referenceDAO.getId());
    businessDAO.apply();
  }

  @Override
  protected void undoIt()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(referenceBusiness);
  }

}
