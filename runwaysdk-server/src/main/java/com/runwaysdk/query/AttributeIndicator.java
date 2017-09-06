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

import java.util.List;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeIndicator_Q;



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
  
  private   String                          attributeName;
 
  
  // Reference to all MdAttributes that were involved in constructing this
  // attribute;
  protected Set<MdAttributeConcreteDAOIF>   entityMdAttributeIFset;
  
  protected AttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeName, String _definingTableName, String _definingTableAlias, ComponentQuery _rootQuery)
  {
    this.init(_mdAttributeIndicator, _attributeName, _definingTableName, _definingTableAlias, null, null, _rootQuery);
  }
  
  protected AttributeIndicator(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeName, String _definingTableName, String _definingTableAlias, String _userDefinedAlias, String _userDefinedDisplayLabel, ComponentQuery _rootQuery)
  {
    this.init(_mdAttributeIndicator, _attributeName, _definingTableName, _definingTableAlias, _userDefinedAlias, _userDefinedDisplayLabel, _rootQuery);
  }
  
  
  private void init(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeName, String _definingTableName, String _definingTableAlias, String _userDefinedAlias, String _userDefinedDisplayLabel, ComponentQuery _rootQuery)
  {
    this.attributeName           = _attributeName;
  
    this.mdAttributeIndicator    =  (MdAttributeIndicator_Q)Attribute.convertMdAttribute(_mdAttributeIndicator);
    
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
  public String getDbColumnName()
  {
    if (this.attributeName == null)
    {
      this.attributeName = this.getRootQuery().getColumnAlias(this.getAttributeNameSpace() + this.calculateName(), this.getMdAttributeIF().definesAttribute());
    }
    return this.attributeName;
  }
  
  @Override
  public String _getAttributeName()
  {
    return this.attributeName;
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
    return this.userDefinedDisplayLabel;
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
