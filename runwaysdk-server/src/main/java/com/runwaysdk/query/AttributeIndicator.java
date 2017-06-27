package com.runwaysdk.query;

import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;



public abstract class AttributeIndicator implements SelectablePrimitive, Statement
{
  /**
   * The {@link MdAttributeIndicatorDAOIF} metadata of the attribute defined on the class.
   */
  protected   MdAttributeIndicatorDAOIF     mdAttributeIndicator;
  
  protected String                          userDefinedAlias;

  protected String                          userDefinedDisplayLabel;
  
  protected AttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator)
  {
    this.init(_mdAttributeIndicator, null, null);
  }
  
  protected AttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    this.init(_mdAttributeIndicator, _userDefinedAlias, _userDefinedDisplayLabel);
  }
  
  
  private void init(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    this.mdAttributeIndicator    = _mdAttributeIndicator;
    
    if (_userDefinedAlias == null)
    {
      this.userDefinedAlias        = "";
    }
    else
    {
      this.userDefinedAlias        = _userDefinedAlias;
    }

    
    if (_userDefinedDisplayLabel == null)
    {
      this.userDefinedDisplayLabel        = "";
    }
    else
    {
      this.userDefinedDisplayLabel        = _userDefinedDisplayLabel;
    }
  }
  
//
//  @Override
//  public Condition EQ(String statement)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Condition NE(String statement)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeCondition SUBSELECT_IN(Selectable selectable)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeCondition SUBSELECT_NOT_IN(Selectable selectable)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getDbColumnName()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String _getAttributeName()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getUserDefinedAlias()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void setUserDefinedAlias(String userDefinedAlias)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public String getUserDefinedDisplayLabel()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void setUserDefinedDisplayLabel(String userDefinedDisplayLabel)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public String getColumnAlias()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getResultAttributeName()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void setColumnAlias(String alias)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public String getDbQualifiedName()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getAttributeNameSpace()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getFullyQualifiedNameSpace()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public MdAttributeConcreteDAOIF getMdAttributeIF()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public String getDefiningTableName()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getDefiningTableAlias()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Attribute getAttribute()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public ComponentQuery getRootQuery()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public SelectableAggregate getAggregateFunction()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public boolean isAggregateFunction()
//  {
//    // TODO Auto-generated method stub
//    return false;
//  }
//
//  @Override
//  public String getSQL()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getSubSelectSQL()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Set<Join> getJoinStatements()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Map<String, String> getFromTableMap()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void accept(Visitor visitor)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public Condition getCondition(String operator, String value)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public ColumnInfo getColumnInfo()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public List<ColumnInfo> getColumnInfoList()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public void setData(Object data)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public Object getData()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }

  @Override
  public Selectable clone() throws CloneNotSupportedException
  {
    return (Selectable)super.clone();
  }
}
