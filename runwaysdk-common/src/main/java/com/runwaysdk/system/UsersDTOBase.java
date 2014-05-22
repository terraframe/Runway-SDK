/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -2046106320)
public abstract class UsersDTOBase extends com.runwaysdk.system.SingleActorDTO
{
  public final static String CLASS = "com.runwaysdk.system.Users";
  private static final long serialVersionUID = -2046106320;
  
  protected UsersDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected UsersDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String INACTIVE = "inactive";
  public static java.lang.String LOCALE = "locale";
  public static java.lang.String PASSWORD = "password";
  public static java.lang.String SESSIONLIMIT = "sessionLimit";
  public static java.lang.String USERNAME = "username";
  public Boolean getInactive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(INACTIVE));
  }
  
  public void setInactive(Boolean value)
  {
    if(value == null)
    {
      setValue(INACTIVE, "");
    }
    else
    {
      setValue(INACTIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isInactiveWritable()
  {
    return isWritable(INACTIVE);
  }
  
  public boolean isInactiveReadable()
  {
    return isReadable(INACTIVE);
  }
  
  public boolean isInactiveModified()
  {
    return isModified(INACTIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getInactiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(INACTIVE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.UserLocalesDTO> getLocale()
  {
    return (java.util.List<com.runwaysdk.system.metadata.UserLocalesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.UserLocalesDTO.CLASS, getEnumNames(LOCALE));
  }
  
  public java.util.List<String> getLocaleEnumNames()
  {
    return getEnumNames(LOCALE);
  }
  
  public void addLocale(com.runwaysdk.system.metadata.UserLocalesDTO enumDTO)
  {
    addEnumItem(LOCALE, enumDTO.toString());
  }
  
  public void removeLocale(com.runwaysdk.system.metadata.UserLocalesDTO enumDTO)
  {
    removeEnumItem(LOCALE, enumDTO.toString());
  }
  
  public void clearLocale()
  {
    clearEnum(LOCALE);
  }
  
  public boolean isLocaleWritable()
  {
    return isWritable(LOCALE);
  }
  
  public boolean isLocaleReadable()
  {
    return isReadable(LOCALE);
  }
  
  public boolean isLocaleModified()
  {
    return isModified(LOCALE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getLocaleMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(LOCALE).getAttributeMdDTO();
  }
  
  public String getPassword()
  {
    return getValue(PASSWORD);
  }
  
  public boolean passwordEquals(java.lang.String value, boolean alreadyEncrypted)
  {
    return getAttributeHashDTO(PASSWORD).encryptionEquals(value, alreadyEncrypted);
  }
  
  public void setPassword(String value)
  {
    if(value == null)
    {
      setValue(PASSWORD, "");
    }
    else
    {
      setValue(PASSWORD, value);
    }
  }
  
  public boolean isPasswordWritable()
  {
    return isWritable(PASSWORD);
  }
  
  public boolean isPasswordReadable()
  {
    return isReadable(PASSWORD);
  }
  
  public boolean isPasswordModified()
  {
    return isModified(PASSWORD);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO getPasswordMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO) getAttributeDTO(PASSWORD).getAttributeMdDTO();
  }
  
  public Integer getSessionLimit()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(SESSIONLIMIT));
  }
  
  public void setSessionLimit(Integer value)
  {
    if(value == null)
    {
      setValue(SESSIONLIMIT, "");
    }
    else
    {
      setValue(SESSIONLIMIT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isSessionLimitWritable()
  {
    return isWritable(SESSIONLIMIT);
  }
  
  public boolean isSessionLimitReadable()
  {
    return isReadable(SESSIONLIMIT);
  }
  
  public boolean isSessionLimitModified()
  {
    return isModified(SESSIONLIMIT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSessionLimitMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SESSIONLIMIT).getAttributeMdDTO();
  }
  
  public String getUsername()
  {
    return getValue(USERNAME);
  }
  
  public void setUsername(String value)
  {
    if(value == null)
    {
      setValue(USERNAME, "");
    }
    else
    {
      setValue(USERNAME, value);
    }
  }
  
  public boolean isUsernameWritable()
  {
    return isWritable(USERNAME);
  }
  
  public boolean isUsernameReadable()
  {
    return isReadable(USERNAME);
  }
  
  public boolean isUsernameModified()
  {
    return isModified(USERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getUsernameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(USERNAME).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.UsersDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.UsersDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
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
  
  public static com.runwaysdk.system.UsersQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.UsersQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.UsersDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.UsersDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.UsersDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.UsersDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.UsersDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.UsersDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.UsersDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
