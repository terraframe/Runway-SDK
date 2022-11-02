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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.database.Database;

public class QueryUtil
{

  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param doubleValue
   * @return
   */
  protected static StatementPrimitive formatAndValidateDouble(double doubleValue)
  {
    String formattedValue = Database.formatJavaToSQL(Double.toString(doubleValue), MdAttributeDoubleInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }


  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param floatValue
   * @return
   */
  protected static StatementPrimitive formatAndValidateFloat(float floatValue)
  {
    String formattedValue = Database.formatJavaToSQL(Float.toString(floatValue), MdAttributeFloatInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param intValue
   * @return
   */
  protected static StatementPrimitive formatAndValidateInteger(int intValue)
  {
    String formattedValue = Database.formatJavaToSQL(Integer.toString(intValue), MdAttributeIntegerInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param longValue
   * @return
   */
  protected static StatementPrimitive formatAndValidateLong(long longValue)
  {
    String formattedValue = Database.formatJavaToSQL(Long.toString(longValue), MdAttributeLongInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }


  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param value
   * @return
   */
  protected static StatementPrimitive formatAndValidateCharacter(String value)
  {
    String formattedValue = Database.formatJavaToSQL(value, MdAttributeCharacterInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Validates and formats the given string into a date format for the
   * current database.
   * @param statement
   * @return
   */
  protected static StatementPrimitive formatAndValidateDate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDate.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid Date";
      throw new AttributeException(error);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDateInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Validates and formats the given string into a dateTime format for the
   * current database.
   * @param statement
   * @return
   */
  protected static StatementPrimitive formatAndValidateDateTime(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDateTime.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid DateTime";
      throw new AttributeException(error);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDateTimeInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Validates and formats the given string into a time format for the
   * current database.
   * @param statement
   * @return
   */
  protected static StatementPrimitive formatAndValidateTime(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeTime.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid Time";
      throw new AttributeException(error);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeTimeInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  protected static StatementPrimitive getCharacterNullPrimitiveStatement(Object statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      statementPrimitive = QueryUtil.formatAndValidateCharacter(statement.toString());
    }

    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param date
   * @return primitive statement object that may represent a null comparrison.
   */
  protected static StatementPrimitive getDateNullPrimitiveStatement(Date date)
  {
    StatementPrimitive statementPrimitive;

    if (date == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formatteDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(date);
      statementPrimitive = QueryUtil.formatAndValidateDate(formatteDate);
    }

    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param date
   * @return primitive statement object that may represent a null comparrison.
   */
  protected static StatementPrimitive getDateTimeNullPrimitiveStatement(Date date)
  {
    StatementPrimitive statementPrimitive;

    if (date == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formatteDate = new SimpleDateFormat(Constants.DATETIME_FORMAT).format(date);
      statementPrimitive = QueryUtil.formatAndValidateDateTime(formatteDate);
    }

    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param date
   * @return primitive statement object that may represent a null comparrison.
   */
  protected static StatementPrimitive getTimeNullPrimitiveStatement(Date date)
  {
    StatementPrimitive statementPrimitive;

    if (date == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formatteDate = new SimpleDateFormat(Constants.TIME_FORMAT).format(date);
      statementPrimitive = QueryUtil.formatAndValidateTime(formatteDate);
    }

    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may not represent a null comparrison.
   * @param statement
   * @return primitive statement object that may not represent a null comparrison.
   */
  protected static StatementPrimitive getCharacterPrimitiveStatement(Object statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      String error = "Parameter may not be null";
      throw new QueryException(error);
    }
    else
    {
      statementPrimitive = QueryUtil.formatAndValidateCharacter(statement.toString());
    }
    return statementPrimitive;
  }

  /**
   * Takes a boolean value and converts it into the runway internal
   * string representation.
   * @param boolValue
   * @return
   */
  protected static StatementPrimitive boolenToStatement(boolean boolValue)
  {
    if (boolValue)
    {
      return formatAndValidateBoolean(MdAttributeBooleanInfo.TRUE);
    }
    else
    {
      return formatAndValidateBoolean(MdAttributeBooleanInfo.FALSE);
    }
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  protected static StatementPrimitive getNullPrimitiveStatement(Boolean statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      statementPrimitive = boolenToStatement(statement);
    }
    return statementPrimitive;
  }

  /**
   * Validates and formats the given string into a boolean format for the
   * current database.
   * @param statement
   * @return StatementPrimitive
   */
  protected static StatementPrimitive formatAndValidateBoolean(String statement)
  {
    // make sure this is a valid boolean.
    com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean.validate(statement);
    String formattedValue = com.runwaysdk.constants.MdAttributeBooleanUtil.format(statement);
    String formattedValidatedValue = Database.formatJavaToSQL(formattedValue, MdAttributeBooleanInfo.CLASS, false);
    return new StatementPrimitive(formattedValidatedValue);
  }


  /**
   * Equal comparitor between selectables.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition EQ(Selectable leftSelectable, Selectable rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionEq(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionEq(leftSelectable, rightSelectable, false);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(leftSelectable, rightSelectable, false);
    }
    else
    {
      return new SubSelectBasicConditionEq(leftSelectable, rightSelectable, false);
    }
  }


  /**
   * Character = comparison case insensitive.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition EQi(Selectable leftSelectable, SelectableChar rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionEq(leftSelectable, (AggregateFunction) rightSelectable, true);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionEq(leftSelectable, rightSelectable, true);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(leftSelectable, rightSelectable, true);
    }
    else
    {
      return new SubSelectBasicConditionEq(leftSelectable, rightSelectable, true);
    }
  }
  
  public static Condition EQi(Selectable leftSelectable, SelectableUUID rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionEq(leftSelectable, (AggregateFunction) rightSelectable, true);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionEq(leftSelectable, rightSelectable, true);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(leftSelectable, rightSelectable, true);
    }
    else
    {
      return new SubSelectBasicConditionEq(leftSelectable, rightSelectable, true);
    }
  }

  /**
   * Not Equal comparitor between selectables.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition NE(Selectable leftSelectable, Selectable rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionNotEq(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionNotEq(leftSelectable, rightSelectable, false);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(leftSelectable, rightSelectable, false);
    }
    else
    {
      return new SubSelectBasicConditionNotEq(leftSelectable, rightSelectable, false);
    }
  }


  /**
   * Character != comparison case insensitive.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Basic Condition object
   */
  protected static Condition NEi(Selectable leftSelectable, SelectableChar rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionNotEq(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionNotEq(leftSelectable, rightSelectable, true);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(leftSelectable, rightSelectable, true);
    }
    else
    {
      return new SubSelectBasicConditionNotEq(leftSelectable, rightSelectable, true);
    }
  }

  /**
   * Character != comparison case insensitive.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Basic Condition object
   */
  protected static Condition NEi(Selectable leftSelectable, SelectableUUID rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionNotEq(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionNotEq(leftSelectable, rightSelectable, true);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(leftSelectable, rightSelectable, true);
    }
    else
    {
      return new SubSelectBasicConditionNotEq(leftSelectable, rightSelectable, true);
    }
  }
  

  /**
   * Greater Than
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition GT(Selectable leftSelectable, Selectable rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionGt(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionGt(leftSelectable, rightSelectable);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionGt(leftSelectable, rightSelectable, false);
    }
    else
    {
      return new SubSelectBasicConditionGt(leftSelectable, rightSelectable);
    }
  }


  /**
   * Moment Greater Than or Equals.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition GE(Selectable leftSelectable, Selectable rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionGtEq(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionGtEq(leftSelectable, rightSelectable);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionGtEq(leftSelectable, rightSelectable, false);
    }
    else
    {
      return new SubSelectBasicConditionGtEq(leftSelectable, rightSelectable);
    }
  }


  /**
   * Less Than or Equal.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition LE(Selectable leftSelectable, Selectable rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionLtEq(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionLtEq(leftSelectable, rightSelectable);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionLtEq(leftSelectable, rightSelectable, false);
    }
    else
    {
      return new SubSelectBasicConditionLtEq(leftSelectable, rightSelectable);
    }
  }


  /**
   * Less Than.
   *
   * @param leftSelectable
   * @param rightSelectable
   * @return Condition object
   */
  protected static Condition LT(Selectable leftSelectable, Selectable rightSelectable)
  {
    if (rightSelectable.isAggregateFunction())
    {
      return new SubSelectFunctionConditionLt(leftSelectable, (AggregateFunction) rightSelectable);
    }
    else if (leftSelectable.getRootQuery() == rightSelectable.getRootQuery())
    {
      return new BasicConditionLt(leftSelectable, rightSelectable);
    }
    else if (rightSelectable.getRootQuery().isUsedInValueQuery() || leftSelectable.getRootQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionLt(leftSelectable, rightSelectable, false);
    }
    else
    {
      return new SubSelectBasicConditionLt(leftSelectable, rightSelectable);
    }
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the Ref version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(Selectable selectable, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectable.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectable.NE(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.REF);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the Blob version of the value.
   *
   * @param selectableBlob
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableBlob selectableBlob, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableBlob.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableBlob.NE(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a blob.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.BLOB);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   *
   * @param selectableBoolean
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableBoolean selectableBoolean, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableBoolean.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableBoolean.NE(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a boolean.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.BOOLEAN);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   *
   * @param selectableChar
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableChar selectableChar, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableChar.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.EQUALS_IGNORE_CASE))
    {
      return selectableChar.EQi(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableChar.NE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS_IGNORE_CASE))
    {
      return selectableChar.NEi(value);
    }
    else if (trimmedOperator.equals(QueryConditions.LIKE))
    {
      return selectableChar.LIKE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.LIKE_IGNORE_CASE))
    {
      return selectableChar.LIKEi(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_LIKE))
    {
      return selectableChar.NLIKE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_LIKE_IGNORE_CASE))
    {
      return selectableChar.NLIKEi(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a string.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.STRING);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the Ref version of the value.
   *
   * @param selectableEnumeration
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableEnumeration selectableEnumeration, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableEnumeration.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableEnumeration.NE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.CONTAINS_ALL))
    {
      value = value.replace(" ", "");
      String[] enumIds = value.split(",");
      return selectableEnumeration.containsAll(enumIds);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_CONTAINS_ALL))
    {
      value = value.replace(" ", "");
      String[] enumIds = value.split(",");
      return selectableEnumeration.notContainsAll(enumIds);
    }
    else if (trimmedOperator.equals(QueryConditions.CONTAINS_ANY))
    {
      value = value.replace(" ", "");
      String[] enumIds = value.split(",");
      return selectableEnumeration.containsAny(enumIds);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_CONTAINS_ANY))
    {
      value = value.replace(" ", "");
      String[] enumIds = value.split(",");
      return selectableEnumeration.notContainsAny(enumIds);
    }
    else if (trimmedOperator.equals(QueryConditions.CONTAINS_EXACTLY))
    {
      value = value.replace(" ", "");
      String[] enumIds = value.split(",");
      return selectableEnumeration.containsExactly(enumIds);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a reference.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.REF);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   *
   * @param selectableMoment
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableMoment selectableMoment, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableMoment.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableMoment.NE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.GT_EQ))
    {
      return selectableMoment.GE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.GT))
    {
      return selectableMoment.GT(value);
    }
    else if (trimmedOperator.equals(QueryConditions.LT_EQ))
    {
      return selectableMoment.LE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.LT))
    {
      return selectableMoment.LT(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a moment.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.MOMENT);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   *
   * @param selectableNumber
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableNumber selectableNumber, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableNumber.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableNumber.NE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.GT_EQ))
    {
      return selectableNumber.GE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.GT))
    {
      return selectableNumber.GT(value);
    }
    else if (trimmedOperator.equals(QueryConditions.LT_EQ))
    {
      return selectableNumber.LE(value);
    }
    else if (trimmedOperator.equals(QueryConditions.LT))
    {
      return selectableNumber.LT(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a number.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.NUMBER);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the Ref version of the value.
   *
   * @param selectableStruct
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public static Condition getCondition(SelectableStruct selectableStruct, String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return selectableStruct.EQ(value);
    }
    else if (trimmedOperator.equals(QueryConditions.NOT_EQUALS))
    {
      return selectableStruct.NE(value);
    }

    String errMsg = "Operator ["+trimmedOperator+"] is invalid for a struct.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.STRUCT);
  }


}
