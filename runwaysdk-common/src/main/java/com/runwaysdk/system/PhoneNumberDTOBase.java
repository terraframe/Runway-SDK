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
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = 594342084)
public abstract class PhoneNumberDTOBase extends com.runwaysdk.business.StructDTO
{
  public final static String CLASS = "com.runwaysdk.system.PhoneNumber";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 594342084;
  
  protected PhoneNumberDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given StructDTO into a new DTO.
  * 
  * @param structDTO The StructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected PhoneNumberDTOBase(com.runwaysdk.business.StructDTO structDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(structDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String AREACODE = "areaCode";
  public static java.lang.String EXTENSION = "extension";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String OID = "oid";
  public static java.lang.String PREFIX = "prefix";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String SUFFIX = "suffix";
  public String getAreaCode()
  {
    return getValue(AREACODE);
  }
  
  public void setAreaCode(String value)
  {
    if(value == null)
    {
      setValue(AREACODE, "");
    }
    else
    {
      setValue(AREACODE, value);
    }
  }
  
  public boolean isAreaCodeWritable()
  {
    return isWritable(AREACODE);
  }
  
  public boolean isAreaCodeReadable()
  {
    return isReadable(AREACODE);
  }
  
  public boolean isAreaCodeModified()
  {
    return isModified(AREACODE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAreaCodeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(AREACODE).getAttributeMdDTO();
  }
  
  public String getExtension()
  {
    return getValue(EXTENSION);
  }
  
  public void setExtension(String value)
  {
    if(value == null)
    {
      setValue(EXTENSION, "");
    }
    else
    {
      setValue(EXTENSION, value);
    }
  }
  
  public boolean isExtensionWritable()
  {
    return isWritable(EXTENSION);
  }
  
  public boolean isExtensionReadable()
  {
    return isReadable(EXTENSION);
  }
  
  public boolean isExtensionModified()
  {
    return isModified(EXTENSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getExtensionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(EXTENSION).getAttributeMdDTO();
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }
  
  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }
  
  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(KEYNAME).getAttributeMdDTO();
  }
  
  public String getPrefix()
  {
    return getValue(PREFIX);
  }
  
  public void setPrefix(String value)
  {
    if(value == null)
    {
      setValue(PREFIX, "");
    }
    else
    {
      setValue(PREFIX, value);
    }
  }
  
  public boolean isPrefixWritable()
  {
    return isWritable(PREFIX);
  }
  
  public boolean isPrefixReadable()
  {
    return isReadable(PREFIX);
  }
  
  public boolean isPrefixModified()
  {
    return isModified(PREFIX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPrefixMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PREFIX).getAttributeMdDTO();
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }
  
  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }
  
  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SITEMASTER).getAttributeMdDTO();
  }
  
  public String getSuffix()
  {
    return getValue(SUFFIX);
  }
  
  public void setSuffix(String value)
  {
    if(value == null)
    {
      setValue(SUFFIX, "");
    }
    else
    {
      setValue(SUFFIX, value);
    }
  }
  
  public boolean isSuffixWritable()
  {
    return isWritable(SUFFIX);
  }
  
  public boolean isSuffixReadable()
  {
    return isReadable(SUFFIX);
  }
  
  public boolean isSuffixModified()
  {
    return isModified(SUFFIX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSuffixMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SUFFIX).getAttributeMdDTO();
  }
  
  public static PhoneNumberDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (PhoneNumberDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createStruct(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.system.PhoneNumberQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.PhoneNumberQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.PhoneNumberDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
}
