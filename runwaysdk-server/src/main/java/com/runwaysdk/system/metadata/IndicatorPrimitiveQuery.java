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

@com.runwaysdk.business.ClassSignature(hash = 608547590)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to IndicatorPrimitive.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class IndicatorPrimitiveQuery extends com.runwaysdk.system.metadata.IndicatorElementQuery

{

  public IndicatorPrimitiveQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public IndicatorPrimitiveQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.IndicatorPrimitive.CLASS;
  }
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction()
  {
    return getIndicatorFunction(null);

  }
 
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION);

    return (com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION);

    return (com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive()
  {
    return getMdAttributePrimitive(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE);

    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE);

    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE)) 
    {
       return new com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  protected com.runwaysdk.query.AttributeEnumeration enumerationFactory( com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  String mdEnumerationTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION)) 
    {
       return new com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends IndicatorPrimitive> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<IndicatorPrimitive>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface IndicatorPrimitiveQueryReferenceIF extends com.runwaysdk.system.metadata.IndicatorElementQuery.IndicatorElementQueryReferenceIF
  {

  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction();
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias);
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias, String displayLabel);
    public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive();
    public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias);
    public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.IndicatorPrimitive indicatorPrimitive);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.IndicatorPrimitive indicatorPrimitive);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class IndicatorPrimitiveQueryReference extends com.runwaysdk.system.metadata.IndicatorElementQuery.IndicatorElementQueryReference
 implements IndicatorPrimitiveQueryReferenceIF

  {

  public IndicatorPrimitiveQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.IndicatorPrimitive indicatorPrimitive)
    {
      if(indicatorPrimitive == null) return this.EQ((java.lang.String)null);
      return this.EQ(indicatorPrimitive.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.IndicatorPrimitive indicatorPrimitive)
    {
      if(indicatorPrimitive == null) return this.NE((java.lang.String)null);
      return this.NE(indicatorPrimitive.getOid());
    }

  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction()
  {
    return getIndicatorFunction(null);

  }
 
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias)
  {
    return (com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION, alias, null);

  }
 
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION, alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive()
  {
    return getMdAttributePrimitive(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias)
  {
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE)) 
    {
       return new com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  protected com.runwaysdk.query.AttributeEnumeration enumerationFactory( com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  String mdEnumerationTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION)) 
    {
       return new com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface IndicatorPrimitiveQueryMultiReferenceIF extends com.runwaysdk.system.metadata.IndicatorElementQuery.IndicatorElementQueryMultiReferenceIF
  {

  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction();
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias);
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias, String displayLabel);
    public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive();
    public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias);
    public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class IndicatorPrimitiveQueryMultiReference extends com.runwaysdk.system.metadata.IndicatorElementQuery.IndicatorElementQueryMultiReference
 implements IndicatorPrimitiveQueryMultiReferenceIF

  {

  public IndicatorPrimitiveQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive)  {

      String[] itemIdArray = new String[indicatorPrimitive.length]; 

      for (int i=0; i<indicatorPrimitive.length; i++)
      {
        itemIdArray[i] = indicatorPrimitive[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive)  {

      String[] itemIdArray = new String[indicatorPrimitive.length]; 

      for (int i=0; i<indicatorPrimitive.length; i++)
      {
        itemIdArray[i] = indicatorPrimitive[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive)  {

      String[] itemIdArray = new String[indicatorPrimitive.length]; 

      for (int i=0; i<indicatorPrimitive.length; i++)
      {
        itemIdArray[i] = indicatorPrimitive[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive)  {

      String[] itemIdArray = new String[indicatorPrimitive.length]; 

      for (int i=0; i<indicatorPrimitive.length; i++)
      {
        itemIdArray[i] = indicatorPrimitive[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.IndicatorPrimitive ... indicatorPrimitive)  {

      String[] itemIdArray = new String[indicatorPrimitive.length]; 

      for (int i=0; i<indicatorPrimitive.length; i++)
      {
        itemIdArray[i] = indicatorPrimitive[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction()
  {
    return getIndicatorFunction(null);

  }
 
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias)
  {
    return (com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION, alias, null);

  }
 
  public com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF getIndicatorFunction(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQueryIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION, alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive()
  {
    return getMdAttributePrimitive(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias)
  {
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF getMdAttributePrimitive(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReferenceIF)this.get(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.metadata.IndicatorPrimitive.MDATTRIBUTEPRIMITIVE)) 
    {
       return new com.runwaysdk.system.metadata.MdAttributePrimitiveQuery.MdAttributePrimitiveQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  protected com.runwaysdk.query.AttributeEnumeration enumerationFactory( com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  String mdEnumerationTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.system.metadata.IndicatorPrimitive.INDICATORFUNCTION)) 
    {
       return new com.runwaysdk.system.metadata.AggregationFunctionQuery.IndicatorAggregateFunctionQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}
