package com.runwaysdk.dataaccess;

public class DisconnectedEntityExceptionDTO extends DataAccessExceptionDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -4479796292250711876L;

  /**
   * Constructs a new DisconnectedEntityExceptionDTO with the specified
   * localized message from the server.
   * 
   * @param type
   *          of the runway exception.
   * @param localizedMessage
   *          end user error message.
   * @param developerMessage
   *          developer error message.
   */
  public DisconnectedEntityExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }

}
