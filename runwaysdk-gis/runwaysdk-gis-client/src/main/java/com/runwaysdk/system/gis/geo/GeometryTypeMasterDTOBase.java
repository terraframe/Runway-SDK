package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -537440697)
public abstract class GeometryTypeMasterDTOBase extends com.runwaysdk.system.EnumerationMasterDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeometryTypeMaster";
  private static final long serialVersionUID = -537440697;
  
  protected GeometryTypeMasterDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected GeometryTypeMasterDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO) dto;
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
  
  public static com.runwaysdk.system.gis.geo.GeometryTypeMasterQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.geo.GeometryTypeMasterQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeometryTypeMasterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
