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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeBlob_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeBoolean_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeCharacter_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeClob_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeDateTime_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeDate_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeDecimal_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeDouble_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeEnumeration_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeFile_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeFloat_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeHash_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeInteger_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeLocalCharacter_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeLocalText_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeLong_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeReference_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeStruct_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeSymmetric_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeTerm_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeText_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeTime_Q;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;

public abstract class Attribute implements SelectableSingle, Statement
{
  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public MdAttributeConcrete_Q convertMdAttribute(MdAttributeConcreteDAOIF mdAttributeIF);
  }

  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  protected ComponentQuery            rootQuery;

  protected String                    definingTableName;

  protected String                    definingTableAlias;

  protected String                    attributeNamespace;

  protected String                    value;

  protected String                    attributeName;

  protected String                    columnName;

  protected String                    attributeType;

  protected MdAttributeConcreteDAOIF  mdAttributeIF;

  protected MdAttributeConcreteDAOIF  mdAttributeStructIF;

  protected String                    userDefinedAlias;

  protected String                    userDefinedDisplayLabel;

  protected String                    columnAlias;

  // Reference to all MdAttributes that were involved in constructing this attribute;
  protected Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset;

  /**
   * Key: TableAlias and Value: TableName
   */
  protected Map<String, String>       fromTableMap;

  protected Set<Join>                 tableJoinSet;

  protected Attribute(MdAttributeConcreteDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet,
      String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery,
        tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, null);
  }

  protected Attribute(MdAttributeConcreteDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet,
      String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super();
    this.attributeNamespace = rootQuery.getAliasSeed() + attributeNamespace;

    this.definingTableName = definingTableName;
    this.definingTableAlias = definingTableAlias;
    this.rootQuery = rootQuery;
    this.fromTableMap = new HashMap<String, String>();
    this.fromTableMap.put(this.definingTableAlias, this.definingTableName);
    this.tableJoinSet = tableJoinSet;
    this.attributeName = mdAttributeIF.definesAttribute();
    this.columnName = mdAttributeIF.getColumnName();
    this.attributeType = mdAttributeIF.getType();

    if (userDefinedAlias == null || userDefinedAlias.trim().equals(""))
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

    this.mdAttributeIF = convertMdAttribute(mdAttributeIF);

    this.entityMdAttributeIFset = new HashSet<MdAttributeConcreteDAOIF>();

    this.columnAlias = rootQuery.getColumnAlias(this.getAttributeNameSpace(), this.getDbColumnName());

    if (mdAttributeStructIF != null)
    {
      this.mdAttributeStructIF = convertMdAttribute(mdAttributeStructIF);
    }
  }

  /**
   * Used only for queries involving parent_id or child_id on a relationship
   */
  protected void recomputeColumnAlias()
  {
    this.columnAlias = rootQuery.getColumnAlias(this.getAttributeNameSpace(), this.getDbColumnName());
  }

  protected static MdAttributeConcrete_Q convertMdAttribute(MdAttributeConcreteDAOIF _mdAttributeIF)
  {
    MdAttributeConcrete_Q mdAttribute_Q = null;

    if (_mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      mdAttribute_Q = new MdAttributeBoolean_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeCharacterDAOIF)
    {
      mdAttribute_Q = new MdAttributeCharacter_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeDateDAOIF)
    {
      mdAttribute_Q = new MdAttributeDate_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
    {
      mdAttribute_Q = new MdAttributeDateTime_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeTimeDAOIF)
    {
      mdAttribute_Q = new MdAttributeTime_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeDecimalDAOIF)
    {
      mdAttribute_Q = new MdAttributeDecimal_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeDoubleDAOIF)
    {
      mdAttribute_Q = new MdAttributeDouble_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeFloatDAOIF)
    {
      mdAttribute_Q = new MdAttributeFloat_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      mdAttribute_Q = new MdAttributeInteger_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeLongDAOIF)
    {
      mdAttribute_Q = new MdAttributeLong_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeTextDAOIF)
    {
      mdAttribute_Q = new MdAttributeText_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeClobDAOIF)
    {
      mdAttribute_Q = new MdAttributeClob_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeBlobDAOIF)
    {
      mdAttribute_Q = new MdAttributeBlob_Q(_mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeHashDAOIF)
    {
      mdAttribute_Q = new MdAttributeHash_Q((MdAttributeHashDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeSymmetricDAOIF)
    {
      mdAttribute_Q = new MdAttributeSymmetric_Q((MdAttributeSymmetricDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      mdAttribute_Q = new MdAttributeEnumeration_Q((MdAttributeEnumerationDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeTermDAOIF)
    {
      mdAttribute_Q = new MdAttributeTerm_Q((MdAttributeTermDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeReferenceDAOIF)
    {
      mdAttribute_Q = new MdAttributeReference_Q((MdAttributeReferenceDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeFileDAOIF)
    {
      mdAttribute_Q = new MdAttributeFile_Q((MdAttributeFileDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeLocalCharacterDAOIF)
    {
      mdAttribute_Q = new MdAttributeLocalCharacter_Q((MdAttributeLocalCharacterDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeLocalTextDAOIF)
    {
      mdAttribute_Q = new MdAttributeLocalText_Q((MdAttributeLocalTextDAOIF) _mdAttributeIF);
    }
    else if (_mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      mdAttribute_Q = new MdAttributeStruct_Q((MdAttributeStructDAOIF) _mdAttributeIF);
    }

    if (mdAttribute_Q == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        mdAttribute_Q = plugin.convertMdAttribute(_mdAttributeIF);

        if (mdAttribute_Q != null)
        {
          break;
        }
      }
    }

    if (mdAttribute_Q != null)
    {
      return mdAttribute_Q;
    }
    else
    {
      String errMsg = "Mapping not defined between ["+_mdAttributeIF.getClass().getName()+"] and ["+MdAttributeConcrete_Q.class.getName()+"]";
      throw new ProgrammingErrorException(errMsg);
    }
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
   *
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

  /**
   * Returns the alias used in the select clause for the database column for
   * this attribute.
   *
   * @return alias used in the select clause for the database column for this
   *         attribute.
   */
  public String getColumnAlias()
  {
    return this.columnAlias;
  }

  /**
   * Sets the column alias.
   *
   * @param alias
   */
  public void setColumnAlias(String alias)
  {
    this.columnAlias = alias;
  }

  /**
   * Sets the name of this attribute.
   *
   * @param attributeName
   */
  protected void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  /**
   * Sets the column name.
   *
   * @param columnName
   */
  protected void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  /**
   * Returns the EntityQuery from which this attribute was created.
   *
   * @return EntityQuery from which this attribute was created.
   */
  public ComponentQuery getRootQuery()
  {
    return this.rootQuery;
  }

  /**
   * Returns the MdAttributeIF that defines the attribute.
   *
   * @return MdAttributeIF that defines the attribute.
   */
  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    return this.mdAttributeIF;
  }

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   *
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeList = new HashSet<MdAttributeConcreteDAOIF>();
    mdAttributeList.add(this.mdAttributeIF);
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
   * Every Selectable eventually boils down to an attribute.
   *
   * @return bottom most attribute.
   */
  public Attribute getAttribute()
  {
    return this;
  }

  /**
   * Returns the name of this attribute.
   *
   * @return name of this attribute.
   */
  public String _getAttributeName()
  {
    return this.attributeName;
  }

  /**
   * Returns the database column name representing this attribute.
   *
   * @return database column name representing this attribute.
   */
  public String getDbColumnName()
  {
    return this.columnName;
  }

  /**
   * Returns the namespace of the attribute.
   *
   * @return namespace of the attribute.
   */
  public String getAttributeNameSpace()
  {
    return this.attributeNamespace;
  }

  /**
   * Returns the alias used in the select clause.
   *
   * @return alias used in the select clause.
   */
  public String getFullyQualifiedNameSpace()
  {
    return this.getAttributeNameSpace() + "." + this._getAttributeName();
  }

  /**
   * Returns the name of the database table that defines the column for this
   * attribute.
   *
   * @return name of the database table that defines the column for this
   *         attribute.
   */
  public String getDefiningTableName()
  {
    return this.definingTableName;
  }

  /**
   * Returns the name of the alias used for the database table that defines the
   * column for this attribute.
   *
   * @return name of the alias used for the database table that defines the
   *         column for this attribute.
   */
  public String getDefiningTableAlias()
  {
    return this.definingTableAlias;
  }

  /**
   * Returns the qualified name of the attribute.
   */
  public String getDbQualifiedName()
  {
    return this.getDefiningTableAlias() + "." + this.getDbColumnName();
  }

  /**
   * Returns the SQL representation of this condition.
   *
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    return Database.formatColumnForCompare(this.getDbQualifiedName(), attributeType);
  }

  /**
   * Returns the SQL required for this selectable in the left-hand side of a
   * subselect clause.
   *
   * @return SQL required for this selectable in the left-hand side of a
   *         subselect clause.
   */
  public String getSubSelectSQL()
  {
    return this.getDbQualifiedName();
  }

  /**
   * Character = comparison case sensitive.
   *
   * @param statement
   * @return Basic Condition object
   */
  public abstract BasicCondition EQ(String statement);

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
   * Represents a left join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable selectable)
  {
    return new LeftJoinEq(this, selectable);
  }

  /**
   * Represents a left join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinEq LEFT_JOIN_EQ(Selectable... selectableArray)
  {
    return new LeftJoinEq(this, selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinEq LEFT_JOIN_EQ(EntityQuery entityQuery)
  {
    return new LeftJoinEq(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param componentQuery
   */
  public LeftJoinEq LEFT_JOIN_EQ(GeneratedEntityQuery componentQuery)
  {
    return new LeftJoinEq(this, componentQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinNotEq LEFT_JOIN_NE(Selectable selectable)
  {
    return new LeftJoinNotEq(this, selectable);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinNotEq LEFT_JOIN_NE(Selectable... selectableArray)
  {
    return new LeftJoinNotEq(this, selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinNotEq LEFT_JOIN_NE(EntityQuery entityQuery)
  {
    return new LeftJoinNotEq(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinNotEq LEFT_JOIN_NE(GeneratedEntityQuery entityQuery)
  {
    return new LeftJoinNotEq(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinGt LEFT_JOIN_GT(Selectable selectable)
  {
    return new LeftJoinGt(this, selectable);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinGt LEFT_JOIN_GT(Selectable... selectableArray)
  {
    return new LeftJoinGt(this, selectableArray);
  }


  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinGt LEFT_JOIN_GT(EntityQuery entityQuery)
  {
    return new LeftJoinGt(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param componentQuery
   */
  public LeftJoinGt LEFT_JOIN_GT(GeneratedEntityQuery componentQuery)
  {
    return new LeftJoinGt(this, componentQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinGtEq LEFT_JOIN_GE(Selectable selectable)
  {
    return new LeftJoinGtEq(this, selectable);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinGtEq LEFT_JOIN_GE(Selectable... selectableArray)
  {
    return new LeftJoinGtEq(this, selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinGtEq LEFT_JOIN_GE(GeneratedEntityQuery entityQuery)
  {
    return new LeftJoinGtEq(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinGtEq LEFT_JOIN_GE(EntityQuery entityQuery)
  {
    return new LeftJoinGtEq(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinLt LEFT_JOIN_LT(Selectable selectable)
  {
    return new LeftJoinLt(this, selectable);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinLt LEFT_JOIN_LT(Selectable... selectableArray)
  {
    return new LeftJoinLt(this, selectableArray);
  }


  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinLt LEFT_JOIN_LT(EntityQuery entityQuery)
  {
    return new LeftJoinLt(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinLt LEFT_JOIN_LT(GeneratedEntityQuery entityQuery)
  {
    return new LeftJoinLt(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable
   */
  public LeftJoinLtEq LEFT_JOIN_LE(Selectable selectable)
  {
    return new LeftJoinLtEq(this, selectable);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectableArray
   */
  public LeftJoinLtEq LEFT_JOIN_LE(Selectable... selectableArray)
  {
    return new LeftJoinLtEq(this, selectableArray);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinLtEq LEFT_JOIN_LE(EntityQuery entityQuery)
  {
    return new LeftJoinLtEq(this, entityQuery.id());
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   */
  public LeftJoinLtEq LEFT_JOIN_LE(GeneratedEntityQuery entityQuery)
  {
    return new LeftJoinLtEq(this, entityQuery.id());
  }



  /**
   * Returns a Set of TableJoin objects that represent joins statements that are
   * required for this expression.
   *
   * @return Set of TableJoin objects that represent joins statements that are
   *         required for this expression, or null of there are none.
   */
  public Set<Join> getJoinStatements()
  {
    return this.tableJoinSet;
  }

  /**
   * Returns a Map representing tables that should be included in the from
   * clause, where the key is the table alias and the value is the name of the
   * table.
   *
   * @return Map representing tables that should be included in the from clause,
   *         where the key is the table alias and the value is the name of the
   *         table.
   */
  public Map<String, String> getFromTableMap()
  {
    return this.fromTableMap;
  }

  /**
   * Visitor to traverse the query object structure.
   *
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    // Don't visit this attribute if it is from another value query.
    // Attributes from value queries come from a nested SELECT within the FROM
    // clause.
    // Visiting this attribute will copy joins in the nested SELECT and put them
    // in the
    // outermost SELECT.
    if (this.rootQuery instanceof EntityQuery)
    {
      boolean shouldVisit = true;

      ComponentQuery queryVisited = visitor.getComponentQuery();

      if (queryVisited instanceof ValueQuery)
      {
        ValueQuery valueQuery = (ValueQuery)queryVisited;

        // do not visit the attribute if it is defined by an entity that is used on the right
        // hand side of a left join
        if (valueQuery.isEntityInLeftJoin((EntityQuery)this.rootQuery))
        {
          shouldVisit = false;
        }
      }

      if (shouldVisit)
      {
        visitor.visit(this);
        this.rootQuery.accept(visitor);
      }
    }
  }

  /**
   *
   */
  public Attribute clone() throws CloneNotSupportedException
  {
    return (Attribute) super.clone();
  }

  public MdAttributeConcreteDAOIF getMdAttributeStructIF()
  {
    return mdAttributeStructIF;
  }
}
