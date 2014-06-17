package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1632909454)
public abstract class MdAttributeTermDTOBase extends com.runwaysdk.system.metadata.MdAttributeReferenceDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeTerm";
  private static final long serialVersionUID = 1632909454;
  
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
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntityAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntityAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO> getAllGeoEntityAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO> getAllGeoEntityAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO addGeoEntityAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO addGeoEntityAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.GeoEntityDTO child)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  public void removeGeoEntityAttributeRoots(com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeGeoEntityAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllGeoEntityAttributeRoots()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  public static void removeAllGeoEntityAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.gis.geo.GeoEntityAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonymAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO> getAllSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO> getAllSynonymAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO> getAllSynonymAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO addSynonymAttributeRoots(com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO addSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.SynonymDTO child)
  {
    return (com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  public void removeSynonymAttributeRoots(com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllSynonymAttributeRoots()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  public static void removeAllSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.gis.geo.SynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllUniversalAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllUniversalAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO> getAllUniversalAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO> getAllUniversalAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO addUniversalAttributeRoots(com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO addUniversalAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  public void removeUniversalAttributeRoots(com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeUniversalAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllUniversalAttributeRoots()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  public static void removeAllUniversalAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.gis.geo.UniversalAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
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
    getRequest().delete(this.getId());
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeTermQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeTermDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeTermDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeTermDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeTermDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
