package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 1204679389)
public abstract class TestInformationDTOBase extends com.runwaysdk.business.InformationDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.jstest.TestInformation";
  private static final long serialVersionUID = 1204679389;
  
  public TestInformationDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String INFOCHAR = "infoChar";
  public static java.lang.String INFOINT = "infoInt";
  public String getInfoChar()
  {
    return getValue(INFOCHAR);
  }
  
  public void setInfoChar(String value)
  {
    if(value == null)
    {
      setValue(INFOCHAR, "");
    }
    else
    {
      setValue(INFOCHAR, value);
    }
  }
  
  public boolean isInfoCharWritable()
  {
    return isWritable(INFOCHAR);
  }
  
  public boolean isInfoCharReadable()
  {
    return isReadable(INFOCHAR);
  }
  
  public boolean isInfoCharModified()
  {
    return isModified(INFOCHAR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getInfoCharMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(INFOCHAR).getAttributeMdDTO();
  }
  
  public Integer getInfoInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(INFOINT));
  }
  
  public void setInfoInt(Integer value)
  {
    if(value == null)
    {
      setValue(INFOINT, "");
    }
    else
    {
      setValue(INFOINT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isInfoIntWritable()
  {
    return isWritable(INFOINT);
  }
  
  public boolean isInfoIntReadable()
  {
    return isReadable(INFOINT);
  }
  
  public boolean isInfoIntModified()
  {
    return isModified(INFOINT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getInfoIntMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(INFOINT).getAttributeMdDTO();
  }
  
}
