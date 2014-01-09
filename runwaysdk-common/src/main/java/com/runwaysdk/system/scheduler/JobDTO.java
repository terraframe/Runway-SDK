package com.runwaysdk.system.scheduler;

public abstract class JobDTO extends JobDTOBase implements JobIF
{
  private static final long serialVersionUID = -661400455;
  
  public JobDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected JobDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.JobIF#getLocalizedDisplayLabel()
   */
  @Override
  public String getLocalizedDisplayLabel()
  {
    return this.getDisplayLabel().getValue();
  }
}
