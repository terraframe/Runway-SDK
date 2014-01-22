package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -920084916)
public abstract class MdTermRelationshipDTOBase extends com.runwaysdk.system.metadata.MdRelationshipDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTermRelationship";
  private static final long serialVersionUID = -920084916;
  
  protected MdTermRelationshipDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdTermRelationshipDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ASSOCIATIONTYPE = "associationType";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.AssociationTypeDTO> getAssociationType()
  {
    return (java.util.List<com.runwaysdk.system.metadata.AssociationTypeDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.AssociationTypeDTO.CLASS, getEnumNames(ASSOCIATIONTYPE));
  }
  
  public java.util.List<String> getAssociationTypeEnumNames()
  {
    return getEnumNames(ASSOCIATIONTYPE);
  }
  
  public void addAssociationType(com.runwaysdk.system.metadata.AssociationTypeDTO enumDTO)
  {
    addEnumItem(ASSOCIATIONTYPE, enumDTO.toString());
  }
  
  public void removeAssociationType(com.runwaysdk.system.metadata.AssociationTypeDTO enumDTO)
  {
    removeEnumItem(ASSOCIATIONTYPE, enumDTO.toString());
  }
  
  public void clearAssociationType()
  {
    clearEnum(ASSOCIATIONTYPE);
  }
  
  public boolean isAssociationTypeWritable()
  {
    return isWritable(ASSOCIATIONTYPE);
  }
  
  public boolean isAssociationTypeReadable()
  {
    return isReadable(ASSOCIATIONTYPE);
  }
  
  public boolean isAssociationTypeModified()
  {
    return isModified(ASSOCIATIONTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getAssociationTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(ASSOCIATIONTYPE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdTermRelationshipDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdTermRelationshipQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdTermRelationshipDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTermRelationshipDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTermRelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTermRelationshipDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTermRelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
