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

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class ParentExportBuilder extends ExportBuilder<Void>
{
  private RelationshipDAO relationship;

  private BusinessDAO     parent;

  private BusinessDAO     child;

  private String          parentType;

  private String          relationshipType;

  public ParentExportBuilder(String parentType, String relationshipType, BusinessDAO child)
  {
    this.parentType = parentType;
    this.relationshipType = relationshipType;
    this.child = child;
  }

  @Request
  protected Void doIt()
  {
    doItInTransaction();

    return null;
  }

  @Transaction
  protected void doItInTransaction()
  {
    parent = BusinessDAO.newInstance(parentType);
    parent.apply();

    relationship = RelationshipDAO.newInstance(parent.getId(), child.getId(), relationshipType);
    relationship.apply();
  }

  @Override
  protected void undoIt()
  {
    relationship.delete();
    parent.delete();
  }
}
