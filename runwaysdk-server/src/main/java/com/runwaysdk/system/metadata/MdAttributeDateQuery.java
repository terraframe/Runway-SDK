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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -388802335)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeDate.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class MdAttributeDateQuery extends com.runwaysdk.system.metadata.MdAttributeMomentQuery

{

  public MdAttributeDateQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public MdAttributeDateQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.MdAttributeDate.CLASS;
  }
  public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive()
  {
    return getAfterTodayExclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYEXCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYEXCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive()
  {
    return getAfterTodayInclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYINCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYINCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive()
  {
    return getBeforeTodayExclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYEXCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYEXCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive()
  {
    return getBeforeTodayInclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYINCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYINCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.DEFAULTVALUE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getEndDate()
  {
    return getEndDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.ENDDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.ENDDATE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getStartDate()
  {
    return getStartDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.STARTDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeDate.STARTDATE, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends MdAttributeDate> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<MdAttributeDate>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MdAttributeDateQueryReferenceIF extends com.runwaysdk.system.metadata.MdAttributeMomentQuery.MdAttributeMomentQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive();
    public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive(String alias);
    public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive();
    public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive(String alias);
    public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive();
    public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive(String alias);
    public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive();
    public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive(String alias);
    public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableMoment getDefaultValue();
    public com.runwaysdk.query.SelectableMoment getDefaultValue(String alias);
    public com.runwaysdk.query.SelectableMoment getDefaultValue(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableMoment getEndDate();
    public com.runwaysdk.query.SelectableMoment getEndDate(String alias);
    public com.runwaysdk.query.SelectableMoment getEndDate(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableMoment getStartDate();
    public com.runwaysdk.query.SelectableMoment getStartDate(String alias);
    public com.runwaysdk.query.SelectableMoment getStartDate(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdAttributeDate mdAttributeDate);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdAttributeDate mdAttributeDate);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MdAttributeDateQueryReference extends com.runwaysdk.system.metadata.MdAttributeMomentQuery.MdAttributeMomentQueryReference
 implements MdAttributeDateQueryReferenceIF

  {

  public MdAttributeDateQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdAttributeDate mdAttributeDate)
    {
      if(mdAttributeDate == null) return this.EQ((java.lang.String)null);
      return this.EQ(mdAttributeDate.getId());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdAttributeDate mdAttributeDate)
    {
      if(mdAttributeDate == null) return this.NE((java.lang.String)null);
      return this.NE(mdAttributeDate.getId());
    }

  public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive()
  {
    return getAfterTodayExclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYEXCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayExclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYEXCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive()
  {
    return getAfterTodayInclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYINCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getAfterTodayInclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.AFTERTODAYINCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive()
  {
    return getBeforeTodayExclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYEXCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayExclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYEXCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive()
  {
    return getBeforeTodayInclusive(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYINCLUSIVE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getBeforeTodayInclusive(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.get(com.runwaysdk.system.metadata.MdAttributeDate.BEFORETODAYINCLUSIVE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.get(com.runwaysdk.system.metadata.MdAttributeDate.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.get(com.runwaysdk.system.metadata.MdAttributeDate.DEFAULTVALUE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getEndDate()
  {
    return getEndDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.get(com.runwaysdk.system.metadata.MdAttributeDate.ENDDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.get(com.runwaysdk.system.metadata.MdAttributeDate.ENDDATE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getStartDate()
  {
    return getStartDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.get(com.runwaysdk.system.metadata.MdAttributeDate.STARTDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.get(com.runwaysdk.system.metadata.MdAttributeDate.STARTDATE, alias, displayLabel);

  }
  }
}