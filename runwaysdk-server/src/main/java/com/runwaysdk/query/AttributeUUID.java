/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.query;

import java.util.Set;

import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.database.Database;


public class AttributeUUID extends AttributePrimitive implements SelectableUUID
{
  protected AttributeUUID(MdAttributeUUIDDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected AttributeUUID(MdAttributeUUIDDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
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
   *            true if case sensitive or case insensitive comparison.
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
   *            true if case sensitive or case insensitive comparison.
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
   *            true if case sensitive or case insensitive comparison.
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
   *            true if case sensitive or case insensitive comparison.
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
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }
  


  /**
   * Formats and validates a character string.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeUUIDInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }
}

