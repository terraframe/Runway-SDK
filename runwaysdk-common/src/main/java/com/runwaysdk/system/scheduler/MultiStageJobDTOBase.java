package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = 852949928)
public abstract class MultiStageJobDTOBase extends com.runwaysdk.system.scheduler.ExecutableJobDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.MultiStageJob";
  private static final long serialVersionUID = 852949928;
  
  protected MultiStageJobDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MultiStageJobDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.AbstractJobDTO> getAllStage()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.AbstractJobDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.AbstractJobDTO> getAllStage(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.AbstractJobDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.JobHasStageDTO> getAllStageRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHasStageDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.JobHasStageDTO> getAllStageRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHasStageDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  public com.runwaysdk.system.scheduler.JobHasStageDTO addStage(com.runwaysdk.system.scheduler.AbstractJobDTO child)
  {
    return (com.runwaysdk.system.scheduler.JobHasStageDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.JobHasStageDTO addStage(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.scheduler.AbstractJobDTO child)
  {
    return (com.runwaysdk.system.scheduler.JobHasStageDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  public void removeStage(com.runwaysdk.system.scheduler.JobHasStageDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeStage(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.scheduler.JobHasStageDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllStage()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  public static void removeAllStage(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.scheduler.JobHasStageDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.MultiStageJobDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.scheduler.MultiStageJobDTO) dto;
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
  
  public static com.runwaysdk.system.scheduler.MultiStageJobQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.scheduler.MultiStageJobQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.scheduler.MultiStageJobDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.scheduler.MultiStageJobDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.MultiStageJobDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.MultiStageJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.scheduler.MultiStageJobDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.MultiStageJobDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.MultiStageJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
