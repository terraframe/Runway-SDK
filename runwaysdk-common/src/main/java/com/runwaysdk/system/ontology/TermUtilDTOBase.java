package com.runwaysdk.system.ontology;

@com.runwaysdk.business.ClassSignature(hash = -1691324971)
public abstract class TermUtilDTOBase extends com.runwaysdk.business.UtilDTO
{
  public final static String CLASS = "com.runwaysdk.system.ontology.TermUtil";
  private static final long serialVersionUID = -1691324971;
  
  protected TermUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final com.runwaysdk.business.RelationshipDTO addAndRemoveLink(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String childId, java.lang.String oldParentId, java.lang.String oldRelType, java.lang.String newParentId, java.lang.String newRelType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{childId, oldParentId, oldRelType, newParentId, newRelType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "addAndRemoveLink", _declaredTypes);
    return (com.runwaysdk.business.RelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.RelationshipDTO addLink(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String childId, java.lang.String parentId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{childId, parentId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "addLink", _declaredTypes);
    return (com.runwaysdk.business.RelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermDTO[] getAllAncestors(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getAllAncestors", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermDTO[] getAllDescendants(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getAllDescendants", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO[] getDirectAncestors(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getDirectAncestors", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO[] getDirectDescendants(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getDirectDescendants", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void removeLink(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String childId, java.lang.String parentId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{childId, parentId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "removeLink", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static TermUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(id);
    
    return (TermUtilDTO) dto;
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
