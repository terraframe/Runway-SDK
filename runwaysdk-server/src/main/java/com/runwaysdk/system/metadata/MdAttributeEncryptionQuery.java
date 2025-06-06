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

@com.runwaysdk.business.ClassSignature(hash = 1858795342)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeEncryption.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class MdAttributeEncryptionQuery extends com.runwaysdk.system.metadata.MdAttributeConcreteQuery

{

  public MdAttributeEncryptionQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public MdAttributeEncryptionQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.system.metadata.MdAttributeEncryption.CLASS;
  }
  public com.runwaysdk.query.SelectableChar getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableChar getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeEncryption.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(com.runwaysdk.system.metadata.MdAttributeEncryption.DEFAULTVALUE, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends MdAttributeEncryption> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<MdAttributeEncryption>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MdAttributeEncryptionQueryReferenceIF extends com.runwaysdk.system.metadata.MdAttributeConcreteQuery.MdAttributeConcreteQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getDefaultValue();
    public com.runwaysdk.query.SelectableChar getDefaultValue(String alias);
    public com.runwaysdk.query.SelectableChar getDefaultValue(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdAttributeEncryption mdAttributeEncryption);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdAttributeEncryption mdAttributeEncryption);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MdAttributeEncryptionQueryReference extends com.runwaysdk.system.metadata.MdAttributeConcreteQuery.MdAttributeConcreteQueryReference
 implements MdAttributeEncryptionQueryReferenceIF

  {

  public MdAttributeEncryptionQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.system.metadata.MdAttributeEncryption mdAttributeEncryption)
    {
      if(mdAttributeEncryption == null) return this.EQ((java.lang.String)null);
      return this.EQ(mdAttributeEncryption.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.system.metadata.MdAttributeEncryption mdAttributeEncryption)
    {
      if(mdAttributeEncryption == null) return this.NE((java.lang.String)null);
      return this.NE(mdAttributeEncryption.getOid());
    }

  public com.runwaysdk.query.SelectableChar getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableChar getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.MdAttributeEncryption.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.MdAttributeEncryption.DEFAULTVALUE, alias, displayLabel);

  }
  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface MdAttributeEncryptionQueryMultiReferenceIF extends com.runwaysdk.system.metadata.MdAttributeConcreteQuery.MdAttributeConcreteQueryMultiReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getDefaultValue();
    public com.runwaysdk.query.SelectableChar getDefaultValue(String alias);
    public com.runwaysdk.query.SelectableChar getDefaultValue(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class MdAttributeEncryptionQueryMultiReference extends com.runwaysdk.system.metadata.MdAttributeConcreteQuery.MdAttributeConcreteQueryMultiReference
 implements MdAttributeEncryptionQueryMultiReferenceIF

  {

  public MdAttributeEncryptionQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption)  {

      String[] itemIdArray = new String[mdAttributeEncryption.length]; 

      for (int i=0; i<mdAttributeEncryption.length; i++)
      {
        itemIdArray[i] = mdAttributeEncryption[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption)  {

      String[] itemIdArray = new String[mdAttributeEncryption.length]; 

      for (int i=0; i<mdAttributeEncryption.length; i++)
      {
        itemIdArray[i] = mdAttributeEncryption[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption)  {

      String[] itemIdArray = new String[mdAttributeEncryption.length]; 

      for (int i=0; i<mdAttributeEncryption.length; i++)
      {
        itemIdArray[i] = mdAttributeEncryption[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption)  {

      String[] itemIdArray = new String[mdAttributeEncryption.length]; 

      for (int i=0; i<mdAttributeEncryption.length; i++)
      {
        itemIdArray[i] = mdAttributeEncryption[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.system.metadata.MdAttributeEncryption ... mdAttributeEncryption)  {

      String[] itemIdArray = new String[mdAttributeEncryption.length]; 

      for (int i=0; i<mdAttributeEncryption.length; i++)
      {
        itemIdArray[i] = mdAttributeEncryption[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.query.SelectableChar getDefaultValue()
  {
    return getDefaultValue(null);

  }
 
  public com.runwaysdk.query.SelectableChar getDefaultValue(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.MdAttributeEncryption.DEFAULTVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getDefaultValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(com.runwaysdk.system.metadata.MdAttributeEncryption.DEFAULTVALUE, alias, displayLabel);

  }
  }
}
