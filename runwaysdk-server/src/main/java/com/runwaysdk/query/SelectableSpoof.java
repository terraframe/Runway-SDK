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

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;

// This class is used as a placeholder for where a selectable is expected, but not used.
public class SelectableSpoof implements SelectableAggregate, Selectable
{
  private ComponentQuery rootQuery;
  
  private String attributeName;
  private String userDefinedAlias;
  private String userDefinedDisplayLabel;
  private String columnAlias;
  private boolean isAggregate;
  private String attributeNameSpace;
  private Object data;
  
  protected SelectableSpoof(boolean isAggregate, ComponentQuery rootQuery, String attributeName)
  {
    this(isAggregate, rootQuery, attributeName, null, null);
  }

  protected SelectableSpoof(boolean isAggregate, ComponentQuery rootQuery, String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this.isAggregate = isAggregate;
    this.rootQuery = rootQuery;

    if (userDefinedAlias == null)
    {
      this.userDefinedAlias = "";
    }
    else
    {
      MetadataDAO.validateName(userDefinedAlias.trim());
      this.userDefinedAlias = userDefinedAlias.trim();
    }

    if (userDefinedDisplayLabel == null)
    {
      this.userDefinedDisplayLabel = "";
    }
    else
    {
      this.userDefinedDisplayLabel = userDefinedDisplayLabel.trim();
    }

    this.attributeName = attributeName;

    this.columnAlias = attributeName;

    this.attributeNameSpace = rootQuery.getTableAlias();
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
  
  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#EQ(java.lang.String)
   */
  @Override
  public Condition EQ(String statement)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#NE(java.lang.String)
   */
  @Override
  public Condition NE(String statement)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#SUBSELECT_IN(com.runwaysdk.query.Selectable)
   */
  @Override
  public AttributeCondition SUBSELECT_IN(Selectable selectable)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#SUBSELECT_NOT_IN(com.runwaysdk.query.Selectable)
   */
  @Override
  public AttributeCondition SUBSELECT_NOT_IN(Selectable selectable)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getDbColumnName()
   */
  @Override
  public String getDbColumnName()
  {
    return this.columnAlias;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#_getAttributeName()
   */
  @Override
  public String _getAttributeName()
  {
    return this.attributeName;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getUserDefinedAlias()
   */
  @Override
  public String getUserDefinedAlias()
  {
    return this.userDefinedAlias;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#setUserDefinedAlias(java.lang.String)
   */
  @Override
  public void setUserDefinedAlias(String userDefinedAlias)
  {
    this.userDefinedAlias = userDefinedAlias;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getUserDefinedDisplayLabel()
   */
  @Override
  public String getUserDefinedDisplayLabel()
  {
    return this.userDefinedDisplayLabel;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#setUserDefinedDisplayLabel(java.lang.String)
   */
  @Override
  public void setUserDefinedDisplayLabel(String userDefinedDisplayLabel)
  {
    this.userDefinedDisplayLabel = userDefinedDisplayLabel;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getColumnAlias()
   */
  @Override
  public String getColumnAlias()
  {
    return this.columnAlias;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getResultAttributeName()
   */
  @Override
  public String getResultAttributeName()
  {
    if (this.userDefinedAlias.trim().length() != 0)
    {
      return this.userDefinedAlias;
    }
    else
    {
      return this.attributeName;
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#setColumnAlias(java.lang.String)
   */
  @Override
  public void setColumnAlias(String alias)
  {
    this.columnAlias = alias;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getDbQualifiedName()
   */
  @Override
  public String getDbQualifiedName()
  {
    return this.getFullyQualifiedNameSpace();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getAttributeNameSpace()
   */
  @Override
  public String getAttributeNameSpace()
  {
    return this.attributeNameSpace;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getFullyQualifiedNameSpace()
   */
  @Override
  public String getFullyQualifiedNameSpace()
  {
    return this.getAttributeNameSpace() + "." + this._getAttributeName();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getMdAttributeIF()
   */
  @Override
  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    // Not proud of this, but an MdAttribute is needed. However, as of this writing, this will
    // not result in extra joins, as the join set is empty. No join set means no from clause.
    MdEntityDAOIF metadataMdEntity = MdEntityDAO.getMdEntityDAO(MetadataInfo.CLASS);
    
    return metadataMdEntity.definesAttribute(ComponentInfo.OID);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getAllEntityMdAttributes()
   */
  @Override
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeSet = new HashSet<MdAttributeConcreteDAOIF>();
    return mdAttributeSet;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#setAdditionalEntityMdAttributes(java.util.List)
   */
  @Override
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    // balk
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefiningTableName()
  {
    return this.rootQuery.getTableAlias();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefiningTableAlias()
  {
    return this.rootQuery.getTableAlias();
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
  
  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getAttribute()
   */
  @Override
  public Attribute getAttribute()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getRootQuery()
   */
  @Override
  public ComponentQuery getRootQuery()
  {
    return this.rootQuery;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getAggregateFunction()
   */
  @Override
  public SelectableAggregate getAggregateFunction()
  {
    if (this.isAggregate)
    {
      return this;
    }
    else
    {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#isAggregateFunction()
   */
  @Override
  public boolean isAggregateFunction()
  {
    if (this.getAggregateFunction() != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getSQL()
   */
  @Override
  public String getSQL()
  {
    return "";
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getSubSelectSQL()
   */
  @Override
  public String getSubSelectSQL()
  {
    return this.getSQL();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getJoinStatements()
   */
  @Override
  public Set<Join> getJoinStatements()
  {
    return new HashSet<Join>(0);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getFromTableMap()
   */
  @Override
  public Map<String, String> getFromTableMap()
  {
    return new HashMap<String, String>(0);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#accept(com.runwaysdk.query.Visitor)
   */
  @Override
  public void accept(Visitor visitor)
  {
    // Nothing to see here. Move along.
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.query.Selectable#getCondition(java.lang.String, java.lang.String)
   */
  @Override
  public Condition getCondition(String operator, String value)
  {
    throw new UnsupportedOperationException();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.query.SelectableAggregate#getSelectable()
   */
  public Selectable getSelectable()
  {
    return this;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.query.SelectableAggregate#getSQLIgnoreCase()
   */
  public String getSQLIgnoreCase()
  {
    return this.getSQL();
  }

  /**
   * 
   */
  public Selectable clone() throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }
}
