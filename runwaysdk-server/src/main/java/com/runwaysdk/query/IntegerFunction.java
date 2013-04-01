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

import com.runwaysdk.dataaccess.attributes.value.MdAttributeInteger_Q;


public abstract class IntegerFunction extends SimpleFunction implements SelectableNumber
{
  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected IntegerFunction(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
    this.setMdAttributeIF(new MdAttributeInteger_Q(this.selectable.getMdAttributeIF()));
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition EQ(String value)
  {
    return new BasicConditionEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Equals.
   * @param attribute
   * @return Basic Condition object
   */
  public Condition EQ(SelectableNumber attribute)
  {
    return this.EQ((Selectable) attribute);
  }

  /**
   * Number Equals.
   * @param function
   * @return Condition object.
   */
  public Condition EQ(AggregateFunction function)
  {
    return new SubSelectFunctionConditionEq(this, function);
  }

  /**
   * Number Equals.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition EQ(SimpleFunction simpleFunction)
  {
    return this.EQ((Selectable) simpleFunction);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition EQ(int intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition NE(String value)
  {
    return new BasicConditionNotEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Not Equals.
   * @param function
   * @return Condition object.
   */
  public Condition NE(AggregateFunction function)
  {
    return new SubSelectFunctionConditionNotEq(this, function);
  }

  /**
   * Number Not Equals.
   * @param attribute
   * @return Basic Condition object
   */
  public Condition NE(SelectableNumber attribute)
  {
    return this.NE((Selectable)attribute);
  }

  /**
   * Number Not Equals.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition NE(SimpleFunction simpleFunction)
  {
    return this.NE((SimpleFunction)simpleFunction);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition NE(int intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GT(String value)
  {
    return new BasicConditionGt(this, new StatementPrimitive(value));
  }

  /**
   * Number Greater Than.
   * @param function
   * @return Condition object.
   */
  public Condition GT(AggregateFunction function)
  {
    return new SubSelectFunctionConditionGt(this, function);
  }

  /**
   * Number Greater Than.
   * @param attribute
   * @return Basic Condition object
   */
  public Condition GT(SelectableNumber attribute)
  {
    return this.GT((Selectable)attribute);
  }

  /**
   * Number Greater Than.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition GT(SimpleFunction simpleFunction)
  {
    return this.GT((Selectable)simpleFunction);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GT(int intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GE(String value)
  {
    return new BasicConditionGtEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Greater Than or Equals.
   * @param attribute
   * @return Basic Condition object
   */
  public Condition GE(SelectableNumber attribute)
  {
    return this.GE((Selectable)attribute);
  }

  /**
   * Number Greater Than or Equals.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition GE(SimpleFunction simpleFunction)
  {
    return this.GE((Selectable)simpleFunction);
  }

  /**
   * Number Greater Than Equal.
   * @param function
   * @return Condition object.
   */
  public Condition GE(AggregateFunction function)
  {
    return new SubSelectFunctionConditionGtEq(this, function);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GE(int intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LT(String value)
  {
    return new BasicConditionLt(this, new StatementPrimitive(value));
  }

  /**
   * Number Less Than.
   * @param function
   * @return Condition object.
   */
  public Condition LT(AggregateFunction function)
  {
    return new SubSelectFunctionConditionLt(this, function);
  }

  /**
   * Number Less Than.
   * @param attribute
   * @return Basic Condition object
   */
  public Condition LT(SelectableNumber attribute)
  {
    return this.LT((Selectable)attribute);
  }

  /**
   * Number Less Than.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition LT(SimpleFunction simpleFunction)
  {
    return this.LT((Selectable)simpleFunction);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LT(int intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LE(String value)
  {
    return new BasicConditionLtEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Less Than or Equal.
   * @param attribute
   * @return Basic Condition object
   */
  public Condition LE(SelectableNumber attribute)
  {
    return this.LE((Selectable)attribute);
  }

  /**
   * Number Less Than or Equal.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition LE(SimpleFunction simpleFunction)
  {
    return this.LE((Selectable)simpleFunction);
  }

  /**
   * Number Less Than Equal.
   * @param function
   * @return Condition object.
   */
  public Condition LE(AggregateFunction function)
  {
    return new SubSelectFunctionConditionLtEq(this, function);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LE(int intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  public IntegerFunction clone() throws CloneNotSupportedException
  {
    return (IntegerFunction)super.clone();
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
}
