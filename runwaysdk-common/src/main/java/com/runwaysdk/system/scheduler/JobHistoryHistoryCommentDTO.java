package com.runwaysdk.system.scheduler;

public class JobHistoryHistoryCommentDTO extends JobHistoryHistoryCommentDTOBase
{
  private static final long serialVersionUID = 747598683;
  
  public JobHistoryHistoryCommentDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected JobHistoryHistoryCommentDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}
