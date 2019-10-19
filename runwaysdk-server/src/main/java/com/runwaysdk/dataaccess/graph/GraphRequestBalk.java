package com.runwaysdk.dataaccess.graph;

/**
 * This class implements the Balk pattern for instances where no graph databae is present.
 * @author nathan
 *
 */
public class GraphRequestBalk implements GraphRequest
{

  @Override
  public void beginTransaction() {}

  @Override
  public void commit() {}

  @Override
  public void rollback() {}

  @Override
  public void close() {}
  
  @Override
  public boolean getIsDDLRequest() { return false; }
  
  @Override
  public void setIsDDLRequest() {}

}
