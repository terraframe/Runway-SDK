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

import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;



public abstract class AggregateFunction extends Function implements Expression, SelectableAggregate
{
  /**
   *
   * @param selectablePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected AggregateFunction(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this(entityQuery.id(), userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected AggregateFunction(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.setMdAttributeIF(this.selectable);

    if (selectable.isAggregateFunction())
    {
      String errMsg = "Aggregate functions cannot directly, or indirectly, contain another aggregate function: ["+this.calculateDisplayLabel()+"].";
      throw new QueryException(errMsg);
    }
  }

  protected void setMdAttributeIF(Selectable attribute)
  {
    this.setMdAttributeIF((MdAttributeConcrete_Q)Attribute.convertMdAttribute(attribute.getMdAttributeIF()));
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition EQ(String value)
  {
    return new BasicConditionEq(this, new StatementPrimitive(value), false);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition EQ(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  // Equals
  /**
   * Double Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Double value)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(value);

    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   *
   * @param floatValue
   * @return
   */
  public BasicCondition EQ(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param floatValue
   * @return
   */
  public BasicCondition EQ(Float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionEq(this, statementPrimitive, false);
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
  public BasicCondition EQ(Integer intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition EQ(long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition EQ(Long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionEq(this, statementPrimitive, false);
  }

  /**
   * Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableNumber selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Not Equals
  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition NE(String value)
  {
    return new BasicConditionNotEq(this, new StatementPrimitive(value), false);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition NE(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition NE(Double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition NE(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition NE(Float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
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
  public BasicCondition NE(Integer intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition NE(long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition NE(Long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition NE(SelectableNumber selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  // Greater Than
  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition GT(String value)
  {
    return new BasicConditionGt(this, new StatementPrimitive(value));
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition GT(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition GT(Double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GT(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GT(Float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionGt(this, statementPrimitive);
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
  public BasicCondition GT(Integer intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition GT(long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition GT(Long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableNumber selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  // Greater Than Equals
  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition GE(String value)
  {
    return new BasicConditionGtEq(this, new StatementPrimitive(value));
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition GE(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition GE(Double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GE(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition GE(Float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionGtEq(this, statementPrimitive);
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
  public BasicCondition GE(Integer intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition GE(long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition GE(Long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableNumber selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  // Less Than
  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition LT(String value)
  {
    return new BasicConditionLt(this, new StatementPrimitive(value));
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition LT(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition LT(Double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LT(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LT(Float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionLt(this, statementPrimitive);
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
  public BasicCondition LT(Integer intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition LT(long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition LT(Long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableNumber selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  // Less Than Equals
  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition LE(String value)
  {
    return new BasicConditionLtEq(this, new StatementPrimitive(value));
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition LE(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   *
   * @param doubleValue
   * @return
   */
  public BasicCondition LE(Double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LE(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LE(Float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionLtEq(this, statementPrimitive);
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

  /**
   *
   * @param intValue
   * @return
   */
  public BasicCondition LE(Integer intValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateInteger(intValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition LE(long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   *
   * @param longValue
   * @return
   */
  public BasicCondition LE(Long longValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateLong(longValue);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableNumber selectable)
  {
    return QueryUtil.LE(this, selectable);
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

  public AggregateFunction clone() throws CloneNotSupportedException
  {
    return (AggregateFunction)super.clone();
  }
}
