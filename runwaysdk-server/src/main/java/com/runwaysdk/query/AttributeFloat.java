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

import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;


public class AttributeFloat extends AttributeNumber implements SelectableFloat
{
  protected AttributeFloat(MdAttributeFloatDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected AttributeFloat(MdAttributeFloatDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }

  // Equals
  /**
   * Float Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Float value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionEq(this, statementPrimitive);
  }

  // Not Equals
  /**
   * Float Not Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition NE(Float value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  // Greater Than
  /**
   * Float Greater Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GT(Float value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionGt(this, statementPrimitive);
  }



  // Greater Than or Equal
  /**
   * Float Greater Than or Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GE(Float value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionGtEq(this, statementPrimitive);
  }

  // Less Than
  /**
   * Float Less Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LT(Float value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionLt(this, statementPrimitive);
  }

  // Less Than or Equal
  /**
   * Float Less Than or Equal.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LE(Float value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Validates and formats the given string into a float format for the
   * current database.
   * @param statement
   * @return StatementPrimitive
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeFloat.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeFloatInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }
}
