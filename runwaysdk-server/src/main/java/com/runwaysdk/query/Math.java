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

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.session.Session;

public abstract class Math extends SimpleFunction implements SelectableNumber
{
  private Statement statement;

  /**
   * Math function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  protected Math(Selectable selectable1, Selectable selectable2)
  {
    this(selectable1, selectable2, null, null);
  }

  /**
   * Math function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable1, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable1);

    this.statement = selectable2;
  }

  /**
   * Math function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected Math(int intValue, Selectable selectable)
  {
    this(intValue, selectable, null, null);
  }

  /**
   * Math function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateInteger(intValue);
  }

  /**
   * Math function.
   *
   * @param intValue
   * @param selectable
   */
  protected Math(Selectable selectable, int intValue)
  {
    this(selectable, intValue, null, null);
  }

  /**
   * Math function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateInteger(intValue);
  }

  /**
   * Math function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected Math(long longValue, Selectable selectable)
  {
    this(longValue, selectable, null, null);
  }

  /**
   * Math function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateLong(longValue);
  }

  /**
   * Math function.
   *
   * @param longValue
   * @param selectable
   */
  protected Math(Selectable selectable, long longValue)
  {
    this(selectable, longValue, null, null);
  }

  /**
   * Math function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateLong(longValue);
  }

  /**
   * Math function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected Math(float floatValue, Selectable selectable)
  {
    this(floatValue, selectable, null, null);
  }

  /**
   * Math function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateFloat(floatValue);
  }

  /**
   * Math function.
   *
   * @param floatValue
   * @param selectable
   */
  protected Math(Selectable selectable, float floatValue)
  {
    this(selectable, floatValue, null, null);
  }

  /**
   * Math function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateFloat(floatValue);
  }

  /**
   * Math function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected Math(double doubleValue, Selectable selectable)
  {
    this(doubleValue, selectable, null, null);
  }

  /**
   * Math function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateDouble(doubleValue);
  }

  /**
   * Math function.
   *
   * @param doubleValue
   * @param selectable
   */
  protected Math(Selectable selectable, double doubleValue)
  {
    this(selectable, doubleValue, null, null);
  }

  /**
   * Math function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected Math(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.validateNumericSelectable(selectable);

    this.statement = QueryUtil.formatAndValidateDouble(doubleValue);
  }


  /**
   * Throws {@link InvalidNumericSelectableException} when
   * @param _selectable
   */
  private void validateNumericSelectable(Selectable _selectable)
  {
    MdAttributeConcreteDAOIF selectableMdAttribute = _selectable.getMdAttributeIF();

    if (!(selectableMdAttribute instanceof MdAttributeNumberDAOIF) &&
        !(selectableMdAttribute instanceof MdAttributeBooleanDAOIF))
    {
      String errMsg = "Selectable ["+selectableMdAttribute.getDisplayLabel(Session.getCurrentLocale())+"] is not numeric and was passed into a numeric function.";
      throw new InvalidNumericSelectableException(errMsg, _selectable);
    }
  }

  /**
   * Calculates a display label for the result set.
   * @return display label for the result set.
   */
  protected String calculateDisplayLabel()
  {
    String displayLabel = this.getFunctionName()+"(";

    // Base case
    if (this.selectable instanceof Attribute)
    {
      displayLabel += this.getMdAttributeIF().getDisplayLabel(Session.getCurrentLocale());
    }
    else
    {
      displayLabel += ((Function)this.selectable).calculateDisplayLabel();
    }

    displayLabel += ",";

    if (this.statement instanceof Attribute)
    {
      Attribute statementAttribute = (Attribute)this.statement;
      displayLabel += statementAttribute.getMdAttributeIF().getDisplayLabel(Session.getCurrentLocale());
    }
    else if (this.statement instanceof StatementPrimitive)
    {
      StatementPrimitive statementPrimitive = (StatementPrimitive)this.statement;
      displayLabel += statementPrimitive.getSQL();
    }
    else if (this.statement instanceof Function)
    {
      displayLabel += ((Function)this.statement).calculateDisplayLabel();
    }


    displayLabel += ")";

    return displayLabel;
  }

  public Math clone() throws CloneNotSupportedException
  {
    return (Math) super.clone();
  }

  public String getSQL()
  {
    return "(" + this.selectable.getSQL() + this.getOperator() + this.statement.getSQL() + ")";
  }

