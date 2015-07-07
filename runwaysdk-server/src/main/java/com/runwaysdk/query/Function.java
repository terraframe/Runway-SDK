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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.session.Session;

public abstract class Function implements SelectablePrimitive, Statement
{
  private MdAttributeConcrete_Q mdAttributeConcrete_Q;

  /**
   * Attribute that is a parameter to the function.
   */
  protected Selectable selectable;

  protected String     userDefinedAlias;

  protected String     userDefinedDisplayLabel;

  protected String     columnAlias;

  private String       attributeName;

  // Reference to all MdAttributes that were involved in constructing this attribute;
  protected Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset;

  /**
   *
   * @param selectable Selectable that is a parameter to the function.
   * @param userDefinedAlias
   */
  protected Function(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this.selectable = selectable;
    this.init(userDefinedAlias, userDefinedDisplayLabel);
  }

  private void init(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    if (userDefinedAlias == null)
    {
      this.userDefinedAlias = "";
    }
    else
    {
      MetadataDAO.validateName(userDefinedAlias.trim());
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

    this.columnAlias        = this.getRootQuery().getColumnAlias(this.getAttributeNameSpace(), this.getDbColumnName());

    this.entityMdAttributeIFset = new HashSet<MdAttributeConcreteDAOIF>();

    this.attributeName = null;
  }

  /**
   * Returns the name of the attribute used in the resultant {@link ValueObject}.
   * It is either the column alias or the user defined alias.
   * @return Returns the name of the attribute used in the resultant {@link ValueObject}.
   */
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

  /**
   * Returns the user defined alias for this attribute.
   * @return user defined alias for this attribute.
   */
  public String getUserDefinedAlias()
  {
    return this.userDefinedAlias;
  }

  /**
   * Sets the user defined alias for this Selectable.
   */
  public void setUserDefinedAlias(String userDefinedAlias)
  {
    this.userDefinedAlias = userDefinedAlias;
  }

  /**
   * Returns the user defined display label for this attribute.
   * @return user defined display label for this attribute.
   */
  public String getUserDefinedDisplayLabel()
  {
    return this.userDefinedDisplayLabel;
  }

  /**
   * Sets the user defined display label for this Selectable.
   */
  public void setUserDefinedDisplayLabel(String userDefinedDisplayLabel)
  {
    this.userDefinedDisplayLabel = userDefinedDisplayLabel;
  }

  /**
   * Returns the alias used in the select clause for the database column for this attribute.
   * @return alias used in the select clause for the database column for this attribute.
   */
  public String getColumnAlias()
  {
    return this.columnAlias;
  }

  public void setColumnAlias(String alias)
  {
    this.columnAlias = alias;
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    if (this instanceof AggregateFunction)
    {
      return (AggregateFunction)this;
    }

    return selectable.getAggregateFunction();
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
   * Calculates a display label for the result set.
   * @return display label for the result set.
   */
  protected String calculateDisplayLabel()
  {
    String displayLabel;

    // Base case
    if (this.selectable instanceof Function)
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateDisplayLabel()+")";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+this.mdAttributeConcrete_Q.getDisplayLabel(Session.getCurrentLocale())+")";
    }

    return displayLabel;
  }

  /**
   * Calculates a name for the result set.
   * @return a name for the result set.
   */
  protected String calculateName()
  {
    String displayLabel;

    //selectable is a function
    if (this.selectable instanceof Function)
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateName()+")";
    }
    else //its an attribute or selectableSQL
    {
      displayLabel = this.getFunctionName()+"("+this.selectable._getAttributeName()+")";
    }

    return displayLabel;
  }

  /**
   * Every Selectable eventually boils down to an attribute.
   * @return bottom most attribute.
   */
  public Attribute getAttribute()
  {
    return this.selectable.getAttribute();
  }

  /**
   * Returns the name of this selectable.
   *
   * @return name of this selectable.
   */
  public String _getAttributeName()
  {
    return this.getDbColumnName();
  }

  /**
   * Returns the database column name representing this attribute.
   *
   * @return database column name representing this attribute.
   */
  public String getDbColumnName()
  {
    if (this.attributeName == null)
    {
      this.attributeName = this.getRootQuery().getColumnAlias(this.getAttributeNameSpace()+this.calculateName(), this.getFunctionName());
    }
    return this.attributeName;
  }

  /**
   * Returns the alias used in the select clause.
   * @return alias used in the select clause.
   */
  public String getFullyQualifiedNameSpace()
  {
    return this.getAttributeNameSpace()+"."+this._getAttributeName();
  }

  /**
   * Returns the Selectable that is a parameter to the function.
   * @return Selectable that is a parameter to the function.
   */
  public Selectable getSelectable()
  {
    return this.selectable;
  }

