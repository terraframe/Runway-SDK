package com.runwaysdk.query;

import com.runwaysdk.business.Business;
import com.runwaysdk.dataaccess.MdTableDAOIF;

public class TableQuery extends TableClassQuery
{  
  /**
   * 
   * @param queryFactory
   * @param type
   */
  protected TableQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
  }
  
  /**
   * Returns the {@link MdTableDAOIF} that defines the type of objects that are queried from
   * this object.
   * 
   * @return {@link MdTableDAOIF} that defines the type of objects that are queried from
   *         this object.
   */
  public MdTableDAOIF getMdTableDAOIF()
  {
    return (MdTableDAOIF)this.getMdTableClassIF();
  }
  
  /**
   * Returns the type that this object queries.
   * 
   * @return type that this object queries.
   */
  public String getType()
  {
    return this.type;
  }

 /**
  * Returns an iterator of Business objects that match the query criteria specified
  * on this query object.
  * @return iterator of Business objects that match the query criteria specified
  * on this query object.
  */
 public OIterator<Business> getIterator()
 {
   return null;
   
//   this.checkNotUsedInValueQuery();
//
//   String sqlStmt = this.getSQL();
//   Map<String, ColumnInfo> columnInfoMap = this.columnInfoMap;
//
//   ResultSet results = Database.query(sqlStmt);
//   return new BusinessIterator<Business>(this.mdEntityIF, columnInfoMap, results);
 }
 
 /**
  * Returns an iterator of Business objects that match the query criteria specified
  * on this query object.
  * @param pageSize   number of results per page
  * @param pageNumber page number
  * @return iterator of Business objects that match the query criteria specified
  * on this query object.
  */
 public OIterator<Business> getIterator(int pageSize, int pageNumber)
 {
   return null;

//   this.checkNotUsedInValueQuery();
//
//   int limit = ComponentQuery.rowLimit(pageSize);
//   int skip = ComponentQuery.rowSkip(pageSize, pageNumber);
//
//   String sqlStmt = this.getSQL(limit, skip);
//   Map<String, ColumnInfo> columnInfoMap = this.columnInfoMap;
//
//   ResultSet results = Database.query(sqlStmt);
//   return new BusinessIterator<Business>(this.mdEntityIF, columnInfoMap, results);
 }
  
//  /**
//   * @see com.runwaysdk.query.ComponentQuery#aBlob(String)
//   */
//  @Override
//  public AttributeBlob aBlob(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
// 
//  
//  @Override
//  public AttributeText aText(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeDate aDate(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeTime aTime(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeDateTime aDateTime(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeInteger aInteger(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeLong aLong(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeDouble aDouble(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeDecimal aDecimal(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeFloat aFloat(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeBoolean aBoolean(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeStruct aStruct(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeLocal aLocalCharacter(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeLocal aLocalText(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeRef aFile(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeRef aReference(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributeRef aEnumeration(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public AttributePrimitive getPrimitive(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public Attribute get(String name)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  protected Map<String, String> getFromTableMapInfoForQuery()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getCountSQL()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  protected String getSQL(boolean limitRowRange, int limit, int skip)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  protected void appendDistinctToSelectClause(StringBuffer selectString)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  protected void computeGroupByClauseForCount()
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  protected String addGroupBySelectablesToSelectClauseForCount()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  protected String buildGroupByClause()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public String getOrderBySQL(OrderBy orderBy)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public OIterator<? extends ComponentIF> getIterator()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public OIterator<? extends ComponentIF> getIterator(int pageSize, int pageNumber)
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
//  

  /**
   * @return returns a rank function 
   */
  @Override
  public RANK RANK()
  {
    throw new UnsupportedOperationException();
  }
 
  /**
   * @return returns a rank function 
   */
  @Override
  public RANK RANK(String userDefinedAlias)
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   *
   * @param selectable
   */
  public RANK RANK(Selectable selectable)
  {
    return new RANK(selectable, null, null);
  }
 
  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public RANK RANK(Selectable selectable, String userDefinedAlias)
  {
    return new RANK(selectable, userDefinedAlias, null);
  }
 
  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public RANK RANK(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new RANK(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

}
