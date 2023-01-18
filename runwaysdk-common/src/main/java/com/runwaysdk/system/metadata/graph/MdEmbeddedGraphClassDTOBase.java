package com.runwaysdk.system.metadata.graph;

@com.runwaysdk.business.ClassSignature(hash = 1248284673)
public abstract class MdEmbeddedGraphClassDTOBase extends com.runwaysdk.system.metadata.MdGraphClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClass";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1248284673;
  
  protected MdEmbeddedGraphClassDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdEmbeddedGraphClassDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String SUPERMDEMBEDDED = "superMdEmbedded";
  public com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO getSuperMdEmbedded()
  {
    if(getValue(SUPERMDEMBEDDED) == null || getValue(SUPERMDEMBEDDED).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO.get(getRequest(), getValue(SUPERMDEMBEDDED));
    }
  }
  
  public String getSuperMdEmbeddedOid()
  {
    return getValue(SUPERMDEMBEDDED);
  }
  
  public void setSuperMdEmbedded(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDEMBEDDED, "");
    }
    else
    {
      setValue(SUPERMDEMBEDDED, value.getOid());
    }
  }
  
  public boolean isSuperMdEmbeddedWritable()
  {
    return isWritable(SUPERMDEMBEDDED);
  }
  
  public boolean isSuperMdEmbeddedReadable()
  {
    return isReadable(SUPERMDEMBEDDED);
  }
  
  public boolean isSuperMdEmbeddedModified()
  {
    return isModified(SUPERMDEMBEDDED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdEmbeddedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDEMBEDDED).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO> getAllSubEmbeddedClasses()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO> getAllSubEmbeddedClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO> getAllSubEmbeddedClassesRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO> getAllSubEmbeddedClassesRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO addSubEmbeddedClasses(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO child)
  {
    return (com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO addSubEmbeddedClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO child)
  {
    return (com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public void removeSubEmbeddedClasses(com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSubEmbeddedClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSubEmbeddedClasses()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public static void removeAllSubEmbeddedClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO> getAllSuperEmbeddedClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO> getAllSuperEmbeddedClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO> getAllSuperEmbeddedClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO> getAllSuperEmbeddedClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO addSuperEmbeddedClass(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO addSuperEmbeddedClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public void removeSuperEmbeddedClass(com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeSuperEmbeddedClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllSuperEmbeddedClass()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public static void removeAllSuperEmbeddedClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.graph.EmbeddedGraphInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
