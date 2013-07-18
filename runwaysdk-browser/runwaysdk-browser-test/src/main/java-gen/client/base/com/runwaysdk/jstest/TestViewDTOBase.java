package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -425969227)
public abstract class TestViewDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.jstest.TestView";
  private static final long serialVersionUID = -425969227;
  
  protected TestViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String VIEWCHARACTER = "viewCharacter";
  public static java.lang.String VIEWPHONE = "viewPhone";
  public static java.lang.String VIEWREFERENCEOBJECT = "viewReferenceObject";
  public static java.lang.String VIEWSINGLESTATE = "viewSingleState";
  public String getViewCharacter()
  {
    return getValue(VIEWCHARACTER);
  }
  
  public void setViewCharacter(String value)
  {
    if(value == null)
    {
      setValue(VIEWCHARACTER, "");
    }
    else
    {
      setValue(VIEWCHARACTER, value);
    }
  }
  
  public boolean isViewCharacterWritable()
  {
    return isWritable(VIEWCHARACTER);
  }
  
  public boolean isViewCharacterReadable()
  {
    return isReadable(VIEWCHARACTER);
  }
  
  public boolean isViewCharacterModified()
  {
    return isModified(VIEWCHARACTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getViewCharacterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(VIEWCHARACTER).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.PhoneNumberDTO getViewPhone()
  {
    return (com.runwaysdk.system.PhoneNumberDTO) this.getAttributeStructDTO(VIEWPHONE).getStructDTO();
  }
  
  public boolean isViewPhoneWritable()
  {
    return isWritable(VIEWPHONE);
  }
  
  public boolean isViewPhoneReadable()
  {
    return isReadable(VIEWPHONE);
  }
  
  public boolean isViewPhoneModified()
  {
    return isModified(VIEWPHONE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getViewPhoneMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(VIEWPHONE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.jstest.RefClassDTO getViewReferenceObject()
  {
    if(getValue(VIEWREFERENCEOBJECT) == null || getValue(VIEWREFERENCEOBJECT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.jstest.RefClassDTO.get(getRequest(), getValue(VIEWREFERENCEOBJECT));
    }
  }
  
  public String getViewReferenceObjectId()
  {
    return getValue(VIEWREFERENCEOBJECT);
  }
  
  public void setViewReferenceObject(com.runwaysdk.jstest.RefClassDTO value)
  {
    if(value == null)
    {
      setValue(VIEWREFERENCEOBJECT, "");
    }
    else
    {
      setValue(VIEWREFERENCEOBJECT, value.getId());
    }
  }
  
  public boolean isViewReferenceObjectWritable()
  {
    return isWritable(VIEWREFERENCEOBJECT);
  }
  
  public boolean isViewReferenceObjectReadable()
  {
    return isReadable(VIEWREFERENCEOBJECT);
  }
  
  public boolean isViewReferenceObjectModified()
  {
    return isModified(VIEWREFERENCEOBJECT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getViewReferenceObjectMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(VIEWREFERENCEOBJECT).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.jstest.StatesDTO> getViewSingleState()
  {
    return (java.util.List<com.runwaysdk.jstest.StatesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.jstest.StatesDTO.CLASS, getEnumNames(VIEWSINGLESTATE));
  }
  
  public java.util.List<String> getViewSingleStateEnumNames()
  {
    return getEnumNames(VIEWSINGLESTATE);
  }
  
  public void addViewSingleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    addEnumItem(VIEWSINGLESTATE, enumDTO.toString());
  }
  
  public void removeViewSingleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    removeEnumItem(VIEWSINGLESTATE, enumDTO.toString());
  }
  
  public void clearViewSingleState()
  {
    clearEnum(VIEWSINGLESTATE);
  }
  
  public boolean isViewSingleStateWritable()
  {
    return isWritable(VIEWSINGLESTATE);
  }
  
  public boolean isViewSingleStateReadable()
  {
    return isReadable(VIEWSINGLESTATE);
  }
  
  public boolean isViewSingleStateModified()
  {
    return isModified(VIEWSINGLESTATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getViewSingleStateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(VIEWSINGLESTATE).getAttributeMdDTO();
  }
  
  public static final java.lang.Integer doubleAnInt(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Integer num)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer"};
    Object[] _parameters = new Object[]{num};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestViewDTO.CLASS, "doubleAnInt", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final com.runwaysdk.jstest.TestViewDTO returnView(com.runwaysdk.jstest.TestViewDTO input)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.jstest.TestView"};
    Object[] _parameters = new Object[]{input};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestViewDTO.CLASS, "returnView", _declaredTypes);
    return (com.runwaysdk.jstest.TestViewDTO) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final com.runwaysdk.jstest.TestViewDTO returnView(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, com.runwaysdk.jstest.TestViewDTO input)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "com.runwaysdk.jstest.TestView"};
    Object[] _parameters = new Object[]{id, input};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestViewDTO.CLASS, "returnView", _declaredTypes);
    return (com.runwaysdk.jstest.TestViewDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static TestViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (TestViewDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
}
