/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1336864171)
public abstract class RestoreAppnameExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.RestoreAppnameException";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1336864171;
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected RestoreAppnameExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public RestoreAppnameExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String CURRENTAPPNAME = "currentAppname";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String RESTOREAPPNAME = "restoreAppname";
  public String getCurrentAppname()
  {
    return getValue(CURRENTAPPNAME);
  }
  
  public void setCurrentAppname(String value)
  {
    if(value == null)
    {
      setValue(CURRENTAPPNAME, "");
    }
    else
    {
      setValue(CURRENTAPPNAME, value);
    }
  }
  
  public boolean isCurrentAppnameWritable()
  {
    return isWritable(CURRENTAPPNAME);
  }
  
  public boolean isCurrentAppnameReadable()
  {
    return isReadable(CURRENTAPPNAME);
  }
  
  public boolean isCurrentAppnameModified()
  {
    return isModified(CURRENTAPPNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getCurrentAppnameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CURRENTAPPNAME).getAttributeMdDTO();
  }
  
  public String getRestoreAppname()
  {
    return getValue(RESTOREAPPNAME);
  }
  
  public void setRestoreAppname(String value)
  {
    if(value == null)
    {
      setValue(RESTOREAPPNAME, "");
    }
    else
    {
      setValue(RESTOREAPPNAME, value);
    }
  }
  
  public boolean isRestoreAppnameWritable()
  {
    return isWritable(RESTOREAPPNAME);
  }
  
  public boolean isRestoreAppnameReadable()
  {
    return isReadable(RESTOREAPPNAME);
  }
  
  public boolean isRestoreAppnameModified()
  {
    return isModified(RESTOREAPPNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getRestoreAppnameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(RESTOREAPPNAME).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{currentAppname}", this.getCurrentAppname().toString());
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{restoreAppname}", this.getRestoreAppname().toString());
    
    return template;
  }
  
}
