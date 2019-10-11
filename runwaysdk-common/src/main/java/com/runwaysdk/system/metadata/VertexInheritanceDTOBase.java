package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 855959183)
public abstract class VertexInheritanceDTOBase extends com.runwaysdk.system.metadata.MetadataRelationshipDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.VertexInheritance";
  private static final long serialVersionUID = 855959183;
  
  public VertexInheritanceDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentOid, java.lang.String childOid)
  {
    super(clientRequest, parentOid, childOid);
    
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.
  * 
  * @param relationshipDTO The RelationshipDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected VertexInheritanceDTOBase(com.runwaysdk.business.RelationshipDTO relationshipDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(relationshipDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MdVertexDTO getParent()
  {
    return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), super.getParentOid());
  }
  
    public com.runwaysdk.system.metadata.MdVertexDTO getChild()
  {
    return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), super.getChildOid());
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.RelationshipDTO dto = (com.runwaysdk.business.RelationshipDTO) clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) dto;
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceQueryDTO parentQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String parentOid)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
    queryDTO.addCondition("parent_oid", "EQ", parentOid);
    return (com.runwaysdk.system.metadata.VertexInheritanceQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public static com.runwaysdk.system.metadata.VertexInheritanceQueryDTO childQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String childOid)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
    queryDTO.addCondition("child_oid", "EQ", childOid);
    return (com.runwaysdk.system.metadata.VertexInheritanceQueryDTO) clientRequest.queryRelationships(queryDTO);
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
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.VertexInheritanceQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
