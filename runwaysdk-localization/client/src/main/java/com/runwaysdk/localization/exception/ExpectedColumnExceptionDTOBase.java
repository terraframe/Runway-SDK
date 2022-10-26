/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

@com.runwaysdk.business.ClassSignature(hash = 303584801)
public abstract class ExpectedColumnExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.localization.exception.ExpectedColumnException";
  private static final long serialVersionUID = 303584801;
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ExpectedColumnExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ExpectedColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String COLUMNNAME = "columnName";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String SHEETNAME = "sheetName";
  public String getColumnName()
  {
    return getValue(COLUMNNAME);
  }
  
  public void setColumnName(String value)
  {
    if(value == null)
    {
      setValue(COLUMNNAME, "");
    }
    else
    {
      setValue(COLUMNNAME, value);
    }
  }
  
  public boolean isColumnNameWritable()
  {
    return isWritable(COLUMNNAME);
  }
  
  public boolean isColumnNameReadable()
  {
    return isReadable(COLUMNNAME);
  }
  
  public boolean isColumnNameModified()
  {
    return isModified(COLUMNNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getColumnNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(COLUMNNAME).getAttributeMdDTO();
  }
  
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
    
    template = template.replace("{columnName}", this.getColumnName().toString());
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{sheetName}", this.getSheetName().toString());
    
    return template;
  }
  
}
