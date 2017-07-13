package com.runwaysdk.transport.metadata.caching;

public class AttributeIndicatorMdSession extends AttributeMdSession
{
  private String javaType;
  
  public AttributeIndicatorMdSession(String _javaType) 
  {
    super();
    
    this.javaType = _javaType;
  }
  
  public String getJavaType()
  {
    return this.javaType;
  }
}
