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

import java.math.BigDecimal;
import java.util.Set;

import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;


public class AttributeDecimal extends AttributeNumber implements SelectableDecimal
{
  protected AttributeDecimal(MdAttributeDecimalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected AttributeDecimal(MdAttributeDecimalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }

  // Equals
  /**
   * Decimal Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition EQ(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(bigDecimal);

    return new BasicConditionEq(this, statementPrimitive);
  }

  // Not Equals
  /**
   * Decimal Not Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition NE(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(bigDecimal);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  // Greater Than
  /**
   * Decimal Greater Than.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition GT(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionGt(this, statementPrimitive);
  }

  // Greater Than or Equal
  /**
   * Decimal Greater Than or Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition GE(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionGtEq(this, statementPrimitive);
  }

  // Less Than
  /**
   * Decimal Less Than.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition LT(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionLt(this, statementPrimitive);
  }

  // Less Than or Equal
  /**
   * Decimal Less Than or Equal.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition LE(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionLtEq(this, statementPrimitive);
  }


  /**
   * Validates and formats the given string into a decimal format for the
   * current database.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDecimalInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

}
