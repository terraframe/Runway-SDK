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

@com.runwaysdk.business.ClassSignature(hash = 94944206)
public abstract class InvalidUniversalRemoveLinkExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.InvalidUniversalRemoveLinkException";
  private static final long serialVersionUID = 94944206;
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public InvalidUniversalRemoveLinkExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CHILDUNI = "childUni";
  public static java.lang.String GEOENTITIES = "geoEntities";
  public static java.lang.String ID = "oid";
  public static java.lang.String PARENTUNI = "parentUni";
  public String getChildUni()
  {
    return getValue(CHILDUNI);
  }
  
  public void setChildUni(String value)
  {
    if(value == null)
    {
      setValue(CHILDUNI, "");
    }
    else
    {
      setValue(CHILDUNI, value);
    }
  }
  
  public boolean isChildUniWritable()
  {
    return isWritable(CHILDUNI);
  }
  
  public boolean isChildUniReadable()
  {
    return isReadable(CHILDUNI);
  }
  
  public boolean isChildUniModified()
  {
    return isModified(CHILDUNI);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getChildUniMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CHILDUNI).getAttributeMdDTO();
  }
  
  public String getGeoEntities()
  {
    return getValue(GEOENTITIES);
  }
  
  public void setGeoEntities(String value)
  {
    if(value == null)
    {
      setValue(GEOENTITIES, "");
    }
    else
    {
      setValue(GEOENTITIES, value);
    }
  }
  
  public boolean isGeoEntitiesWritable()
  {
    return isWritable(GEOENTITIES);
  }
  
  public boolean isGeoEntitiesReadable()
  {
    return isReadable(GEOENTITIES);
  }
  
  public boolean isGeoEntitiesModified()
  {
    return isModified(GEOENTITIES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getGeoEntitiesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(GEOENTITIES).getAttributeMdDTO();
  }
  
  public String getParentUni()
  {
    return getValue(PARENTUNI);
  }
  
  public void setParentUni(String value)
  {
    if(value == null)
    {
      setValue(PARENTUNI, "");
    }
    else
    {
      setValue(PARENTUNI, value);
    }
  }
  
  public boolean isParentUniWritable()
  {
    return isWritable(PARENTUNI);
  }
  
  public boolean isParentUniReadable()
  {
    return isReadable(PARENTUNI);
  }
  
  public boolean isParentUniModified()
  {
    return isModified(PARENTUNI);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParentUniMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARENTUNI).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{childUni}", this.getChildUni().toString());
    template = template.replace("{geoEntities}", this.getGeoEntities().toString());
    template = template.replace("{oid}", this.getOid().toString());
    template = template.replace("{parentUni}", this.getParentUni().toString());
    
    return template;
  }
  
}
