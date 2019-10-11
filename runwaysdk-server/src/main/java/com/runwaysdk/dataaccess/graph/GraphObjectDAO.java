package com.runwaysdk.dataaccess.graph;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ComponentDAO;

public abstract class GraphObjectDAO extends ComponentDAO implements GraphObjectDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 765877467852678882L;
  
  
  
  

  @Override
  public AttributeIF getAttributeIF(String name)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasAttribute(String name)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public AttributeIF[] getAttributeArrayIF()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isNew()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setIsNew(boolean isNew)
  {
    // TODO Auto-generated method stub

  }

}
