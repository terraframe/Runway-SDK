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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.sql.MdAttributeConcrete_SQL;

public class ValueQuery extends ComponentQuery
{
  // contains all selectables put into the select clause
  private List<Selectable>              selectableList;

  // The actual selectables used in the select clause. They differ sometimes.
  private List<Selectable>              selectableUsedInSelectClause;

  // Key: fully qualified attribute name
  private Map<String, SelectableSingle> selectableSinglesInSelectMap;

  // Key: fully qualified selectable name
  // Processed selectables have had their database aliases and display labels
  // updated
  private Map<String, Selectable>       processedSelectableMap;

  // Key: fully qualified selectable name
  // These are the original selectables specified in the select clause.
  private Map<String, Selectable>       rawSelectableMap;

  // Key: aliasSeed of the ComponentQuery
  private Map<String, List<Selectable>> getRawSelectableComponentQueryMap;

  // Key: aliasSeed of the EntityQuery
  private Map<String, TableClassQuery>  entityQueryInRightSideOfLeftJoin;

  private Set<LeftJoin>                 leftOuterJoinSet;

  private Set<InnerJoin>                customInnerJoinSet;

  // Key: columnAlias
  private Map<String, SelectableSingle> groupByAttributeMap;

  private List<SelectableAggregate>     selectableAggregateList;

  private List<String>                  groupByList;

  // ValueQueries from selectables in select clause
  private Set<ValueQuery>               fromClauseValueQueriesInSelectClause;

  private boolean                       selectDistinct;

  // this is a string that is appended before the query
  // used for with queries and the like
  private String                        sqlPrefix                 = "";

  // There are very rare scenarios where it is useful to execute a list of sql
  // statements before
  // the query can be invoked. For example, DDMS ticket #3577
  private String                        dependentPreSqlStatements = "";

  private Condition                     havingCondition;

  private String                        tableAlias;

  private static Map<String, PluginIF>  pluginMap                 = new ConcurrentHashMap<String, PluginIF>();

  protected Integer                     _pageSize                 = null;

  protected Integer                     _pageNumber               = null;

  protected Integer                     _limit                    = null;

  protected Integer                     _skip                     = null;

  private ValueQuery[]                  combineValueQueryArray;

  // Used if minus, union, or intersect is being used
  private CombineType.Type              combineType;

  // The selectables need to be preprossed to account for updated display labels
  // and
  // other join criteria that can change once conditions are added to the query.
  private boolean                       staleSelectables;

  /**
   * The alias of the selectable that will contain the total count of the query.
   * This can be used to optimize queries that need a result set and count but
   * calling ValueQuery.getCount() is too expensive (because the query must be
   * run again). If this value is set then ValueQuery.getCount() will not be
   * called automatically in the translation process.
   */
  private String                        countSelectableAlias;

  /**
   * Key:table alias Value:table name
   */
  private Map<String, String>           customFromTableMap;

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * 
   * @param queryFactory
   */
  public ValueQuery(QueryFactory queryFactory)
  {
    super(queryFactory);

    this.init();
  }

  private void init()
  {
    this.countSelectableAlias = null;
    this.combineType = null;
    this.combineValueQueryArray = null;

    this.selectableList = new ArrayList<Selectable>();
    this.selectableUsedInSelectClause = new ArrayList<Selectable>();

    this.selectableSinglesInSelectMap = new HashMap<String, SelectableSingle>();
    this.processedSelectableMap = new HashMap<String, Selectable>();
    this.rawSelectableMap = new HashMap<String, Selectable>();
    this.getRawSelectableComponentQueryMap = new HashMap<String, List<Selectable>>();
    this.entityQueryInRightSideOfLeftJoin = new HashMap<String, TableClassQuery>();
    this.leftOuterJoinSet = new LinkedHashSet<LeftJoin>();
    this.customInnerJoinSet = new LinkedHashSet<InnerJoin>();
    this.groupByAttributeMap = new HashMap<String, SelectableSingle>();
    this.selectableAggregateList = new LinkedList<SelectableAggregate>();
    this.groupByList = new LinkedList<String>();
    this.selectDistinct = false;
    this.havingCondition = null;

    this.fromClauseValueQueriesInSelectClause = new LinkedHashSet<ValueQuery>();

    this.tableAlias = this.getTableAlias("", "ValueQuery");

    this.customFromTableMap = new HashMap<String, String>();

    this.staleSelectables = true;
  }

  /**
   * Restricts the query to return rows from the given page number where each
   * page has the given number of rows.
   * 
   * @param pageSize
   * @param pageNumber
   */
  public void restrictRows(int pageSize, int pageNumber)
  {
    _pageSize = pageSize;
    _pageNumber = pageNumber;

    _limit = ComponentQuery.rowLimit(pageSize);
    _skip = ComponentQuery.rowSkip(pageSize, pageNumber);
  }

  /**
   * Removes any specified row restriction. Query will return all rows that
   * match the query criteria.
   */
  public void unrestrictRows()
  {
    _pageSize = null;
    _pageNumber = null;

    _limit = null;
    _skip = null;
  }

  /**
   * Sets the SelectableSQLLong object that will be used to calculate the total
   * result set count. If null is passed in the Selectable will remain in the
   * select list but will not be used to calculate the count. As with other
   * selectables this can only be cleared from the select clause by calling
   * ValueQuery.clearSelectClause().
   * 
   * It is important to note that the given selectable must have already been
   * added to the query using ValueQuery.SELECT(). The sole purpose of this
   * method is to simply mark a specific selectable as being used for count
   * logic.
   * 
   * @param selectable
   *          The selectable that contains the count logic or null to clear the
   *          selectable.
   */
  public void setCountSelectable(SelectableSQLLong selectable)
  {
    this.countSelectableAlias = selectable != null ? selectable.getUserDefinedAlias() : null;
  }

  /**
   * Checks if a Selectable has been marked as containing the logic to calculate
   * the count.
   * 
   * @return
   */
  public boolean hasCountSelectable()
  {
    return this.countSelectableAlias != null;
  }

  /**
   * Returns the SelectableSQLLong object that contains the logic to calculate
   * the result set count.
   * 
   * @return SelectableSQLLong object or null if one was not set.
   */
  public SelectableSQLLong getCountSelectable()
  {
    return (SelectableSQLLong) ( this.countSelectableAlias != null ? this.getSelectableRef(this.countSelectableAlias) : null );
  }

  /**
   * Returns the alias used for this ValueQuery.
   * 
   * @return alias used for this ValueQuery.
   */
  public String getTableAlias()
  {
    return this.tableAlias;
  }

  /**
   * Clears and resets the select clause.
   */
  public void clearSelectClause()
  {
    this.countSelectableAlias = null;
    this.combineType = null;
    this.combineValueQueryArray = null;

    this.entityQueryInRightSideOfLeftJoin.clear();
    this.selectableList = new ArrayList<Selectable>();
    this.selectableUsedInSelectClause = new ArrayList<Selectable>();
    this.processedSelectableMap.clear();
    this.rawSelectableMap.clear();
    this.getRawSelectableComponentQueryMap.clear();

    this.selectableSinglesInSelectMap.clear();
    this.selectableAggregateList.clear();

    this.fromClauseValueQueriesInSelectClause.clear();
    this.customFromTableMap = new HashMap<String, String>();

    this.staleSelectables = true;
  }

  /**
   * @return returns a rank function
   */
  public RANK RANK()
  {
    SelectableSpoof selectableSpoof = new SelectableSpoof(false, this, "RankSpoof");
    return new RANK(selectableSpoof);
  }

  /**
   * @return returns a rank function
   */
  public RANK RANK(String userDefinedAlias)
  {
    SelectableSpoof selectableSpoof = new SelectableSpoof(false, this, "RankSpoof");
    return new RANK(selectableSpoof, userDefinedAlias);
  }

  /**
   * Replaces an existing Selectable with the same result attribute name from
   * the SELECT clause and returns the old value. If no match is found then the
   * Selectable is added to the SELECT clause but null is returned.
   * 
   * @return The replaced Selectable or null if one wasn't found.
   */
  public Selectable replaceSelectable(Selectable selectable)
  {
    Selectable replaced = null;
    if (this.hasSelectableRef(selectable.getResultAttributeName()))
    {
      // preserve the custom FROM clauses
      Map<String, String> customFromBackup = new HashMap<String, String>();
      customFromBackup.putAll(this.customFromTableMap);

      List<Selectable> oldSelectables = this.getSelectableRefs();

      this.clearSelectClause();

      for (Selectable oldSelectable : oldSelectables)
      {
        if (oldSelectable.getResultAttributeName().equals(selectable.getResultAttributeName()))
        {
          replaced = oldSelectable;
          this.SELECT(selectable);
        }
        else
        {
          this.SELECT(oldSelectable);
        }
      }

      // restore the custom FROM tables
      for (Entry<String, String> alias : customFromBackup.entrySet())
      {
        this.FROM(alias.getValue(), alias.getKey());
      }
    }
    else
    {
      this.SELECT(selectable);
    }

    return replaced;
  }

  /**
   * Removes the provided selectable from the SELECT clause. If this ValueQuery
   * does not contain the provided selectable null is returned.
   * 
   * @param removeMe
   *          The selectable to remove.
   * @return The removed selectable.
   */
  public Selectable removeSelectable(Selectable removeMe)
  {
    if (!this.hasSelectableRef(removeMe.getResultAttributeName()))
    {
      return null;
    }

    // preserve the custom FROM clauses
    Map<String, String> customFromBackup = new HashMap<String, String>();
    customFromBackup.putAll(this.customFromTableMap);

    List<Selectable> oldSelectables = this.getSelectableRefs();

    this.clearSelectClause();

    for (Selectable oldSelectable : oldSelectables)
    {
      if (!oldSelectable.getResultAttributeName().equals(removeMe.getResultAttributeName()))
      {
        this.SELECT(oldSelectable);
      }
    }

    // restore the custom FROM tables
    for (Entry<String, String> alias : customFromBackup.entrySet())
    {
      this.FROM(alias.getValue(), alias.getKey());
    }

    return removeMe;
  }

  /**
   * Removes the selectable by attribute name, delegating to
   * removeSelectable(Selectable).
   * 
   * @param attribute
   * @return Null if this ValueQuery does not contain a selectable by the given
   *         attribute name.
   */
  public Selectable removeSelectable(String attribute)
  {
    if (!this.hasSelectableRef(attribute))
    {
      return null;
    }

    return removeSelectable(this.getSelectableRef(attribute));
  }

  /**
   * 
   * @param newSelectableArray
   */
  public void SELECT(Selectable... newSelectableArray)
  {
    for (Selectable selectable : newSelectableArray)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      this.updateAllSelectableDataStructures(componentQuery, selectable);

      this.selectableList.add(selectable);
    }

    this.selectDistinct = false;

