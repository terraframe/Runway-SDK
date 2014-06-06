/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 152586028)
public abstract class TestExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.jstest.TestException";
  private static final long serialVersionUID = 152586028;
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected TestExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public TestExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String EXCHAR = "exChar";
  public static java.lang.String EXINT = "exInt";
  public static java.lang.String ID = "id";
  public String getExChar()
  {
    return getValue(EXCHAR);
  }
  
  public void setExChar(String value)
  {
    if(value == null)
    {
      setValue(EXCHAR, "");
    }
    else
    {
      setValue(EXCHAR, value);
    }
  }
  
  public boolean isExCharWritable()
  {
    return isWritable(EXCHAR);
  }
  
  public boolean isExCharReadable()
  {
    return isReadable(EXCHAR);
  }
  
  public boolean isExCharModified()
  {
    return isModified(EXCHAR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getExCharMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(EXCHAR).getAttributeMdDTO();
  }
  
  public Integer getExInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(EXINT));
  }
  
  public void setExInt(Integer value)
  {
    if(value == null)
    {
      setValue(EXINT, "");
    }
    else
    {
      setValue(EXINT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isExIntWritable()
  {
    return isWritable(EXINT);
  }
  
  public boolean isExIntReadable()
  {
    return isReadable(EXINT);
  }
  
  public boolean isExIntModified()
  {
    return isModified(EXINT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getExIntMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(EXINT).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{exChar}", this.getExChar().toString());
    template = template.replace("{exInt}", this.getExInt().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
