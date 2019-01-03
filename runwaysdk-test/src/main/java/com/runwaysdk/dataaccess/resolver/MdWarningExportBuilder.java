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

import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class MdWarningExportBuilder extends ExportBuilder<MdWarningDAO>
{
  private MdWarningDAO mdWarning;

  @Override
  @Request
  protected MdWarningDAO doIt()
  {
    this.inTransaction();

    return mdWarning;
  }

  @Transaction
  private void inTransaction()
  {
    mdWarning = TestFixtureFactory.createMdWarning();
    mdWarning.apply();

    TestFixtureFactory.addCharacterAttribute(mdWarning).apply();
  }

  @Override
  protected void undoIt()
  {
    TestFixtureFactory.delete(mdWarning);
  }

}
