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

import com.runwaysdk.dataaccess.attributes.value.MdAttributeCharacter_Q;


public abstract class StringFunction extends SimpleFunction implements SelectableChar
{
  /**
   *
   * @param function nested function.
   * @param selectable
   */
  protected StringFunction(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
    this.setMdAttributeIF(new MdAttributeCharacter_Q(this.selectable.getMdAttributeIF()));
  }

  // Equals
  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(String statement)
  {
    return this.privateEq(statement, false);
  }

  /**
   * Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableChar selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Equals IgnoreCase
  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQi(String statement)
  {
    return this.privateEq(statement, true);
  }

  /**
   * Equals Ignore Case.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQi(SelectableChar selectable)
  {
    return QueryUtil.EQi(this, selectable);
  }

  /**
   * Character comparison of statements.
   * @param statement
   * @param ignoreCase true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateEq(String statement, boolean ignoreCase)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getCharacterNullPrimitiveStatement(statement);
    return new BasicConditionEq(this, statementPrimitive, ignoreCase);
  }

  // Not Equals
  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(String statement)
  {
    return this.privateNotEq(statement, false);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition NE(SelectableChar selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NEi(String statement)
  {
    return this.privateNotEq(statement, true);
  }

  /**
   * Not Equals Ignore Case.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition NEi(SelectableChar selectable)
  {
    return QueryUtil.NEi(this, selectable);
  }

  /**
   * Character LIKE comparison of statements.
   * @param statementArray
   * @param ignoreCase true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateNotEq(String statement, boolean ignoreCase)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getCharacterNullPrimitiveStatement(statement);
    return new BasicConditionNotEq(this, statementPrimitive, ignoreCase);
  }

  // LIKE
  /**
   * Character LIKE comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LIKE(String statement)
  {
    return this.privateLike(statement, false);
  }
  /**
   * Character LIKE comparison case insensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LIKEi(String statement)
  {
    return this.privateLike(statement, true);
  }

  /**
   * Character LIKE comparison of statements.
   * @param statementArray
   * @param ignoreCase true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateLike(String statement, boolean ignoreCase)
  {
    String processedString = statement.replace('*', '%');
    StatementPrimitive statementPrimitive = QueryUtil.getCharacterPrimitiveStatement(processedString);
    return new BasicConditionLike(this, statementPrimitive, ignoreCase);
  }

  // NOT LIKE
  /**
   * Character NOT LIKE comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NLIKE(String statement)
  {
    return this.privateNotLike(statement, false);
  }
  /**
   * Character NOT LIKE comparison case insensitive.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NLIKEi(String statement)
  {
    return this.privateNotLike(statement, true);
  }

  /**
   * Character NOT LIKE comparison of statements.
   * @param statementArray
   * @param ignoreCase true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateNotLike(String statement, boolean ignoreCase)
  {
    String processedString = statement.replace('*', '%');
    StatementPrimitive statementPrimitive = QueryUtil.getCharacterPrimitiveStatement(processedString);
    return new BasicConditionNotLike(this, statementPrimitive, ignoreCase);
  }

  // IN
  /**
   * Character IN case sensitive comparison of statements.
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition IN(String ... statementArray)
  {
    return this.privateIn(statementArray, false);
  }
  /**
   * Character IN case insensitive comparison of statements.
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition INi(String ... statementArray)
  {
    return this.privateIn(statementArray, true);
  }

  /**
   * Character IN comparison of statements.
   * @param statementArray
   * @param ignoreCase true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateIn(String[] statementArray, boolean ignoreCase)
  {
    StatementPrimitive[] tempStatementArray = new StatementPrimitive[statementArray.length];
    for (int i=0; i<statementArray.length; i++)
    {
      tempStatementArray[i] = QueryUtil.getCharacterPrimitiveStatement(statementArray[i]);
    }
    return new BasicConditionIn(this, tempStatementArray, ignoreCase);
  }

  // NOT IN
  /**
   * Character NOT IN case sensitive comparison of statements.
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition NI(String ... statementArray)
  {
    return this.privateNotIn(statementArray, false);
  }
  /**
   * Character IN case insensitive comparison of statements.
   * @param statementArray
   * @return Basic Condition object
   */
  public BasicCondition NIi(String ... statementArray)
  {
    return this.privateNotIn(statementArray, true);
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
   * Character NOT IN comparison of statements.
   * @param statementArray
   * @param ignoreCase true if case sensitive or case insensitive comparison.
   * @return Basic Condition object
   */
  private BasicCondition privateNotIn(String[] statementArray, boolean ignoreCase)
  {
    StatementPrimitive[] tempStatementArray = new StatementPrimitive[statementArray.length];
    for (int i=0; i<statementArray.length; i++)
    {
      tempStatementArray[i] = QueryUtil.getCharacterPrimitiveStatement(statementArray[i]);
    }
    return new BasicConditionNotIn(this, tempStatementArray, ignoreCase);
  }

  public StringFunction clone() throws CloneNotSupportedException
  {
    return (StringFunction)super.clone();
  }

}
