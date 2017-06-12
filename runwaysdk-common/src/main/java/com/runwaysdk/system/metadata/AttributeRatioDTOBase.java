package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -2099598522)
public abstract class AttributeRatioDTOBase extends com.runwaysdk.system.metadata.MetadataRelationshipDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.AttributeRatio";
  private static final long serialVersionUID = -2099598522;
  
  public AttributeRatioDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentId, java.lang.String childId)
  {
    super(clientRequest, parentId, childId);
    
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.
  * 
  * @param relationshipDTO The RelationshipDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected AttributeRatioDTOBase(com.runwaysdk.business.RelationshipDTO relationshipDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(relationshipDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MdAttributeRatioDTO getParent()
  {
    return com.runwaysdk.system.metadata.MdAttributeRatioDTO.get(getRequest(), super.getParentId());
  }
  
    public com.runwaysdk.system.RatioDTO getChild()
  {
    return com.runwaysdk.system.RatioDTO.get(getRequest(), super.getChildId());
  }
  
  public static com.runwaysdk.system.metadata.AttributeRatioDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.RelationshipDTO dto = (com.runwaysdk.business.RelationshipDTO) clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) dto;
  }
  
  public static com.runwaysdk.system.metadata.AttributeRatioQueryDTO parentQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String parentId)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
    queryDTO.addCondition("parent_id", "EQ", parentId);
    return (com.runwaysdk.system.metadata.AttributeRatioQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public static com.runwaysdk.system.metadata.AttributeRatioQueryDTO childQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String childId)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
    queryDTO.addCondition("child_id", "EQ", childId);
    return (com.runwaysdk.system.metadata.AttributeRatioQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createRelationship(this);
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
  
  public static com.runwaysdk.system.metadata.AttributeRatioQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.AttributeRatioQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.AttributeRatioDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.AttributeRatioDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
