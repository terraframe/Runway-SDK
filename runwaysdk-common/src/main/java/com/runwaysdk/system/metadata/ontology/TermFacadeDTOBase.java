package com.runwaysdk.system.metadata.ontology;

@com.runwaysdk.business.ClassSignature(hash = 1398491483)
public abstract class TermFacadeDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.ontology.TermFacade";
  private static final long serialVersionUID = 1398491483;
  
  protected TermFacadeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final java.lang.String[] getAllAncestors(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ontology.TermFacadeDTO.CLASS, "getAllAncestors", _declaredTypes);
    return (java.lang.String[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String[] getAllDescendants(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ontology.TermFacadeDTO.CLASS, "getAllDescendants", _declaredTypes);
    return (java.lang.String[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static TermFacadeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (TermFacadeDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
}
