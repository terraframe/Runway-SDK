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

@com.runwaysdk.business.ClassSignature(hash = -959371374)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to Locale.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class LocaleQuery extends com.runwaysdk.system.EnumerationMasterQuery

{

  public LocaleQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public LocaleQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.Locale.CLASS;
  }
  public com.runwaysdk.query.AttributeChar getLocaleLabel()
  {
    return getLocaleLabel(null);

  }
 
  public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias)
  {
    return (com.runwaysdk.query.AttributeChar)this.getComponentQuery().get(com.runwaysdk.system.metadata.Locale.LOCALELABEL, alias, null);

  }
 
  public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.AttributeChar)this.getComponentQuery().get(com.runwaysdk.system.metadata.Locale.LOCALELABEL, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends Locale> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<Locale>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_UserLocales()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.metadata.UserLocales.CLASS); 
    businessQuery.WHERE(businessQuery.id().EQ(mdEnumerationIF.getId()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_UserLocales()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.metadata.UserLocales.CLASS); 
    businessQuery.WHERE(businessQuery.id().EQ(mdEnumerationIF.getId()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface LocaleQueryReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReferenceIF
  {

    public com.runwaysdk.query.AttributeChar getLocaleLabel();
    public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias);
    public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.Locale locale);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.Locale locale);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class LocaleQueryReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReference
 implements LocaleQueryReferenceIF

  {

  public LocaleQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.Locale locale)
    {
      return this.EQ(locale.getId());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.Locale locale)
    {
      return this.NE(locale.getId());
    }

  public com.runwaysdk.query.AttributeChar getLocaleLabel()
  {
    return getLocaleLabel(null);

  }
 
  public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias)
  {
    return (com.runwaysdk.query.AttributeChar)this.get(com.runwaysdk.system.metadata.Locale.LOCALELABEL, alias, null);

  }
 
  public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.AttributeChar)this.get(com.runwaysdk.system.metadata.Locale.LOCALELABEL, alias, displayLabel);

  }
  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface LocaleQueryEnumerationIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumerationIF
  {

    public com.runwaysdk.query.AttributeChar getLocaleLabel();
    public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias);
    public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias, String displayLabel);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class LocaleQueryEnumeration extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumeration
 implements LocaleQueryEnumerationIF
  {

  public LocaleQueryEnumeration(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

  public com.runwaysdk.query.AttributeChar getLocaleLabel()
  {
    return getLocaleLabel(null);

  }
 
  public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias)
  {
    return (com.runwaysdk.query.AttributeChar)this.get(com.runwaysdk.system.metadata.Locale.LOCALELABEL, alias, null);

  }
 
  public com.runwaysdk.query.AttributeChar getLocaleLabel(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.AttributeChar)this.get(com.runwaysdk.system.metadata.Locale.LOCALELABEL, alias, displayLabel);

  }
  }

/**
 * Specifies type safe query methods for the enumeration com.runwaysdk.system.metadata.UserLocales.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface UserLocalesQueryIF extends LocaleQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.UserLocales ... userLocales);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.UserLocales ... userLocales);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.UserLocales ... userLocales);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.UserLocales ... userLocales);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.UserLocales ... userLocales);
  }

/**
 * Implements type safe query methods for the enumeration com.runwaysdk.system.metadata.UserLocales.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class UserLocalesQuery extends LocaleQueryEnumeration implements  UserLocalesQueryIF
  {
  public UserLocalesQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.UserLocales ... userLocales)  {

      String[] enumIdArray = new String[userLocales.length]; 

      for (int i=0; i<userLocales.length; i++)
      {
        enumIdArray[i] = userLocales[i].getId();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.UserLocales ... userLocales)  {

      String[] enumIdArray = new String[userLocales.length]; 

      for (int i=0; i<userLocales.length; i++)
      {
        enumIdArray[i] = userLocales[i].getId();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.UserLocales ... userLocales)  {

      String[] enumIdArray = new String[userLocales.length]; 

      for (int i=0; i<userLocales.length; i++)
      {
        enumIdArray[i] = userLocales[i].getId();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.UserLocales ... userLocales)  {

      String[] enumIdArray = new String[userLocales.length]; 

      for (int i=0; i<userLocales.length; i++)
      {
        enumIdArray[i] = userLocales[i].getId();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.UserLocales ... userLocales)  {

      String[] enumIdArray = new String[userLocales.length]; 

      for (int i=0; i<userLocales.length; i++)
      {
        enumIdArray[i] = userLocales[i].getId();
      }

      return this.containsExactly(enumIdArray);
  }
  }}