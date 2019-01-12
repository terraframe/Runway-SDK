package com.runwaysdk.localization.exception;

@com.runwaysdk.business.ClassSignature(hash = 1319672455)
public abstract class ExpectedSheetExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.localization.exception.ExpectedSheetException";
  private static final long serialVersionUID = 1319672455;
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ExpectedSheetExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ExpectedSheetExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OID = "oid";
  public static java.lang.String SHEETNAME = "sheetName";
  public String getSheetName()
  {
    return getValue(SHEETNAME);
  }
  
  public void setSheetName(String value)
  {
    if(value == null)
    {
      setValue(SHEETNAME, "");
    }
    else
    {
      setValue(SHEETNAME, value);
    }
  }
  
  public boolean isSheetNameWritable()
  {
    return isWritable(SHEETNAME);
  }
  
  public boolean isSheetNameReadable()
  {
    return isReadable(SHEETNAME);
  }
  
  public boolean isSheetNameModified()
  {
    return isModified(SHEETNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getSheetNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(SHEETNAME).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{sheetName}", this.getSheetName().toString());
    
    return template;
  }
  
}
