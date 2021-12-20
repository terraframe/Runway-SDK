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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.query.sql.MdAttributeConcrete_SQL;

public abstract class SelectableSQL implements SelectableSingle, SelectableAggregate
{
  private ValueQuery rootQuery;

  protected MdAttributeConcrete_SQL mdAttributeConcrete_SQL;

  private String attributeName;
  private String sql;
  private String userDefinedAlias;
  private String userDefinedDisplayLabel;
  private String columnAlias;
  private boolean isAggregate;
  private String attributeNameSpace;
  private Object data;

  // Reference to all MdAttributes that were involved in constructing this attribute;
  protected Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset;

  protected SelectableSQL(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    this(isAggregate, rootQuery, attributeName, sql, null, null);
  }

  protected SelectableSQL(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this.isAggregate = isAggregate;
    this.rootQuery = rootQuery;
    this.sql = sql;

    if (userDefinedAlias == null || userDefinedAlias.equals(""))
    {
      this.userDefinedAlias = "";
    }
    else
    {
      MetadataDAO.validateName(userDefinedAlias.trim());
      this.userDefinedAlias = userDefinedAlias.trim();
    }

    if (userDefinedDisplayLabel == null || userDefinedDisplayLabel.equals(""))
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

    this.entityMdAttributeIFset = new HashSet<MdAttributeConcreteDAOIF>();
  }

  public String generateColumnAlias()
  {
    this.columnAlias = this.rootQuery.getQueryFactory().getColumnAlias(ServerIDGenerator.nextID() + "-" + this.attributeNameSpace, this.getDbColumnName());
    return this.columnAlias;
  }
  
  protected String calculateDisplayLabel(String attributeName, String userDefinedDisplayLabel)
  {
    String displayLabel = null;

    if (userDefinedDisplayLabel != null && !userDefinedDisplayLabel.trim().equals(""))
    {
      displayLabel = userDefinedDisplayLabel;
    }
    else
    {
      displayLabel = attributeName;
    }
    return displayLabel;
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

  public void accept(Visitor visitor)
  {
    // This has hardcoded SQL so nothing to visit to automatically determine joins, from clauses, and such.
    if(visitor instanceof BuildSQLVisitor)
    {
      ( (BuildSQLVisitor) visitor ).setContainsSelectableSQL(true);
    }
  }

  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeSet = new HashSet<MdAttributeConcreteDAOIF>();
    mdAttributeSet.add(this.mdAttributeConcrete_SQL);
    mdAttributeSet.addAll(this.entityMdAttributeIFset);
    return mdAttributeSet;
  }

  /**
   * Sets additional MdAttributes that are involved in building the select clause.
   * @param mdAttributeConcreteDAOIFList additional MdAttributes
   */
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    this.entityMdAttributeIFset.addAll(mdAttributeConcreteDAOIFList);
  }

  public Attribute getAttribute()
  {
    return null;
  }

  public String getAttributeNameSpace()
  {
    return this.attributeNameSpace;
  }
  
  public void setAttributeNameSpace(String _attrns)
  {
    this.attributeNameSpace = _attrns;
  }

  public String getColumnAlias()
  {
    return this.columnAlias;
  }

  public void setColumnAlias(String alias)
  {
    this.columnAlias = alias;
  }

  public Map<String, String> getFromTableMap()
  {
    return new HashMap<String, String>(0);
  }

  public String getFullyQualifiedNameSpace()
  {
    return this.getAttributeNameSpace() + "." + this._getAttributeName();
  }

  public Set<Join> getJoinStatements()
  {
    return new HashSet<Join>(0);
  }

  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    return this.mdAttributeConcrete_SQL;
  }

  public String _getAttributeName()
  {
    return this.attributeName;
  }

  public String getDbColumnName()
  {
    return this.columnAlias;
  }

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

  public String getDbQualifiedName()
  {
    return this.getFullyQualifiedNameSpace();
  }

  public String getDefiningTableAlias()
  {
    return this.rootQuery.getTableAlias();
  }

  public String getDefiningTableName()
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
  
  public ValueQuery getRootQuery()
  {
    return this.rootQuery;
  }

  /**
   * Returns the Selectable that is a parameter to the function.
   * @return Selectable that is a parameter to the function.
   */
  public Selectable getSelectable()
  {
    return this;
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
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

  /**
   *
   */
  public String getSQLIgnoreCase()
  {
    return Database.toUpperFunction(this.getSQL());
  }

  public String getSQL()
  {
    return "("+this.sql+")";
  }

  public void setSQL(String sql)
  {
    this.sql = "("+sql+")";
  }

  public String getSubSelectSQL()
  {
    return this.getSQL();
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

  public abstract Condition getCondition(String operator, String value);


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


  /**
   *
   */
  public SelectableSQL clone() throws CloneNotSupportedException
  {
    return (SelectableSQL) super.clone();
  }

}
