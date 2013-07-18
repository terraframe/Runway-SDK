package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -1868365048)
public abstract class TestProblemDTOBase extends com.runwaysdk.business.ProblemDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.jstest.TestProblem";
  private static final long serialVersionUID = -1868365048;
  
  public TestProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  public TestProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF, java.util.Locale locale)
  {
    super(clientRequestIF, locale);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String PROBLEMCHAR = "problemChar";
  public static java.lang.String PROBLEMINT = "problemInt";
  public String getProblemChar()
  {
    return getValue(PROBLEMCHAR);
  }
  
  public void setProblemChar(String value)
  {
    if(value == null)
    {
      setValue(PROBLEMCHAR, "");
    }
    else
    {
      setValue(PROBLEMCHAR, value);
    }
  }
  
  public boolean isProblemCharWritable()
  {
    return isWritable(PROBLEMCHAR);
  }
  
  public boolean isProblemCharReadable()
  {
    return isReadable(PROBLEMCHAR);
  }
  
  public boolean isProblemCharModified()
  {
    return isModified(PROBLEMCHAR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getProblemCharMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PROBLEMCHAR).getAttributeMdDTO();
  }
  
  public Integer getProblemInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(PROBLEMINT));
  }
  
  public void setProblemInt(Integer value)
  {
    if(value == null)
    {
      setValue(PROBLEMINT, "");
    }
    else
    {
      setValue(PROBLEMINT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isProblemIntWritable()
  {
    return isWritable(PROBLEMINT);
  }
  
  public boolean isProblemIntReadable()
  {
    return isReadable(PROBLEMINT);
  }
  
  public boolean isProblemIntModified()
  {
    return isModified(PROBLEMINT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getProblemIntMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(PROBLEMINT).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{problemChar}", this.getProblemChar().toString());
    template = template.replace("{problemInt}", this.getProblemInt().toString());
    
    return template;
  }
  
}
