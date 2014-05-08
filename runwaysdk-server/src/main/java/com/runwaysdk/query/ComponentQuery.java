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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;

public abstract class ComponentQuery
{
  protected static String           selectIndent = "     ";

  protected QueryFactory            queryFactory;

  private String                    aliasSeed;

  protected Condition               queryCondition;

  protected List<OrderBy>           orderByList;

  /**
   * Whether this ComponentQuery participates in a <code>ValueQuery</code>
   * clause affects how joins are implemented.
   */
  protected ValueQuery              usedInValueQuery;

  /**
   * Key: attribute namespace + attribute Value ColumnInfo Object attribute
   * namespace example: package.Class.attributeName attribute namespace example:
   * package.Class.refAttributeName.attributeName
   */
  protected Map<String, ColumnInfo> columnInfoMap;

  protected ComponentQuery(QueryFactory queryFactory)
  {
    super();

    this.queryFactory = queryFactory;
    this.usedInValueQuery = null;
    init();
  }

  protected ComponentQuery(ValueQuery valueQuery)
  {
    this.queryFactory = valueQuery.getQueryFactory();
    this.usedInValueQuery = valueQuery;
    init();
  }

  private void init()
  {
    this.aliasSeed = ServerIDGenerator.nextID();

    this.queryCondition = null;
    this.orderByList = new LinkedList<OrderBy>();

    // Used for queries that select all attributes
    this.columnInfoMap = new HashMap<String, ColumnInfo>();
  }

  public Map<String, ColumnInfo> getColumnInfoMap()
  {
    return this.columnInfoMap;
  }

  /**
   * Returns the alias seed for this query. This is used to ensure proper
   * namespaces.
   * 
   * @return alias seed for this query. This is used to ensure proper
   *         namespaces.
   */
  protected String getAliasSeed()
  {
    return this.aliasSeed;
  }

  /**
   * Returns the query factory used by this ComponentQuery.
   * 
   * @return query factory used by this ComponentQuery.
   */
  public QueryFactory getQueryFactory()
  {
    return this.queryFactory;
  }

