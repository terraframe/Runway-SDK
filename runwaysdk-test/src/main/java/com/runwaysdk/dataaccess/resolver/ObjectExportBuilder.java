/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.resolver;

import java.io.File;

import org.junit.Before;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.session.Request;

public class ObjectExportBuilder extends ExportBuilder<BusinessDAO>
{
  protected MdBusinessExportBuilder builder;

  protected File                    file;

  private BusinessDAO               business;

  public ObjectExportBuilder(File file, MdBusinessExportBuilder builder)
  {
    this.file = file;
    this.builder = builder;
  }

  @Override
  @Request
  @Before
  public void setUp()
  {
    new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
  }

  @Override
  protected BusinessDAO doIt()
  {
    MdBusinessDAO mdBusiness = builder.getMdBusiness();
    MdAttributeCharacterDAO mdAttribute = builder.getMdAttribute();
    String attributeName = mdAttribute.definesAttribute();

    business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(attributeName, "TestValue");
    business.apply();

    return business;
  }

  public BusinessDAO getBusiness()
  {
    return business;
  }

  @Override
  protected void undoIt()
  {
    builder.undoIt();
  }
}
