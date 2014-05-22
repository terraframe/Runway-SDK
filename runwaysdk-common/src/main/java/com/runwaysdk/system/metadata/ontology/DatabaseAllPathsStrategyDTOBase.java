package com.runwaysdk.system.metadata.ontology;

@com.runwaysdk.business.ClassSignature(hash = 1039960871)
public abstract class DatabaseAllPathsStrategyDTOBase extends com.runwaysdk.system.metadata.ontology.OntologyStrategyDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy";
  private static final long serialVersionUID = 1039960871;
  
  protected DatabaseAllPathsStrategyDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DatabaseAllPathsStrategyDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategyDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