  /**
   * Indicates whether this <code>ComponentQuery</code> participates in a
   * <code>ValueQuery</code>, as it affects how joins are constructed.
   * 
   * @return true if this participates in a select clause, false otherwise.
   */
  protected boolean isUsedInValueQuery()
  {
    if (this.usedInValueQuery == null)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * Sets the <code>ValueQuery</code> where this <code>ComponentQuery</code>
   * participates in a <code>ValueQuery</code>.
   * 
   * @param valueQuery
   */
  protected void setUsedInValueQuery(ValueQuery valueQuery)
  {
    this.usedInValueQuery = valueQuery;
  }

  /**
   * Returns the <code>ValueQuery</code> where this <code>ComponentQuery</code>
   * participates in a <code>ValueQuery</code>. Returns null if this object does
   * not participate in a <code>ValueQuery</code>.
   * 
   * @return <code>ValueQuery</code> where this <code>ComponentQuery</code>
   *         participates in a <code>ValueQuery</code>, or null.
   */
  protected ValueQuery getUsedInValueQuery()
  {
    return this.usedInValueQuery;
  }

  /**
   * Returns the alias used in the query for the given table.
   * 
   * @param namespace
   *          used to determine where the table is used in the query. If the
   *          table is defined by the type queried by this ComponentQuery
   *          object, then the namespace is an empty String. If the table is
   *          used by a reference, field, struct, or enumeration, then the
   *          namespace involves the name of the attribute.
   * @param tableName
   * @return alias used in the query for the given table.
   */
  protected String getTableAlias(String namespace, String tableName)
  {
    return this.queryFactory.getTableAlias(this.aliasSeed + "-" + namespace, tableName);
  }

  /**
   * Returns the alias used in the query for the given column.
   * 
   * @param namespace
   *          used to determine where the column is used in the query. The
   *          namespace is normally the type that defines the attribute.
   * @param attributeName
   *          .
   * @return alias used in the query for the given table.
   */
  protected String getColumnAlias(String namespace, String columnName)
  {
    return this.queryFactory.getColumnAlias(this.aliasSeed + "-" + namespace, columnName);
  }

  /**
   * Resets any criteria conditions made on this query.
   */
  protected void resetConditions()
  {
    this.queryCondition = null;
  }

  /**
   * Adds a condition to this query. Will perform an AND with any prior
   * condition previously added.
   * 
   * @param condition
   *          condition to add.
   */
  public void WHERE(Condition condition)
  {
    this.AND(condition);
  }

  /**
   * Adds a condition to this query. Will perform an AND with any prior
   * condition previously added.
   * 
   * @param condition
   *          condition to add.
   */
  public void AND(Condition condition)
  {
    if (condition != null)
    {
      if (this.queryCondition == null)
      {
        this.queryCondition = condition;
      }
      else
      {
        this.queryCondition = new AND(this.queryCondition, condition);
      }
    }
  }

  /**
   * Adds a condition to this query. Will perform an AND with any prior
   * condition previously added.
   * 
   * @param condition
   *          condition to add.
   */
  public void OR(Condition condition)
  {
    if (condition != null)
    {
      if (this.queryCondition == null)
      {
        this.queryCondition = condition;
      }
      else
      {
        this.queryCondition = new OR(this.queryCondition, condition);
      }
    }
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute text statement object.
   */
  public abstract AttributeText aText(String name);

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute date statement object.
   */
  public abstract AttributeDate aDate(String name);

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute time statement object.
   */
  public abstract AttributeTime aTime(String name);

  /**
   * Returns an attribute datetime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute datetime statement object.
   */
  public abstract AttributeDateTime aDateTime(String name);

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute integer statement object.
   */
  public abstract AttributeInteger aInteger(String name);

  /**
   * Returns an attribute long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute long statement object.
   */
  public abstract AttributeLong aLong(String name);

  /**
   * Returns an attribute double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute double statement object.
   */
  public abstract AttributeDouble aDouble(String name);

  /**
   * Returns an attribute decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute decimal statement object.
   */
  public abstract AttributeDecimal aDecimal(String name);

  /**
   * Returns an attribute float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute float statement object.
   */
  public abstract AttributeFloat aFloat(String name);

  /**
   * Returns an attribute boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute boolean statement object.
   */
  public abstract AttributeBoolean aBoolean(String name);

  /**
   * Returns an attribute blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute blob statement object.
   */
  public abstract AttributeBlob aBlob(String name);

  /**
   * Returns an attribute Struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Struct statement object.
   */
  public abstract AttributeStruct aStruct(String name);

  /**
   * Returns an attribute Local character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Local character statement object.
   */
  public abstract AttributeLocal aLocalCharacter(String name);

  /**
   * Returns an attribute Local text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Local text statement object.
   */
  public abstract AttributeLocal aLocalText(String name);

  /**
   * Returns an attribute File statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute File statement object.
   */
  public abstract AttributeRef aFile(String name);

  /**
   * Returns an attribute Reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Reference statement object.
   */
  public abstract AttributeRef aReference(String name);

  /**
   * Returns an attribute Enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute Enumeration statement object.
   */
  public abstract AttributeRef aEnumeration(String name);

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute statement object.
   */
  public abstract AttributePrimitive getPrimitive(String name);

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @return Attribute statement object.
   */
  public abstract Attribute get(String name);

  /**
   * Sort in ascending order.
   * 
   * @param selectablePrimitive
   */
  public void ORDER_BY_ASC(SelectablePrimitive selectablePrimitive)
  {
    this.ORDER_BY(selectablePrimitive, OrderBy.SortOrder.ASC);
  }

  /**
   * Sort in ascending order.
   * 
   * @param selectablePrimitive
   */
  public void ORDER_BY_ASC(SelectablePrimitive selectablePrimitive, String sortAlias)
  {
    this.ORDER_BY(selectablePrimitive, OrderBy.SortOrder.ASC, sortAlias);
  }

  /**
   * Sort in descending order.
   * 
   * @param selectablePrimitive
   */
  public void ORDER_BY_DESC(SelectablePrimitive selectablePrimitive)
  {
    this.ORDER_BY(selectablePrimitive, OrderBy.SortOrder.DESC);
  }

  /**
   * Sort in descending order.
   * 
   * @param selectablePrimitive
   */
  public void ORDER_BY_DESC(SelectablePrimitive selectablePrimitive, String sortAlias)
  {
    this.ORDER_BY(selectablePrimitive, OrderBy.SortOrder.DESC, sortAlias);
  }

  /**
   * Adds an order by clause to this query.
   * 
   * @param attribute
   *          Attribute query object.
   */
  public void ORDER_BY(SelectablePrimitive selectablePrimitive, OrderBy.SortOrder order)
  {
    if (selectablePrimitive != null)
    {
      this.orderByList.add(new OrderBy(selectablePrimitive, order));
    }
  }

  /**
   * Adds an order by clause to this query.
   * 
   * @param attribute
   *          Attribute query object.
   */
  public void CLEAR_ORDER_BY()
  {
    this.orderByList.clear();
  }

  /**
   * Adds an order by clause to this query.
   * 
   * @param attribute
   *          Attribute query object.
   */
  public void ORDER_BY(SelectablePrimitive selectablePrimitive, OrderBy.SortOrder order, String sortAlias)
  {
    if (selectablePrimitive != null)
    {
      this.orderByList.add(new OrderBy(selectablePrimitive, order, sortAlias));
    }
  }

  /**
   * Returns a visitor that has traversed this structure.
   * 
   * @return visitor that has traversed this structure.
   */
  protected BuildSQLVisitor visitQuery()
  {
    BuildSQLVisitor visitor = new BuildSQLVisitor(this);

    this.accept(visitor);

    return visitor;
  }

  /**
   * Returns a visitor that has traversed this structure.
   * 
   * @return visitor that has traversed this structure.
   */
  protected void accept(Visitor visitor)
  {
    if (visitor.hasVisitedQuery(this))
    {
      return;
    }
    visitor.addVisitedQuery(this);

    if (this.queryCondition != null)
    {
      this.queryCondition.accept(visitor);
    }

    for (OrderBy orderBy : this.orderByList)
    {
      orderBy.getSelectable().accept(visitor);
    }
  }

  /**
   * Returns the SQL restriction criteria.
   * 
   * @return the SQL restriction criteria.
   */
  protected String getQueryConditionSQL()
  {
    if (this.queryCondition != null)
    {
      return this.queryCondition.getSQL();
    }
    else
    {
      return "";
    }
  }

  /**
   * Returns a Map with the tableName as the value and the table alias as the
   * key for any tables that need to be included in a query.
   * 
   * @return Map with the tableName as the value and the table alias as the key
   *         for any tables that need to be included in a query.
   */
  protected abstract Map<String, String> getFromTableMapInfoForQuery();

  /**
   * Subclasses can over ride this to do extra joint to get an accurate count
   */

  protected void addCustomJoins(Set<Join> tableJoinSet)
  {
    // Do nothing, subclassses can override this to do something
  }

  /**
   * Returns the count of the objects that match the specified criteria.
   * 
   * @return count of the objects that match the specified criteria.
   */
  public long getCount()
  {

    ResultSet resultSet = Database.query(this.getCountSQL());

    Long count = 0L;

    try
    {
      resultSet.next();
      String result = resultSet.getString("ct");
      count = new Long(result.toString());
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return count;
  }

  /**
   * Returns the SQL to count the objects that match the specified criteria.
   * 
   * @return sql for counting objects match the specified criteria.
   */
  public abstract String getCountSQL();

  /**
   * Builds a SQL query string to be used as a subselect in another query with
   * an IN operator.
   * 
   * @param selectClauseAttribute
   *          attribute that will apear in the select clause.
   * @param fromClauseAttribute
   *          attribute that will apear in any criteria to compare with the
   *          enclosing query.
   * @param furtherRestricted
   *          kind of a hack, but whatever. If true then the result will be
   *          futher restricted by the client. Therefore, the return string will
   *          end in either a "WHERE" or an "AND" so the client can append
   *          further query criteria.
   * @param ignoreCase
   *          true if an upper case function should be wrapped around the
   *          attribute in the select clause.
   * @return SQL query string to be used as a subselect in another query.
   */
  protected String getSubSelectSQL(Selectable selectClauseAttribute, Selectable fromClauseAttribute, boolean furtherRestricted, boolean ignoreCase)
  {
    BuildSQLVisitor visitor = this.visitQuery();
    fromClauseAttribute.accept(visitor);

    Set<Join> tableJoinSet = new HashSet<Join>();
    Map<String, String> fromTableMap = this.getFromTableMapInfoForQuery();

    tableJoinSet.addAll(visitor.tableJoinSet);

    StringBuffer selectStmt = null;
    if (ignoreCase)
    {
      selectStmt = new StringBuffer("SELECT " + Database.toUpperFunction(selectClauseAttribute.getSQL()) + "\n");
    }
    else
    {
      selectStmt = new StringBuffer("SELECT " + selectClauseAttribute.getSQL() + "\n");
    }

    selectStmt.append(this.buildFromClause(visitor, tableJoinSet, fromTableMap));

    List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

    StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
    StringBuffer queryCriteria = new StringBuffer(this.getQueryConditionSQL());

    sqlQueryClauses.add(joinClause);
    sqlQueryClauses.add(queryCriteria);

    this.appendQueryClauses(selectStmt, sqlQueryClauses);

    // Client will append additional query criteria.
    if (furtherRestricted)
    {
      if (joinClause.toString().trim().length() == 0 && queryCriteria.toString().trim().length() == 0)
      {
        selectStmt.append("\nWHERE ");
      }
      else
      {
        selectStmt.append("\nAND ");
      }
    }

    return selectStmt.toString();
  }

  /**
   * Builds a SQL query string to be used as a subselect in another query with
   * an IN operator.
   * 
   * @param selectClauseAttribute
   *          attribute that will apear in the select clause.
   * @param ignoreCase
   *          true if an upper case function should be wrapped around the
   *          attribute in the select clause.
   * @return SQL query string to be used as a subselect in another query.
   */
  protected String getSubSelectSQL(Selectable selectClauseAttribute, boolean ignoreCase)
  {
    BuildSQLVisitor visitor = this.visitQuery();

    Set<Join> tableJoinSet = new HashSet<Join>();
    Map<String, String> fromTableMap = this.getFromTableMapInfoForQuery();

    tableJoinSet.addAll(visitor.tableJoinSet);

    StringBuffer selectStmt = null;
    if (ignoreCase)
    {
      selectStmt = new StringBuffer("SELECT " + Database.toUpperFunction(selectClauseAttribute.getSQL()) + "\n");
    }
    else
    {
      selectStmt = new StringBuffer("SELECT " + selectClauseAttribute.getSQL() + "\n");
    }

    selectStmt.append(this.buildFromClause(visitor, tableJoinSet, fromTableMap));

    List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

    StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
    StringBuffer queryCriteria = new StringBuffer(this.getQueryConditionSQL());

    sqlQueryClauses.add(joinClause);
    sqlQueryClauses.add(queryCriteria);

    this.appendQueryClauses(selectStmt, sqlQueryClauses);

    return selectStmt.toString();
  }

  /**
   * Returns the SQL representation of this query, including all attributes of
   * the type in the select clause.
   * 
   * @return SQL representation of this query, including all attributes of the
   *         type in the select clause.
   */
  public String getSQL()
  {
    return this.getSQL(false, 0, 0);
  }

  /**
   * Returns the SQL representation of this query, including all attributes of
   * the type in the select clause.
   * 
   * @return SQL representation of this query, including all attributes of the
   *         type in the select clause.
   */
  public String getSQL(int limit, int skip)
  {
    return this.getSQL(true, limit, skip);
  }

  /**
   * Returns the SQL representation of this query, including all attributes of
   * the type in the select clause.
   * 
   * @return SQL representation of this query, including all attributes of the
   *         type in the select clause.
   */
  protected abstract String getSQL(boolean limitRowRange, int limit, int skip);

  /**
   * Build the select clause for this query (without the SELECT keyword),
   * including all attributes required to instantiate instances of this object.
   * 
   * @param mdAttributeIDMap
   *          Key: MdAttribute.getId() Value: MdAttributeIF
   * @return select clause for this query.
   */
  protected StringBuffer buildSelectClause(List<Selectable> _selectableList, Set<Join> tableJoinSet, Map<String, String> fromTableMap, Map<String, ColumnInfo> _columnInfoMap)
  {
    // Key: ID of an MdAttribute Value: MdEntity that defines the attribute;
    Map<String, MdEntityDAOIF> mdEntityMap = new HashMap<String, MdEntityDAOIF>();

    StringBuffer selectString = new StringBuffer("SELECT \n");

    this.appendDistinctToSelectClause(selectString);

    Set<String> hashSet = new HashSet<String>();

    // Order by fields must also be in the select clause.
    boolean firstIteration = true;

    for (Selectable selectable : _selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      String attributeQualifiedName = selectable.getFullyQualifiedNameSpace();

      ColumnInfo columnInfo = _columnInfoMap.get(attributeQualifiedName);

      hashSet.add(columnInfo.getColumnAlias());

      if (!firstIteration)
      {
        selectString.append(",\n");
      }

      this.buildSelectColumn(selectString, selectable, mdAttributeIF, columnInfo);

      if (componentQuery instanceof EntityQuery)
      {
        MdEntityDAOIF mdEntityIF = mdEntityMap.get(mdAttributeIF.getId());
        if (mdEntityIF == null)
        {
          mdEntityIF = (MdEntityDAOIF) mdAttributeIF.definedByClass();
          mdEntityMap.put(mdAttributeIF.getId(), mdEntityIF);
        }

        fromTableMap.put(columnInfo.getTableAlias(), columnInfo.getTableName());

        String baseTableName = mdEntityIF.getTableName();
        if (!columnInfo.getColumnName().equals(EntityDAOIF.ID_COLUMN) && !baseTableName.equals(columnInfo.getTableName()))
        {
          String baseTableAlias = componentQuery.getTableAlias("", baseTableName);
          Join tableJoin = new InnerJoinEq(EntityDAOIF.ID_COLUMN, baseTableName, baseTableAlias, EntityDAOIF.ID_COLUMN, columnInfo.getTableName(), columnInfo.getTableAlias());
          tableJoinSet.add(tableJoin);
        }
      }

      if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();
        String cacheAttributeQualifiedName = selectable.getAttributeNameSpace() + "." + cacheColumnName;
        ColumnInfo cacheColumnInfo = _columnInfoMap.get(cacheAttributeQualifiedName);
        selectString.append(",\n");

        this.buildSelectColumn(selectString, selectable, mdAttributeIF, cacheColumnInfo);
      }
      else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
      {
        MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
        MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
        List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

        if (componentQuery instanceof EntityQuery)
        {
          tableJoinSet.addAll(selectable.getJoinStatements());
        }

        for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
        {
          String structQualifiedAttributeName = attributeQualifiedName + "." + structMdAttributeIF.definesAttribute();
          ColumnInfo structColumnInfo = _columnInfoMap.get(structQualifiedAttributeName);

          if (componentQuery instanceof EntityQuery)
          {
            fromTableMap.put(structColumnInfo.getTableAlias(), structColumnInfo.getTableName());
          }
          selectString.append(",\n");

          Selectable structSelectable = ( (AttributeStruct) selectable ).attributeFactory(structMdAttributeIF.definesAttribute(), structMdAttributeIF.getType(), null, null);
          this.buildSelectColumn(selectString, structSelectable, structMdAttributeIF, structColumnInfo);

          // If the attribute is an enumeration, include the cache column
          if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
          {
            MdAttributeEnumerationDAOIF structEnumMdAttributeIF = (MdAttributeEnumerationDAOIF) structMdAttributeIF;
            String structEnumQualifiedAttributeName = attributeQualifiedName + "." + structEnumMdAttributeIF.getCacheColumnName();
            ColumnInfo structEnumColumnInfo = _columnInfoMap.get(structEnumQualifiedAttributeName);

            if (componentQuery instanceof EntityQuery)
            {
              fromTableMap.put(structEnumColumnInfo.getTableAlias(), structEnumColumnInfo.getTableName());
            }
            selectString.append(",\n");

            this.buildSelectColumn(selectString, structSelectable, structMdAttributeIF, structColumnInfo);
          }
        }
      }

      firstIteration = false;
    }

    this.addOrderByAttributesToSelectClause(selectString, hashSet, this.orderByList, firstIteration);

    return selectString;
  }

  protected void buildSelectColumn(StringBuffer selectString, Selectable selectable, MdAttributeConcreteDAOIF mdAttributeIF, ColumnInfo columnInfo)
  {
    if (selectable instanceof AggregateFunction || selectable instanceof SimpleFunction || selectable instanceof SelectableSubSelect || selectable instanceof SelectableSQL)
    {
      String columnAlias = Database.formatColumnAlias(columnInfo.getColumnAlias());

      selectString.append(selectIndent + selectable.getSQL() + " " + columnAlias);

    }
    else
    {
      selectString.append(selectIndent + columnInfo.getSelectClauseString(mdAttributeIF));
    }
  }

  /**
   * Adds the DISTINCT keyword to the select string, if required.
   * 
   * @param selectString
   */
  protected abstract void appendDistinctToSelectClause(StringBuffer selectString);

  /**
   * Determines what should be in the group by clause.
   */
  protected abstract void computeGroupByClauseForCount();

  /**
   * Adds selectables to the select clause when the count() function has a group
   * by clause.
   */
  protected abstract String addGroupBySelectablesToSelectClauseForCount();

  /**
   * Builds the group by clause for this query.
   * 
   * @return group by clause for this query.
   */
  protected abstract String buildGroupByClause();

  /**
   * Adds attributes to the select clause that are specified in the order by
   * clause.
   * 
   * @param selectString
   * @param hashSet
   *          used to determine if an attribute has already been added to the
   *          selet clause
   * @param _orderByList
   * @param firstIteration
   */
  protected void addOrderByAttributesToSelectClause(StringBuffer selectString, Set<String> hashSet, List<OrderBy> _orderByList, boolean firstIteration)
  {
    for (OrderBy orderBy : _orderByList)
    {
      // The attribute used in the order by clause has already been added to the
      // select clause.
      if (hashSet.contains(orderBy.getColumnAlias()))
      {
        continue;
      }

      if (!firstIteration)
      {
        selectString.append(",\n");
      }
      selectString.append(selectIndent + orderBy.getSelectClauseString());

      firstIteration = false;
    }
  }

  /**
   * 
   * @param limitRowRange
   * @param limit
   * @param skip
   * @param sqlStmt
   * @param _columnInfoMap
   * @param orderByClause
   * @return
   */
  protected StringBuffer appendOderByClause(boolean limitRowRange, int limit, int skip, StringBuffer sqlStmt, Map<String, ColumnInfo> _columnInfoMap, String orderByClause)
  {
    // Don't do anything if no columns were selected.
    if (_columnInfoMap.size() == 0)
    {
      return sqlStmt;
    }

    // Restrict the number of rows returned from the database.
    if (limitRowRange)
    {
      String selectClauseAttributes = "";
      boolean firstIteration = true;
      ColumnInfo fistColumnInfo = null;
      for (ColumnInfo columnInfo : _columnInfoMap.values())
      {
        if (!firstIteration)
        {
          selectClauseAttributes += ", ";
        }
        else
        {
          fistColumnInfo = columnInfo;
        }
        selectClauseAttributes += columnInfo.getColumnAlias();

        firstIteration = false;
      }

      if (orderByClause.trim().length() == 0)
      {
        orderByClause = "ORDER BY " + fistColumnInfo.getColumnAlias() + " ASC";
      }

      sqlStmt = Database.buildRowRangeRestriction(sqlStmt, limit, skip, selectClauseAttributes, orderByClause);
    }
    else
    {
      sqlStmt.append("\n" + orderByClause);
    }
    return sqlStmt;
  }

  /**
   * 
   * @param sqlStmt
   * @param sqlQueryClauses
   */
  protected void appendQueryClauses(StringBuffer sqlStmt, List<StringBuffer> sqlQueryClauses)
  {
    boolean setWhereClause = false;
    for (int k = 0; k < sqlQueryClauses.size(); k++)
    {
      if (sqlQueryClauses.get(k).length() > 0)
      {
        if (!setWhereClause)
        {
          sqlStmt.append("\nWHERE ");
          setWhereClause = true;
        }
        else
        {
          sqlStmt.append("\nAND ");
        }
        sqlStmt.append(sqlQueryClauses.get(k));
      }
    }
  }

  /**
   * Each instance of this object represents an entity for which a SQL query is
   * generated.
   * 
   * @author nathan
   */
  protected class QueryMdEntityInfo
  {
    private MdEntityDAOIF       mdEntityIF;

    private List<MdEntityDAOIF> excludeEntityTypeList;

    public QueryMdEntityInfo(MdEntityDAOIF mdEntityIF, List<MdEntityDAOIF> excludeEntityTypeList)
    {
      this.mdEntityIF = mdEntityIF;
      this.excludeEntityTypeList = excludeEntityTypeList;
    }

    /**
     * Returns the MdEntityIF object for which a query will be generated.
     * 
     * @return MdEntityIF object for which a query will be generated.
     */
    public MdEntityDAOIF getMdEntityIF()
    {
      return this.mdEntityIF;
    }

    /**
     * List of strings representing types that will be excluded from the query.
     * 
     * @return strings representing types that will be excluded from the query.
     */
    public List<MdEntityDAOIF> getExcludeEntityTypeList()
    {
      return this.excludeEntityTypeList;
    }
  }

  /**
   * Builids the order by clause of the query.
   * 
   * @return order by clause of the query.
   */
  protected String buildOrderByClause()
  {
    String orderByClause = "";
    if (orderByList.size() > 0)
    {
      orderByClause = "ORDER BY ";
      boolean firstIteration = true;
      for (OrderBy orderBy : orderByList)
      {
        if (!firstIteration)
        {
          orderByClause += ",";
        }
        else
        {
          firstIteration = false;
        }
        orderByClause += " " + getOrderBySQL(orderBy) + " ";
      }
      orderByClause += "\n";
    }

    return orderByClause;
  }

  public abstract String getOrderBySQL(OrderBy orderBy);

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
   * 
   * @param _columnInfoMap
   * @param attributePrimitive
   * @param componentQuery
   * @param columnAlias
   * @param fullyQualifiedAttributeNamespace
   * @param mdAttributeIF
   */
  protected void buildColumnInfoForAttribute(Map<String, ColumnInfo> _columnInfoMap, String attributeName, String columnName, String attributeDefiningTableName, String attributeDefiningTableAlias, String attributeNamespace, String columnAlias, String fullyQualifiedAttributeNamespace, ComponentQuery componentQuery, MdAttributeDAOIF mdAttributeIF)
  {
    ColumnInfo columnInfo = new ColumnInfo(attributeDefiningTableName, attributeDefiningTableAlias, columnName, columnAlias);

    _columnInfoMap.put(fullyQualifiedAttributeNamespace, columnInfo);

    if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();
      String cacheColumnAlias = componentQuery.getColumnAlias(attributeNamespace, cacheColumnName);

      String cacheAttributeQualifiedName = attributeNamespace + "." + cacheColumnName;

      ColumnInfo cacheColumnInfo = new ColumnInfo(attributeDefiningTableName, attributeDefiningTableAlias, cacheColumnName, cacheColumnAlias);
      _columnInfoMap.put(cacheAttributeQualifiedName, cacheColumnInfo);
    }
    else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      MdStructDAOIF mdStructIF = ( (MdAttributeStructDAOIF) mdAttributeIF ).getMdStructDAOIF();
      String structTableName = mdStructIF.getTableName();

      String structTableAlias = componentQuery.getTableAlias(attributeName, structTableName);

      List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.getAllDefinedMdAttributes();

      for (MdAttributeConcreteDAOIF mdAttributeStructIF : structMdAttributeList)
      {
        String structSelectAliasSpace = fullyQualifiedAttributeNamespace + "." + mdAttributeStructIF.definesAttribute();

        ColumnInfo structColumnInfo = new ColumnInfo(mdStructIF.getTableName(), structTableAlias, mdAttributeStructIF.getColumnName(), componentQuery.getColumnAlias(fullyQualifiedAttributeNamespace, mdAttributeStructIF.getColumnName()));

        _columnInfoMap.put(structSelectAliasSpace, structColumnInfo);

        // Put in a record for the cache column attribute.
        if (mdAttributeStructIF instanceof MdAttributeEnumerationDAOIF)
        {
          String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeStructIF ).getCacheColumnName();

          String structEnumSelectAliasSpace = fullyQualifiedAttributeNamespace + "." + cacheColumnName;

          String cacheColumnAlias = componentQuery.getColumnAlias(fullyQualifiedAttributeNamespace, cacheColumnName);

          ColumnInfo structCacheColumnInfo = new ColumnInfo(mdStructIF.getTableName(), structTableAlias, cacheColumnName, cacheColumnAlias);
          _columnInfoMap.put(structEnumSelectAliasSpace, structCacheColumnInfo);
        }

      } // for (MdAttributeIF mdAttributeStructIF : structMdAttributeList)
    } // if (mdAttributeIF instanceof MdAttributeStructIF)
  }

  /**
   * Builds the from clause for the query including all tables required to
   * instantiate instances of the object..
   * 
   * @param visitor
   * @param fromTableMap
   * @return from clause for the query.
   */
  protected StringBuffer buildFromClause(BuildSQLVisitor visitor, Set<Join> tableJoinSet, Map<String, String> fromTableMap)
  {
    fromTableMap.putAll(visitor.tableFromMap);

    Set<LeftJoin> leftOuterJoinSet = new HashSet<LeftJoin>();

    for (Join tableJoin : tableJoinSet)
    {
      if (tableJoin instanceof LeftJoin)
      {
        leftOuterJoinSet.add((LeftJoin) tableJoin);
      }
      else
      {
        fromTableMap.put(tableJoin.getTableAlias1(), tableJoin.getTableName1());
        fromTableMap.put(tableJoin.getTableAlias2(), tableJoin.getTableName2());
      }
    }

    return this.buildFromClauseFromMap(fromTableMap, leftOuterJoinSet);
  }

  /**
   * Builds a SQL FROM clause based on the values provided in the given map.
   * tables in the LeftOuterJionSet parameter are removed from the fromMap
   * parameter.
   * 
   * @param fromMap
   *          Key: Alias, Value: TableName
   */
  protected StringBuffer buildFromClauseFromMap(Map<String, String> fromMap, Set<LeftJoin> leftOuterJoinSet)
  {
    StringBuffer fromClause = new StringBuffer();
    fromClause.append("FROM ");

    boolean firstIteration = true;

    Map<String, List<LeftJoin>> leftJoinMap = new HashMap<String, List<LeftJoin>>();

    for (LeftJoin leftOuterTableJoin : leftOuterJoinSet)
    {

      if (!leftJoinMap.containsKey(leftOuterTableJoin.getTableAlias1()))
      {
        List<LeftJoin> leftOuterJoinList = new LinkedList<LeftJoin>();
        leftOuterJoinList.add(leftOuterTableJoin);
        leftJoinMap.put(leftOuterTableJoin.getTableAlias1(), leftOuterJoinList);
      }
      else
      {
        List<LeftJoin> leftOuterJoinList = leftJoinMap.get(leftOuterTableJoin.getTableAlias1());
        leftOuterJoinList.add(leftOuterTableJoin);
      }
    }

    for (String tableAlias : leftJoinMap.keySet())
    {

      List<LeftJoin> leftOuterJoinList = leftJoinMap.get(tableAlias);

      if (!firstIteration)
      {
        fromClause.append(",\n" + selectIndent);
      }

      LeftJoin leftOuterTableJoin_first = leftOuterJoinList.get(0);
      fromClause.append(leftOuterTableJoin_first.leftSideSQL());

      for (LeftJoin leftOuterTableJoin : leftOuterJoinList)
      {
        fromClause.append(leftOuterTableJoin.getSQL() + " ");

        fromMap.remove(leftOuterTableJoin.getTableAlias1());
        fromMap.remove(leftOuterTableJoin.getTableAlias2());
      }

      firstIteration = false;
    }

    for (String tableAlias : fromMap.keySet())
    {
      if (!firstIteration)
      {
        fromClause.append(",\n" + selectIndent);
      }
      fromClause.append(fromMap.get(tableAlias) + " " + tableAlias);
      firstIteration = false;
    }

    fromClause.append(" ");
    return fromClause;

  }

  /**
   * Builds the join clause for this query. Only joins tables where query
   * criteria have been specified, plus the table of the root of the hierarchy.
   * 
   * @return join clause for this query.
   */
  protected StringBuffer buildJoinClause(Set<Join> tableJoinSet)
  {
    StringBuffer joinClauseString = new StringBuffer("");

    boolean firstIteration = true;
    for (Join tableJoin : tableJoinSet)
    {
      if (tableJoin instanceof InnerJoin)
      {

        if (!firstIteration)
        {
          joinClauseString.append("\nAND ");
        }
        joinClauseString.append(tableJoin.getSQL());
        firstIteration = false;
      }
    }

    return joinClauseString;
  }

  /**
   * Checks if the given requested attribute exists and is of the correct type.
   * 
   * @param name
   *          name of the attribute
   * @param mdEntityIF
   *          metadata that defines the entity that defines the given attribute.
   * @param mdAttributeIF
   *          attribute metadata. If null then the entity that this query object
   *          represents does not define the attribute.
   * @param requestedType
   *          the type that the caller believes the attribute with the given
   *          name to be.
   */
  protected void checkValidAttributeRequest(String name, MdEntityDAOIF mdEntityIF, MdAttributeDAOIF mdAttributeIF, String requestedType)
  {
    if (mdAttributeIF == null)
    {
      String error = "Entity [" + mdEntityIF.definesType() + "] does not define attribute [" + name + "]";
      throw new QueryException(error);
    }

    if (!requestedType.equals(mdAttributeIF.getType()))
    {
      String error = "Attribute [" + name + "] is of type [" + mdAttributeIF.getType() + "], and not [" + requestedType + "] as requested";
      throw new QueryException(error);
    }
  }

  /**
   * Returns an iterator of Comoponets that match the query criteria specified
   * on this query object.
   * 
   * @return iterator of Comoponets that match the query criteria specified on
   *         this query object.
   */
  public abstract OIterator<? extends ComponentIF> getIterator();

  /**
   * Returns an iterator of Comoponets that match the query criteria specified
   * on this query object.
   * 
   * @param pageSize
   *          number of results per page
   * @param pageNumber
   *          page number
   * @return iterator of Comoponets that match the query criteria specified on
   *         this query object.
   */
  public abstract OIterator<? extends ComponentIF> getIterator(int pageSize, int pageNumber);

  /**
   * returns the row limit for a query with the given page size.
   * 
   * @param pageSize
   * @return row limit for a query with the given page size.
   */
  public static int rowLimit(int pageSize)
  {
    return pageSize;
  }

  /**
   * Returns the query row offset for the given page from a query with the given
   * page size.
   * 
   * @param pageSize
   * @param pageNumber
   * @return query row offset for the given page from a query with the given
   *         page size.
   */
  public static int rowSkip(int pageSize, int pageNumber)
  {
    if (pageNumber <= 0)
    {
      String error = "The page number [" + pageNumber + "] cannot be less than or equal to zero.";
      throw new QueryException(error);
    }
    return pageSize * ( pageNumber - 1 );
  }

  /**
   * Returns the hash code of the aliasSeed.
   * 
   * @return hash code of the aliasSeed.
   */
  public int hashCode()
  {
    return this.aliasSeed.hashCode();
  }

}
