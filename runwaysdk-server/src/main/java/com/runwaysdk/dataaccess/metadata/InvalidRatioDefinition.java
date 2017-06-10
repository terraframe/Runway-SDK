package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class InvalidRatioDefinition extends AttributeDefinitionException
{
  /**
   * 
   */
  private static final long serialVersionUID = 4369827709633657887L;

  public InvalidRatioDefinition(String devMessage, MdAttributeDAOIF mdAttribute)
  {
    super(devMessage, mdAttribute);
  }

  public InvalidRatioDefinition(String devMessage, Throwable cause, MdAttributeDAOIF mdAttribute)
  {
    super(devMessage, cause, mdAttribute);
  }

  public InvalidRatioDefinition(Throwable cause, MdAttributeDAOIF mdAttribute)
  {
    super(cause, mdAttribute);
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.invalidRatioDefinition(this.getLocale(), this.getMdAttribute());
  }

}
