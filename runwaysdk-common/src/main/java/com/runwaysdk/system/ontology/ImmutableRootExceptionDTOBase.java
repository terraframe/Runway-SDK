package com.runwaysdk.system.ontology;

@com.runwaysdk.business.ClassSignature(hash = 1903459229)
public abstract class ImmutableRootExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.ontology.ImmutableRootException";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1903459229;
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ImmutableRootExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ImmutableRootExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OID = "oid";
  public static java.lang.String ROOTNAME = "rootName";
  public String getRootName()
  {
    return getValue(ROOTNAME);
  }
  
  public void setRootName(String value)
  {
    if(value == null)
    {
      setValue(ROOTNAME, "");
    }
    else
    {
      setValue(ROOTNAME, value);
    }
  }
  
  public boolean isRootNameWritable()
  {
    return isWritable(ROOTNAME);
  }
  
  public boolean isRootNameReadable()
  {
    return isReadable(ROOTNAME);
  }
  
  public boolean isRootNameModified()
  {
    return isModified(ROOTNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getRootNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ROOTNAME).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{rootName}", this.getRootName().toString());
    
    return template;
  }
  
}
