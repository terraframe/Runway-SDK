package com.runwaysdk.business;

@com.runwaysdk.business.ClassSignature(hash = -1488791685)
public abstract class BusinessSystemDTOBase extends com.runwaysdk.business.ElementSystemDTO
{
  public final static String CLASS = "com.runwaysdk.business.Business";
  private static final long serialVersionUID = -1488791685;
  
  protected BusinessSystemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected BusinessSystemDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public void removeParentMetadata(com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllParentMetadata()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public static void removeAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.business.BusinessSystemDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.business.BusinessSystemDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
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
  
  public static com.runwaysdk.business.BusinessQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.business.BusinessQueryDTO) clientRequest.getAllInstances(com.runwaysdk.business.BusinessDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.business.BusinessSystemDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.business.BusinessSystemDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.business.BusinessSystemDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.business.BusinessSystemDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.business.BusinessSystemDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.business.BusinessSystemDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
