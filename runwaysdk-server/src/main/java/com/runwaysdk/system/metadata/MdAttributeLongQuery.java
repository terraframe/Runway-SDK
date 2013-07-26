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

@com.runwaysdk.business.ClassSignature(hash = 1169467105)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeLong.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class MdAttributeLongQuery extends com.runwaysdk.system.metadata.MdAttributeIntQuery

{

  public MdAttributeLongQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public MdAttributeLongQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.MdAttributeLong.CLASS;
  }
  public com.runwaysdk.query.SelectableLong getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableLong getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeLong.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeLong.DEFAULTVALUE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableLong getEndRange()
  {
    return getEndRange(null);

  }
 
  public com.runwaysdk.query.SelectableLong getEndRange(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeLong.ENDRANGE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getEndRange(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeLong.ENDRANGE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableLong getStartRange()
  {
    return getStartRange(null);

  }
 
  public com.runwaysdk.query.SelectableLong getStartRange(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeLong.STARTRANGE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getStartRange(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeLong.STARTRANGE, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends MdAttributeLong> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<MdAttributeLong>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MdAttributeLongQueryReferenceIF extends com.runwaysdk.system.metadata.MdAttributeIntQuery.MdAttributeIntQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableLong getDefaultValue();
    public com.runwaysdk.query.SelectableLong getDefaultValue(String alias);
    public com.runwaysdk.query.SelectableLong getDefaultValue(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableLong getEndRange();
    public com.runwaysdk.query.SelectableLong getEndRange(String alias);
    public com.runwaysdk.query.SelectableLong getEndRange(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableLong getStartRange();
    public com.runwaysdk.query.SelectableLong getStartRange(String alias);
    public com.runwaysdk.query.SelectableLong getStartRange(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdAttributeLong mdAttributeLong);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdAttributeLong mdAttributeLong);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MdAttributeLongQueryReference extends com.runwaysdk.system.metadata.MdAttributeIntQuery.MdAttributeIntQueryReference
 implements MdAttributeLongQueryReferenceIF

  {

  public MdAttributeLongQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdAttributeLong mdAttributeLong)
    {
      if(mdAttributeLong == null) return this.EQ((java.lang.String)null);
      return this.EQ(mdAttributeLong.getId());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdAttributeLong mdAttributeLong)
    {
      if(mdAttributeLong == null) return this.NE((java.lang.String)null);
      return this.NE(mdAttributeLong.getId());
    }

  public com.runwaysdk.query.SelectableLong getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableLong getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.get(com.runwaysdk.system.metadata.MdAttributeLong.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.get(com.runwaysdk.system.metadata.MdAttributeLong.DEFAULTVALUE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableLong getEndRange()
  {
    return getEndRange(null);

  }
 
  public com.runwaysdk.query.SelectableLong getEndRange(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.get(com.runwaysdk.system.metadata.MdAttributeLong.ENDRANGE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getEndRange(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.get(com.runwaysdk.system.metadata.MdAttributeLong.ENDRANGE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableLong getStartRange()
  {
    return getStartRange(null);

  }
 
  public com.runwaysdk.query.SelectableLong getStartRange(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.get(com.runwaysdk.system.metadata.MdAttributeLong.STARTRANGE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getStartRange(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.get(com.runwaysdk.system.metadata.MdAttributeLong.STARTRANGE, alias, displayLabel);

  }
  }
}