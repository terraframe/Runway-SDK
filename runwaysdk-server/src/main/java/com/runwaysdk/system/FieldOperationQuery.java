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
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = 805967152)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to FieldOperation.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class FieldOperationQuery extends com.runwaysdk.system.EnumerationMasterQuery

{

  public FieldOperationQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public FieldOperationQuery(com.runwaysdk.query.ValueQuery valueQuery)
  {
    super(valueQuery);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = new com.runwaysdk.business.BusinessQuery(valueQuery, this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public String getClassType()
  {
    return com.runwaysdk.system.FieldOperation.CLASS;
  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends FieldOperation> getIterator()
  {
    this.checkNotUsedInValueQuery();
    String sqlStmt;
    if (_limit != null && _skip != null)
    {
      sqlStmt = this.getComponentQuery().getSQL(_limit, _skip);
    }
    else
    {
      sqlStmt = this.getComponentQuery().getSQL();
    }
    java.util.Map<String, com.runwaysdk.query.ColumnInfo> columnInfoMap = this.getComponentQuery().getColumnInfoMap();

    java.sql.ResultSet results = com.runwaysdk.dataaccess.database.Database.query(sqlStmt);
    return new com.runwaysdk.business.BusinessIterator<FieldOperation>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_AllOperation()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.AllOperation.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_AllOperation()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.AllOperation.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_CharacterOperation()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.CharacterOperation.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_CharacterOperation()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.CharacterOperation.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface FieldOperationQueryReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReferenceIF
  {


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.FieldOperation fieldOperation);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.FieldOperation fieldOperation);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class FieldOperationQueryReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReference
 implements FieldOperationQueryReferenceIF

  {

  public FieldOperationQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.FieldOperation fieldOperation)
    {
      if(fieldOperation == null) return this.EQ((java.lang.String)null);
      return this.EQ(fieldOperation.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.FieldOperation fieldOperation)
    {
      if(fieldOperation == null) return this.NE((java.lang.String)null);
      return this.NE(fieldOperation.getOid());
    }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface FieldOperationQueryEnumerationIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumerationIF
  {


  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class FieldOperationQueryEnumeration extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumeration
 implements FieldOperationQueryEnumerationIF
  {

  public FieldOperationQueryEnumeration(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

  }

/**
 * Specifies type safe query methods for the enumeration com.runwaysdk.system.AllOperation.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface AllOperationQueryIF extends FieldOperationQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.AllOperation ... allOperation);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.AllOperation ... allOperation);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.AllOperation ... allOperation);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.AllOperation ... allOperation);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.AllOperation ... allOperation);
  }

/**
 * Specifies type safe query methods for the enumeration com.runwaysdk.system.CharacterOperation.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface CharacterOperationQueryIF extends FieldOperationQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.CharacterOperation ... characterOperation);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.CharacterOperation ... characterOperation);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.CharacterOperation ... characterOperation);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.CharacterOperation ... characterOperation);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.CharacterOperation ... characterOperation);
  }

/**
 * Implements type safe query methods for the enumeration com.runwaysdk.system.AllOperation.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class AllOperationQuery extends FieldOperationQueryEnumeration implements  AllOperationQueryIF
  {
  public AllOperationQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.AllOperation ... allOperation)  {

      String[] enumIdArray = new String[allOperation.length]; 

      for (int i=0; i<allOperation.length; i++)
      {
        enumIdArray[i] = allOperation[i].getOid();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.AllOperation ... allOperation)  {

      String[] enumIdArray = new String[allOperation.length]; 

      for (int i=0; i<allOperation.length; i++)
      {
        enumIdArray[i] = allOperation[i].getOid();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.AllOperation ... allOperation)  {

      String[] enumIdArray = new String[allOperation.length]; 

      for (int i=0; i<allOperation.length; i++)
      {
        enumIdArray[i] = allOperation[i].getOid();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.AllOperation ... allOperation)  {

      String[] enumIdArray = new String[allOperation.length]; 

      for (int i=0; i<allOperation.length; i++)
      {
        enumIdArray[i] = allOperation[i].getOid();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.AllOperation ... allOperation)  {

      String[] enumIdArray = new String[allOperation.length]; 

      for (int i=0; i<allOperation.length; i++)
      {
        enumIdArray[i] = allOperation[i].getOid();
      }

      return this.containsExactly(enumIdArray);
  }
  }
/**
 * Implements type safe query methods for the enumeration com.runwaysdk.system.CharacterOperation.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class CharacterOperationQuery extends FieldOperationQueryEnumeration implements  CharacterOperationQueryIF
  {
  public CharacterOperationQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.CharacterOperation ... characterOperation)  {

      String[] enumIdArray = new String[characterOperation.length]; 

      for (int i=0; i<characterOperation.length; i++)
      {
        enumIdArray[i] = characterOperation[i].getOid();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.CharacterOperation ... characterOperation)  {

      String[] enumIdArray = new String[characterOperation.length]; 

      for (int i=0; i<characterOperation.length; i++)
      {
        enumIdArray[i] = characterOperation[i].getOid();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.CharacterOperation ... characterOperation)  {

      String[] enumIdArray = new String[characterOperation.length]; 

      for (int i=0; i<characterOperation.length; i++)
      {
        enumIdArray[i] = characterOperation[i].getOid();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.CharacterOperation ... characterOperation)  {

      String[] enumIdArray = new String[characterOperation.length]; 

      for (int i=0; i<characterOperation.length; i++)
      {
        enumIdArray[i] = characterOperation[i].getOid();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.CharacterOperation ... characterOperation)  {

      String[] enumIdArray = new String[characterOperation.length]; 

      for (int i=0; i<characterOperation.length; i++)
      {
        enumIdArray[i] = characterOperation[i].getOid();
      }

      return this.containsExactly(enumIdArray);
  }
  }
/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface FieldOperationQueryMultiReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReferenceIF
  {


    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.FieldOperation ... fieldOperation);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.FieldOperation ... fieldOperation);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.FieldOperation ... fieldOperation);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.FieldOperation ... fieldOperation);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.FieldOperation ... fieldOperation);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class FieldOperationQueryMultiReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReference
 implements FieldOperationQueryMultiReferenceIF

  {

  public FieldOperationQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.FieldOperation ... fieldOperation)  {

      String[] itemIdArray = new String[fieldOperation.length]; 

      for (int i=0; i<fieldOperation.length; i++)
      {
        itemIdArray[i] = fieldOperation[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.FieldOperation ... fieldOperation)  {

      String[] itemIdArray = new String[fieldOperation.length]; 

      for (int i=0; i<fieldOperation.length; i++)
      {
        itemIdArray[i] = fieldOperation[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.FieldOperation ... fieldOperation)  {

      String[] itemIdArray = new String[fieldOperation.length]; 

      for (int i=0; i<fieldOperation.length; i++)
      {
        itemIdArray[i] = fieldOperation[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.FieldOperation ... fieldOperation)  {

      String[] itemIdArray = new String[fieldOperation.length]; 

      for (int i=0; i<fieldOperation.length; i++)
      {
        itemIdArray[i] = fieldOperation[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.FieldOperation ... fieldOperation)  {

      String[] itemIdArray = new String[fieldOperation.length]; 

      for (int i=0; i<fieldOperation.length; i++)
      {
        itemIdArray[i] = fieldOperation[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  }
}
