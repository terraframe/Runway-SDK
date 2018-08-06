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
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.session.Request;

public abstract class RelationshipExportBuilder extends ExportBuilder<RelationshipDAO>
{
  private File        file;

  private BusinessDAO parent;

  private BusinessDAO child;

  private String      relationshipType;

  public RelationshipExportBuilder(File file, BusinessDAO parent, BusinessDAO child, String relationshipType)
  {
    super();

    this.file = file;
    this.parent = parent;
    this.child = child;
    this.relationshipType = relationshipType;
  }

  @Override
  @Request
  @Before
  public void setUp()
  {
    new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
  }

  @Request
  protected RelationshipDAO doIt()
  {
    RelationshipDAO relationship = inTransaction();

    return relationship;
  }

  @Transaction
  private RelationshipDAO inTransaction()
  {
    RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), relationshipType);

    relationship.apply();

    return relationship;
  }
}
