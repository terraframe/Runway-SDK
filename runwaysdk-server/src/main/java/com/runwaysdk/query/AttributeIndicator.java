package com.runwaysdk.query;

import java.util.List;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;



public abstract class AttributeIndicator implements SelectableIndicator
{
  protected String                         definingTableName;

  protected String                         definingTableAlias;
  
  /**
   * The {@link MdAttributeIndicatorDAOIF} metadata of the attribute defined on the class.
   */
  protected MdAttributeIndicatorDAOIF       mdAttributeIndicator;
  
  protected String                          userDefinedAlias;

  protected String                          userDefinedDisplayLabel;
  
  protected ComponentQuery                  rootQuery;
  
  private   Object                          data;
 
  
  // Reference to all MdAttributes that were involved in constructing this
  // attribute;
  protected Set<MdAttributeConcreteDAOIF>   entityMdAttributeIFset;
  
  protected AttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _definingTableName, String _definingTableAlias, ComponentQuery _rootQuery)
  {
    this.init(_mdAttributeIndicator, _definingTableName, _definingTableAlias, null, null, _rootQuery);
  }
  
  protected AttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _definingTableName, String _definingTableAlias, String _userDefinedAlias, String _userDefinedDisplayLabel, ComponentQuery _rootQuery)
  {
    this.init(_mdAttributeIndicator, _definingTableName, _definingTableAlias, _userDefinedAlias, _userDefinedDisplayLabel, _rootQuery);
  }
  
  
  private void init(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _definingTableName, String _definingTableAlias, String _userDefinedAlias, String _userDefinedDisplayLabel, ComponentQuery _rootQuery)
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
      this.userDefinedDisplayLabel   = "";
    }
    else
    {
      this.userDefinedDisplayLabel   = _userDefinedDisplayLabel;
    }
    
    this.definingTableName           = _definingTableName;
    
    this.definingTableAlias          = _definingTableAlias;
    
    this.rootQuery                   = _rootQuery;
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


  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    this.entityMdAttributeIFset.addAll(mdAttributeConcreteDAOIFList);
  }  
  
  @Override
  public String getDefiningTableName()
  {
    return this.definingTableName;
  }
  
  @Override
  public String getDefiningTableAlias()
  {
    return this.definingTableAlias;
  }
  

  @Override
  public ComponentQuery getRootQuery()
  {
    return this.rootQuery;
  }
  
  /**
   * Calculates a name for the result set.
   * 
   * @return a name for the result set.
   */
  protected abstract String calculateName();
  
  
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

  @Override
  public Selectable clone() throws CloneNotSupportedException
  {
    return (Selectable)super.clone();
  }
}
