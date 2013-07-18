package com.runwaysdk.jstest;

public class TestStruct extends TestStructBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 208794916;
  
  public TestStruct()
  {
    super();
  }
  
  public TestStruct(com.runwaysdk.business.MutableWithStructs entity, String structName)
  {
    super(entity, structName);
  }
  
}
