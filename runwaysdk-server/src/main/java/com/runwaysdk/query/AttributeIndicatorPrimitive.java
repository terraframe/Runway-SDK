package com.runwaysdk.query;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;

public class AttributeIndicatorPrimitive extends AttributeIndicator
{
  /**
   * The {@link MdAttributeIndicatorDAOIF} metadata of the attribute defined on the class.
   */
  private   MdAttributeIndicatorDAOIF       mdAttributeIndicator;
  
  private   AggregateFunction               aggregateFunction;
  
  protected String                          userDefinedAlias;

  protected String                          userDefinedDisplayLabel;
  
  private   Object                          data;
  
  // Reference to all MdAttributes that were involved in constructing this
  // attribute;
  protected Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset;
  
  protected AttributeIndicatorPrimitive(MdAttributeIndicatorDAOIF _mdAttributeIndicator, AggregateFunction _aggregateFunction)
  {
    this.init(_mdAttributeIndicator, _aggregateFunction, null, null);
  }
  
  protected AttributeIndicatorPrimitive(MdAttributeIndicatorDAOIF _mdAttributeIndicator, AggregateFunction _aggregateFunction, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    this.init(_mdAttributeIndicator, _aggregateFunction, _userDefinedAlias, _userDefinedDisplayLabel);
  }


  private void init(MdAttributeIndicatorDAOIF _mdAttributeIndicator, AggregateFunction _aggregateFunction,
      String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    this.mdAttributeIndicator    = _mdAttributeIndicator;
    this.aggregateFunction       = _aggregateFunction;
    
    if (_userDefinedAlias == null)
    {
      this.userDefinedAlias = "";
    }
    else
    {
      MetadataDAO.validateName(_userDefinedAlias.trim());
      this.userDefinedAlias        = _userDefinedAlias;
    }
    
    if (_userDefinedDisplayLabel == null)
    {
      this.userDefinedDisplayLabel = "";
    }
    else
    {
      this.userDefinedDisplayLabel = _userDefinedDisplayLabel.trim();
    }
    
    this.entityMdAttributeIFset = new HashSet<MdAttributeConcreteDAOIF>();
  }
  
  @Override
  public Object getData()
  {
    return this.data;
  }

  @Override
  public void setData(Object data)
  {
    this.data = data;
  }
  
  /**
   * Returns the database column name representing this attribute.
   *
   * @return database column name representing this attribute.
   */
  public String getDbColumnName()
  {
    return this.aggregateFunction.getDbColumnName();
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
  public String _getAttributeName()
  {
    return this.aggregateFunction._getAttributeName();
  }

  @Override
  public String getUserDefinedAlias()
  {
    return this.userDefinedAlias;
  }

  @Override
  public void setUserDefinedAlias(String _userDefinedAlias)
  {
    this.userDefinedAlias = _userDefinedAlias;
  }

  @Override
  public String getUserDefinedDisplayLabel()
  {
    return this.getUserDefinedDisplayLabel();
  }

  @Override
  public void setUserDefinedDisplayLabel(String _userDefinedDisplayLabel)
  {
    this.userDefinedDisplayLabel = _userDefinedDisplayLabel;
  }

  @Override
  public String getColumnAlias()
  {
    return this.aggregateFunction.getColumnAlias();
  }

  @Override
  public String getResultAttributeName()
  {
    if (this.userDefinedAlias.trim().length() != 0)
    {
      return this.userDefinedAlias;
    }
    else
    {
      return this._getAttributeName();
    }
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
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    this.entityMdAttributeIFset.addAll(mdAttributeConcreteDAOIFList);
  }

  @Override
  public String getDefiningTableName()
  {
    return this.aggregateFunction.getDefiningTableName();
  }

  @Override
  public String getDefiningTableAlias()
  {
    return this.aggregateFunction.getDefiningTableAlias();
  }

  @Override
  public Attribute getAttribute()
  {
    return this.aggregateFunction.getAttribute();
  }

  @Override
  public ComponentQuery getRootQuery()
  {
    return this.aggregateFunction.getRootQuery();
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
    return this.aggregateFunction.getSQL();
  }

  /**
   * Returns the SQL required for this selectable in the lefthand side of a
   * subselect clause.
   * 
   * @return SQL required for this selectable in the lefthand side of a
   *         subselect clause.
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
  
}