    this.staleSelectables = true;
  }

  /**
   * Updates internal selectable data structures for the given selectable.
   * 
   * @param componentQuery
   * @param selectable
   */
  private void updateAllSelectableDataStructures(ComponentQuery componentQuery, Selectable selectable)
  {
    this.updateRawSelectableDataStructures(componentQuery, selectable);
    this.updatesProssedSelectableDataStructures(componentQuery, selectable);
  }

  /**
   * Updates internal raw selectable data structures for the given selectable.
   * 
   * @param componentQuery
   * @param selectable
   */
  private void updateRawSelectableDataStructures(ComponentQuery componentQuery, Selectable selectable)
  {
    // Raw selectables
    if (this.rawSelectableMap.containsKey(selectable.getResultAttributeName()))
    {
      String errMsg = "Attribute [" + selectable.getResultAttributeName() + "] is ambiguous.";
      throw new AmbiguousAttributeException(errMsg, selectable.getResultAttributeName());
    }
    else
    {
      this.rawSelectableMap.put(selectable.getResultAttributeName(), selectable);
    }

    List<Selectable> compQuerySelectableList = this.getRawSelectableComponentQueryMap.get(componentQuery.getAliasSeed());
    if (compQuerySelectableList == null)
    {
      compQuerySelectableList = new LinkedList<Selectable>();
      this.getRawSelectableComponentQueryMap.put(componentQuery.getAliasSeed(), compQuerySelectableList);
    }
    compQuerySelectableList.add(selectable);
  }

  /**
   * Updates internal processed selectable data structures for the given
   * selectable.
   * 
   * @param componentQuery
   * @param selectable
   */
  private void updatesProssedSelectableDataStructures(ComponentQuery componentQuery, Selectable selectable)
  {
    // Processed Selectables
    if (this.processedSelectableMap.containsKey(selectable.getResultAttributeName()))
    {
      String errMsg = "Attribute [" + selectable.getResultAttributeName() + "] is ambiguous.";
      throw new AmbiguousAttributeException(errMsg, selectable.getResultAttributeName());
    }
    else
    {
      this.processedSelectableMap.put(selectable.getResultAttributeName(), selectable);
    }

    if (selectable instanceof SelectableSingle)
    {
      this.selectableSinglesInSelectMap.put(selectable.getDbQualifiedName(), (SelectableSingle) selectable);
    }

    SelectableAggregate selectableAggregate = selectable.getAggregateFunction();
    if (selectableAggregate != null)
    {
      this.selectableAggregateList.add(selectableAggregate);
    }

    if (componentQuery instanceof ValueQuery && ! ( selectable instanceof SelectableSubSelect ) && ! ( selectable instanceof SelectableSQL ) && componentQuery != this)
    {
      this.fromClauseValueQueriesInSelectClause.add((ValueQuery) componentQuery);
    }
    componentQuery.setUsedInValueQuery(this);
  }

  /**
   * Some selectables need to be replaced to better reflect namespaces and
   * column aliases.
   */
  private void preprossesSelectableStructures()
  {
    // clear data structures
    this.processedSelectableMap.clear();

    this.selectableSinglesInSelectMap.clear();
    this.selectableAggregateList.clear();

    this.fromClauseValueQueriesInSelectClause.clear();

    // Find out which entites participate in an outer join
    Set<String> entityQueryInLeftJoin = new LinkedHashSet<String>();
    for (LeftJoin leftOuterjoin : this.leftOuterJoinSet)
    {
      if (! ( leftOuterjoin instanceof BasicLeftJoin ))
      {
        entityQueryInLeftJoin.addAll(leftOuterjoin.getLeftSideEntityQueryAlias());
      }
    }

    for (int i = 0; i < this.selectableList.size(); i++)
    {
      Selectable loopSelectable = this.selectableList.get(i);

      ComponentQuery componentQuery = loopSelectable.getRootQuery();

      Selectable selectable;
      // This changes the db column alias to accomodate for the fact the
      // selectable comes from a left join
      if (entityQueryInLeftJoin.contains(componentQuery.getAliasSeed()) && componentQuery instanceof TableClassQuery)
      {
        selectable = convertSelectableUsedInLeftJoin(loopSelectable, (EntityQuery) componentQuery);
      }
      else
      {
        selectable = loopSelectable;
      }

      // Set the display labels for functions in the select clause
      this.setSelectableDisplayLabels(selectable);
      this.updatesProssedSelectableDataStructures(componentQuery, selectable);
      this.selectableList.set(i, selectable);
    }

    this.staleSelectables = false;
  }

  private void setSelectableDisplayLabels(Selectable selectable)
  {
    MdAttributeDAOIF mdAttributeIF = selectable.getMdAttributeIF();
    if (mdAttributeIF instanceof MdAttributeConcrete_Q)
    {
      MdAttributeConcrete_Q mdAttributeQ = (MdAttributeConcrete_Q) mdAttributeIF;
      String resultAttributeName = selectable.getResultAttributeName();
      mdAttributeQ.setDefinedAttributeName(resultAttributeName);

      String userDefinedDisplayLabel = selectable.getUserDefinedDisplayLabel();
      if (selectable instanceof Function)
      {
        if (userDefinedDisplayLabel.trim().length() != 0)
        {
          mdAttributeQ.setDisplayLabel(userDefinedDisplayLabel);
        }
        else
        {
          mdAttributeQ.setDisplayLabel( ( (Function) selectable ).calculateDisplayLabel());
        }
      }
      else
      {
        if (userDefinedDisplayLabel.trim().length() != 0)
        {
          mdAttributeQ.setDisplayLabel(userDefinedDisplayLabel);
        }
      }
    }
    else if (mdAttributeIF instanceof MdAttributeConcrete_SQL)
    {
      MdAttributeConcrete_SQL mdAttributeSQL = (MdAttributeConcrete_SQL) mdAttributeIF;
      String resultAttributeName = selectable.getResultAttributeName();
      mdAttributeSQL.setDefinedAttributeName(resultAttributeName);
    }
  }

  protected List<Selectable> getRawSelectebleFromComponentQuery(String aliasSeed)
  {
    return this.getRawSelectableComponentQueryMap.get(aliasSeed);
  }

  protected void setEntityQueryInLeftJoin(TableClassQuery entityQuery)
  {
    this.entityQueryInRightSideOfLeftJoin.put(entityQuery.getAliasSeed(), entityQuery);
  }

  protected boolean isEntityInLeftJoin(TableClassQuery entityQuery)
  {
    return this.entityQueryInRightSideOfLeftJoin.containsKey(entityQuery.getAliasSeed());
  }

  public void FROM(ComponentQuery componentQuery)
  {
    componentQuery.setUsedInValueQuery(this);
  }

  public void FROM(GeneratedComponentQuery generatedComponentQuery)
  {
    generatedComponentQuery.getComponentQuery().setUsedInValueQuery(this);
  }

  /**
   * Adds a table to the where clause that is not controlled by the framework.
   * If the tableAlias maps to a preexisting alias included in the FROM clause,
   * either from a ComponentQuery, GeneratedComponentQuery, or ValueQuery then
   * this method replaces the original value. This is useful for overriding
   * table definitions but must also be used with caution.
   * 
   * @param tableName
   *          Name of the table
   * @param tableAlias
   *          database table alias
   */
  public void FROM(String tableName, String tableAlias)
  {
    this.customFromTableMap.put(tableAlias, tableName);
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
    StringBuffer fromClause = super.buildFromClause(visitor, tableJoinSet, fromTableMap);

    for (LeftJoin leftOuterJoin : this.leftOuterJoinSet)
    {
      List<ValueQuery> valueQueryObjects = leftOuterJoin.getValueObjectQueries();
      for (ValueQuery valueObjectQuery : valueQueryObjects)
      {
        this.fromClauseValueQueriesInSelectClause.remove(valueObjectQuery);
      }
    }

    boolean firstIteration = true;
    for (ValueQuery valueObjectQuery : this.fromClauseValueQueriesInSelectClause)
    {
      if (!fromClause.toString().toUpperCase().trim().equals("FROM") || firstIteration == false)
      {
        fromClause.append(",\n" + selectIndent);
      }

      fromClause.append("(" + valueObjectQuery.getSQL() + ") " + valueObjectQuery.getTableAlias());

      firstIteration = false;
    }

    return fromClause;
  }

  /**
   * Select distinct values from the attributes in the DISTINCT function.
   * 
   * @param selectableArray
   */
  public void SELECT_DISTINCT(Selectable... selectableArray)
  {
    this.SELECT(selectableArray);
    this.selectDistinct = true;
  }

  /**
   * Adds a left outer join to the query.
   * 
   * @param leftOuterJoint
   */
  public void WHERE(LeftJoin leftOuterJoin)
  {
    this.AND(leftOuterJoin);
  }

  /**
   * Adds a left outer join to the query.
   * 
   * @param leftOuterJoin
   */
  private void AND(LeftJoin leftOuterJoin)
  {
    // String attributeName = null;
    // String tableName = null;
    //
    // // TODO evaluate these conditions because some might be too restrictive
    // and
    // // disallow JOIN chaining
    // for (LeftJoin currLeftOuterJoin : this.leftOuterJoinSet)
    // {
    // // This was removed to allow LEFT JOIN chaining from table A to B twice:
    // A
    // // LEFT JOIN B ... LEFT JOIN B
    // // if
    // //
    // (currLeftOuterJoin.getTableAlias1().equals(leftOuterJoin.getTableAlias1()))
    // // {
    // // attributeName = leftOuterJoin.getColumnName1();
    // // tableName = leftOuterJoin.getTableName1();
    // // }
    // if
    // (currLeftOuterJoin.getTableAlias2().equals(leftOuterJoin.getTableAlias2()))
    // {
    // attributeName = leftOuterJoin.getColumnName2();
    // tableName = leftOuterJoin.getTableName2();
    // }
    // else if
    // (currLeftOuterJoin.getTableAlias1().equals(leftOuterJoin.getTableAlias2()))
    // {
    // attributeName = leftOuterJoin.getColumnName2();
    // tableName = leftOuterJoin.getTableName2();
    // }
    // else if
    // (currLeftOuterJoin.getTableAlias2().equals(leftOuterJoin.getTableAlias1()))
    // {
    // attributeName = leftOuterJoin.getColumnName1();
    // tableName = leftOuterJoin.getTableName1();
    // }
    // }
    //
    // if (attributeName != null)
    // {
    // String errMsg = "Attribute [" + attributeName + "] is defined by table ["
    // + tableName + "] which is already used in the LEFT JOIN clause. " + "The
    // type that defines the attribute has been specified twice, either directly
    // or indirectly through inheritance. ";
    // throw new QueryException(errMsg);
    // }

    this.leftOuterJoinSet.add(leftOuterJoin);

    leftOuterJoin.setValueQuery(this);

    this.staleSelectables = true;

  }

  /**
   * This changes the db column alias to accommodate for the fact the selectable
   * comes from a left join.
   * 
   * @param selectable
   * @param entityQuery
   * @return
   */
  private Selectable convertSelectableUsedInLeftJoin(Selectable selectable, EntityQuery entityQuery)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    MdEntityDAOIF definingEntityIF = (MdEntityDAOIF) mdAttributeIF.definedByClass();
    String definingTableName = definingEntityIF.getTableName();
    String definingTableAlias = entityQuery.getTableAlias("", entityQuery.getMdEntityIF().getTableName());

    selectable = attributeFactory(this, selectable, definingEntityIF, definingTableName, definingTableAlias, mdAttributeIF, selectable.getUserDefinedAlias(), selectable.getUserDefinedDisplayLabel());
    return selectable;
  }

  /**
   * Adds an inner join to the query.
   * 
   * @param innerJoin
   */
  public void WHERE(InnerJoin innerJoin)
  {
    this.AND(innerJoin);
  }

  /**
   * Adds an inner join to the query.
   * 
   * @param innerJoin
   */
  private void AND(InnerJoin innerJoin)
  {
    this.customInnerJoinSet.add(innerJoin);
  }

  /**
   * Adds a join to the query.
   * 
   * @param join
   */
  public void WHERE(Join join)
  {
    this.AND(join);
  }

  /**
   * Adds a join to the query.
   * 
   * @param join
   */
  public void AND(Join join)
  {
    if (join instanceof LeftJoin)
    {
      this.AND((LeftJoin) join);
    }
    else
    {
      this.AND((InnerJoin) join);
    }

  }

  /**
   * Creates a query out of the UNION of the given queries.
   * 
   * @param valueQueryArray
   */
  public void UNION(ValueQuery... valueQueryArray)
  {
    this.processCombineType(CombineType.Type.UNION, valueQueryArray);
  }

  /**
   * Creates a query out of the UNION of the given queries.
   * 
   * @param valueQueryArray
   */
  public void UNION_ALL(ValueQuery... valueQueryArray)
  {
    this.processCombineType(CombineType.Type.UNION_ALL, valueQueryArray);
  }

  /**
   * Creates a query out of the MINUS of the given queries.
   * 
   * @param valueQueryArray
   */
  public void MINUS(ValueQuery... valueQueryArray)
  {
    this.processCombineType(CombineType.Type.MINUS, valueQueryArray);
  }

  /**
   * Creates a query out of the INTERSECT of the given queries.
   * 
   * @param valueQueryArray
   */
  public void INTERSECT(ValueQuery... valueQueryArray)
  {
    this.processCombineType(CombineType.Type.INTERSECT, valueQueryArray);
  }

  private void processCombineType(CombineType.Type combineType, ValueQuery... valueQueryArray)
  {

    if (valueQueryArray.length < 2)
    {
      String errMsg = "A [" + combineType.toString() + "] operator must be performed on two queries or more.";
      throw new QueryException(errMsg);
    }

    int selectableLength = 0;
    for (ValueQuery valueQuery : valueQueryArray)
    {
      if (selectableLength != 0)
      {
        if (valueQuery.getSelectableRefs().size() != selectableLength)
        {
          String errMsg = "A [" + combineType.toString() + "] operator must be performed on queries with the same number of attributes.";
          throw new QueryException(errMsg);
        }
      }

      selectableLength = valueQuery.getSelectableRefs().size();

    }

    Selectable[] selectableArray = new Selectable[selectableLength];

    int loopCount = 0;
    for (Selectable selectable : valueQueryArray[0].getSelectableRefs())
    {
      selectableArray[loopCount] = selectable;
      loopCount++;
    }

    for (int i = 1; i < valueQueryArray.length; i++)
    {
      ValueQuery someValueQuery = valueQueryArray[i];

      List<Selectable> someValueQuerySelectables = someValueQuery.getSelectableRefs();

      for (int k = 0; k < someValueQuerySelectables.size(); k++)
      {
        if (!selectableArray[k].getMdAttributeIF().getMdAttributeConcrete().getClass().getName().equals(someValueQuerySelectables.get(k).getMdAttributeIF().getMdAttributeConcrete().getClass().getName()))
        {
          String errMsg = "Attributes from different queries in the same position in the select clause must be of the same type. " + " Attributes at position [" + k + "] are not of the same type.";
          throw new QueryException(errMsg);
        }

        List<MdAttributeConcreteDAOIF> mdAttributeList = new LinkedList<MdAttributeConcreteDAOIF>();
        mdAttributeList.addAll(selectableArray[k].getAllEntityMdAttributes());
        selectableArray[k].setAdditionalEntityMdAttributes(mdAttributeList);
      }
    }

    this.SELECT(selectableArray);

    this.combineType = combineType;
    this.combineValueQueryArray = valueQueryArray;
  }

  public boolean isGrouping()
  {
    return this.groupByList.size() > 0 || this.selectableAggregateList.size() > 0;
  }

  /**
   * Add a GROUP BY clause with the following attributes. Note, do NOT call this
   * method if you already have an aggregate function in the select clause. A
   * GROUP BY clause is automatically created in that case. Calling this method
   * will override the GROUP BY clause that was generated by adding an aggregate
   * function into the select clause.
   * 
   * @param groupByAttributes
   */
  public void GROUP_BY(SelectableSingle... groupByAttributes)
  {
    for (SelectableSingle singleSelectable : groupByAttributes)
    {
      if (!this.groupByAttributeMap.containsKey(singleSelectable.getColumnAlias()))
      {
        // If the selectable is not in the select list, then reference the
        // qualified name instead of the
        // column alias. Since the column alias is defined in the select clause,
        // it cannot be referenced int
        // the group by clause if it is not defined in the select clause.
        if (!this.selectableSinglesInSelectMap.containsKey(singleSelectable.getDbQualifiedName()))
        {
          this.groupByList.add(singleSelectable.getDbQualifiedName());
        }
        else
        {
          this.groupByList.add(singleSelectable.getColumnAlias());
        }
        this.groupByAttributeMap.put(singleSelectable.getColumnAlias(), singleSelectable);
      }
    }
  }

  /**
   * Adds a condition to this query. Will create a HAVING clause.
   * 
   * @param condition
   *          condition to add.
   */
  public void HAVING(Condition condition)
  {
    if (condition != null)
    {
      if (this.havingCondition == null)
      {
        this.havingCondition = condition;
      }
      else
      {
        this.havingCondition = new AND(this.havingCondition, condition);
      }
    }
  }

  /**
   * Returns a visitor that has traversed this structure.
   * 
   * @return visitor that has traversed this structure.
   */
  protected BuildSQLVisitor visitQuery()
  {
    BuildSQLVisitor visitor = super.visitQuery();

    for (Selectable selectable : this.selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      // Don't visit this attribute if it is from another value query.
      // Attributes from value queries come from a nested SELECT within the FROM
      // clause.
      // Visiting this attribute will copy joins in the nested SELECT and put
      // them in the
      // outermost SELECT.
      if (componentQuery instanceof TableClassQuery)
      {
        selectable.accept(visitor);
      }
      else if (selectable instanceof SelectableSQL)
      {
        visitor.setContainsSelectableSQL(true);
      }
    }

    if (this.havingCondition != null)
    {
      this.havingCondition.accept(visitor);
    }

    return visitor;
  }

  /**
   * Returns SQL if this ValueQuery is UNION'ed, MINUS'ed or INTERSECT'ed with
   * other value queries
   * 
   * @param limitRowRange
   * @param limit
   * @param skip
   * @return
   */
  private String getCombineSQL(boolean limitRowRange, int limit, int skip)
  {
    StringBuilder sql = new StringBuilder();

    ValueQuery valueQueryFirst = this.combineValueQueryArray[0];
    sql.append("(" + valueQueryFirst.getSQL(limitRowRange, limit, skip) + ")\n");

    for (int i = 1; i < this.combineValueQueryArray.length; i++)
    {
      ValueQuery valueQuery = this.combineValueQueryArray[i];

      sql.append(this.combineType.getOperator() + "\n");
      sql.append("(" + valueQuery.getSQL(limitRowRange, limit, skip) + ")");
    }

    return sql.toString();
  }

  @Override
  protected void addCustomJoins(Set<Join> tableJoinSet)
  {
    tableJoinSet.addAll(this.leftOuterJoinSet);
    tableJoinSet.addAll(this.customInnerJoinSet);

  }

  /**
   * Allows a prefix to be set will will be appended to the beginning of the SQL
   * string. useful for WITH queries
   */
  public void setSqlPrefix(String sql)
  {
    this.sqlPrefix = sql;
  }

  /**
   * A "dependent pre sql statement" is a standalone sql statement that is
   * required to be executed before the ValueQuery can be executed. This is
   * useful for creating things like temp tables that are required for the
   * ValueQuery to execute properly. DDMS ticket 3577 is a good example.
   * 
   * @param statements
   */
  public void setDependentPreSqlStatements(String statements)
  {
    this.dependentPreSqlStatements = statements;
  }

  /**
   * A "dependent pre sql statement" is a standalone sql statement that is
   * required to be executed before the ValueQuery can be executed. This is
   * useful for creating things like temp tables that are required for the
   * ValueQuery to execute properly. DDMS ticket 3577 is a good example.
   */
  public String getDependentPreSqlStatements()
  {
    return this.dependentPreSqlStatements;
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
    this.selectableUsedInSelectClause = new ArrayList<Selectable>();
    this.selectableUsedInSelectClause.add(this.getRawSelectable(selectClauseAttribute.getUserDefinedAlias()));

    String sql = this.getSQLInternal(this.selectableUsedInSelectClause, false, 0, 0);

    return sql;
  }

  /**
   * Returns the SQL representation of this query, including all attributes of
   * the type in the select clause.
   * 
   * @return SQL representation of this query, including all attributes of the
   *         type in the select clause.
   */
  protected String getSQL(boolean limitRowRange, int limit, int skip)
  {
    this.selectableUsedInSelectClause = this.selectableList;

    String sql = this.getSQLInternal(this.selectableUsedInSelectClause, limitRowRange, limit, skip);

    return this.dependentPreSqlStatements + this.sqlPrefix + sql;
  }

  /**
   * Returns the Sql representation of this query, excluding the dependent pre
   * sql statements.
   */
  public String getSQLWithoutDependentPreSql()
  {
    this.selectableUsedInSelectClause = this.selectableList;

    String sql = this.getSQLInternal(this.selectableUsedInSelectClause, false, 0, 0);

    return this.sqlPrefix + sql;
  }

  public boolean containsSelectableSQL()
  {
    BuildSQLVisitor visitor = this.visitQuery();

    // Make sure we visit what was specified in the outer join clause.
    for (LeftJoin leftOuterjoin : this.leftOuterJoinSet)
    {
      leftOuterjoin.accept(visitor);
    }

    return visitor.containsSelectableSQL();
  }

  /**
   * Returns a list of {@link AttributeIndicator} objects in the select clause.
   * 
   * @return
   */
  protected List<AttributeIndicator> getIndicatorSelectables()
  {
    List<AttributeIndicator> selectables = new LinkedList<AttributeIndicator>();

    // Make sure functions that are indicators are added to the select list.
    for (Selectable selectable : this.selectableList)
    {
      if (selectable instanceof AttributeIndicator)
      {
        selectables.add((AttributeIndicator) selectable);
      }
    }

    return selectables;
  }

  /**
   * Returns the SQL to count the objects that match the specified criteria.
   * 
   * @return sql for counting objects match the specified criteria.
   */
  @Override
  public String getCountSQL()
  {
    if (this.containsSelectableSQL() || this.havingCondition != null)
    {
      return this.dependentPreSqlStatements + "SELECT COUNT(*) " + Database.formatColumnAlias("ct") + " FROM (\n" + this.getSQLWithoutDependentPreSql() + "\n) AS UNION_COUNT";
    }

    String sql;

    // If this is a unioned/intersect/minus query,
    if (this.combineType != null)
    {
      this.selectableUsedInSelectClause = this.selectableList;
      sql = this.getSQLInternal(this.selectableUsedInSelectClause, false, 0, 0);
      sql = "SELECT COUNT(*) " + Database.formatColumnAlias("ct") + " FROM (\n" + sql + "\n) AS UNION_COUNT";
    }
    else
    {
      if (this.staleSelectables)
      {
        this.preprossesSelectableStructures();
      }
      this.addToGroupByList(this.selectableList);

      List<AttributeIndicator> indicatorList = this.getIndicatorSelectables();

      // There are items in the group by clause
      // The select clause will contain everything that is a non aggregate.
      if (this.groupByList.size() > 0 || indicatorList.size() > 0)
      {
        List<Selectable> selectables = new LinkedList<Selectable>();
        for (SelectableSingle selectable : groupByAttributeMap.values())
        {
          selectables.add(selectable);
        }

        // Make sure functions that are indicators are added to the select list.
        for (AttributeIndicator selectable : indicatorList)
        {
          selectables.add(selectable);
        }

        sql = this.getSQLInternal(selectables, false, 0, 0);
        sql = "SELECT COUNT(*) " + Database.formatColumnAlias("ct") + " FROM (\n" + sql + "\n) AS UNION_COUNT";
      }
      else
      {
        BuildSQLVisitor visitor = this.visitQuery();

        Set<Join> tableJoinSet = new LinkedHashSet<Join>();

        Map<String, String> fromTableMap = this.getFromTableMapInfoForQuery();

        tableJoinSet.addAll(visitor.tableJoinSet);
        this.addCustomJoins(tableJoinSet);

        StringBuffer selectStmt = new StringBuffer("SELECT COUNT(*) " + Database.formatColumnAlias("ct"));

        selectStmt.append(" " + this.addGroupBySelectablesToSelectClauseForCount() + "\n");

        selectStmt.append(this.buildFromClause(visitor, tableJoinSet, fromTableMap));

        List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

        StringBuffer joinClause = this.buildJoinClause(tableJoinSet);
        StringBuffer queryCriteria = new StringBuffer(this.getQueryConditionSQL());

        sqlQueryClauses.add(joinClause);
        sqlQueryClauses.add(queryCriteria);

        this.appendQueryClauses(selectStmt, sqlQueryClauses);

        selectStmt.append(this.buildGroupByClause());

        sql = selectStmt.toString();
      }
    }

    // TODO : Shouldn't the prefix have been added inside of the FROM stuff
    // above??
    return this.dependentPreSqlStatements + this.sqlPrefix + sql;
  }

  /**
   * Returns the SQL representation of this query, including all attributes of
   * the type in the select clause.
   * 
   * @return SQL representation of this query, including all attributes of the
   *         type in the select clause.
   */
  private String getSQLInternal(List<Selectable> selectables, boolean limitRowRange, int limit, int skip)
  {
    if (this.staleSelectables)
    {
      this.preprossesSelectableStructures();
    }

    if (selectables == null || selectables.size() == 0)
    {
      String errMsg = "Value queries must have attributes specified in the SELECT clause.";
      throw new ValueQueryMissingSelectCaluseException(errMsg);
    }

    StringBuffer sqlStmt = null;

    if (this.combineType != null)
    {
      sqlStmt = new StringBuffer(this.getCombineSQL(limitRowRange, limit, skip));
    }

    if (sqlStmt == null)
    {
      sqlStmt = new StringBuffer();

      BuildSQLVisitor visitor = this.visitQuery();
      Set<Join> tableJoinSet = new LinkedHashSet<Join>();

      // Make sure we visit what was specified in the outer join clause.
      for (LeftJoin leftOuterjoin : this.leftOuterJoinSet)
      {
        leftOuterjoin.accept(visitor);
      }

      tableJoinSet.addAll(this.leftOuterJoinSet);
      tableJoinSet.addAll(visitor.tableJoinSet);
      tableJoinSet.addAll(this.customInnerJoinSet);

      Map<String, String> fromTableMap = new HashMap<String, String>();

      StringBuffer selectClause = this.buildSelectClause(selectables, tableJoinSet, fromTableMap);

      this.addToGroupByList(selectables);

      // JN Change: Databases allow queries to have columns in the SELECT clause
      // that are not in GROUP BY and visa-versa
      // for (SelectableSingle selectableSingle :
      // this.groupByAttributeMap.values())
      // {
      // if
      // (!this.selectableSinglesInSelectMap.containsKey(selectableSingle.getDbQualifiedName()))
      // {
      // String errMsg = "The attribute [" +
      // selectableSingle._getAttributeName() + "] specified in the GROUP BY
      // clause must also be specified in the SELECT clause.";
      // throw new MissingAttributeInSelectForGroupByException(errMsg,
      // selectableSingle);
      // }
      // }

      // Make sure there are no single selectables in the select clause that are
      // not also
      // in the group by clause
      if (this.groupByAttributeMap.size() > 0)
      {
        for (SelectableSingle selectableSingle : this.selectableSinglesInSelectMap.values())
        {
          if (!this.groupByAttributeMap.containsKey(selectableSingle.getColumnAlias()) && !selectableSingle.isAggregateFunction())
          {
            String errMsg = "The attribute [" + selectableSingle._getAttributeName() + "]  must appear in the GROUP BY clause or be used in an aggregate function";
            throw new AttributeNotInGroupByOrAggregate(errMsg, selectableSingle);
          }
        }
      }

      StringBuffer fromClause = this.buildFromClause(visitor, tableJoinSet, fromTableMap);

      StringBuffer joinClause = this.buildJoinClause(tableJoinSet);

      StringBuffer criteriaClause = new StringBuffer(this.getQueryConditionSQL());

      List<StringBuffer> sqlQueryClauses = new LinkedList<StringBuffer>();

      sqlQueryClauses.add(joinClause);
      sqlQueryClauses.add(criteriaClause);

      sqlStmt.append(selectClause);
      sqlStmt.append("\n" + fromClause);

      this.appendQueryClauses(sqlStmt, sqlQueryClauses);
    }

    sqlStmt.append(this.buildGroupByClause());

    String orderByClause = this.buildOrderByClause();
    sqlStmt = this.appendOderByClause(limitRowRange, limit, skip, sqlStmt, selectables, orderByClause);

    return sqlStmt.toString();
  }

  /**
   * Build the select clause for this query (without the SELECT keyword),
   * including all attributes required to instantiate instances of this object.
   * 
   * @param mdAttributeIDMap
   *          Key: MdAttribute.getOid() Value: MdAttributeIF
   * @return select clause for this query.
   */
  protected StringBuffer buildSelectClause(List<Selectable> _selectableList, Set<Join> tableJoinSet, Map<String, String> fromTableMap)
  {
    // Key: OID of an MdAttribute Value: MdTableClass that defines the
    // attribute;
    Map<String, MdTableClassIF> mdTableClassMap = new HashMap<String, MdTableClassIF>();

    StringBuffer selectString = new StringBuffer("SELECT \n");

    this.appendDistinctToSelectClause(selectString);

    Set<String> hashSet = new LinkedHashSet<String>();

    // Order by fields must also be in the select clause.
    boolean firstIteration = true;

    for (Selectable selectable : _selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      ColumnInfo columnInfo = selectable.getColumnInfo();

      hashSet.add(columnInfo.getColumnAlias());

      if (!firstIteration)
      {
        selectString.append(",\n");
      }

      this.buildSelectColumn(selectString, selectable, mdAttributeIF, columnInfo);

      if (componentQuery instanceof TableClassQuery)
      {
        Selectable fSelectable = null;

        if (selectable instanceof Function)
        {
          fSelectable = ( (Function) selectable ).getSelectable();
        }

        if (fSelectable == null || ! ( fSelectable instanceof SelectableSpoof ))
        {
          MdTableClassIF mdTableClassIF = mdTableClassMap.get(mdAttributeIF.getOid());
          if (mdTableClassIF == null)
          {
            mdTableClassIF = (MdTableClassIF) mdAttributeIF.definedByClass();
            mdTableClassMap.put(mdAttributeIF.getOid(), mdTableClassIF);
          }

          fromTableMap.put(columnInfo.getTableAlias(), columnInfo.getTableName());

          String baseTableName = mdTableClassIF.getTableName();
          if (!columnInfo.getColumnName().equals(EntityDAOIF.ID_COLUMN) && !baseTableName.equals(columnInfo.getTableName())
          // For functions, sometimes they are applying either to the OID or to
          // the
          // type itself, and therefore do not need to be joined with the table
          // that defines the OID in metadata
              && ! ( selectable instanceof Function && fSelectable.getDbColumnName().equals(EntityDAOIF.ID_COLUMN) && selectable.getDefiningTableName().equals(columnInfo.getTableName()) ))
          {
            String baseTableAlias = componentQuery.getTableAlias("", baseTableName);
            Join tableJoin = new InnerJoinEq(EntityDAOIF.ID_COLUMN, baseTableName, baseTableAlias, EntityDAOIF.ID_COLUMN, columnInfo.getTableName(), columnInfo.getTableAlias());
            tableJoinSet.add(tableJoin);
          }
        }
      }

      if (selectable instanceof SelectableEnumeration)
      {
        ColumnInfo cacheColumnInfo = ( (SelectableEnumeration) selectable ).getCacheColumnInfo();

        selectString.append(",\n");

        this.buildSelectColumn(selectString, selectable, mdAttributeIF, cacheColumnInfo);
      }
      else if (selectable instanceof SelectableStruct)
      {
        SelectableStruct selectableStruct = (SelectableStruct) selectable;

        if (componentQuery instanceof TableClassQuery)
        {
          tableJoinSet.addAll(selectableStruct.getJoinStatements());
        }

        List<Attribute> structAttributeList = selectableStruct.getStructAttributes();
        for (Attribute structAttribute : structAttributeList)
        {
          if (componentQuery instanceof TableClassQuery)
          {
            fromTableMap.put(structAttribute.getDefiningTableAlias(), structAttribute.getDefiningTableName());
          }
          selectString.append(",\n");

          this.buildSelectColumn(selectString, structAttribute, structAttribute.getMdAttributeIF(), structAttribute.getColumnInfo());

          if (structAttribute instanceof SelectableEnumeration)
          {
            SelectableEnumeration selectableEnumeration = (SelectableEnumeration) structAttribute;
            ColumnInfo structEnumCacheColumnInfo = selectableEnumeration.getCacheColumnInfo();

            if (componentQuery instanceof EntityQuery)
            {
              fromTableMap.put(structEnumCacheColumnInfo.getTableAlias(), structEnumCacheColumnInfo.getTableName());
            }
            selectString.append(",\n");

            this.buildSelectColumn(selectString, structAttribute, structAttribute.getMdAttributeIF(), structEnumCacheColumnInfo);
          }
        }
      }

      firstIteration = false;
    }

    this.addOrderByAttributesToSelectClause(selectString, hashSet, this.orderByList, firstIteration);

    return selectString;
  }

  /**
   * 
   * @param limitRowRange
   * @param limit
   * @param skip
   * @param sqlStmt
   * @param _selectableList
   * @param orderByClause
   * @return
   */
  protected StringBuffer appendOderByClause(boolean limitRowRange, int limit, int skip, StringBuffer sqlStmt, List<Selectable> _selectableList, String orderByClause)
  {
    List<ColumnInfo> columnInfoList = new LinkedList<ColumnInfo>();

    for (Selectable selectable : _selectableList)
    {
      columnInfoList.addAll(selectable.getColumnInfoList());
    }

    // Don't do anything if no columns were selected.
    if (columnInfoList.size() == 0)
    {
      return sqlStmt;
    }

    // Restrict the number of rows returned from the database.
    if (limitRowRange)
    {
      StringBuilder selectClauseAttributes = new StringBuilder();
      boolean firstIteration = true;
      ColumnInfo fistColumnInfo = null;
      for (ColumnInfo columnInfo : columnInfoList)
      {
        if (!firstIteration)
        {
          selectClauseAttributes.append(", ");
        }
        else
        {
          fistColumnInfo = columnInfo;
        }
        selectClauseAttributes.append(columnInfo.getColumnAlias());

        firstIteration = false;
      }

      if (orderByClause.trim().length() == 0)
      {
        orderByClause = "ORDER BY " + fistColumnInfo.getColumnAlias() + " ASC";
      }

      sqlStmt = Database.buildRowRangeRestriction(sqlStmt, limit, skip, selectClauseAttributes.toString(), orderByClause);
    }
    else
    {
      sqlStmt.append("\n" + orderByClause);
    }
    return sqlStmt;
  }

  /**
   * Adds the custom table definitions to the FROM clause.
   */
  @Override
  protected StringBuffer buildFromClauseFromMap(Map<String, String> fromMap, Set<LeftJoin> leftOuterJoinSet)
  {
    fromMap.putAll(this.customFromTableMap);

    return super.buildFromClauseFromMap(fromMap, leftOuterJoinSet);
  }

  /**
   * Builds the group by clause for this query.
   * 
   * @return group by clause for this query.
   */
  protected String buildGroupByClause()
  {
    StringBuilder sql = new StringBuilder();

    if (this.groupByList.size() > 0)
    {
      sql.append("\nGROUP BY ");

      boolean goupByFirstIteration = true;
      for (String groupBy : this.groupByList)
      {
        if (!goupByFirstIteration)
        {
          sql.append(", ");
        }

        sql.append(groupBy);
        goupByFirstIteration = false;
      }
    }

    if (this.havingCondition != null)
    {
      Selectable selectable = this.havingCondition.getLeftSelectable();

      if (selectable != null && !selectable.isAggregateFunction() && ( selectable instanceof SelectableSingle ))
      {
        SelectableSingle selectableSingle = (SelectableSingle) selectable;
        String attributeQualifiedName = selectableSingle.getDbQualifiedName();
        if (!this.groupByAttributeMap.containsKey(attributeQualifiedName))
        {
          String errMessage = "The attribute [" + selectableSingle._getAttributeName() + "] defined by [" + selectableSingle.getMdAttributeIF().definedByClass().definesType() + "]" + " in the HAVING clause must also be present in the SELECT clause or in the GROUP_BY clause.";

          MdAttributeConcreteDAOIF definingMdAttributeDAOIF = selectableSingle.getMdAttributeIF();

          throw new MissingHavingClauseAttributeException(errMessage, selectable, definingMdAttributeDAOIF.definedByClass());
        }
      }

      sql.append("\nHAVING " + this.havingCondition.getSQL());
    }

    return sql.toString();
  }

  /**
   * Adds the given selectable to the group by list.
   * 
   * @param structSelectable
   * @param qualifiedName
   */
  protected void addToGroupByListStruct(Selectable structSelectable)
  {
    this.selectableSinglesInSelectMap.put(structSelectable.getDbQualifiedName(), (Attribute) structSelectable);

    if (structSelectable instanceof SelectableSingle)
    {
      SelectableSingle selectableSingle = (SelectableSingle) structSelectable;
      if (this.selectableAggregateList.size() > 0)
      {
        if (!this.groupByAttributeMap.containsKey(selectableSingle.getColumnAlias()) && !structSelectable.isAggregateFunction())
        {
          this.groupByList.add(selectableSingle.getColumnAlias());
          this.groupByAttributeMap.put(selectableSingle.getColumnAlias(), selectableSingle);
        }

      }
    }
  }

  /**
   * Adds selectables to the select clause when the count() function has a group
   * by clause.
   */
  @Override
  protected String addGroupBySelectablesToSelectClauseForCount()
  {
    StringBuilder sql = new StringBuilder();

    boolean firstIteration = true;
    for (String selectable : this.groupByList)
    {
      if (!firstIteration)
      {
        sql.append(", ");
      }
      firstIteration = false;
      sql.append(selectable);
    }

    return sql.toString();
  }

  /**
   * Determines what should be in the group by clause.
   */
  @Override
  protected void computeGroupByClauseForCount()
  {

    this.addToGroupByList(this.selectableList);
  }

  /**
   * Adds the appropriate selecteables to the group by clause.
   * 
   * @param _selectableList
   * @param _columnInfoMap
   */
  protected void addToGroupByList(List<Selectable> _selectableList)
  {
    for (Selectable selectable : _selectableList)
    {
      ComponentQuery componentQuery = selectable.getRootQuery();

      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      this.addToGroupByList(selectable);

      if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        this.addToGroupByList(selectable);
      }
      else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
      {
        MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
        MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
        List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

        if (componentQuery instanceof EntityQuery)
        {
          for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
          {
            Selectable structSelectable = ( (AttributeStruct) selectable ).attributeFactory(structMdAttributeIF.definesAttribute(), structMdAttributeIF.getType(), null, null);

            this.addToGroupByListStruct(structSelectable);

            if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
            {
              this.addToGroupByList(structSelectable);
            }
          }
        }
      }
    }
  }

  /**
   * Adds the given selectable to the group by list.
   * 
   * @param selectable
   */
  protected void addToGroupByList(Selectable selectable)
  {
    if (selectable instanceof SelectableSingle)
    {
      if (this.selectableAggregateList.size() > 0)
      {
        if (!this.groupByAttributeMap.containsKey(selectable.getColumnAlias()) && !selectable.isAggregateFunction())
        {
          this.groupByList.add(selectable.getColumnAlias());
          this.groupByAttributeMap.put(selectable.getColumnAlias(), (SelectableSingle) selectable);
        }
      }
    }
  }

  /**
   * Adds the DISTINCT keyword to the select string, if required.
   * 
   * @param selectString
   */
  protected void appendDistinctToSelectClause(StringBuffer selectString)
  {
    if (this.selectDistinct)
    {
      selectString.append(selectIndent + "DISTINCT\n");
    }
  }

  /**
   * Returns an Condition object representing an equals with the attribute with
   * the given name with the given value.
   * 
   * @param attributeName
   *          name of the attribute.
   * @param value
   *          value to compare.
   * @return Condition object representing an equals with the attribute with the
   *         given name with the given value.
   */
  public AttributePrimitive getPrimitive(String attributeName)
  {
    return (AttributePrimitive) get(attributeName);
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute statement object.
   */
  public Attribute get(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    return (Attribute) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute statement object.
   */
  public Attribute get(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    return this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a {@link Selectable} that is defined on this
   * {@link TableClassDAOIF}.
   * 
   * @param userDefinedAlias
   *          name of the attribute.
   * @param value
   *          value to compare.
   * @return Condition object representing an equals with the attribute with the
   *         given name with the given value.
   */
  public Selectable getS(String userDefinedAlias)
  {
    return get(userDefinedAlias, null);
  }
  // Heads up: Test
  // /**
  // * Returns a {@link Selectable} that is defined on this {@link
  // TableClassDAOIF}.
  // *
  // * @param userDefinedAlias
  // * user defined alias.
  // * @param userDefinedDisplayLabel
  // * @return Condition object representing an equals with the attribute with
  // the
  // * given name with the given value.
  // */
  // public Selectable getS(String userDefinedAlias, String
  // userDefinedDisplayLabel)
  // {
  // Selectable selectable = this.getSelectableRef(userDefinedAlias);
  //
  // MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();
  //
  // if (mdAttributeIF instanceof MdAttributeIndicatorDAOIF)
  // {
  // MdAttributeIndicatorDAOIF mdAttributeIndicator =
  // (MdAttributeIndicatorDAOIF)mdAttributeIF;
  //
  // IndicatorElementDAOIF indicatorElement =
  // mdAttributeIndicator.getIndicator();
  //
  // return this.buildAttributeIndicator(mdAttributeIndicator, indicatorElement,
  // selectable, userDefinedAlias, userDefinedDisplayLabel);
  // }
  // else
  // {
  // return this.get(userDefinedAlias, userDefinedDisplayLabel);
  // }
  // }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeCharacterDAOIF ))
    {
      this.invalidAttributeType(MdAttributeCharacterInfo.CLASS, userDefinedAlias);
    }
    return (AttributeCharacter) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter aCharacter(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeCharacterDAOIF ))
    {
      this.invalidAttributeType(MdAttributeCharacterInfo.CLASS, userDefinedAlias);
    }
    return (AttributeCharacter) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeUUID aUUID(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeUUIDDAOIF ))
    {
      this.invalidAttributeType(MdAttributeUUIDInfo.CLASS, userDefinedAlias);
    }
    return (AttributeUUID) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute character statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeUUID aUUID(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeUUIDDAOIF ))
    {
      this.invalidAttributeType(MdAttributeUUIDInfo.CLASS, userDefinedAlias);
    }
    return (AttributeUUID) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute text statement object.
   */
  public AttributeText aText(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeTextDAOIF ))
    {
      this.invalidAttributeType(MdAttributeTextInfo.CLASS, userDefinedAlias);
    }
    return (AttributeText) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute text statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute text statement object.
   */
  public AttributeText aText(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeTextDAOIF ))
    {
      this.invalidAttributeType(MdAttributeTextInfo.CLASS, userDefinedAlias);
    }
    return (AttributeText) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeClobDAOIF ))
    {
      this.invalidAttributeType(MdAttributeClobInfo.CLASS, userDefinedAlias);
    }
    return (AttributeClob) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute CLOB statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute CLOB statement object.
   */
  public AttributeClob aClob(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeClobDAOIF ))
    {
      this.invalidAttributeType(MdAttributeClobInfo.CLASS, userDefinedAlias);
    }
    return (AttributeClob) this.internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDateDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDateInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDate) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute date statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute date statement object.
   */
  public AttributeDate aDate(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDateDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDateInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDate) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeTimeDAOIF ))
    {
      this.invalidAttributeType(MdAttributeTimeInfo.CLASS, userDefinedAlias);
    }
    return (AttributeTime) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute time statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute time statement object.
   */
  public AttributeTime aTime(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeTimeDAOIF ))
    {
      this.invalidAttributeType(MdAttributeTimeInfo.CLASS, userDefinedAlias);
    }
    return (AttributeTime) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute DateTime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute DateTime statement object.
   */
  public AttributeDateTime aDateTime(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDateTimeDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDateTimeInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDateTime) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute DateTime statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute DateTime statement object.
   */
  public AttributeDateTime aDateTime(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDateTimeDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDateTimeInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDateTime) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeIntegerDAOIF ))
    {
      this.invalidAttributeType(MdAttributeIntegerInfo.CLASS, userDefinedAlias);
    }
    return (AttributeInteger) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute integer statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute integer statement object.
   */
  public AttributeInteger aInteger(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeIntegerDAOIF ))
    {
      this.invalidAttributeType(MdAttributeIntegerInfo.CLASS, userDefinedAlias);
    }
    return (AttributeInteger) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Long statement object.
   */
  public AttributeLong aLong(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeLongDAOIF ))
    {
      this.invalidAttributeType(MdAttributeLongInfo.CLASS, userDefinedAlias);
    }
    return (AttributeLong) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Long statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Long statement object.
   */
  public AttributeLong aLong(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeLongDAOIF ))
    {
      this.invalidAttributeType(MdAttributeLongInfo.CLASS, userDefinedAlias);
    }
    return (AttributeLong) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Double statement object.
   */
  public AttributeDouble aDouble(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDoubleDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDoubleInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDouble) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Double statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Double statement object.
   */
  public AttributeDouble aDouble(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDoubleDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDoubleInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDouble) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Decimal statement object.
   */
  public AttributeDecimal aDecimal(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDecimalDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDecimalInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDecimal) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Decimal statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Decimal statement object.
   */
  public AttributeDecimal aDecimal(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeDecimalDAOIF ))
    {
      this.invalidAttributeType(MdAttributeDecimalInfo.CLASS, userDefinedAlias);
    }
    return (AttributeDecimal) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Float statement object.
   */
  public AttributeFloat aFloat(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeFloatDAOIF ))
    {
      this.invalidAttributeType(MdAttributeFloatInfo.CLASS, userDefinedAlias);
    }
    return (AttributeFloat) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Float statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Float statement object.
   */
  public AttributeFloat aFloat(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeFloatDAOIF ))
    {
      this.invalidAttributeType(MdAttributeFloatInfo.CLASS, userDefinedAlias);
    }
    return (AttributeFloat) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Boolean statement object.
   */
  public AttributeBoolean aBoolean(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeBooleanDAOIF ))
    {
      this.invalidAttributeType(MdAttributeBooleanInfo.CLASS, userDefinedAlias);
    }
    return (AttributeBoolean) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Boolean statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Boolean statement object.
   */
  public AttributeBoolean aBoolean(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeBooleanDAOIF ))
    {
      this.invalidAttributeType(MdAttributeBooleanInfo.CLASS, userDefinedAlias);
    }
    return (AttributeBoolean) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Blob statement object.
   */
  public AttributeBlob aBlob(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeBlobDAOIF ))
    {
      this.invalidAttributeType(MdAttributeBlobInfo.CLASS, userDefinedAlias);
    }
    return (AttributeBlob) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Blob statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Blob statement object.
   */
  public AttributeBlob aBlob(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeBlobDAOIF ))
    {
      this.invalidAttributeType(MdAttributeBlobInfo.CLASS, userDefinedAlias);
    }
    return (AttributeBlob) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Struct statement object.
   */
  public AttributeStruct aStruct(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeStructDAOIF ))
    {
      this.invalidAttributeType(MdAttributeStructInfo.CLASS, userDefinedAlias);
    }
    return (AttributeStruct) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Struct statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Struct statement object.
   */
  public AttributeStruct aStruct(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeStructDAOIF ))
    {
      this.invalidAttributeType(MdAttributeStructInfo.CLASS, userDefinedAlias);
    }
    return (AttributeStruct) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeLocalCharacterDAOIF ))
    {
      this.invalidAttributeType(MdAttributeLocalCharacterInfo.CLASS, userDefinedAlias);
    }
    return (AttributeLocal) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalCharacter(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeLocalCharacterDAOIF ))
    {
      this.invalidAttributeType(MdAttributeLocalCharacterInfo.CLASS, userDefinedAlias);
    }
    return (AttributeLocal) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeLocalTextDAOIF ))
    {
      this.invalidAttributeType(MdAttributeLocalTextInfo.CLASS, userDefinedAlias);
    }
    return (AttributeLocal) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute local statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute local statement object.
   */
  public AttributeLocal aLocalText(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeLocalTextDAOIF ))
    {
      this.invalidAttributeType(MdAttributeLocalTextInfo.CLASS, userDefinedAlias);
    }
    return (AttributeLocal) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute File statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute File statement object.
   */
  public AttributeReference aFile(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeFileDAOIF ))
    {
      this.invalidAttributeType(MdAttributeFileInfo.CLASS, userDefinedAlias);
    }
    return (AttributeReference) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute File statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute File statement object.
   */
  public AttributeReference aFile(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeFileDAOIF ))
    {
      this.invalidAttributeType(MdAttributeFileInfo.CLASS, userDefinedAlias);
    }
    return (AttributeReference) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Reference statement object.
   */
  public AttributeReference aReference(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeReferenceDAOIF ))
    {
      this.invalidAttributeType(MdAttributeReferenceInfo.CLASS, userDefinedAlias);
    }
    return (AttributeReference) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Reference statement object.
   */
  public AttributeTerm aTerm(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeReferenceDAOIF ))
    {
      this.invalidAttributeType(MdAttributeTermInfo.CLASS, userDefinedAlias);
    }
    return (AttributeTerm) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Reference statement object.
   */
  public AttributeReference aReference(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeReferenceDAOIF ))
    {
      this.invalidAttributeType(MdAttributeReferenceInfo.CLASS, userDefinedAlias);
    }
    return (AttributeReference) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Reference statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Reference statement object.
   */
  public AttributeTerm aTerm(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeTermDAOIF ))
    {
      this.invalidAttributeType(MdAttributeTermInfo.CLASS, userDefinedAlias);
    }

    return (AttributeTerm) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute Enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @return Attribute Enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String userDefinedAlias)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeEnumerationDAOIF ))
    {
      this.invalidAttributeType(MdAttributeEnumerationInfo.CLASS, userDefinedAlias);
    }
    return (AttributeEnumeration) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, selectable.getUserDefinedDisplayLabel());
  }

  /**
   * Returns an attribute Enumeration statement object.
   * 
   * @param name
   *          name of the attribute.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute Enumeration statement object.
   */
  public AttributeEnumeration aEnumeration(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    Selectable selectable = this.getSelectableRef(userDefinedAlias);

    MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

    if (! ( mdAttributeIF instanceof MdAttributeEnumerationDAOIF ))
    {
      this.invalidAttributeType(MdAttributeEnumerationInfo.CLASS, userDefinedAlias);
    }
    return (AttributeEnumeration) internalAttributeFactory(selectable, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a subselect where this value object has one selectable.
   * 
   * @return {@link SelectableSubSelect}
   */
  public SelectableSubSelect getSubSelect()
  {
    return new SelectableSubSelect(this);
  }

  /**
   * Returns a subselect where this value object has one selectable.
   * 
   * @return {@link SelectableSubSelect}
   */
  public SelectableSubSelect getSubSelect(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSubSelect(this, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Boolean selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Boolean selectable.
   */
  public SelectableSQLBoolean aSQLBoolean(String attributeName, String sql)
  {
    return new SelectableSQLBoolean(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Boolean selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Boolean selectable.
   */
  public SelectableSQLBoolean aSQLBoolean(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLBoolean(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Boolean selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Boolean selectable.
   */
  public SelectableSQLBoolean aSQLBoolean(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLBoolean(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Boolean selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Boolean selectable.
   */
  public SelectableSQLBoolean aSQLAggregateBoolean(String attributeName, String sql)
  {
    return new SelectableSQLBoolean(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Boolean selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Boolean selectable.
   */
  public SelectableSQLBoolean aSQLAggregateBoolean(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLBoolean(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Boolean selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Boolean selectable.
   */
  public SelectableSQLBoolean aSQLAggregateBoolean(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLBoolean(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLCharacter aSQLCharacter(String attributeName, String sql)
  {
    return new SelectableSQLCharacter(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLCharacter aSQLCharacter(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLCharacter(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLCharacter aSQLCharacter(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLCharacter(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLUUID aSQLUUID(String attributeName, String sql)
  {
    return new SelectableSQLUUID(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLUUID aSQLUUID(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLUUID(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLUUID aSQLUUID(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLUUID(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLCharacter aSQLAggregateCharacter(String attributeName, String sql)
  {
    return new SelectableSQLCharacter(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLCharacter aSQLAggregateCharacter(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLCharacter(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL character selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL character selectable.
   */
  public SelectableSQLCharacter aSQLAggregateCharacter(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLCharacter(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Text selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Text selectable.
   */
  public SelectableSQLText aSQLText(String attributeName, String sql)
  {
    return new SelectableSQLText(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Text selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Text selectable.
   */
  public SelectableSQLText aSQLText(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLText(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Text selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Text selectable.
   */
  public SelectableSQLText aSQLText(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLText(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Text selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Text selectable.
   */
  public SelectableSQLText aSQLAggregateText(String attributeName, String sql)
  {
    return new SelectableSQLText(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Text selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Text selectable.
   */
  public SelectableSQLText aSQLAggregateText(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLText(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Text selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Text selectable.
   */
  public SelectableSQLText aSQLAggregateText(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLText(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL CLOB selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL CLOB selectable.
   */
  public SelectableSQLClob aSQLClob(String attributeName, String sql)
  {
    return new SelectableSQLClob(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL CLOB selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL CLOB selectable.
   */
  public SelectableSQLClob aSQLClob(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLClob(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL CLOB selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL CLOB selectable.
   */
  public SelectableSQLClob aSQLClob(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLClob(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL CLOB selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL CLOB selectable.
   */
  public SelectableSQLClob aSQLAggregateClob(String attributeName, String sql)
  {
    return new SelectableSQLClob(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL CLOB selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL CLOB selectable.
   */
  public SelectableSQLClob aSQLAggregateClob(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLClob(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL CLOB selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL CLOB selectable.
   */
  public SelectableSQLClob aSQLAggregateClob(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLClob(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Date selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Date selectable.
   */
  public SelectableSQLDate aSQLDate(String attributeName, String sql)
  {
    return new SelectableSQLDate(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Date selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Date selectable.
   */
  public SelectableSQLDate aSQLDate(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDate(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Date selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Date selectable.
   */
  public SelectableSQLDate aSQLDate(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDate(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Date selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Date selectable.
   */
  public SelectableSQLDate aSQLAggregateDate(String attributeName, String sql)
  {
    return new SelectableSQLDate(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Date selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Date selectable.
   */
  public SelectableSQLDate aSQLAggregateDate(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDate(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Date selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Date selectable.
   */
  public SelectableSQLDate aSQLAggregateDate(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDate(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL DateTime selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL DateTime selectable.
   */
  public SelectableSQLDateTime aSQLDateTime(String attributeName, String sql)
  {
    return new SelectableSQLDateTime(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL DateTime selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL DateTime selectable.
   */
  public SelectableSQLDateTime aSQLDateTime(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDateTime(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL DateTime selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL DateTime selectable.
   */
  public SelectableSQLDateTime aSQLDateTime(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDateTime(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL DateTime selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL DateTime selectable.
   */
  public SelectableSQLDateTime aSQLAggregateDateTime(String attributeName, String sql)
  {
    return new SelectableSQLDateTime(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL DateTime selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL DateTime selectable.
   */
  public SelectableSQLDateTime aSQLAggregateDateTime(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDateTime(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL DateTime selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL DateTime selectable.
   */
  public SelectableSQLDateTime aSQLAggregateDateTime(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDateTime(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Time selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Time selectable.
   */
  public SelectableSQLTime aSQLTime(String attributeName, String sql)
  {
    return new SelectableSQLTime(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Time selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Time selectable.
   */
  public SelectableSQLTime aSQLTime(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLTime(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Time selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Time selectable.
   */
  public SelectableSQLTime aSQLTime(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLTime(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Time selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Time selectable.
   */
  public SelectableSQLTime aSQLAggregateTime(String attributeName, String sql)
  {
    return new SelectableSQLTime(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Time selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Time selectable.
   */
  public SelectableSQLTime aSQLAggregateTime(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLTime(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Time selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Time selectable.
   */
  public SelectableSQLTime aSQLAggregateTime(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLTime(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Decimal selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Decimal selectable.
   */
  public SelectableSQLDecimal aSQLDecimal(String attributeName, String sql)
  {
    return new SelectableSQLDecimal(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Decimal selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Decimal selectable.
   */
  public SelectableSQLDecimal aSQLDecimal(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDecimal(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Decimal selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Decimal selectable.
   */
  public SelectableSQLDecimal aSQLDecimal(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDecimal(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Decimal selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Decimal selectable.
   */
  public SelectableSQLDecimal aSQLAggregateDecimal(String attributeName, String sql)
  {
    return new SelectableSQLDecimal(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Decimal selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Decimal selectable.
   */
  public SelectableSQLDecimal aSQLAggregateDecimal(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDecimal(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Decimal selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Decimal selectable.
   */
  public SelectableSQLDecimal aSQLAggregateDecimal(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDecimal(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Double selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Double selectable.
   */
  public SelectableSQLDouble aSQLDouble(String attributeName, String sql)
  {
    return new SelectableSQLDouble(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Double selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Double selectable.
   */
  public SelectableSQLDouble aSQLDouble(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDouble(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Double selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Double selectable.
   */
  public SelectableSQLDouble aSQLDouble(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDouble(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Double selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Double selectable.
   */
  public SelectableSQLDouble aSQLAggregateDouble(String attributeName, String sql)
  {
    return new SelectableSQLDouble(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Double selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Double selectable.
   */
  public SelectableSQLDouble aSQLAggregateDouble(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLDouble(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Double selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Double selectable.
   */
  public SelectableSQLDouble aSQLAggregateDouble(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLDouble(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Float selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Float selectable.
   */
  public SelectableSQLFloat aSQLFloat(String attributeName, String sql)
  {
    return new SelectableSQLFloat(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Float selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Float selectable.
   */
  public SelectableSQLFloat aSQLFloat(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLFloat(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Float selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Float selectable.
   */
  public SelectableSQLFloat aSQLFloat(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLFloat(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Float selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Float selectable.
   */
  public SelectableSQLFloat aSQLAggregateFloat(String attributeName, String sql)
  {
    return new SelectableSQLFloat(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Float selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Float selectable.
   */
  public SelectableSQLFloat aSQLAggregateFloat(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLFloat(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Float selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Float selectable.
   */
  public SelectableSQLFloat aSQLAggregateFloat(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLFloat(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Integer selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Integer selectable.
   */
  public SelectableSQLInteger aSQLInteger(String attributeName, String sql)
  {
    return new SelectableSQLInteger(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Integer selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Integer selectable.
   */
  public SelectableSQLInteger aSQLInteger(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLInteger(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Integer selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Integer selectable.
   */
  public SelectableSQLInteger aSQLInteger(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLInteger(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Integer selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Integer selectable.
   */
  public SelectableSQLInteger aSQLAggregateInteger(String attributeName, String sql)
  {
    return new SelectableSQLInteger(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Integer selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Integer selectable.
   */
  public SelectableSQLInteger aSQLAggregateInteger(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLInteger(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Integer selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Integer selectable.
   */
  public SelectableSQLInteger aSQLAggregateInteger(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLInteger(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Long selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Long selectable.
   */
  public SelectableSQLLong aSQLLong(String attributeName, String sql)
  {
    return new SelectableSQLLong(false, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Long selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Long selectable.
   */
  public SelectableSQLLong aSQLLong(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLLong(false, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Long selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Long selectable.
   */
  public SelectableSQLLong aSQLLong(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLLong(false, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a custom SQL Long selectable.
   * 
   * @param attributeName
   * @param sql
   * 
   * @return custom SQL Long selectable.
   */
  public SelectableSQLLong aSQLAggregateLong(String attributeName, String sql)
  {
    return new SelectableSQLLong(true, this, attributeName, sql);
  }

  /**
   * Returns a custom SQL Long selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * 
   * @return custom SQL Long selectable.
   */
  public SelectableSQLLong aSQLAggregateLong(String attributeName, String sql, String userDefinedAlias)
  {
    return new SelectableSQLLong(true, this, attributeName, sql, userDefinedAlias, null);
  }

  /**
   * Returns a custom SQL Long selectable.
   * 
   * @param attributeName
   * @param sql
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * 
   * @return custom SQL Long selectable.
   */
  public SelectableSQLLong aSQLAggregateLong(String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new SelectableSQLLong(true, this, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Throws QueryException saying that a Selectable with the given alias is not
   * of the given type.
   * 
   * @param attributeType
   * @param userDefinedAlias
   * @throws QueryException
   */
  private void invalidAttributeType(String attributeType, String userDefinedAlias)
  {
    String error = "Attribute [" + userDefinedAlias + "] is not of type [" + attributeType + "]";
    throw new QueryException(error);
  }

  /**
   * DO NOT USE IN SELECT CLAUSES OR ELSE THE DATABASE COLUMN ALIASES MAY BE
   * INCORRECT! Returns the selectable object that was specified in the select
   * clause.
   * 
   * @param attributeAlias
   *          alias of the selectable.
   * @throws QueryException
   *           if no selectable object exists with the given alias.
   * @return selectable object that was specified in the select clause.
   */
  public Selectable getSelectableRef(String attributeAlias)
  {
    if (this.staleSelectables)
    {
      this.preprossesSelectableStructures();
    }

    Selectable selectable = this.processedSelectableMap.get(attributeAlias);

    if (selectable == null)
    {
      String error = "ValueQuery does not contain attribute [" + attributeAlias + "]";
      throw new QueryException(error);
    }

    return selectable;
  }

  /**
   * Returns true if the selectable object that was specified in the select
   * clause.
   * 
   * @param attributeAlias
   *          alias of the selectable.
   * @throws QueryException
   *           if no selectable object exists with the given alias.
   * @return selectable object that was specified in the select clause.
   */
  public boolean hasSelectableRef(String attributeAlias)
  {
    if (this.staleSelectables)
    {
      this.preprossesSelectableStructures();
    }

    return this.processedSelectableMap.containsKey(attributeAlias);
  }

  /**
   * Returns the an unprocessed selectable object that was specified in the
   * select clause. An unprocessed selectable is the original object that was
   * put into the select clause and not one that has been processed to include
   * updated aliases and display labels.
   * 
   * @param attributeAlias
   *          alias of the selectable.
   * @throws QueryException
   *           if no selectable object exists with the given alias.
   * @return selectable object that was specified in the select clause.
   */
  protected Selectable getRawSelectable(String attributeAlias)
  {
    if (this.staleSelectables)
    {
      this.preprossesSelectableStructures();
    }

    Selectable selectable = this.rawSelectableMap.get(attributeAlias);

    if (selectable == null)
    {
      String error = "ValueQuery does not contain attribute [" + attributeAlias + "]";
      throw new QueryException(error);
    }

    return selectable;
  }

  /**
   * Returns an array of the selectables specified.
   * 
   * @return list of the selectables specified.
   */
  public List<Selectable> getSelectableRefs()
  {
    if (this.staleSelectables)
    {
      this.preprossesSelectableStructures();
    }

    return this.selectableList;
  }

  // Heads up: Test
  // /**
  // *
  // * @param _mdAttributeIndicator
  // * @param _indicatorElement
  // * @param _attributeName
  // * @param _userDefinedAlias
  // * @param _userDefinedDisplayLabel
  // * @return
  // */
  // protected AttributeIndicator
  // buildAttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator,
  // IndicatorElementDAOIF _indicatorElement, Selectable _selectable, String
  // _userDefinedAlias,
  // String _userDefinedDisplayLabel)
  // {
  // if (_indicatorElement instanceof IndicatorPrimitiveDAOIF)
  // {
  // return this.buildAttributeIndicatorPrimitive(_mdAttributeIndicator,
  // (IndicatorPrimitiveDAOIF)_indicatorElement, _selectable, _userDefinedAlias,
  // _userDefinedDisplayLabel);
  // }
  // else // indicator composite
  // {
  // return this.buildAttributeIndicatorComposite(_mdAttributeIndicator,
  // (IndicatorCompositeDAOIF)_indicatorElement, _selectable, _userDefinedAlias,
  // _userDefinedDisplayLabel);
  // }
  // }
  //
  //
  // /**
  // *
  // * @param _mdAttributeIndicator
  // * @param _indicatorComposite
  // * @param _attributeName
  // * @param _userDefinedAlias
  // * @param _userDefinedDisplayLabel
  // * @return
  // */
  // protected AttributeIndicatorComposite
  // buildAttributeIndicatorComposite(MdAttributeIndicatorDAOIF
  // _mdAttributeIndicator, IndicatorCompositeDAOIF _indicatorComposite,
  // Selectable _selectable, String _userDefinedAlias,
  // String _userDefinedDisplayLabel)
  // {
  //
  // IndicatorElementDAOIF indicatorElementLeft =
  // _indicatorComposite.getLeftOperand();
  // AttributeIndicator left =
  // this.buildAttributeIndicator(_mdAttributeIndicator, indicatorElementLeft,
  // _selectable, _userDefinedAlias, _userDefinedDisplayLabel);
  //
  // EnumerationItemDAOIF operator = _indicatorComposite.getOperator();
  //
  // IndicatorElementDAOIF indicatorElementRight =
  // _indicatorComposite.getRightOperand();
  // AttributeIndicator right =
  // this.buildAttributeIndicator(_mdAttributeIndicator, indicatorElementRight,
  // _selectable, _userDefinedAlias, _userDefinedDisplayLabel);
  //
  // MdTableClassIF mdTableClass = (MdTableClassIF)
  // _mdAttributeIndicator.definedByClass();
  // String definingTableName = mdTableClass.getTableName();
  // String definingTableAlias = this.getTableAlias("", definingTableName);
  //
  // return new AttributeIndicatorComposite(_mdAttributeIndicator,
  // mdTableClass.definesType(), definingTableName, definingTableAlias, this,
  // left, operator, right, _userDefinedAlias, _userDefinedDisplayLabel);
  // }
  //
  // /**
  // *
  // * @param _mdAttributeIndicator
  // * @param _indicatorPrimitive
  // * @param _attributeName
  // * @param _userDefinedAlias
  // * @param _userDefinedDisplayLabel
  // * @return
  // */
  // protected AttributeIndicatorPrimitive
  // buildAttributeIndicatorPrimitive(MdAttributeIndicatorDAOIF
  // _mdAttributeIndicator, IndicatorPrimitiveDAOIF _indicatorPrimitive,
  // Selectable _selectable, String _userDefinedAlias,
  // String _userDefinedDisplayLabel)
  // {
  //
  // MdAttributePrimitiveDAOIF mdAttrPrimitive =
  // _indicatorPrimitive.getMdAttributePrimitive();
  //
  // // String userDefinedAlias, String userDefinedDisplayLabel
  // Selectable attributeInIndictor = this.internalAttributeFactory(_selectable,
  // mdAttrPrimitive, null, null);
  //
  // // Get the aggregate function.
  // EnumerationItemDAOIF aggFuncEnumItem =
  // _indicatorPrimitive.getAggregateFunction();
  //
  // AggregateFunction aggregateFunction = null;
  //
  // aggregateFunction = this.createIndicatorFunction(attributeInIndictor,
  // aggFuncEnumItem, aggregateFunction);
  //
  // MdTableClassIF mdTableClass = (MdTableClassIF)
  // _mdAttributeIndicator.definedByClass();
  // String definingTableName = mdTableClass.getTableName();
  // String definingTableAlias = this.getTableAlias("", definingTableName);
  //
  // return new AttributeIndicatorPrimitive(_mdAttributeIndicator,
  // definingTableName, definingTableAlias, this, aggregateFunction,
  // _userDefinedAlias, _userDefinedDisplayLabel);
  // }

  /**
   * Used internally by this class.
   * 
   * @return query Attribute object.
   */
  private Attribute internalAttributeFactory(Selectable selectable, MdAttributeConcreteDAOIF mdAttributeIF, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    if (this.staleSelectables)
    {
      this.preprossesSelectableStructures();
    }

    MdTableClassIF definingTableClassIF = (MdTableClassIF) mdAttributeIF.definedByClass();
    String definingTableName = this.tableAlias;
    String definingTableAlias = this.tableAlias;

    return attributeFactory(this, selectable, definingTableClassIF, definingTableName, definingTableAlias, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Used internally by this class.
   * 
   * @return query Attribute object.
   */
  protected static Attribute attributeFactory(ComponentQuery rootComponentQuery, Selectable selectable, MdTableClassIF definingTableClassIF, String definingTableName, String definingTableAlias, MdAttributeConcreteDAOIF mdAttributeIF, String userDefinedAlias, String userDefinedDisplayLabel)
  {

    Set<Join> attrTableJoinSet = new LinkedHashSet<Join>();

    Attribute attribute = null;

    if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger((MdAttributeIntegerDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeCharacterDAOIF)
    {
      attribute = new AttributeCharacter((MdAttributeCharacterDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeUUIDDAOIF)
    {
      attribute = new AttributeUUID((MdAttributeUUIDDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean((MdAttributeBooleanDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTextDAOIF)
    {
      attribute = new AttributeText((MdAttributeTextDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeClobDAOIF)
    {
      attribute = new AttributeClob((MdAttributeClobDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate((MdAttributeDateDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime((MdAttributeDateTimeDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime((MdAttributeTimeDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDecimalDAOIF)
    {
      attribute = new AttributeDecimal((MdAttributeDecimalDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble((MdAttributeDoubleDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat((MdAttributeFloatDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger((MdAttributeIntegerDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong((MdAttributeLongDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeBlobDAOIF)
    {
      attribute = new AttributeBlob((MdAttributeBlobDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeLocalDAOIF)
    {
      attribute = new AttributeLocal((MdAttributeLocalDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, ( (AttributeLocal) selectable ).getMdStructDAOIF(), ( (AttributeLocal) selectable ).structTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      attribute = new AttributeStruct((MdAttributeStructDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, ( (AttributeStruct) selectable ).getMdStructDAOIF(), ( (AttributeStruct) selectable ).structTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeFileDAOIF)
    {
      attribute = new AttributeReference((MdAttributeRefDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, ( (AttributeRef) selectable ).referenceMdBusinessIF, ( (AttributeRef) selectable ).referenceTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeReferenceDAOIF)
    {
      attribute = new AttributeReference((MdAttributeRefDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, ( (AttributeRef) selectable ).referenceMdBusinessIF, ( (AttributeRef) selectable ).referenceTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      attribute = new AttributeEnumeration((MdAttributeEnumerationDAOIF) mdAttributeIF, selectable.getAttributeNameSpace(), definingTableName, definingTableAlias, ( (AttributeEnumeration) selectable ).mdEnumerationTableName, ( (AttributeEnumeration) selectable ).referenceMdBusinessIF, ( (AttributeEnumeration) selectable ).referenceTableAlias, rootComponentQuery, attrTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    // A potential work in progress. Not sure the best way to convert the
    // MdAttributeIndicator
    // else if (mdAttributeIF instanceof MdAttributeIndicatorDAOIF)
    // {
    // MdAttributeIndicatorDAOIF mdAttributeIndicatorDAOIF =
    // (MdAttributeIndicatorDAOIF)mdAttributeIF;
    //
    // if
    // (mdAttributeIndicatorDAOIF.javaType(false).equals(BigDecimal.class.getName()))
    // {
    // attribute = new AttributeDecimal((MdAttributeDecimalDAOIF) mdAttributeIF,
    // selectable.getAttributeNameSpace(), definingTableName,
    // definingTableAlias, rootComponentQuery, attrTableJoinSet,
    // userDefinedAlias, userDefinedDisplayLabel);
    // }
    // else
    // {
    // attribute = new AttributeDouble((MdAttributeDoubleDAOIF) mdAttributeIF,
    // selectable.getAttributeNameSpace(), definingTableName,
    // definingTableAlias, rootComponentQuery, attrTableJoinSet,
    // userDefinedAlias, userDefinedDisplayLabel);
    //
    // }
    // }

    if (attribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.createAttribute(rootComponentQuery, selectable, definingTableClassIF, definingTableName, definingTableAlias, mdAttributeIF, userDefinedAlias, userDefinedDisplayLabel);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "Attribute type [" + mdAttributeIF.getType() + "] is invalid.";
      throw new QueryException(error);
    }

    attribute.setColumnName(selectable.getColumnAlias());

    return attribute;
  }

  /**
   * If you're actually calling this method, then you're probably doing
   * something wrong. I'm exposing it for IRS because IRS is a nightmare, but if
   * you actually need this method then check with Nathan/Smethie/Rich to make
   * sure what you're doing makes sense (because it probably doesn't).
   * 
   * @return
   */
  public Set<LeftJoin> getLeftOuterJoins()
  {
    return leftOuterJoinSet;
  }

  @Override
  protected Map<String, String> getFromTableMapInfoForQuery()
  {
    HashMap<String, String> returnMap = new HashMap<String, String>();
    returnMap.putAll(this.customFromTableMap);
    return returnMap;
  }

  /**
   * Returns an iterator of BusinessDAOs that match the query criteria specified
   * on this query object.
   * 
   * @return iterator of BusinessDAOs that match the query criteria specified on
   *         this query object.
   */
  public OIterator<ValueObject> getIterator()
  {
    String sqlStmt;
    if (_limit != null && _skip != null)
    {
      sqlStmt = this.getSQL(_limit, _skip);
    }
    else
    {
      sqlStmt = this.getSQL();
    }

    ResultSet results = Database.query(sqlStmt);
    return new ValueIterator<ValueObject>(this.selectableUsedInSelectClause, results);
  }

  /**
   * Returns an iterator of BusinessDAOs that match the query criteria specified
   * on this query object.
   * 
   * @param pageSize
   *          number of results per page
   * @param pageNumber
   *          page number
   * @return iterator of BusinessDAOs that match the query criteria specified on
   *         this query object.
   */
  public OIterator<ValueObject> getIterator(int pageSize, int pageNumber)
  {
    int limit = ComponentQuery.rowLimit(pageSize);
    int skip = ComponentQuery.rowSkip(pageSize, pageNumber);

    String sqlStmt = this.getSQL(limit, skip);
    ResultSet results = Database.query(sqlStmt);
    return new ValueIterator<ValueObject>(this.selectableUsedInSelectClause, results);

  }

  public String getOrderBySQL(OrderBy orderBy)
  {
    return orderBy.getSQL();
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Attribute createAttribute(ComponentQuery rootComponentQuery, Selectable selectable, MdTableClassIF definingTableClassIF, String definingTableName, String definingTableAlias, MdAttributeConcreteDAOIF mdAttributeIF, String userDefinedAlias, String userDefinedDisplayLabel);
  }

}
