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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class SelectableSubSelect implements SelectablePrimitive
{
  /**
   * ValueQuery for the subselect.
   */
  protected ValueQuery valueQuery;

  protected Selectable selectable;

  protected String     userDefinedAlias;

  protected String     userDefinedDisplayLabel;

  protected String     columnAlias;

  private String       attributeName;

  private String       columnName;

  /**
   *
   * @param selectable Selectable that is a parameter to the function.
   * @param userDefinedAlias
   */
  protected SelectableSubSelect(ValueQuery valueQuery)
  {
    this.init(valueQuery, null, null);
  }

  /**
   *
   * @param selectable Selectable that is a parameter to the function.
   * @param userDefinedAlias
   */
  protected SelectableSubSelect(ValueQuery valueQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this.init(valueQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  private void init(ValueQuery valueQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    if (valueQuery.getSelectableRefs().size() != 1)
    {
      String error = "Value query specified in a sub-select must have exactly one attribute specified in the SELECT clause.";
      throw new QueryException(error);
    }

    this.selectable = valueQuery.getSelectableRefs().get(0);

    this.valueQuery = valueQuery;

    if (userDefinedAlias == null)
    {
      this.userDefinedAlias = "";
    }
    else
    {
      this.userDefinedAlias  = userDefinedAlias.trim();
    }

    if (userDefinedDisplayLabel == null)
    {
      this.userDefinedDisplayLabel = "";
    }
    else
    {
      this.userDefinedDisplayLabel  = userDefinedDisplayLabel.trim();
    }

    this.columnAlias        = this.getRootQuery().getColumnAlias(selectable.getAttributeNameSpace(), selectable.getDbColumnName());

    this.attributeName = selectable._getAttributeName();

    this.columnName = selectable.getDbColumnName();
  }

  public void accept(Visitor visitor)
  {
    // this is a self contained sql statement, so no visiting is required

  }

  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    return this.selectable.getAllEntityMdAttributes();
  }

  /**
   * Sets additional MdAttributes that are involved in building the select clause.
   * @param mdAttributeConcreteDAOIFList additional MdAttributes
   */
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    this.selectable.setAdditionalEntityMdAttributes(mdAttributeConcreteDAOIFList);
  }

  public Attribute getAttribute()
  {
    return selectable.getAttribute();
  }

  public String getAttributeNameSpace()
  {
    return this.selectable.getAttributeNameSpace();
  }

  public String getColumnAlias()
  {
    return this.columnAlias ;
  }

  /**
   * Returns the name of the alias used for the database table that defines the column for this attribute.
   * @return name of the alias used for the database table that defines the column for this attribute.
   */
  public String getDefiningTableAlias()
  {
    return this.selectable.getDefiningTableAlias();
  }

  /**
   * Returns the name of the database table that defines the column for this attribute.
   * @return name of the database table that defines the column for this attribute.
   */
  public String getDefiningTableName()
  {
    return this.selectable.getDefiningTableName();
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
  
  public Map<String, String> getFromTableMap()
  {
    return new HashMap<String, String>(0);
  }

  public String getFullyQualifiedNameSpace()
  {
    return this.getAttributeNameSpace()+"."+this._getAttributeName();
  }

  public Set<Join> getJoinStatements()
  {
    return new HashSet<Join>(0);
  }

  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    return this.selectable.getMdAttributeIF();
  }

  /**
   * Returns the name of this selectable.
   *
   * @return name of this selectable.
   */
  public String _getAttributeName()
  {
    return this.attributeName;
  }

  /**
   * Returns the database column name representing this selectable.
   *
   * @return database column name representing this selectable.
   */
  public String getDbColumnName()
  {
    return this.columnName;
  }

  public String getDbQualifiedName()
  {
    return this.selectable.getDbQualifiedName();
  }

  public String getResultAttributeName()
  {
    if (this.userDefinedAlias.trim().length() != 0)
    {
      return this.userDefinedAlias;
    }
    else
    {
      return this.columnAlias;
    }
  }

  public ComponentQuery getRootQuery()
  {
    return this.valueQuery;
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    return null;
  }

  /**
   * Returns true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   * @return true if this selectable is an aggregate function or contains an aggregate function.  False otherwise.
   */
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

  public String getSQL()
  {
    return "("+this.valueQuery.getSQL()+")";
  }

  public String getSubSelectSQL()
  {
    return this.getSQL();
  }

  public void setColumnAlias(String alias)
  {
    this.columnAlias = alias;
  }

  public String getUserDefinedAlias()
  {
    return this.userDefinedAlias;
  }

  public void setUserDefinedAlias(String userDefinedAlias)
  {
    this.userDefinedAlias = userDefinedAlias;
  }

  /**
   * Returns the user defined display label for this attribute.
   *
   * @return user defined display label for this attribute.
   */
  public String getUserDefinedDisplayLabel()
  {
    return this.userDefinedDisplayLabel;
  }

  /**
   * Sets the user defined display Label for this Selectable.
   */
  public void setUserDefinedDisplayLabel(String userDefinedDisplayLabel)
  {
    this.userDefinedDisplayLabel = userDefinedDisplayLabel;
  }

  public Condition getCondition(String operator, String value)
  {
    return this.selectable.getCondition(operator, value);
  }

  public Condition EQ(String statement)
  {
    return this.selectable.EQ(statement);
  }

  public Condition NE(String statement)
  {
    return this.selectable.EQ(statement);
  }

  /**
   * Creates a subselect IN condition where this attribute and the given ValueQuery.
   *
   * @param selectable
   * @return Conidtion to add to the query.
   */
  public AttributeCondition SUBSELECT_IN(Selectable selectable)
  {
    return new SubSelectExplicit_IN_Condition(this, selectable);
  }

  /**
   * Creates a subselect NOT IN condition where this attribute and the given ValueQuery.
   *
   * @param selectable
   * @return Conidtion to add to the query.
   */
  public AttributeCondition SUBSELECT_NOT_IN(Selectable selectable)
  {
    return new SubSelectExplicit_NOT_IN_Condition(this, selectable);
  }

  public SelectableSubSelect clone() throws CloneNotSupportedException
  {
    return (SelectableSubSelect)super.clone();
  }

}
