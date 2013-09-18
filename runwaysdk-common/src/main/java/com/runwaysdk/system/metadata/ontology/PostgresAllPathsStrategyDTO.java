package com.runwaysdk.system.metadata.ontology;

public class PostgresAllPathsStrategyDTO extends PostgresAllPathsStrategyDTOBase
{
  private static final long serialVersionUID = 492378796;
  
  public PostgresAllPathsStrategyDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected PostgresAllPathsStrategyDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
}
