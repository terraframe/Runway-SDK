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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ValueObject;

public interface Selectable extends Statement, Cloneable
{
  /**
   * blob equal comparison.
   * @param statement
   * @return Condition object
   */
  public Condition EQ(String statement);

  /**
   * blob not equal comparison.
   * @param statement
   * @return Condition object
   */
  public Condition NE(String statement);

  /**
   * Creates a subselect IN condition where this attribute and the given ValueQuery.
   *
   * @param selectable
   * @return Condition to add to the query.
   */
  public AttributeCondition SUBSELECT_IN(Selectable selectable);

  /**
   * Creates a subselect NOT IN condition where this attribute and the given ValueQuery.
   *
   * @param selectable
   * @return Condition to add to the query.
   */
  public AttributeCondition SUBSELECT_NOT_IN(Selectable selectable);

  /**
   * Returns the database column name representing this selectable.
   *
   * @return database column name representing this selectable.
   */
  public String getDbColumnName();

  /**
   * Returns the name of this selectable.
   *
   * @return name of this selectable.
   */
  public String _getAttributeName();

  /**
   * Returns the user defined alias for this Selectable.
   * @return user defined alias for this Selectable.
   */
  public String getUserDefinedAlias();

  /**
   * Sets the user defined alias for this Selectable.
   */
  public void setUserDefinedAlias(String userDefinedAlias);

  /**
   * Returns the user defined display label for this Selectable.
   * @return user defined display label for this Selectable.
   */
  public String getUserDefinedDisplayLabel();

  /**
   * Sets the user defined display label for this Selectable.
   */
  public void setUserDefinedDisplayLabel(String userDefinedDisplayLabel);

  /**
   * Returns the alias used in the select clause for the database column for this attribute.
   * @return alias used in the select clause for the database column for this attribute.
   */
  public String getColumnAlias();

  /**
   * Returns the name of the attribute used in the resultant {@link ValueObject}.
   * It is either the column alias or the user defined alias.
   * @return Returns the name of the attribute used in the resultant {@link ValueObject}.
   */
  public String getResultAttributeName();

  /**
   * Sets the alias of the column. Note that if the alias
   * already exists on another Selectable that no meaningful
   * error message will be given. It is up to the developer to
   * set this correctly.
   */
  public void setColumnAlias(String alias);

  /**
   * Returns the qualified name of the attribute.
   */
  public String getDbQualifiedName();

  /**
   * Returns the namespace of the attribute.
   * @return namespace of the attribute.
   */
  public String getAttributeNameSpace();

  /**
   * Returns the alias used in the select clause.
   * @return alias used in the select clause.
   */
  public String getFullyQualifiedNameSpace();

  /**
   * Returns the MdAttributeIF that defines the attribute.
   * @return MdAttributeIF that defines the attribute.
   */
  public MdAttributeConcreteDAOIF getMdAttributeIF();

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes();

  /**
   * Sets additional MdAttributes that are involved in building the select clause.
   * @param mdAttributeConcreteDAOIFList additional MdAttributes
   */
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList);

  /**
   * Returns the name of the database table that defines the column for this attribute.
   * @return name of the database table that defines the column for this attribute.
   */
  public String getDefiningTableName();

  /**
   * Returns the name of the alias used for the database table that defines the column for this attribute.
   * @return name of the alias used for the database table that defines the column for this attribute.
   */
  public String getDefiningTableAlias();

  /**
   * Every Selectable eventually boils down to an attribute.
   * @return bottom most attribute.
   */
  public Attribute getAttribute();

  /**
   * Returns the ComponentQuery from which this attribute was created.
   * @return ComponentQuery from which this attribute was created.
   */
  public ComponentQuery getRootQuery();

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction();

  /**
   * Returns true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   * @return true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   */
  public boolean isAggregateFunction();

  /**
   * Returns the SQL representation of this component.
   *
   * @return SQL representation of this component.
   */
  public String getSQL();

  /**
   * Returns the SQL required for this selectable in the lefthand side of a subselect clause.
   * @return SQL required for this selectable in the lefthand side of a subselect clause.
   */
  public String getSubSelectSQL();

  /**
   * Returns a Set of TableJoin objects that represent joins statements
   * that are required for this expression.
   * @return Set of TableJoin objects that represent joins statements
   * that are required for this expression, or null of there are none.
   */
  public Set<Join> getJoinStatements();

  /**
   * Returns a Map representing tables that should be included in the from clause,
   * where the key is the table alias and the value is the name of the table.
   * @return Map representing tables that should be included in the from clause,
   * where the key is the table alias and the value is the name of the table.
   */
  public Map<String, String> getFromTableMap();

  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor);

  public Selectable clone() throws CloneNotSupportedException;

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public Condition getCondition(String operator, String value);
  
  /**
   * Returns a {@link ColumnInfo} object that contains SQL specific attributes.
   * 
   * @return a {@link ColumnInfo} object that contains SQL specific attributes.
   */
  public ColumnInfo getColumnInfo();
  
  /**
   * Returns a {@link List} of {@link ColumnInfo} objects that contains SQL specific attributes. 
   * Some attributes, such as structs, map to more than one database column.
   * 
   * @return a {@link List} of {@link ColumnInfo} objects that contains SQL specific attributes.
   */
  public List<ColumnInfo> getColumnInfoList();
}