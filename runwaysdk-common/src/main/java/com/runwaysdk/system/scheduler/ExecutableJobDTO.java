package com.runwaysdk.system.scheduler;

public abstract class ExecutableJobDTO extends ExecutableJobDTOBase implements Job
{
  private static final long serialVersionUID = 1231291252;
  
  public ExecutableJobDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ExecutableJobDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.JobIF#getLocalizedDescription()
   */
  @Override
  public String getLocalizedDescription()
  {
    return this.getDescription().getValue();
  }
  
}
