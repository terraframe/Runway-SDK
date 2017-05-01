package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 429290203)
public abstract class MdTableDTOBase extends com.runwaysdk.system.metadata.MdClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTable";
  private static final long serialVersionUID = 429290203;
  
  protected MdTableDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdTableDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String TABLENAME = "tableName";
  public String getTableName()
  {
    return getValue(TABLENAME);
  }
  
  public boolean isTableNameWritable()
  {
    return isWritable(TABLENAME);
  }
  
  public boolean isTableNameReadable()
  {
    return isReadable(TABLENAME);
  }
  
  public boolean isTableNameModified()
  {
    return isModified(TABLENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getTableNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(TABLENAME).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdTableDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdTableDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdTableQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdTableQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdTableDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTableDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTableDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTableDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTableDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTableDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTableDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
