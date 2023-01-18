package com.runwaysdk.system.metadata.graph;

public class EmbeddedGraphInheritanceDTO extends EmbeddedGraphInheritanceDTOBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1645351834;
  
  public EmbeddedGraphInheritanceDTO(com.runwaysdk.constants.ClientRequestIF clientRequest, String parentOid, String childOid)
  {
    super(clientRequest, parentOid, childOid);
    
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.
  * 
  * @param relationshipDTO The RelationshipDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected EmbeddedGraphInheritanceDTO(com.runwaysdk.business.RelationshipDTO relationshipDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(relationshipDTO, clientRequest);
  }
  
}
