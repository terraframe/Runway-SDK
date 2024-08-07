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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1397931821)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to SymmetricMethods.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class SymmetricMethodsQuery extends com.runwaysdk.system.EnumerationMasterQuery

{

  public SymmetricMethodsQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public SymmetricMethodsQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.SymmetricMethods.CLASS;
  }
  public com.runwaysdk.query.SelectableChar getTransformation()
  {
    return getTransformation(null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends SymmetricMethods> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<SymmetricMethods>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_SymmetricOptions()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.metadata.SymmetricOptions.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_SymmetricOptions()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.metadata.SymmetricOptions.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface SymmetricMethodsQueryReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getTransformation();
    public com.runwaysdk.query.SelectableChar getTransformation(String alias);
    public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.SymmetricMethods symmetricMethods);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.SymmetricMethods symmetricMethods);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class SymmetricMethodsQueryReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReference
 implements SymmetricMethodsQueryReferenceIF

  {

  public SymmetricMethodsQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.SymmetricMethods symmetricMethods)
    {
      if(symmetricMethods == null) return this.EQ((java.lang.String)null);
      return this.EQ(symmetricMethods.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.SymmetricMethods symmetricMethods)
    {
      if(symmetricMethods == null) return this.NE((java.lang.String)null);
      return this.NE(symmetricMethods.getOid());
    }

  public com.runwaysdk.query.SelectableChar getTransformation()
  {
    return getTransformation(null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, displayLabel);

  }
  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface SymmetricMethodsQueryEnumerationIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumerationIF
  {

    public com.runwaysdk.query.SelectableChar getTransformation();
    public com.runwaysdk.query.SelectableChar getTransformation(String alias);
    public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class SymmetricMethodsQueryEnumeration extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumeration
 implements SymmetricMethodsQueryEnumerationIF
  {

  public SymmetricMethodsQueryEnumeration(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

  public com.runwaysdk.query.SelectableChar getTransformation()
  {
    return getTransformation(null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, displayLabel);

  }
  }

/**
 * Specifies type safe query methods for the enumeration com.runwaysdk.system.metadata.SymmetricOptions.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface SymmetricOptionsQueryIF extends SymmetricMethodsQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions);
  }

/**
 * Implements type safe query methods for the enumeration com.runwaysdk.system.metadata.SymmetricOptions.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class SymmetricOptionsQuery extends SymmetricMethodsQueryEnumeration implements  SymmetricOptionsQueryIF
  {
  public SymmetricOptionsQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions)  {

      String[] enumIdArray = new String[symmetricOptions.length]; 

      for (int i=0; i<symmetricOptions.length; i++)
      {
        enumIdArray[i] = symmetricOptions[i].getOid();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions)  {

      String[] enumIdArray = new String[symmetricOptions.length]; 

      for (int i=0; i<symmetricOptions.length; i++)
      {
        enumIdArray[i] = symmetricOptions[i].getOid();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions)  {

      String[] enumIdArray = new String[symmetricOptions.length]; 

      for (int i=0; i<symmetricOptions.length; i++)
      {
        enumIdArray[i] = symmetricOptions[i].getOid();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions)  {

      String[] enumIdArray = new String[symmetricOptions.length]; 

      for (int i=0; i<symmetricOptions.length; i++)
      {
        enumIdArray[i] = symmetricOptions[i].getOid();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.SymmetricOptions ... symmetricOptions)  {

      String[] enumIdArray = new String[symmetricOptions.length]; 

      for (int i=0; i<symmetricOptions.length; i++)
      {
        enumIdArray[i] = symmetricOptions[i].getOid();
      }

      return this.containsExactly(enumIdArray);
  }
  }
/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface SymmetricMethodsQueryMultiReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getTransformation();
    public com.runwaysdk.query.SelectableChar getTransformation(String alias);
    public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class SymmetricMethodsQueryMultiReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReference
 implements SymmetricMethodsQueryMultiReferenceIF

  {

  public SymmetricMethodsQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods)  {

      String[] itemIdArray = new String[symmetricMethods.length]; 

      for (int i=0; i<symmetricMethods.length; i++)
      {
        itemIdArray[i] = symmetricMethods[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods)  {

      String[] itemIdArray = new String[symmetricMethods.length]; 

      for (int i=0; i<symmetricMethods.length; i++)
      {
        itemIdArray[i] = symmetricMethods[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods)  {

      String[] itemIdArray = new String[symmetricMethods.length]; 

      for (int i=0; i<symmetricMethods.length; i++)
      {
        itemIdArray[i] = symmetricMethods[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods)  {

      String[] itemIdArray = new String[symmetricMethods.length]; 

      for (int i=0; i<symmetricMethods.length; i++)
      {
        itemIdArray[i] = symmetricMethods[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.SymmetricMethods ... symmetricMethods)  {

      String[] itemIdArray = new String[symmetricMethods.length]; 

      for (int i=0; i<symmetricMethods.length; i++)
      {
        itemIdArray[i] = symmetricMethods[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.query.SelectableChar getTransformation()
  {
    return getTransformation(null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getTransformation(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.SymmetricMethods.TRANSFORMATION, alias, displayLabel);

  }
  }
}
