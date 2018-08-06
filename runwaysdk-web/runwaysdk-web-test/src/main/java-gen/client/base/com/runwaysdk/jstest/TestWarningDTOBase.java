/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

@com.runwaysdk.business.ClassSignature(hash = -1884108763)
public abstract class TestWarningDTOBase extends com.runwaysdk.business.WarningDTO implements com.runwaysdk.generation.loader.
{
  public final static String CLASS = "com.runwaysdk.jstest.TestWarning";
  private static final long serialVersionUID = -1884108763;
  
  public TestWarningDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "oid";
  public static java.lang.String WARNINGCHAR = "warningChar";
  public static java.lang.String WARNINGINT = "warningInt";
  public String getWarningChar()
  {
    return getValue(WARNINGCHAR);
  }
  
  public void setWarningChar(String value)
  {
    if(value == null)
    {
      setValue(WARNINGCHAR, "");
    }
    else
    {
      setValue(WARNINGCHAR, value);
    }
  }
  
  public boolean isWarningCharWritable()
  {
    return isWritable(WARNINGCHAR);
  }
  
  public boolean isWarningCharReadable()
  {
    return isReadable(WARNINGCHAR);
  }
  
  public boolean isWarningCharModified()
  {
    return isModified(WARNINGCHAR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getWarningCharMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(WARNINGCHAR).getAttributeMdDTO();
  }
  
  public Integer getWarningInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(WARNINGINT));
  }
  
  public void setWarningInt(Integer value)
  {
    if(value == null)
    {
      setValue(WARNINGINT, "");
    }
    else
    {
      setValue(WARNINGINT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isWarningIntWritable()
  {
    return isWritable(WARNINGINT);
  }
  
  public boolean isWarningIntReadable()
  {
    return isReadable(WARNINGINT);
  }
  
  public boolean isWarningIntModified()
  {
    return isModified(WARNINGINT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getWarningIntMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(WARNINGINT).getAttributeMdDTO();
  }
  
}
