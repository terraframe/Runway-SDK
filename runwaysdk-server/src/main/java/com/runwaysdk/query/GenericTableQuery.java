package com.runwaysdk.query;

import com.runwaysdk.dataaccess.MdTableDAOIF;

public class GenericTableQuery extends GeneratedTableClassQuery
{
  private MdTableDAOIF mdTableDAOIF;
  
  public GenericTableQuery(MdTableDAOIF _mdTableDAOIF, QueryFactory _componentQueryFactory)
  {
    super();
    
    this.mdTableDAOIF = _mdTableDAOIF;
    
    if (this.getComponentQuery() == null)
    {
      this.setComponentQuery(_componentQueryFactory.tableQuery(_mdTableDAOIF.definesType()));
    }
  }
  
  @Override
  public MdTableDAOIF getMdClassIF()
  {
    return (MdTableDAOIF)super.getMdClassIF();
  }
  
  public MdTableDAOIF getMdTableF()
  {
    return (MdTableDAOIF)super.getMdClassIF();
  }

  @Override
  public String getClassType()
  {
    return mdTableDAOIF.definesType();
  }

  /**
   * Returns {@link TableQuery} that all generated query methods delegate to.
   * 
   * @return {@link TableQuery} that all generated query methods delegate to.
   */
  @Override
  protected TableQuery getComponentQuery()
  {
    return (TableQuery)super.getComponentQuery();
  }
  
  /**
   * Sets the {@link TableQuery} that all generated query methods delegate to.
   */
  protected void setComponentQuery(TableQuery _tableQuery)
  {
    super.setComponentQuery(_tableQuery);
  }

//  @Override
//  protected SelectableChar getId()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }

}
