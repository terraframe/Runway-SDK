package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -400773134)
public abstract class CorruptBackupExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.CorruptBackupException";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -400773134;
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected CorruptBackupExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public CorruptBackupExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String BACKUPNAME = "backupName";
  public static java.lang.String OID = "oid";
  public String getBackupName()
  {
    return getValue(BACKUPNAME);
  }
  
  public void setBackupName(String value)
  {
    if(value == null)
    {
      setValue(BACKUPNAME, "");
    }
    else
    {
      setValue(BACKUPNAME, value);
    }
  }
  
  public boolean isBackupNameWritable()
  {
    return isWritable(BACKUPNAME);
  }
  
  public boolean isBackupNameReadable()
  {
    return isReadable(BACKUPNAME);
  }
  
  public boolean isBackupNameModified()
  {
    return isModified(BACKUPNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getBackupNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(BACKUPNAME).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{backupName}", this.getBackupName().toString());
    template = template.replace("{oid}", this.getOid().toString());
    
    return template;
  }
  
}
