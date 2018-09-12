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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
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
    if ( !(this.getMdEntityIF() instanceof MdRelationshipDAOIF))
    {
      String error = "RelationshipQuery can only query for relationships, not ["
          + this.getMdEntityIF().definesType() + "]s.";
      throw new QueryException(error);
    }

    this.parentAttributeQualifiedName = this.getMdEntityIF().definesType()+"."+RelationshipInfo.PARENT_OID;
    this.childAttributeQualifiedName = this.getMdEntityIF().definesType()+"."+RelationshipInfo.CHILD_OID;
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
    return (MdRelationshipDAOIF)this.getMdEntityIF();
  }

  /**
   * Initializes the columnInfoMap to include all attributes required to build a select clause that includes all attributes
   * of the component.
   */
  protected void getColumnInfoForSelectClause(MdTableClassIF _mdTableClassIF, Map<String, ColumnInfo> columnInfoMap2, List<MdAttributeConcreteDAOIF> totalMdAttributeIFList)
  {
    super.getColumnInfoForSelectClause(_mdTableClassIF, columnInfoMap2, totalMdAttributeIFList);

    String tableName = _mdTableClassIF.getTableName();
    String definingTableAlias = this.getTableAlias("", tableName);

    ColumnInfo columnInfo = new ColumnInfo(tableName, definingTableAlias,
        RelationshipDAOIF.PARENT_OID_COLUMN, this.getColumnAlias("", RelationshipDAOIF.PARENT_OID_COLUMN));
    columnInfoMap2.put(this.parentAttributeQualifiedName, columnInfo);

    columnInfo = new ColumnInfo(tableName, definingTableAlias,
        RelationshipDAOIF.CHILD_OID_COLUMN, this.getColumnAlias("", RelationshipDAOIF.CHILD_OID_COLUMN));
    columnInfoMap2.put(this.childAttributeQualifiedName, columnInfo);
  }

  /**
   * Build the select clause for this query, including all attributes required
   * to instantiate instances of this object.
   * @param mdAttributeIDMap Key: MdAttribute.getOid() Value: MdAttributeIF
   * @return select clause for this query.
   */
  protected StringBuffer buildSelectClause(MdTableClassIF _mdTableClassIF,  Set<Join> tableJoinSet2,
      Map<String, String> fromTableMap, Map<String, ColumnInfo> columnInfoMap2,
      List<MdAttributeConcreteDAOIF> totalMdAttributeIFList, Map<String, ? extends MdAttributeConcreteDAOIF> mdAttributeIDMap)
  {
    StringBuffer selectClause =  super.buildSelectClause(_mdTableClassIF,  tableJoinSet2, fromTableMap, columnInfoMap2,
        totalMdAttributeIFList, mdAttributeIDMap);

    ColumnInfo columnInfo = columnInfoMap2.get(this.parentAttributeQualifiedName);
    selectClause.append(",\n"+selectIndent+columnInfo.getSelectClauseString());

    columnInfo = columnInfoMap2.get(this.childAttributeQualifiedName);
    selectClause.append(",\n"+selectIndent+columnInfo.getSelectClauseString());

    return selectClause;
  }


  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @return Attribute character statement object.
   */
  public AttributeUUID parentOid()
  {
    return this.parentOid(null, null);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeUUID parentOid(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String definingTableName = this.getMdEntityIF().getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    // The parent OID field is defined by the
    Set<Join> characterTableJoinSet = new HashSet<Join>();

    // Major Hack here.  The query API requires that all Attributes have an MdAttribute.  PARENT_OID has no medadata that defines it.
    // So, I just gave it the one for the OID field, and then hardcoded the name of the attribute to PARENT_OID.

    MdAttributeUUIDDAOIF mdAttributeIF = (MdAttributeUUIDDAOIF)this.getMdEntityIF().getRootMdClassDAO().definesAttribute(EntityInfo.OID);

    AttributeUUID attributeCharacter =
      new AttributeUUID(mdAttributeIF, this.getMdEntityIF().definesType(), definingTableName, definingTableAlias, this, characterTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    attributeCharacter.setAttributeName(RelationshipInfo.PARENT_OID);
    attributeCharacter.setColumnName(RelationshipDAOIF.PARENT_OID_COLUMN);
    attributeCharacter.recomputeColumnAlias();

    return attributeCharacter;
  } 
  
  
  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @return Attribute character statement object.
   */
  public AttributeUUID childOid()
  {
    return this.childOid(null, null);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeUUID childOid(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String definingTableName = this.getMdEntityIF().getTableName();
    String definingTableAlias = this.getTableAlias("", definingTableName);

    // The parent OID field is defined by the
    Set<Join> characterTableJoinSet = new HashSet<Join>();

    // Major Hack here.  The query API requires that all Attributes have an MdAttribute.  CHILD_OID has no metadata that defines it.
    // So, I just gave it the one for the OID field, and then hardcoded the name of the attribute to CHILD_OID.

    MdAttributeUUIDDAOIF mdAttributeIF = (MdAttributeUUIDDAOIF)this.getMdEntityIF().getRootMdClassDAO().definesAttribute(EntityInfo.OID);

    AttributeUUID attributeCharacter =
      new AttributeUUID(mdAttributeIF, this.getMdEntityIF().definesType(), definingTableName, definingTableAlias, this, characterTableJoinSet, userDefinedAlias, userDefinedDisplayLabel);

    attributeCharacter.setAttributeName(RelationshipInfo.CHILD_OID);
    attributeCharacter.setColumnName(RelationshipDAOIF.CHILD_OID_COLUMN);
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
      return new ValueJoinConditionEq(this.childOid(), abstractObjectQuery.oid());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.childOid(), abstractObjectQuery.oid());
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
      return new ValueJoinConditionEq(this.childOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.childOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
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
      return new ValueJoinConditionNotEq(this.childOid(), abstractObjectQuery.oid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.childOid(), abstractObjectQuery.oid());
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
      return new ValueJoinConditionNotEq(this.childOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.childOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
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
      return new ValueJoinConditionEq(this.parentOid(), abstractObjectQuery.oid());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.parentOid(), abstractObjectQuery.oid());
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
      return new ValueJoinConditionEq(this.parentOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
    }
    else
    {
      return new SubSelectBasicConditionEq(this.parentOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
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
      return new ValueJoinConditionNotEq(this.parentOid(), abstractObjectQuery.oid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.parentOid(), abstractObjectQuery.oid());
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
      return new ValueJoinConditionNotEq(this.parentOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
    }
    else
    {
      return new SubSelectBasicConditionNotEq(this.parentOid(), (AttributeCharacter)generatedBusinessQuery.getOid());
    }
  }
}
