package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 644685400)
public abstract class MdAttributeMultiTermDTOBase extends com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeMultiTerm";
  private static final long serialVersionUID = 644685400;
  
  protected MdAttributeMultiTermDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeMultiTermDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntityMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO> getAllGeoEntityMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO> getAllGeoEntityMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO addGeoEntityMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO addGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeGeoEntityMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllGeoEntityMultiTermAttributeRoots()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllGeoEntityMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.GeoEntityMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonymMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO> getAllSynonymMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO> getAllSynonymMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO addSynonymMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO addSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeSynonymMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSynonymMultiTermAttributeRoots()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllUniversalMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO> getAllUniversalMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO> getAllUniversalMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO addUniversalMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO addUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeUniversalMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllUniversalMultiTermAttributeRoots()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiTermDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeMultiTermDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiTermQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeMultiTermQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiTermDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeMultiTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiTermDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeMultiTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
