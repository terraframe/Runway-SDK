package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1151618306)
public abstract class MdAttributeTermDTOBase extends com.runwaysdk.system.metadata.MdAttributeReferenceDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeTerm";
  private static final long serialVersionUID = 1151618306;
  
  protected MdAttributeTermDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeTermDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntityTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO> getAllGeoEntityTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO> getAllGeoEntityTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO addGeoEntityTermAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO addGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public void removeGeoEntityTermAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllGeoEntityTermAttributeRoots()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllGeoEntityTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.GeoEntityTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonymTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO> getAllSynonymTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO> getAllSynonymTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO addSynonymTermAttributeRoots(com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO addSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public void removeSynonymTermAttributeRoots(com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSynonymTermAttributeRoots()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllUniversalTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO> getAllUniversalTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO> getAllUniversalTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO addUniversalTermAttributeRoots(com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO addUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public void removeUniversalTermAttributeRoots(com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllUniversalTermAttributeRoots()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeTermDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeTermQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeTermQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeTermDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeTermDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeTermDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
