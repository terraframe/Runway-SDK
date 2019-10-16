package com.runwaysdk;

public class AttributeUUIDParseExceptionDTO extends AttributeParseExceptionDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -573571593056377361L;

  /**
   * Constructs a new <code>AttributeUUIDParseExceptionDTO</code> with the specified localized message from the server. 
   * 
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public AttributeUUIDParseExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type,localizedMessage, developerMessage);
  }
}
