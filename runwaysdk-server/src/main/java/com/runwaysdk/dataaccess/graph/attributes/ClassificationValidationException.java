package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.attributes.AttributeException;

public class ClassificationValidationException extends AttributeException
{

  private static final long serialVersionUID = 142554656678968998L;

  public ClassificationValidationException(String devMessage)
  {
    super(devMessage);
  }
  
  public ClassificationValidationException(String devMessage, Throwable cause)
  {
    super(devMessage, cause);
  }
  
  public ClassificationValidationException(Throwable cause)
  {
    super(cause);
  }
  
  @Override
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.attributeClassificationValidationException(this.getLocale());
  }
  
}
