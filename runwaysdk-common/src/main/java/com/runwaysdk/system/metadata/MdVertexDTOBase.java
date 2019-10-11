package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1429677778)
public abstract class MdVertexDTOBase extends com.runwaysdk.system.metadata.MdGraphClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdVertex";
  private static final long serialVersionUID = -1429677778;
  
  protected MdVertexDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdVertexDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ISABSTRACT = "isAbstract";
  public static java.lang.String SUPERMDVERTEX = "superMdVertex";
  public Boolean getIsAbstract()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISABSTRACT));
  }
  
  public void setIsAbstract(Boolean value)
  {
    if(value == null)
    {
      setValue(ISABSTRACT, "");
    }
    else
    {
      setValue(ISABSTRACT, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsAbstractWritable()
  {
    return isWritable(ISABSTRACT);
  }
  
  public boolean isIsAbstractReadable()
  {
    return isReadable(ISABSTRACT);
  }
  
  public boolean isIsAbstractModified()
  {
    return isModified(ISABSTRACT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsAbstractMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISABSTRACT).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdVertexDTO getSuperMdVertex()
  {
    if(getValue(SUPERMDVERTEX) == null || getValue(SUPERMDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(SUPERMDVERTEX));
    }
  }
  
  public String getSuperMdVertexOid()
  {
    return getValue(SUPERMDVERTEX);
  }
  
  public void setSuperMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDVERTEX, "");
    }
    else
    {
      setValue(SUPERMDVERTEX, value.getOid());
    }
  }
  
  public boolean isSuperMdVertexWritable()
  {
    return isWritable(SUPERMDVERTEX);
  }
  
  public boolean isSuperMdVertexReadable()
  {
    return isReadable(SUPERMDVERTEX);
  }
  
  public boolean isSuperMdVertexModified()
  {
    return isModified(SUPERMDVERTEX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDVERTEX).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO> getAllSubVertexClasses()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO> getAllSubVertexClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO> getAllSubVertexClassesRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO> getAllSubVertexClassesRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.VertexInheritanceDTO addSubVertexClasses(com.runwaysdk.system.metadata.MdVertexDTO child)
  {
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceDTO addSubVertexClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdVertexDTO child)
  {
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public void removeSubVertexClasses(com.runwaysdk.system.metadata.VertexInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSubVertexClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.VertexInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSubVertexClasses()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public static void removeAllSubVertexClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO> getAllSuperVertexClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO> getAllSuperVertexClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdVertexDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO> getAllSuperVertexClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO> getAllSuperVertexClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VertexInheritanceDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.VertexInheritanceDTO addSuperVertexClass(com.runwaysdk.system.metadata.MdVertexDTO parent)
  {
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.VertexInheritanceDTO addSuperVertexClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdVertexDTO parent)
  {
    return (com.runwaysdk.system.metadata.VertexInheritanceDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public void removeSuperVertexClass(com.runwaysdk.system.metadata.VertexInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeSuperVertexClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.VertexInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllSuperVertexClass()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public static void removeAllSuperVertexClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.VertexInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdVertexDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdVertexDTO) dto;
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
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.system.metadata.MdVertexQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdVertexQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdVertexDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdVertexDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdVertexDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdVertexDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdVertexDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdVertexDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdVertexDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
