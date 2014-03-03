package com.runwaysdk.system.scheduler;

public class JobHistoryHistoryInformationDTO extends JobHistoryHistoryInformationDTOBase
{
  private static final long serialVersionUID = -1937263192;
  
  public JobHistoryHistoryInformationDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected JobHistoryHistoryInformationDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}