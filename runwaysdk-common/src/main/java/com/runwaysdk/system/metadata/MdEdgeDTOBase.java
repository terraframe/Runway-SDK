package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -2077678388)
public abstract class MdEdgeDTOBase extends com.runwaysdk.system.metadata.MdGraphClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdEdge";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -2077678388;
  
  protected MdEdgeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdEdgeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CHILDMDVERTEX = "childMdVertex";
  public static java.lang.String PARENTMDVERTEX = "parentMdVertex";
  public static java.lang.String SUPERMDEDGE = "superMdEdge";
  public com.runwaysdk.system.metadata.MdVertexDTO getChildMdVertex()
  {
    if(getValue(CHILDMDVERTEX) == null || getValue(CHILDMDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(CHILDMDVERTEX));
    }
  }
  
  public String getChildMdVertexOid()
  {
    return getValue(CHILDMDVERTEX);
  }
  
  public void setChildMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(CHILDMDVERTEX, "");
    }
    else
    {
      setValue(CHILDMDVERTEX, value.getOid());
    }
  }
  
  public boolean isChildMdVertexWritable()
  {
    return isWritable(CHILDMDVERTEX);
  }
  
  public boolean isChildMdVertexReadable()
  {
    return isReadable(CHILDMDVERTEX);
  }
  
  public boolean isChildMdVertexModified()
  {
    return isModified(CHILDMDVERTEX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getChildMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CHILDMDVERTEX).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdVertexDTO getParentMdVertex()
  {
    if(getValue(PARENTMDVERTEX) == null || getValue(PARENTMDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(PARENTMDVERTEX));
    }
  }
  
  public String getParentMdVertexOid()
  {
    return getValue(PARENTMDVERTEX);
  }
  
  public void setParentMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(PARENTMDVERTEX, "");
    }
    else
    {
      setValue(PARENTMDVERTEX, value.getOid());
    }
  }
  
  public boolean isParentMdVertexWritable()
  {
    return isWritable(PARENTMDVERTEX);
  }
  
  public boolean isParentMdVertexReadable()
  {
    return isReadable(PARENTMDVERTEX);
  }
  
  public boolean isParentMdVertexModified()
  {
    return isModified(PARENTMDVERTEX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getParentMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(PARENTMDVERTEX).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdEdgeDTO getSuperMdEdge()
  {
    if(getValue(SUPERMDEDGE) == null || getValue(SUPERMDEDGE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdEdgeDTO.get(getRequest(), getValue(SUPERMDEDGE));
    }
  }
  
  public String getSuperMdEdgeOid()
  {
    return getValue(SUPERMDEDGE);
  }
  
  public void setSuperMdEdge(com.runwaysdk.system.metadata.MdEdgeDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDEDGE, "");
    }
    else
    {
      setValue(SUPERMDEDGE, value.getOid());
    }
  }
  
  public boolean isSuperMdEdgeWritable()
  {
    return isWritable(SUPERMDEDGE);
  }
  
  public boolean isSuperMdEdgeReadable()
  {
    return isReadable(SUPERMDEDGE);
  }
  
  public boolean isSuperMdEdgeModified()
  {
    return isModified(SUPERMDEDGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdEdgeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDEDGE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO> getAllSubEdgeClasses()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO> getAllSubEdgeClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO> getAllSubEdgeClassesRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO> getAllSubEdgeClassesRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO addSubEdgeClasses(com.runwaysdk.system.metadata.MdEdgeDTO child)
  {
    return (com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO addSubEdgeClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdEdgeDTO child)
  {
    return (com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public void removeSubEdgeClasses(com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSubEdgeClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSubEdgeClasses()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public static void removeAllSubEdgeClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO> getAllSuperEdgeClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO> getAllSuperEdgeClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEdgeDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO> getAllSuperEdgeClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO> getAllSuperEdgeClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO addSuperEdgeClass(com.runwaysdk.system.metadata.MdEdgeDTO parent)
  {
    return (com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO addSuperEdgeClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdEdgeDTO parent)
  {
    return (com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public void removeSuperEdgeClass(com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeSuperEdgeClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllSuperEdgeClass()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public static void removeAllSuperEdgeClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.graph.EdgeInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdEdgeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdEdgeDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdEdgeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdEdgeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdEdgeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEdgeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEdgeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEdgeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEdgeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEdgeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEdgeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
