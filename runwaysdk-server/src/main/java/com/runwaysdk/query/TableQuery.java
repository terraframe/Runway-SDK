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
   * 
   * @param valueQuery
   * @param type
   */
  protected TableQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
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
    throw new UnsupportedOperationException();
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
    throw new UnsupportedOperationException();
  }  

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
