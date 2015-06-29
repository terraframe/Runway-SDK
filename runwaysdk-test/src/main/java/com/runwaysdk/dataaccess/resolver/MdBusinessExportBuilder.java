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

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class MdBusinessExportBuilder extends ExportBuilder<MdBusinessDAO>
{
  private MdBusinessDAO           mdBusiness;

  private MdAttributeCharacterDAO mdAttribute;

  @Override
  protected MdBusinessDAO doIt()
  {
    createTypesInTransaction();

    return mdBusiness;
  }

  @Transaction
  private void createTypesInTransaction()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttribute.apply();
  }

  @Override
  protected void undoIt()
  {
    TestFixtureFactory.delete(mdBusiness);
  }

  public MdBusinessDAO getMdBusiness()
  {
    return mdBusiness;
  }
  
  public MdAttributeCharacterDAO getMdAttribute()
  {
    return mdAttribute;
  }
}