  /**
   * Returns the ComponentQuery from which this attribute was created.
   * @return ComponentQuery from which this attribute was created.
   */
  public ComponentQuery getRootQuery()
  {
    return this.selectable.getRootQuery();
  }

  /**
   * Returns the namespace of the attribute.
   * @return namespace of the attribute.
   */
  public String getAttributeNameSpace()
  {
    return this.selectable.getAttributeNameSpace();
  }

  /**
   * Returns the MdAttributeIF that defines the attribute.
   * @return MdAttributeIF that defines the attribute.
   */
  public MdAttributeConcrete_Q getMdAttributeIF()
  {
    return this.mdAttributeConcrete_Q;
  }

  /**
   * Sets the MdAttributeIF that defines this function.
   *
   * @param _mdAttributeConcrete_Q
   */
  protected void setMdAttributeIF(MdAttributeConcrete_Q _mdAttributeConcrete_Q)
  {
    this.mdAttributeConcrete_Q = _mdAttributeConcrete_Q;
  }

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeList = new HashSet<MdAttributeConcreteDAOIF>();
    mdAttributeList.addAll(this.selectable.getAllEntityMdAttributes());
    mdAttributeList.addAll(this.entityMdAttributeIFset);
    return mdAttributeList;
  }

  /**
   * Sets additional MdAttributes that are involved in building the select clause.
   * @param mdAttributeConcreteDAOIFList additional MdAttributes
   */
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    this.entityMdAttributeIFset.addAll(mdAttributeConcreteDAOIFList);
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
   * Returns the name of the alias used for the database table that defines the column for this attribute.
   * @return name of the alias used for the database table that defines the column for this attribute.
   */
  public String getDefiningTableAlias()
  {
    return this.selectable.getDefiningTableAlias();
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
   * Returns the name of the database function.
   * @return name of the database function.
   */
  protected abstract String getFunctionName();

  /**
   *
   */
  public String getSQL()
  {
    return this.getFunctionName()+"("+this.selectable.getSQL()+this.appendSQL()+")";
  }

  /**
   * Used for functions that need to append to the function.
   */
  protected String appendSQL()
  {
    return "";
  }
  
  /**
   * Returns the qualified name of the attribute.
   */
  public String getDbQualifiedName()
  {
    return this.selectable.getDbQualifiedName();
  }

  /**
   * Returns the SQL required for this selectable in the lefthand side of a subselect clause.
   * @return SQL required for this selectable in the lefthand side of a subselect clause.
   */
  public String getSubSelectSQL()
  {
    return this.getSQL();
  }

  /**
   *
   */
  public String getSQLIgnoreCase()
  {
    return this.getFunctionName()+"("+Database.toUpperFunction(this.selectable.getSQL())+")";
  }

  /**
   * Returns a Map representing tables that should be included in the from clause,
   * where the key is the table alias and the value is the name of the table.
   * @return Map representing tables that should be included in the from clause,
   * where the key is the table alias and the value is the name of the table.
   */
  public Map<String, String> getFromTableMap()
  {
    return this.selectable.getFromTableMap();
  }

  /**
   * Returns a Set of TableJoin objects that represent joins statements
   * that are required for this expression.
   * @return Set of TableJoin objects that represent joins statements
   * that are required for this expression, or null of there are none.
   */
  public Set<Join> getJoinStatements()
  {
    return this.selectable.getJoinStatements();
  }

  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    this.selectable.accept(visitor);
  }


  // Equals
  /**
   * Character = comparison case sensitive. This method accepts any Attribute because
   * databases generally allow for comparisons between different types.
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(Selectable selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }


  /**
   * Character = comparison case sensitive. This method accepts any Attribute because
   * databases generally allow for comparisons between different types.
   * @param selectable
   * @return Condition object
   */
  public Condition EQi(SelectableChar selectable)
  {
    return QueryUtil.EQi(this, selectable);
  }

  /**
   * Not Equals.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(Selectable selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Character != comparison case insensitive.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NEi(SelectableChar selectable)
  {
    return QueryUtil.NEi(this, selectable);
  }

  /**
   * Moment Less Than.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition LT(Selectable selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  /**
   * Moment Less Than or Equal.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition LE(Selectable selectable)
  {
    return QueryUtil.LE(this, selectable);
  }

  /**
   * Greater Than.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition GT(Selectable selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * Greater Than or Equals.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition GE(Selectable selectable)
  {
    return QueryUtil.GE(this, selectable);
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


  public Function clone() throws CloneNotSupportedException
  {
    return (Function)super.clone();
  }
}
