package com.runwaysdk.system.web;

import com.runwaysdk.AttributeNotificationDTO;

public abstract class AttributeNotificationProblemDTO extends AttributeNotificationProblemDTOBase implements AttributeNotificationDTO
{
  private static final long serialVersionUID = -896676451;
  
  public AttributeNotificationProblemDTO(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  public AttributeNotificationProblemDTO(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
}
