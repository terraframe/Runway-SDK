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
package com.runwaysdk.query;

import java.util.Set;

import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.database.Database;

public class AttributeGraphRef extends Attribute implements SelectableUUID
{
  protected AttributeGraphRef(MdAttributeGraphRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  // Equals
  /**
   * Character = comparison case sensitive.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(String statement)
  {
    return this.privateEq(statement, false);
  }

  /**
   * Character Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableUUID selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Equals Ignore Case
  /**
   * Character = comparison case insensitive.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQi(String statement)
  {
    return this.privateEq(statement, true);
  }

  /**
   * Character = comparison case insensitive.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQi(SelectableUUID selectable)
  {
    return QueryUtil.EQi(this, selectable);
  }

  /**
   * Character comparison of statements.
   *
   * @param statement
   * @param ignoreCase
   *          true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateEq(String statement, boolean ignoreCase)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionEq(this, statementPrimitive, ignoreCase);
  }

  // Not Equals
  /**
   * Character != comparison case sensitive.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(String statement)
  {
    return this.privateNotEq(statement, false);
  }

  // Not Equals Ignore Case
  /**
   * Character != comparison case insensitive.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NEi(String statement)
  {
    return this.privateNotEq(statement, true);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableUUID selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Character != comparison case insensitive.
   *
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NEi(SelectableUUID selectable)
  {
    return QueryUtil.NEi(this, selectable);
  }

  /**
   * Character LIKE comparison of statements.
   *
   * @param statementArray
   * @param ignoreCase
   *          true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateNotEq(String statement, boolean ignoreCase)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionNotEq(this, statementPrimitive, ignoreCase);
  }

  // IN
  /**
   * Character IN case sensitive comparison of statements.
   *
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition IN(String... statementArray)
  {
    return this.privateIn(statementArray, false);
  }

  /**
   * Character IN case insensitive comparison of statements.
   *
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition INi(String... statementArray)
  {
    return this.privateIn(statementArray, true);
  }

  /**
   * Character IN comparison of statements.
   *
   * @param statementArray
   * @param ignoreCase
   *          true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateIn(String[] statementArray, boolean ignoreCase)
  {
    StatementPrimitive[] tempStatementArray = new StatementPrimitive[statementArray.length];
    for (int i = 0; i < statementArray.length; i++)
    {
      tempStatementArray[i] = this.getPrimitiveStatement(statementArray[i]);
    }
    return new BasicConditionIn(this, tempStatementArray, ignoreCase);
  }

  // NOT IN
  /**
   * Character NOT IN case sensitive comparison of statements.
   *
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition NI(String... statementArray)
  {
    return this.privateNotIn(statementArray, false);
  }

  /**
   * Character IN case insensitive comparison of statements.
   *
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition NIi(String... statementArray)
  {
    return this.privateNotIn(statementArray, true);
  }

  /**
   * Character NOT IN comparison of statements.
   *
   * @param statementArray
   * @param ignoreCase
   *          true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateNotIn(String[] statementArray, boolean ignoreCase)
  {
    StatementPrimitive[] tempStatementArray = new StatementPrimitive[statementArray.length];
    for (int i = 0; i < statementArray.length; i++)
    {
      tempStatementArray[i] = this.getPrimitiveStatement(statementArray[i]);
    }
    return new BasicConditionNotIn(this, tempStatementArray, ignoreCase);
  }

  /**
   * Returns a condition based on the String version of the operator and the
   * String version of the value.
   * 
   * @param operator
   * @param value
   * @return condition based on the String version of the operator and the
   *         String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

  /**
   * Formats and validates a character string.
   * 
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeUUIDInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Returns a primitive statement object that may represent a null comparison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * 
   * @param statement
   * @return primitive statement object that may represent a null comparison.
   */
  protected StatementPrimitive getNullPrimitiveStatement(Object statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      statementPrimitive = this.formatAndValidate(statement.toString());
    }

    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may not represent a null
   * comparrison.
   * 
   * @param statement
   * @return primitive statement object that may not represent a null
   *         comparrison.
   */
  protected StatementPrimitive getPrimitiveStatement(Object statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      String error = "Parameter may not be null";
      throw new QueryException(error);
    }
    else
    {
      statementPrimitive = this.formatAndValidate(statement.toString());
    }
    return statementPrimitive;
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if
   * there is one, or return null;
   * 
   * @return nested aggregate function in this composite function tree, if there
   *         is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    return null;
  }

  /**
   * Returns true if this selectable is an aggregate function or contains an
   * aggregate function. False otherwise.
   * 
   * @return true if this selectable is an aggregate function or contains an
   *         aggregate function. False otherwise.
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
}
