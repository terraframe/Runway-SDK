package com.runwaysdk.dataaccess.metadata;

public class InvalidRatioDefinitionDTO extends MetadataExceptionDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 842333357679599494L;

  /**
   * Constructs a new {@link InvalidRatioDefinitionDTO} with the specified localized message from the server. 
   * 
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public InvalidRatioDefinitionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }
}
