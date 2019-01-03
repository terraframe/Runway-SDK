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
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -1735142044)
public abstract class InvalidGeoEntityUniversalExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.InvalidGeoEntityUniversalException";
  private static final long serialVersionUID = -1735142044;
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public InvalidGeoEntityUniversalExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CHILDUNIVERSAL = "childUniversal";
  public static java.lang.String OID = "oid";
  public static java.lang.String PARENTUNIVERSAL = "parentUniversal";
  public String getChildUniversal()
  {
    return getValue(CHILDUNIVERSAL);
  }
  
  public void setChildUniversal(String value)
  {
    if(value == null)
    {
      setValue(CHILDUNIVERSAL, "");
    }
    else
    {
      setValue(CHILDUNIVERSAL, value);
    }
  }
  
  public boolean isChildUniversalWritable()
  {
    return isWritable(CHILDUNIVERSAL);
  }
  
  public boolean isChildUniversalReadable()
  {
    return isReadable(CHILDUNIVERSAL);
  }
  
  public boolean isChildUniversalModified()
  {
    return isModified(CHILDUNIVERSAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getChildUniversalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CHILDUNIVERSAL).getAttributeMdDTO();
  }
  
  public String getParentUniversal()
  {
    return getValue(PARENTUNIVERSAL);
  }
  
  public void setParentUniversal(String value)
  {
    if(value == null)
    {
      setValue(PARENTUNIVERSAL, "");
    }
    else
    {
      setValue(PARENTUNIVERSAL, value);
    }
  }
  
  public boolean isParentUniversalWritable()
  {
    return isWritable(PARENTUNIVERSAL);
  }
  
  public boolean isParentUniversalReadable()
  {
    return isReadable(PARENTUNIVERSAL);
  }
  
  public boolean isParentUniversalModified()
  {
    return isModified(PARENTUNIVERSAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParentUniversalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARENTUNIVERSAL).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{childUniversal}", this.getChildUniversal().toString());
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{parentUniversal}", this.getParentUniversal().toString());
    
    return template;
  }
  
}
