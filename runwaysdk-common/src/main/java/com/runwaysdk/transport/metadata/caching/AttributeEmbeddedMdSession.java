package com.runwaysdk.transport.metadata.caching;

public class AttributeEmbeddedMdSession extends AttributeMdSession
{
  /**
   * The MdStruct that defines the struct.
   */
  private String embeddedMdClassType;
  
  public AttributeEmbeddedMdSession(String embeddedMdClassType) 
  {
    this.embeddedMdClassType = embeddedMdClassType;
  }

  public String getEmbeddedgMdClass() 
  {
    return embeddedMdClassType;
  }
}
