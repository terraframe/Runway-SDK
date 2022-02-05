package com.runwaysdk.system.metadata;

public class MdAttributeUUIDDTO extends MdAttributeUUIDDTOBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1636795084;
  
  public MdAttributeUUIDDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeUUIDDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
}
