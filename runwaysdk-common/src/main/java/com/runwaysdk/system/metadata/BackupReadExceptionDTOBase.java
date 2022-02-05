package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 899248235)
public abstract class BackupReadExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.BackupReadException";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 899248235;
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected BackupReadExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public BackupReadExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String LOCATION = "location";
  public static java.lang.String OID = "oid";
  public String getLocation()
  {
    return getValue(LOCATION);
  }
  
  public void setLocation(String value)
  {
    if(value == null)
    {
      setValue(LOCATION, "");
    }
    else
    {
      setValue(LOCATION, value);
    }
  }
  
  public boolean isLocationWritable()
  {
    return isWritable(LOCATION);
  }
  
  public boolean isLocationReadable()
  {
    return isReadable(LOCATION);
  }
  
  public boolean isLocationModified()
  {
    return isModified(LOCATION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLocationMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LOCATION).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{location}", this.getLocation().toString());
    template = template.replace("{oid}", this.getOid().toString());
    
    return template;
  }
  
}
