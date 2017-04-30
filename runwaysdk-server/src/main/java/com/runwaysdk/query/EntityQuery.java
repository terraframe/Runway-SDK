/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;

public abstract class EntityQuery extends TableClassQuery implements HasAttributeFactory
{  
  /**
   * 
   * @param queryFactory
   * @param type
   */
  protected EntityQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
// Heads up: Test
//  this.init();
  }

  /**
   * 
   * @param valueQuery
   * @param type
   */
  protected EntityQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
// Heads up: Test
//    this.init();
  }

//Heads up: Test
//  private void init()
//  {
//
//  }

  public Map<String, ColumnInfo> getColumnInfoMap()
  {
    return this.columnInfoMap;
  }
  
  /**
   * Returns the {@link MdEntityDAOIF} that defines the type of objects that are queried from
   * this object.
   * 
   * @return {@link MdEntityDAOIF} that defines the type of objects that are queried from
   *         this object.
   */
  public MdEntityDAOIF getMdEntityIF()
  {
    return (MdEntityDAOIF)this.getMdTableClassIF();
  }

  /**
   * Returns the type that this object queries.
   * 
   * @return type that this object queries.
   */
  public String getType()
  {
    return this.type;
  }

  /**
   * Returns the type of components that are queried by this object.
   * 
   * @return type of components that are queried by this object.
   */
  public String getEntityType()
  {
    return this.type;
  }


  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute character statement object.
   */
  public AttributeCharacter id()
  {
    return this.id(null);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter id(String userDefinedAlias)
  {
    String name = EntityInfo.ID;
    MdAttributeDAOIF mdAttributeIF = this.getMdAttributeROfromMap(name);

    String definingTableName = this.getMdEntityIF().getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    Set<Join> attributeTableJoinSet = new HashSet<Join>();

    return new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, type, definingTableName, definingTableAlias, this, attributeTableJoinSet, userDefinedAlias, null);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable selectable)
  {
    return new LeftJoinEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable... selectableArray)
  {
    return new LeftJoinEq(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinNotEq LEFT_JOIN_NE(SelectableSingle selectable)
  {
    return new LeftJoinNotEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinNotEq LEFT_JOIN_NE(SelectableSingle... selectableArray)
  {
    return new LeftJoinNotEq(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinGt LEFT_JOIN_GT(SelectableSingle selectable)
  {
    return new LeftJoinGt(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinGt LEFT_JOIN_GT(SelectableSingle... selectableArray)
  {
    return new LeftJoinGt(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param entityQuery
   */
  public LeftJoinGtEq LEFT_JOIN_GE(SelectableSingle selectable)
  {
    return new LeftJoinGtEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param entityQuery
   */
  public LeftJoinGtEq LEFT_JOIN_GE(SelectableSingle... selectableArray)
  {
    return new LeftJoinGtEq(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinLt LEFT_JOIN_LT(SelectableSingle selectable)
  {
    return new LeftJoinLt(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinLt LEFT_JOIN_LT(SelectableSingle... selectableArray)
  {
    return new LeftJoinLt(this.id(), selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectable
   */
  public LeftJoinLtEq LEFT_JOIN_LE(SelectableSingle selectable)
  {
    return new LeftJoinLtEq(this.id(), selectable);
  }

  /**
   * Represents a join between tables in the query.
   * 
   * @param selectableArray
   */
  public LeftJoinLtEq LEFT_JOIN_LE(SelectableSingle... selectableArray)
  {
    return new LeftJoinLtEq(this.id(), selectableArray);
  }


  /**
   * @return returns a rank function 
   */
  public RANK RANK()
  {
    return new RANK(this);
  }
  
  /**
   * @return returns a rank function 
   */
  public RANK RANK(String userDefinedAlias)
  {
    return new RANK(this, userDefinedAlias);
  }
  
  /**
   * Returns the SQL representation of this query to be used within a left join.
   * <code>Selectable</code> array represents the attributes in the select
   * clause that come from this entity.
   * 
   * @return SQL representation of this query to be used within a left join.
   */
  protected String getSQL(List<Selectable> selectableList)
  {
    for (Selectable selectable : selectableList)
    {
      MdAttributeDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      if (mdAttributeIF instanceof MdAttributeConcrete_Q)
      {
        ( (MdAttributeConcrete_Q) mdAttributeIF ).resetAttributeNameAndLabel();
      }
    }

    BuildSQLVisitor visitor = this.visitQuery();
    for (Selectable selectable : selectableList)
    {
      selectable.accept(visitor);
    }

    Set<Join> tableJoinSet = new HashSet<Join>();

    tableJoinSet.addAll(visitor.tableJoinSet);

    Set<Selectable> leftAttributeSubSelectSet = visitor.leftAttributeSubSelectSet;

    StringBuffer sqlStmt = new StringBuffer();

    // Build the columnInfoMap for all attributes
    Map<String, ColumnInfo> _columnInfoMap = new HashMap<String, ColumnInfo>();
    this.getColumnInfoForSelectClause(selectableList, _columnInfoMap);

    Map<String, String> fromTableMap = new HashMap<String, String>();

    StringBuffer selectClause = this.buildSelectClause(selectableList, tableJoinSet, fromTableMap, _columnInfoMap);

    StringBuffer fromClause = this.buildFromClause(visitor, tableJoinSet, fromTableMap);

    this.addJoinForSubSelects(this.getMdEntityIF(), tableJoinSet, leftAttributeSubSelectSet);

    StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
    StringBuffer criteriaClause = new StringBuffer(this.getQueryConditionSQL());

    List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

    sqlQueryClauses.add(joinClause);
    sqlQueryClauses.add(criteriaClause);

    sqlStmt.append(selectClause);
    sqlStmt.append("\n" + fromClause);

    this.appendQueryClauses(sqlStmt, sqlQueryClauses);

    this.columnInfoMap = _columnInfoMap;

    return sqlStmt.toString();
  }

  /**
   * Initializes the columnInfoMap to include all attributes required to build a
   * select clause that includes all attributes of the component.
   */
  protected void getColumnInfoForSelectClause(List<Selectable> selectableList, Map<String, ColumnInfo> _columnInfoMap)
  {
    for (Selectable selectable : selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      String columnAlias = selectable.getColumnAlias();

      String fullyQualifiedAttributeNamespace = selectable.getFullyQualifiedNameSpace();

      MdAttributeDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      this.buildColumnInfoForAttribute(_columnInfoMap, selectable._getAttributeName(), selectable.getDbColumnName(), selectable.getDefiningTableName(), selectable.getDefiningTableAlias(), selectable.getAttributeNameSpace(), columnAlias, fullyQualifiedAttributeNamespace, componentQuery, mdAttributeIF);
    }
  }

  /**
   * Throws <code>QueryException</code> if this <code>EntityQuery</code> is used
   * in a <code>ValueQuery</code>.
   */
  protected void checkNotUsedInValueQuery()
  {
    if (this.isUsedInValueQuery())
    {
      String errMsg = "Entity query for [" + this.getType() + "]is used for value queries and cannot be used for querying objects.";
      throw new QueryException(errMsg);
    }
  }
}
