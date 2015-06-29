/**
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
 */
package com.runwaysdk.query;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;


public abstract class AbstractRelationshipQuery extends EntityQuery
{
  public String parentAttributeQualifiedName;
  public String childAttributeQualifiedName;

  protected AbstractRelationshipQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
    this.init();
  }

  /**
   *
   * @param valueQuery
   * @param type
   */
  protected AbstractRelationshipQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
    this.init();
  }

  private void init()
  {
    if ( !(this.mdEntityIF instanceof MdRelationshipDAOIF))
    {
      String error = "RelationshipQuery can only query for relationships, not ["
          + mdEntityIF.definesType() + "]s.";
      throw new QueryException(error);
    }

    this.parentAttributeQualifiedName = this.mdEntityIF.definesType()+"."+RelationshipInfo.PARENT_ID;
    this.childAttributeQualifiedName = this.mdEntityIF.definesType()+"."+RelationshipInfo.CHILD_ID;
  }

  /**
   * Returns the MdRelationship that defines the type of relationships that
   * are queried from this object.
   *
   * @return MdRelationship that defines the type of relationships that
   * are queried from this object.
   */
  protected MdRelationshipDAOIF getMdRelationshipIF()
  {
    return (MdRelationshipDAOIF)this.mdEntityIF;
  }

  /**
   * Initializes the columnInfoMap to include all attributes required to build a select clause that includes all attributes
   * of the component.
   */
  protected void getColumnInfoForSelectClause(MdEntityDAOIF _mdEntityIF, Map<String, ColumnInfo> columnInfoMap2, List<MdAttributeConcreteDAOIF> totalMdAttributeIFList)
  {
    super.getColumnInfoForSelectClause(_mdEntityIF, columnInfoMap2, totalMdAttributeIFList);

    String tableName = _mdEntityIF.getTableName();
    String definingTableAlias = this.getTableAlias("", tableName);

    ColumnInfo columnInfo = new ColumnInfo(tableName, definingTableAlias,
        RelationshipDAOIF.PARENT_ID_COLUMN, this.getColumnAlias("", RelationshipDAOIF.PARENT_ID_COLUMN));
    columnInfoMap2.put(this.parentAttributeQualifiedName, columnInfo);

    columnInfo = new ColumnInfo(tableName, definingTableAlias,
        RelationshipDAOIF.CHILD_ID_COLUMN, this.getColumnAlias("", RelationshipDAOIF.CHILD_ID_COLUMN));
    columnInfoMap2.put(this.childAttributeQualifiedName, columnInfo);
  }

  /**
   * Build the select clause for this query, including all attributes required
   * to instantiate instances of this object.
   * @param mdAttributeIDMap Key: MdAttribute.getId() Value: MdAttributeIF
   * @return select clause for this query.
   */
  protected StringBuffer buildSelectClause(MdEntityDAOIF _mdEntityIF,  Set<Join> tableJoinSet2,
      Map<String, String> fromTableMap, Map<String, ColumnInfo> columnInfoMap2,
      List<MdAttributeConcreteDAOIF> totalMdAttributeIFList, Map<String, ? extends MdAttributeConcreteDAOIF> mdAttributeIDMap)
  {
    StringBuffer selectClause =  super.buildSelectClause(_mdEntityIF,  tableJoinSet2, fromTableMap, columnInfoMap2,
        totalMdAttributeIFList, mdAttributeIDMap);

    ColumnInfo columnInfo = columnInfoMap2.get(this.parentAttributeQualifiedName);
    selectClause.append(",\n"+selectIndent+columnInfo.getSelectClauseString());

    columnInfo = columnInfoMap2.get(this.childAttributeQualifiedName);
    selectClause.append(",\n"+selectIndent+columnInfo.getSelectClauseString());

    return selectClause;
  }


  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_ID.
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentId()
  {
    return this.parentId(null, null);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_ID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentId(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String definingTableName = this.getMdEntityIF().getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    // The parent ID field is defined by the
    Set<Join> characterTableJoinSet = new HashSet<Join>();

    // Major Hack here.  The query API requires that all Attributes have an MdAttribute.  PARENT_ID has no medadata that defines it.
    // So, I just gave it the one for the ID field, and then hardcoded the name of the attribute to PARENT_ID.

    MdAttributeCharacterDAOIF mdAttributeIF = (MdAttributeCharacterDAOIF)this.getMdEntityIF().getRootMdClassDAO().definesAttribute(EntityInfo.ID);

    AttributeCharacter attributeCharacter =
      new AttributeCharacter(mdAttributeIF, this.getMdEntityIF().definesType(), definingTableName, definingTableAlias, this, characterTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    attributeCharacter.setAttributeName(RelationshipInfo.PARENT_ID);
    attributeCharacter.setColumnName(RelationshipDAOIF.PARENT_ID_COLUMN);
    attributeCharacter.recomputeColumnAlias();

    return attributeCharacter;
  } 
  
  
  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_ID.
   * @return Attribute character statement object.
   */
  public AttributeCharacter childId()
  {
    return this.childId(null, null);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_ID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter childId(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String definingTableName = this.getMdEntityIF().getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    // The parent ID field is defined by the
    Set<Join> characterTableJoinSet = new HashSet<Join>();

    // Major Hack here.  The query API requires that all Attributes have an MdAttribute.  CHILD_ID has no metadata that defines it.
    // So, I just gave it the one for the ID field, and then hardcoded the name of the attribute to CHILD_ID.

    MdAttributeCharacterDAOIF mdAttributeIF = (MdAttributeCharacterDAOIF)this.getMdEntityIF().getRootMdClassDAO().definesAttribute(EntityInfo.ID);

    AttributeCharacter attributeCharacter =
      new AttributeCharacter(mdAttributeIF, this.getMdEntityIF().definesType(), definingTableName, definingTableAlias, this, characterTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

    attributeCharacter.setAttributeName(RelationshipInfo.CHILD_ID);
    attributeCharacter.setColumnName(RelationshipDAOIF.CHILD_ID_COLUMN);
    attributeCharacter.recomputeColumnAlias();

    return attributeCharacter;

  }
  
  /**
   * Restricts the query to include objects that are children in this relationship.
   * AbstractObjectQuery should be of a type that is defined as the child type in this relationship.
   * @param abstractObjectQuery
   * @return Condition restricting objects that are children in this relationship.
   */
  public Condition hasChild(AbstractObjectQuery abstractObjectQuery)
  {
    if (this.isUsedInValueQuery() || abstractObjectQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(this.childId(), abstractObjectQuery.id());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.childId(), abstractObjectQuery.id());
    }
  }

  /**
   * Restricts the query to include objects that are children in this relationship.
   * GeneratedBusinessQuery should be of a type that is defined as the child type in this relationship.
   * @param generatedBusinessQuery
   * @return Condition restricting objects that are children in this relationship.
   */
  public Condition hasChild(GeneratedBusinessQuery generatedBusinessQuery)
  {
    if (this.isUsedInValueQuery() || generatedBusinessQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(this.childId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.childId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
  }

  /**
   * Restricts the query to include objects that are not children in this relationship.
   * AbstractObjectQuery should be of a type that is defined as the child type in this relationship.
   * @param abstractObjectQuery
   * @return Condition restricting objects that are not children in this relationship.
   */
  public Condition doesNotHaveChild(AbstractObjectQuery abstractObjectQuery)
  {
    if (this.isUsedInValueQuery() || abstractObjectQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(this.childId(), abstractObjectQuery.id());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.childId(), abstractObjectQuery.id());
    }
  }

  /**
   * Restricts the query to include objects that are not children in this relationship.
   * GeneratedBusinessQuery should be of a type that is defined as the child type in this relationship.
   * @param generatedBusinessQuery
   * @return Condition restricting objects that are not children in this relationship.
   */
  public Condition doesNotHaveChild(GeneratedBusinessQuery generatedBusinessQuery)
  {
    if (this.isUsedInValueQuery() || generatedBusinessQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(this.childId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.childId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
  }

  /**
   * Restricts the query to include objects that are parents in this relationship.
   * AbstractObjectQuery should be of a type that is defined as the parent type in this relationship.
   * @param abstractObjectQuery
   * @return Condition restricting objects that are parents in this relationship.
   */
  public Condition hasParent(AbstractObjectQuery abstractObjectQuery)
  {
    if (this.isUsedInValueQuery() || abstractObjectQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(this.parentId(), abstractObjectQuery.id());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.parentId(), abstractObjectQuery.id());
    }
  }

  /**
   * Restricts the query to include objects that are parents in this relationship.
   * GeneratedBusinessQuery should be of a type that is defined as the parent type in this relationship.
   * @param generatedBusinessQuery
   * @return Condition restricting objects that are parents in this relationship.
   */
  public Condition hasParent(GeneratedBusinessQuery generatedBusinessQuery)
  {
    if (this.isUsedInValueQuery() || generatedBusinessQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionEq(this.parentId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.parentId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
  }

  /**
   * Restricts the query to include objects that are not parents in this relationship.
   * AbstractObjectQuery should be of a type that is defined as the parent type in this relationship.
   * @param abstractObjectQuery
   * @return Condition restricting objects that are not parents in this relationship.
   */
  public Condition doesNotHaveParent(AbstractObjectQuery abstractObjectQuery)
  {
    if (this.isUsedInValueQuery() || abstractObjectQuery.isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(this.parentId(), abstractObjectQuery.id());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.parentId(), abstractObjectQuery.id());
    }
  }

  /**
   * Restricts the query to include objects that are not parents in this relationship.
   * GeneratedBusinessQuery should be of a type that is defined as the parent type in this relationship.
   * @param generatedBusinessQuery
   * @return Condition restricting objects that are not parents in this relationship.
   */
  public Condition doesNotHaveParent(GeneratedBusinessQuery generatedBusinessQuery)
  {
    if (this.isUsedInValueQuery() || generatedBusinessQuery.getComponentQuery().isUsedInValueQuery())
    {
      return new ValueJoinConditionNotEq(this.parentId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.parentId(), (AttributeCharacter)generatedBusinessQuery.getId());
    }
  }
}
