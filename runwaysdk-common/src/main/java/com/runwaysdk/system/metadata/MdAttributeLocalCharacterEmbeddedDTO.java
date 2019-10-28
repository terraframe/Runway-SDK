package com.runwaysdk.system.metadata;

public class MdAttributeLocalCharacterEmbeddedDTO extends MdAttributeLocalCharacterEmbeddedDTOBase
{
  private static final long serialVersionUID = -1263475209;
  
  public MdAttributeLocalCharacterEmbeddedDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeLocalCharacterEmbeddedDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
}
