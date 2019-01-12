package com.runwaysdk.localization.exception;

@com.runwaysdk.business.ClassSignature(hash = -1150023722)
public abstract class LocaleNotInstalledExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.localization.exception.LocaleNotInstalledException";
  private static final long serialVersionUID = -1150023722;
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected LocaleNotInstalledExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public LocaleNotInstalledExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String LOCALESTRING = "localeString";
  public static java.lang.String OID = "oid";
  public String getLocaleString()
  {
    return getValue(LOCALESTRING);
  }
  
  public void setLocaleString(String value)
  {
    if(value == null)
    {
      setValue(LOCALESTRING, "");
    }
    else
    {
      setValue(LOCALESTRING, value);
    }
  }
  
  public boolean isLocaleStringWritable()
  {
    return isWritable(LOCALESTRING);
  }
  
  public boolean isLocaleStringReadable()
  {
    return isReadable(LOCALESTRING);
  }
  
  public boolean isLocaleStringModified()
  {
    return isModified(LOCALESTRING);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLocaleStringMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LOCALESTRING).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{localeString}", this.getLocaleString().toString());
    template = template.replace("{oid}", this.getOid().toString());
    
    return template;
  }
  
}
