/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.query;

import java.util.Set;

import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;

public class AttributeBlob extends Attribute implements SelectableBlob
{
  protected AttributeBlob(MdAttributeBlobDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected AttributeBlob(MdAttributeBlobDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }

  @Override
  public BasicCondition EQ(String statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formattedValue = Database.formatJavaToSQL(statement, MdAttributeBlobInfo.CLASS, false);
      statementPrimitive = new StatementPrimitive(formattedValue);
    }
    return new BasicConditionEq(this, statementPrimitive);
  }

  public BasicCondition NE(String statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formattedValue = Database.formatJavaToSQL(statement, MdAttributeBlobInfo.CLASS, false);
      statementPrimitive = new StatementPrimitive(formattedValue);
    }
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    return null;
  }

  /**
   * Returns true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   * @return true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   */
  public boolean isAggregateFunction()
  {
    if (this.getAggregateFunction() != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the Blob version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

  public AttributeBlob clone() throws CloneNotSupportedException
  {
    return (AttributeBlob)super.clone();
  }
}
