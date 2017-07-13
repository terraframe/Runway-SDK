package com.runwaysdk.query;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.database.Database;

public class AttributeIndicatorPrimitive extends AttributeIndicator
{
  
  private   AggregateFunction               aggregateFunction;
 
  
  protected AttributeIndicatorPrimitive(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeName, String _definingTableName, String _definingTableAlias, ComponentQuery _rootQuery, AggregateFunction _aggregateFunction)
  {
    super(_mdAttributeIndicator, _attributeName, _definingTableName, _definingTableAlias, _rootQuery);
    this.init(_aggregateFunction);
  }
  
  protected AttributeIndicatorPrimitive(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeName, String _definingTableName, String _definingTableAlias, ComponentQuery _rootQuery, AggregateFunction _aggregateFunction, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    super(_mdAttributeIndicator, _attributeName, _definingTableName, _definingTableAlias, _userDefinedAlias, _userDefinedDisplayLabel, _rootQuery);
    this.init(_aggregateFunction);
  }


  private void init(AggregateFunction _aggregateFunction)
  { 
    this.aggregateFunction       = _aggregateFunction;
    
    this.entityMdAttributeIFset = new HashSet<MdAttributeConcreteDAOIF>();
  }
  
  /**
   * Returns the database column name representing this attribute.
   *
   * @return database column name representing this attribute.
   */
// Heads Up: Test
//  public String getDbColumnName()
//  {
//    return this.aggregateFunction.getDbColumnName();
//  }

  /**
   * Calculates a name for the result set.
   * 
   * @return a name for the result set.
   */
  protected String calculateName()
  {
    return this.aggregateFunction.calculateName();
  }
  
  /**
   * Returns the namespace of the attribute.
   * 
   * @return namespace of the attribute.
   */
  public String getAttributeNameSpace()
  {
    return this.aggregateFunction.getAttributeNameSpace();
  }

  // Equals
  /**
   * Character = comparison case sensitive. This method accepts any Attribute
   * because databases generally allow for comparisons between different types.
   * 
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(Selectable _selectable)
  {
    return this.aggregateFunction.EQ(_selectable);
  }
  
  public Condition EQ(String statement)
  {
    return this.aggregateFunction.EQ(statement);
  }


  /**
   * Not Equals.
   * 
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(Selectable _selectable)
  {
    return this.aggregateFunction.NE(_selectable);
  }

  
  /**
   * blob not equal comparison.
   * 
   * @param statement
   * @return Condition object
   */
  public Condition NE(String statement)
  {
    return this.aggregateFunction.NE(statement);
  }
  
  /**
   * Creates a subselect IN condition where this attribute and the given
   * ValueQuery.
   *
   * @param selectable
   * @return Condition to add to the query.
   */
  @Override
  public AttributeCondition SUBSELECT_IN(Selectable _selectable)
  {
    return this.aggregateFunction.SUBSELECT_IN(_selectable);
  }

  @Override
  public AttributeCondition SUBSELECT_NOT_IN(Selectable _selectable)
  {
    return this.aggregateFunction.SUBSELECT_NOT_IN(_selectable);
  }
  
  @Override
  public String getColumnAlias()
  {
    return this.aggregateFunction.getColumnAlias();
  }

  @Override
  public void setColumnAlias(String _alias)
  {
    this.aggregateFunction.setColumnAlias(_alias);
  }

  @Override
  public String getDbQualifiedName()
  {
    return this.aggregateFunction.getDbQualifiedName();
  }

  @Override
  public String getFullyQualifiedNameSpace()
  {
    return this.aggregateFunction.getFullyQualifiedNameSpace();
  }

  @Override
  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    return this.mdAttributeIndicator;
  }

  @Override
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeList = new HashSet<MdAttributeConcreteDAOIF>();
    mdAttributeList.add(this.mdAttributeIndicator);
    mdAttributeList.addAll(this.aggregateFunction.getAllEntityMdAttributes());
    mdAttributeList.addAll(this.entityMdAttributeIFset);
    return mdAttributeList;
  }


  @Override
  public Attribute getAttribute()
  {
    return this.aggregateFunction.getAttribute();
  }

  @Override
  public SelectableAggregate getAggregateFunction()
  {
    return this.aggregateFunction;
  }

  @Override
  public boolean isAggregateFunction()
  {
    return true;
  }

  @Override
  public String getSQL()
  {
    String sql = this.aggregateFunction.getSQL();
    
    if (this.aggregateFunction.getClass().equals(COUNT.class))
    {
      sql = Database.castToDecimal(sql);
    }

    return sql;
  }

  /**
   *
   */
  public String getSQLIgnoreCase()
  {
    return this.aggregateFunction.getSQLIgnoreCase();
  }
  
  /**
   * Returns the SQL required for this {@link Selectable} in the lefthand side of a
   * {@link Selectable} clause.
   * 
   * @return SQL required for this {@link Selectable} in the lefthand side of a
   *         {@link Selectable} clause.
   */
  @Override
  public String getSubSelectSQL()
  {
    return this.getSQL();
  }

  /**
   * Returns a Set of TableJoin objects that represent joins statements that are
   * required for this expression.
   * 
   * @return Set of TableJoin objects that represent joins statements that are
   *         required for this expression, or null of there are none.
   */
  @Override
  public Set<Join> getJoinStatements()
  {
    return this.aggregateFunction.getJoinStatements();
  }

  /**
   * Visitor to traverse the query object structure.
   * 
   * @param visitor
   */
  @Override
  public Map<String, String> getFromTableMap()
  {
    return this.aggregateFunction.getFromTableMap();
  }

  @Override
  public void accept(Visitor visitor)
  {
    this.aggregateFunction.accept(visitor);
  }

  @Override
  public AttributeIndicatorPrimitive clone() throws CloneNotSupportedException
  {
    return (AttributeIndicatorPrimitive) super.clone();
  }

  @Override
  public Condition getCondition(String _operator, String _value)
  {
    return this.aggregateFunction.getCondition(_operator, _value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ColumnInfo getColumnInfo()
  {
    return new ColumnInfo(this.getDefiningTableName(), this.getDefiningTableAlias(), this.getDbColumnName(), this.getColumnAlias());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ColumnInfo> getColumnInfoList()
  {
    List<ColumnInfo> columnInfoList = new LinkedList<ColumnInfo>();
    columnInfoList.add(this.getColumnInfo());
    return columnInfoList;
  }
  
  /**
   * Returns the Selectable that is a parameter to the function.
   * 
   * @return Selectable that is a parameter to the function.
   */
  public Selectable getSelectable()
  {
    return this.aggregateFunction.getSelectable();
  }

  
}
