package com.runwaysdk.dataaccess.graph;

public interface GraphRequest
{
  public void beginTransaction();
  
  public void commit();
  
  public void rollback();
  
  public void close();
}
