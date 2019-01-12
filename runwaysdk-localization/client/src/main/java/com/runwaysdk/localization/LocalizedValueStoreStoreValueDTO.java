package com.runwaysdk.localization;

public class LocalizedValueStoreStoreValueDTO extends LocalizedValueStoreStoreValueDTOBase
{
  private static final long serialVersionUID = 2130376160;
  
  public LocalizedValueStoreStoreValueDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected LocalizedValueStoreStoreValueDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}
