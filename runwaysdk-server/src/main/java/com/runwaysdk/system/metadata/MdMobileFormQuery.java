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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -204023716)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdMobileForm.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class MdMobileFormQuery extends com.runwaysdk.system.metadata.MdFormQuery

{

  public MdMobileFormQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public MdMobileFormQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.MdMobileForm.CLASS;
  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends MdMobileForm> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<MdMobileForm>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


  public com.runwaysdk.query.Condition mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.getBusinessQuery().isParentIn(relationshipQuery);
  }


  public com.runwaysdk.query.Condition SUBSELECT_mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.getBusinessQuery().isParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.getBusinessQuery().isParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.getBusinessQuery().isParentIn_SUBSELECT(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.getBusinessQuery().isParentIn(relationshipQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.getBusinessQuery().isParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.getBusinessQuery().isParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.getBusinessQuery().isParentIn_SUBSELECT(mobileFormFieldQuery);
  }


  public com.runwaysdk.query.Condition NOT_IN_mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.getBusinessQuery().isNotParentIn(relationshipQuery);
  }


  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.getBusinessQuery().isNotParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.getBusinessQuery().isNotParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.getBusinessQuery().isNotParentIn_SUBSELECT(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.getBusinessQuery().isNotParentIn(relationshipQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.getBusinessQuery().isNotParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.getBusinessQuery().isNotParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.getBusinessQuery().isNotParentIn_SUBSELECT(mobileFormFieldQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MdMobileFormQueryReferenceIF extends com.runwaysdk.system.metadata.MdFormQuery.MdFormQueryReferenceIF
  {


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdMobileForm mdMobileForm);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdMobileForm mdMobileForm);


  public com.runwaysdk.query.Condition mdFields();

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery);

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery);


  public com.runwaysdk.query.Condition SUBSELECT_mdFields();

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery);

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery);


  public com.runwaysdk.query.Condition NOT_IN_mdFields();

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery);

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery);


  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields();

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery);

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MdMobileFormQueryReference extends com.runwaysdk.system.metadata.MdFormQuery.MdFormQueryReference
 implements MdMobileFormQueryReferenceIF

  {

  public MdMobileFormQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdMobileForm mdMobileForm)
    {
      if(mdMobileForm == null) return this.EQ((java.lang.String)null);
      return this.EQ(mdMobileForm.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdMobileForm mdMobileForm)
    {
      if(mdMobileForm == null) return this.NE((java.lang.String)null);
      return this.NE(mdMobileForm.getOid());
    }


  public com.runwaysdk.query.Condition mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.isParentIn(relationshipQuery);
  }


  public com.runwaysdk.query.Condition SUBSELECT_mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.isParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.isParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.isParentIn_SUBSELECT(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.isParentIn(relationshipQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.isParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.isParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.isParentIn_SUBSELECT(mobileFormFieldQuery);
  }


  public com.runwaysdk.query.Condition NOT_IN_mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.isNotParentIn(relationshipQuery);
  }


  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);

    return this.isNotParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.isNotParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    return this.isNotParentIn_SUBSELECT(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.isNotParentIn(relationshipQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery)
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.MobileFormField.CLASS);
    relationshipQuery.AND(relationshipQuery.hasChild(mdMobileFieldQuery));

    return this.isNotParentIn_SUBSELECT(relationshipQuery);
  }

  public com.runwaysdk.query.Condition NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.isNotParentIn(mobileFormFieldQuery);
  }

  public com.runwaysdk.query.Condition SUBSELECT_NOT_IN_mdFields(com.runwaysdk.system.metadata.MdMobileFieldQuery mdMobileFieldQuery, com.runwaysdk.system.metadata.MobileFormFieldQuery mobileFormFieldQuery)
  {
    mobileFormFieldQuery.AND(mobileFormFieldQuery.hasChild(mdMobileFieldQuery));
    return this.isNotParentIn_SUBSELECT(mobileFormFieldQuery);
  }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MdMobileFormQueryMultiReferenceIF extends com.runwaysdk.system.metadata.MdFormQuery.MdFormQueryMultiReferenceIF
  {


    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MdMobileFormQueryMultiReference extends com.runwaysdk.system.metadata.MdFormQuery.MdFormQueryMultiReference
 implements MdMobileFormQueryMultiReferenceIF

  {

  public MdMobileFormQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm)  {

      String[] itemIdArray = new String[mdMobileForm.length]; 

      for (int i=0; i<mdMobileForm.length; i++)
      {
        itemIdArray[i] = mdMobileForm[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm)  {

      String[] itemIdArray = new String[mdMobileForm.length]; 

      for (int i=0; i<mdMobileForm.length; i++)
      {
        itemIdArray[i] = mdMobileForm[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm)  {

      String[] itemIdArray = new String[mdMobileForm.length]; 

      for (int i=0; i<mdMobileForm.length; i++)
      {
        itemIdArray[i] = mdMobileForm[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm)  {

      String[] itemIdArray = new String[mdMobileForm.length]; 

      for (int i=0; i<mdMobileForm.length; i++)
      {
        itemIdArray[i] = mdMobileForm[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.MdMobileForm ... mdMobileForm)  {

      String[] itemIdArray = new String[mdMobileForm.length]; 

      for (int i=0; i<mdMobileForm.length; i++)
      {
        itemIdArray[i] = mdMobileForm[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  }
}