  protected abstract String getOperator();

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
   *
   * @param attribute
   * @return Basic Condition object
   */
  public Condition EQ(SelectableNumber attribute)
  {
    return this.EQ((Selectable) attribute);
  }

  /**
   * Number Equals.
   *
   * @param function
   * @return Condition object.
   */
  public Condition EQ(AggregateFunction function)
  {
    return new SubSelectFunctionConditionEq(this, function);
  }

  /**
   * Number Equals.
   *
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
   * @param doubleValue
   * @return
   */
  public BasicCondition EQ(double doubleValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDouble(doubleValue);
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
   * @param intValue
   * @return
   */
  public BasicCondition NE(String value)
  {
    return new BasicConditionNotEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Not Equals.
   *
   * @param function
   * @return Condition object.
   */
  public Condition NE(AggregateFunction function)
  {
    return new SubSelectFunctionConditionNotEq(this, function);
  }

  /**
   * Number Not Equals.
   *
   * @param attribute
   * @return Basic Condition object
   */
  public Condition NE(SelectableNumber attribute)
  {
    return this.NE((Selectable) attribute);
  }

  /**
   * Number Not Equals.
   *
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition NE(SimpleFunction simpleFunction)
  {
    return super.NE((SimpleFunction) simpleFunction);
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
   * @param floatValue
   * @return
   */
  public BasicCondition NE(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionNotEq(this, statementPrimitive, false);
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
   * @param intValue
   * @return
   */
  public BasicCondition GT(String value)
  {
    return new BasicConditionGt(this, new StatementPrimitive(value));
  }

  /**
   * Number Greater Than.
   *
   * @param function
   * @return Condition object.
   */
  public Condition GT(AggregateFunction function)
  {
    return new SubSelectFunctionConditionGt(this, function);
  }

  /**
   * Number Greater Than.
   *
   * @param attribute
   * @return Basic Condition object
   */
  public Condition GT(SelectableNumber attribute)
  {
    return this.GT((Selectable) attribute);
  }

  /**
   * Number Greater Than.
   *
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition GT(SimpleFunction simpleFunction)
  {
    return this.GT((Selectable) simpleFunction);
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
   * @param floatValue
   * @return
   */
  public BasicCondition GT(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionGt(this, statementPrimitive);
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
   * @param intValue
   * @return
   */
  public BasicCondition GE(String value)
  {
    return new BasicConditionGtEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Greater Than or Equals.
   *
   * @param attribute
   * @return Basic Condition object
   */
  public Condition GE(SelectableNumber attribute)
  {
    return this.GE((Selectable) attribute);
  }

  /**
   * Number Greater Than or Equals.
   *
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition GE(SimpleFunction simpleFunction)
  {
    return this.GE((Selectable) simpleFunction);
  }

  /**
   * Number Greater Than Equal.
   *
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
   * @param floatValue
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
  public BasicCondition LT(String value)
  {
    return new BasicConditionLt(this, new StatementPrimitive(value));
  }

  /**
   * Number Less Than.
   *
   * @param function
   * @return Condition object.
   */
  public Condition LT(AggregateFunction function)
  {
    return new SubSelectFunctionConditionLt(this, function);
  }

  /**
   * Number Less Than.
   *
   * @param attribute
   * @return Basic Condition object
   */
  public Condition LT(SelectableNumber attribute)
  {
    return this.LT((Selectable) attribute);
  }

  /**
   * Number Less Than.
   *
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition LT(SimpleFunction simpleFunction)
  {
    return this.LT((Selectable) simpleFunction);
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
   * @param floatValue
   * @return
   */
  public BasicCondition LT(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionLt(this, statementPrimitive);
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
   * @param intValue
   * @return
   */
  public BasicCondition LE(String value)
  {
    return new BasicConditionLtEq(this, new StatementPrimitive(value));
  }

  /**
   * Number Less Than or Equal.
   *
   * @param attribute
   * @return Basic Condition object
   */
  public Condition LE(SelectableNumber attribute)
  {
    return this.LE((Selectable) attribute);
  }

  /**
   * Number Less Than or Equal.
   *
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition LE(SimpleFunction simpleFunction)
  {
    return this.LE((Selectable) simpleFunction);
  }

  /**
   * Number Less Than Equal.
   *
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
   * @param floatValue
   * @return
   */
  public BasicCondition LE(float floatValue)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateFloat(floatValue);
    return new BasicConditionLtEq(this, statementPrimitive);
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
}
