/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -1456260719)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoEntityProblemTypeMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class GeoEntityProblemTypeMasterQuery extends com.runwaysdk.system.EnumerationMasterQuery

{

  public GeoEntityProblemTypeMasterQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public GeoEntityProblemTypeMasterQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.CLASS;
  }
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription()
  {
    return getDescription(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION);

    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION);

    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeLocal localFactory( com.runwaysdk.dataaccess.MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStruct((com.runwaysdk.dataaccess.MdAttributeLocalDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.localFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends GeoEntityProblemTypeMaster> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<GeoEntityProblemTypeMaster>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition enum_GeoEntityProblemType()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemType.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isChildIn(relationshipQuery);
  }


/**
 * 
 **/

  public com.runwaysdk.query.Condition notEnum_GeoEntityProblemType()
  {
    com.runwaysdk.query.QueryFactory queryFactory = this.getQueryFactory();
    com.runwaysdk.business.RelationshipQuery relationshipQuery = queryFactory.relationshipQuery(com.runwaysdk.system.metadata.EnumerationAttributeItem.CLASS);

    com.runwaysdk.business.BusinessQuery businessQuery = queryFactory.businessQuery(com.runwaysdk.system.metadata.MdEnumeration.CLASS);
    com.runwaysdk.dataaccess.MdEnumerationDAOIF mdEnumerationIF = com.runwaysdk.dataaccess.metadata.MdEnumerationDAO.getMdEnumerationDAO(com.runwaysdk.system.gis.geo.GeoEntityProblemType.CLASS); 
    businessQuery.WHERE(businessQuery.oid().EQ(mdEnumerationIF.getOid()));

    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));

    return this.getBusinessQuery().isNotChildIn(relationshipQuery);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeoEntityProblemTypeMasterQueryReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReferenceIF
  {

    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription();
    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias);
    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster geoEntityProblemTypeMaster);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster geoEntityProblemTypeMaster);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeoEntityProblemTypeMasterQueryReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryReference
 implements GeoEntityProblemTypeMasterQueryReferenceIF

  {

  public GeoEntityProblemTypeMasterQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster geoEntityProblemTypeMaster)
    {
      if(geoEntityProblemTypeMaster == null) return this.EQ((java.lang.String)null);
      return this.EQ(geoEntityProblemTypeMaster.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster geoEntityProblemTypeMaster)
    {
      if(geoEntityProblemTypeMaster == null) return this.NE((java.lang.String)null);
      return this.NE(geoEntityProblemTypeMaster.getOid());
    }

  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription()
  {
    return getDescription(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.attributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, com.runwaysdk.system.metadata.MdAttributeLocalCharacter.CLASS, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.attributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, com.runwaysdk.system.metadata.MdAttributeLocalCharacter.CLASS, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeLocal localFactory( com.runwaysdk.dataaccess.MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStruct((com.runwaysdk.dataaccess.MdAttributeLocalDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.localFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface GeoEntityProblemTypeMasterQueryEnumerationIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumerationIF
  {

    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription();
    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias);
    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class GeoEntityProblemTypeMasterQueryEnumeration extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryEnumeration
 implements GeoEntityProblemTypeMasterQueryEnumerationIF
  {

  public GeoEntityProblemTypeMasterQueryEnumeration(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription()
  {
    return getDescription(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.attributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, com.runwaysdk.system.metadata.MdAttributeLocalCharacter.CLASS, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.attributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, com.runwaysdk.system.metadata.MdAttributeLocalCharacter.CLASS, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeLocal localFactory( com.runwaysdk.dataaccess.MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStruct((com.runwaysdk.dataaccess.MdAttributeLocalDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.localFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }

/**
 * Specifies type safe query methods for the enumeration com.runwaysdk.system.gis.geo.GeoEntityProblemType.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public interface GeoEntityProblemTypeQueryIF extends GeoEntityProblemTypeMasterQueryEnumerationIF  {

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType);
  }

/**
 * Implements type safe query methods for the enumeration com.runwaysdk.system.gis.geo.GeoEntityProblemType.
 * This type is used when a join is performed on this class as an enumeration.
 **/
  public static class GeoEntityProblemTypeQuery extends GeoEntityProblemTypeMasterQueryEnumeration implements  GeoEntityProblemTypeQueryIF
  {
  public GeoEntityProblemTypeQuery(com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdEnumerationTableName,com.runwaysdk.dataaccess.MdBusinessDAOIF masterMdBusinessIF, String masterTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType)  {

      String[] enumIdArray = new String[geoEntityProblemType.length]; 

      for (int i=0; i<geoEntityProblemType.length; i++)
      {
        enumIdArray[i] = geoEntityProblemType[i].getOid();
      }

      return this.containsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType)  {

      String[] enumIdArray = new String[geoEntityProblemType.length]; 

      for (int i=0; i<geoEntityProblemType.length; i++)
      {
        enumIdArray[i] = geoEntityProblemType[i].getOid();
      }

      return this.notContainsAny(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType)  {

      String[] enumIdArray = new String[geoEntityProblemType.length]; 

      for (int i=0; i<geoEntityProblemType.length; i++)
      {
        enumIdArray[i] = geoEntityProblemType[i].getOid();
      }

      return this.containsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType)  {

      String[] enumIdArray = new String[geoEntityProblemType.length]; 

      for (int i=0; i<geoEntityProblemType.length; i++)
      {
        enumIdArray[i] = geoEntityProblemType[i].getOid();
      }

      return this.notContainsAll(enumIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.gis.geo.GeoEntityProblemType ... geoEntityProblemType)  {

      String[] enumIdArray = new String[geoEntityProblemType.length]; 

      for (int i=0; i<geoEntityProblemType.length; i++)
      {
        enumIdArray[i] = geoEntityProblemType[i].getOid();
      }

      return this.containsExactly(enumIdArray);
  }
  }
/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeoEntityProblemTypeMasterQueryMultiReferenceIF extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReferenceIF
  {

    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription();
    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias);
    public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeoEntityProblemTypeMasterQueryMultiReference extends com.runwaysdk.system.EnumerationMasterQuery.EnumerationMasterQueryMultiReference
 implements GeoEntityProblemTypeMasterQueryMultiReferenceIF

  {

  public GeoEntityProblemTypeMasterQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster)  {

      String[] itemIdArray = new String[geoEntityProblemTypeMaster.length]; 

      for (int i=0; i<geoEntityProblemTypeMaster.length; i++)
      {
        itemIdArray[i] = geoEntityProblemTypeMaster[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster)  {

      String[] itemIdArray = new String[geoEntityProblemTypeMaster.length]; 

      for (int i=0; i<geoEntityProblemTypeMaster.length; i++)
      {
        itemIdArray[i] = geoEntityProblemTypeMaster[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster)  {

      String[] itemIdArray = new String[geoEntityProblemTypeMaster.length]; 

      for (int i=0; i<geoEntityProblemTypeMaster.length; i++)
      {
        itemIdArray[i] = geoEntityProblemTypeMaster[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster)  {

      String[] itemIdArray = new String[geoEntityProblemTypeMaster.length]; 

      for (int i=0; i<geoEntityProblemTypeMaster.length; i++)
      {
        itemIdArray[i] = geoEntityProblemTypeMaster[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster ... geoEntityProblemTypeMaster)  {

      String[] itemIdArray = new String[geoEntityProblemTypeMaster.length]; 

      for (int i=0; i<geoEntityProblemTypeMaster.length; i++)
      {
        itemIdArray[i] = geoEntityProblemTypeMaster[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription()
  {
    return getDescription(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.attributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, com.runwaysdk.system.metadata.MdAttributeLocalCharacter.CLASS, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF getDescription(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStructIF)this.attributeFactory(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION, com.runwaysdk.system.metadata.MdAttributeLocalCharacter.CLASS, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeLocal localFactory( com.runwaysdk.dataaccess.MdAttributeLocalDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdLocalStructDAOIF mdLocalStructIF, String structTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMaster.DESCRIPTION)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityProblemTypeMasterDescriptionQuery.GeoEntityProblemTypeMasterDescriptionQueryStruct((com.runwaysdk.dataaccess.MdAttributeLocalDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.localFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}
