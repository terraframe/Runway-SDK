/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.localization.exception;

@com.runwaysdk.business.ClassSignature(hash = -1850561792)
public abstract class LocalizedRowIgnoredWarningDTOBase extends com.runwaysdk.business.WarningDTO
{
  public final static String CLASS = "com.runwaysdk.localization.exception.LocalizedRowIgnoredWarning";
  private static final long serialVersionUID = -1850561792;
  
  public LocalizedRowIgnoredWarningDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OID = "oid";
  public static java.lang.String ROW = "row";
  public static java.lang.String SHEET = "sheet";
  public Integer getRow()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(ROW));
  }
  
  public void setRow(Integer value)
  {
    if(value == null)
    {
      setValue(ROW, "");
    }
    else
    {
      setValue(ROW, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isRowWritable()
  {
    return isWritable(ROW);
  }
  
  public boolean isRowReadable()
  {
    return isReadable(ROW);
  }
  
  public boolean isRowModified()
  {
    return isModified(ROW);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getRowMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(ROW).getAttributeMdDTO();
  }
  
  public String getSheet()
  {
    return getValue(SHEET);
  }
  
  public void setSheet(String value)
  {
    if(value == null)
    {
      setValue(SHEET, "");
    }
    else
    {
      setValue(SHEET, value);
    }
  }
  
  public boolean isSheetWritable()
  {
    return isWritable(SHEET);
  }
  
  public boolean isSheetReadable()
  {
    return isReadable(SHEET);
  }
  
  public boolean isSheetModified()
  {
    return isModified(SHEET);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSheetMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SHEET).getAttributeMdDTO();
  }
  
}
