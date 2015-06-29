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

import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class MdRelationshipExportBuilder extends ExportBuilder<Void>
{
  protected MdBusinessDAO     parentBusiness;

  protected MdBusinessDAO     childBusiness;

  protected MdRelationshipDAO mdRelationship;

  protected BusinessDAO       parent;

  protected BusinessDAO       child;

  @Override
  protected Void doIt()
  {
    createTypesInTransaction();

    createObjectsInTransaction();

    return null;
  }

  @Transaction
  protected void createObjectsInTransaction()
  {
    parent = BusinessDAO.newInstance(parentBusiness.definesType());
    parent.apply();

    child = BusinessDAO.newInstance(childBusiness.definesType());
    child.apply();
  }

  @Transaction
  protected void createTypesInTransaction()
  {
    parentBusiness = TestFixtureFactory.createMdBusiness1();
    parentBusiness.apply();

    childBusiness = TestFixtureFactory.createMdBusiness2();
    childBusiness.apply();

    mdRelationship = TestFixtureFactory.createMdRelationship1(parentBusiness, childBusiness);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "1");
    mdRelationship.apply();
  }

  @Override
  protected void undoIt()
  {
    TestFixtureFactory.delete(mdRelationship);
    TestFixtureFactory.delete(parentBusiness);
    TestFixtureFactory.delete(childBusiness);
  }

  public MdBusinessDAO getParentBusiness()
  {
    return parentBusiness;
  }

  public MdBusinessDAO getChildBusiness()
  {
    return childBusiness;
  }

  public MdRelationshipDAO getMdRelationship()
  {
    return mdRelationship;
  }

  public BusinessDAO getParent()
  {
    return parent;
  }

  public BusinessDAO getChild()
  {
    return child;
  }
}
