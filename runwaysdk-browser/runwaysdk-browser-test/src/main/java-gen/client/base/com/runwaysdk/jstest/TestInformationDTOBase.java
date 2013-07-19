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
