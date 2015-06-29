/**
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
 */
package com.runwaysdk.query;

import java.util.Date;

import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;
import com.runwaysdk.session.Session;

public class Coalesce extends SimpleFunction implements SelectableChar, SelectableNumber, SelectableMoment, SelectableBoolean
{
  private SelectableSingle[] optionalSelectableArray;

  /**
   * Coalesce function
   *
   * @param selectable  need at least one selectable
   * @param optionalSelectableArray optional additional selectables
   */
  protected Coalesce(SelectableSingle selectable, SelectableSingle...optionalSelectableArray)
  {
    super(selectable, null, null);

    this.init(selectable, optionalSelectableArray);
  }

  /**
   * Coalesce function
   *
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @param selectable  need at least one selectable
   * @param optionalSelectableArray optional additional selectables
   */
  protected Coalesce(String userDefinedAlias, String userDefinedDisplayLabel, SelectableSingle selectable, SelectableSingle...optionalSelectableArray)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.init(selectable, optionalSelectableArray);
  }

  /**
   * Common constructor method.
   *
   * @param selectable
   * @param optionalSelectableArray
   */
  private void init(SelectableSingle selectable, SelectableSingle... optionalSelectableArray)
  {
    this.optionalSelectableArray = optionalSelectableArray;

    this.setMdAttributeIF((MdAttributeConcrete_Q)Attribute.convertMdAttribute(selectable.getMdAttributeIF()));

    String attributeType = selectable.getMdAttributeIF().getMdAttributeConcrete().getType();

    for (SelectableSingle optionalSelectable : optionalSelectableArray)
    {
      if (!attributeType.equals(optionalSelectable.getMdAttributeIF().getMdAttributeConcrete().getType()))
      {
        String errMsg =
          "All selectables in the [Coalesce] function must be of the same type";
        throw new QueryException(errMsg);
      }
    }
  }

  @Override
  protected String getFunctionName()
  {
    return "COALESCE";
  }

  /**
   * Calculates a display label for the result set.
   * @return display label for the result set.
   */
  protected String calculateDisplayLabel()
  {
    String displayLabel = this.getFunctionName()+"(";

    displayLabel += this.calculateDisplayLabel(this.selectable);

    for (Selectable optionalSelectable : this.optionalSelectableArray)
    {
      displayLabel += ","+this.calculateDisplayLabel(optionalSelectable);
    }

    displayLabel += ")";

    return displayLabel;
  }

  private String calculateDisplayLabel(Selectable optionalSelectable)
  {
    if (optionalSelectable instanceof Attribute)
    {
      return optionalSelectable.getMdAttributeIF().getDisplayLabel(Session.getCurrentLocale());
    }
    else
    {
      return ((Function)optionalSelectable).calculateDisplayLabel();
    }
  }

  @Override
  public String getSQL()
  {
    String sql = this.getFunctionName()+"(";
    sql += this.selectable.getSQL();

    for (Selectable optionalSelectable : this.optionalSelectableArray)
    {
      sql += ", "+optionalSelectable.getSQL();
    }

    sql += ")";

    return sql;
  }

  @Override
  public String getDbQualifiedName()
  {
    return this.getSQL();
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    SelectableAggregate selectableAggregate = this.selectable.getAggregateFunction();

    if (selectableAggregate != null)
    {
      return selectableAggregate;
    }

    for (Selectable optionalSelectable : this.optionalSelectableArray)
    {
      selectableAggregate = optionalSelectable.getAggregateFunction();

      if (selectableAggregate != null)
      {
        return selectableAggregate;
      }
    }

    // Base case
    return null;
  }

  /**
   * Returns all selectables used in the {@link Coalesce} function.
   *
   * @return selectables used in the {@link Coalesce} function.
   */
  public SelectableSingle[] getSelectables()
  {
    SelectableSingle[] allSelectables = new SelectableSingle[this.optionalSelectableArray.length + 1];
    allSelectables[0] = (SelectableSingle)this.getSelectable();

    for (int i = 1; i<allSelectables.length; i++)
    {
      allSelectables[i] = this.optionalSelectableArray[i];
    }

    return allSelectables;
  }

  // Equals
  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public Condition EQ(String statement)
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

  /**
   * boolean equal comparison.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(Boolean statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);

    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Boolean Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableBoolean selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  /**
   * Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableMoment selectable)
  {
    return QueryUtil.EQ(this, selectable);
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

  /**
   * Date Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQdate(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDate(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * DateTime Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQdateTime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDateTime(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Time Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQtime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateTime(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Date Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition EQdate(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateNullPrimitiveStatement(date);
    return new BasicConditionEq(this, statementPrimitive);
  }


  /**
   * Same as <code>getDateTime</code> Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition EQ(Date date)
  {
    return EQdateTime(date);
  }

  /**
   * DateTime Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition EQdateTime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateTimeNullPrimitiveStatement(date);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Time Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition EQtime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getTimeNullPrimitiveStatement(date);
    return new BasicConditionEq(this, statementPrimitive);
  }

  // Equals IgnoreCase
  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Basic Condition object
   */
  public Condition EQi(String statement)
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
  private Condition privateEq(String statement, boolean ignoreCase)
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
  public Condition NE(String statement)
  {
    return this.privateNotEq(statement, false);
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
   * boolean not equal comparison.
   * @param statement
   * @return Basic Condition object
   */
  public Condition NE(Boolean statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableBoolean selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Date Not Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NEdate(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDate(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Same as <code>notEqDateTime</code>.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(Date date)
  {
    return NEdateTime(date);
  }

  /**
   * DateTime Not Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NEdateTime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDateTime(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Time Not Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NEtime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateTime(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Date Not Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition NEdate(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateNullPrimitiveStatement(date);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * DateTime Not Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition NEdateTime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateTimeNullPrimitiveStatement(date);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Time Not Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition NEtime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getTimeNullPrimitiveStatement(date);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition NE(SelectableMoment selectable)
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

  // Greater than
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
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableNumber selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * Date Greater than.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GTdate(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDate(statement);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * DateTime Greater than.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GTdateTime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDateTime(statement);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Time Greater than.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GTtime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateTime(statement);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Date Greater than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GTdate(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateNullPrimitiveStatement(date);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Same as <code>gtDateTime</code> Greater than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GT(Date date)
  {
    return GTdateTime(date);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableMoment selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableChar selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableBoolean selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * DateTime Greater than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GTdateTime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateTimeNullPrimitiveStatement(date);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Time Greater than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GTtime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getTimeNullPrimitiveStatement(date);
    return new BasicConditionGt(this, statementPrimitive);
  }

  // Greater than Eq
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
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableNumber selectable)
  {
    return QueryUtil.GE(this, selectable);
  }


  /**
   * Date Greater than Eq.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GEdate(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDate(statement);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * DateTime Greater than Eq.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GEdateTime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDateTime(statement);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Time Greater than Eq.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GEtime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateTime(statement);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Date Greater than Eq.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GEdate(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateNullPrimitiveStatement(date);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Same as <code>gtEqDateTime</code>.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GE(Date date)
  {
    return GEdateTime(date);
  }

  /**
   * DateTime Greater than Eq.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GEdateTime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateTimeNullPrimitiveStatement(date);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Time Greater than Eq.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GEtime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getTimeNullPrimitiveStatement(date);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableMoment selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableChar selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableBoolean selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  // Less than
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
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableNumber selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  /**
   * Date Less than.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LTdate(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDate(statement);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * DateTime Less than.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LTdateTime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDateTime(statement);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Time Less than.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LTtime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateTime(statement);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Date Less than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LTdate(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateNullPrimitiveStatement(date);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Same as <code>ltDateTime</code>
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LT(Date date)
  {
    return LTdateTime(date);
  }


  /**
   * DateTime Less than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LTdateTime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateTimeNullPrimitiveStatement(date);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Time Less than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LTtime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getTimeNullPrimitiveStatement(date);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableChar selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableMoment selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableBoolean selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  // Less than Eq
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
   * Date Less than Eq.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LEdate(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDate(statement);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * DateTime Less than Eq.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LEdateTime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateDateTime(statement);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Time Less than Eq.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LEtime(String statement)
  {
    StatementPrimitive statementPrimitive = QueryUtil.formatAndValidateTime(statement);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Date Less than Eq.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LEdate(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateNullPrimitiveStatement(date);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Same as <code>ltEqDateTime</code>.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LE(Date date)
  {
    return LEdateTime(date);
  }

  /**
   * DateTime Less than Eq.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LEdateTime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getDateTimeNullPrimitiveStatement(date);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Time Less than Eq.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LEtime(Date date)
  {
    StatementPrimitive statementPrimitive = QueryUtil.getTimeNullPrimitiveStatement(date);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableMoment selectable)
  {
    return QueryUtil.LE(this, selectable);
  }

  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableChar selectable)
  {
    return QueryUtil.LE(this, selectable);
  }
  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableBoolean selectable)
  {
    return QueryUtil.LE(this, selectable);
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  private StatementPrimitive getNullPrimitiveStatement(Boolean statement)
  {
    return QueryUtil.getNullPrimitiveStatement(statement);
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
    try
    {
      return QueryUtil.getCondition((Selectable)this, operator, value);
    }
    catch (InvalidComparisonOperator e)
    {
      return QueryUtil.getCondition((SelectableChar)this, operator, value);
    }
  }

  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    super.accept(visitor);

    for (Selectable optionalSelectable : this.optionalSelectableArray)
    {
      optionalSelectable.accept(visitor);
    }
  }

}
