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
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -254652680)
public abstract class AddressDTOBase extends com.runwaysdk.business.StructDTO
{
  public final static String CLASS = "com.runwaysdk.system.Address";
  private static final long serialVersionUID = -254652680;
  
  protected AddressDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given StructDTO into a new DTO.
  * 
  * @param structDTO The StructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected AddressDTOBase(com.runwaysdk.business.StructDTO structDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(structDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CITY = "city";
  public static java.lang.String ID = "id";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String POSTALCODE = "postalCode";
  public static java.lang.String PRIMARYADDRESS = "primaryAddress";
  public static java.lang.String SECONDARYADDRESS = "secondaryAddress";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String ZIPCODE = "zipCode";
  public String getCity()
  {
    return getValue(CITY);
  }
  
  public void setCity(String value)
  {
    if(value == null)
    {
      setValue(CITY, "");
    }
    else
    {
      setValue(CITY, value);
    }
  }
  
  public boolean isCityWritable()
  {
    return isWritable(CITY);
  }
  
  public boolean isCityReadable()
  {
    return isReadable(CITY);
  }
  
  public boolean isCityModified()
  {
    return isModified(CITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getCityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CITY).getAttributeMdDTO();
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
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.AllPostalCodesDTO> getPostalCode()
  {
    return (java.util.List<com.runwaysdk.system.AllPostalCodesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.AllPostalCodesDTO.CLASS, getEnumNames(POSTALCODE));
  }
  
  public java.util.List<String> getPostalCodeEnumNames()
  {
    return getEnumNames(POSTALCODE);
  }
  
  public void addPostalCode(com.runwaysdk.system.AllPostalCodesDTO enumDTO)
  {
    addEnumItem(POSTALCODE, enumDTO.toString());
  }
  
  public void removePostalCode(com.runwaysdk.system.AllPostalCodesDTO enumDTO)
  {
    removeEnumItem(POSTALCODE, enumDTO.toString());
  }
  
  public void clearPostalCode()
  {
    clearEnum(POSTALCODE);
  }
  
  public boolean isPostalCodeWritable()
  {
    return isWritable(POSTALCODE);
  }
  
  public boolean isPostalCodeReadable()
  {
    return isReadable(POSTALCODE);
  }
  
  public boolean isPostalCodeModified()
  {
    return isModified(POSTALCODE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getPostalCodeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(POSTALCODE).getAttributeMdDTO();
  }
  
  public String getPrimaryAddress()
  {
    return getValue(PRIMARYADDRESS);
  }
  
  public void setPrimaryAddress(String value)
  {
    if(value == null)
    {
      setValue(PRIMARYADDRESS, "");
    }
    else
    {
      setValue(PRIMARYADDRESS, value);
    }
  }
  
  public boolean isPrimaryAddressWritable()
  {
    return isWritable(PRIMARYADDRESS);
  }
  
  public boolean isPrimaryAddressReadable()
  {
    return isReadable(PRIMARYADDRESS);
  }
  
  public boolean isPrimaryAddressModified()
  {
    return isModified(PRIMARYADDRESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPrimaryAddressMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PRIMARYADDRESS).getAttributeMdDTO();
  }
  
  public String getSecondaryAddress()
  {
    return getValue(SECONDARYADDRESS);
  }
  
  public void setSecondaryAddress(String value)
  {
    if(value == null)
    {
      setValue(SECONDARYADDRESS, "");
    }
    else
    {
      setValue(SECONDARYADDRESS, value);
    }
  }
  
  public boolean isSecondaryAddressWritable()
  {
    return isWritable(SECONDARYADDRESS);
  }
  
  public boolean isSecondaryAddressReadable()
  {
    return isReadable(SECONDARYADDRESS);
  }
  
  public boolean isSecondaryAddressModified()
  {
    return isModified(SECONDARYADDRESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSecondaryAddressMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SECONDARYADDRESS).getAttributeMdDTO();
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
  
  public String getZipCode()
  {
    return getValue(ZIPCODE);
  }
  
  public void setZipCode(String value)
  {
    if(value == null)
    {
      setValue(ZIPCODE, "");
    }
    else
    {
      setValue(ZIPCODE, value);
    }
  }
  
  public boolean isZipCodeWritable()
  {
    return isWritable(ZIPCODE);
  }
  
  public boolean isZipCodeReadable()
  {
    return isReadable(ZIPCODE);
  }
  
  public boolean isZipCodeModified()
  {
    return isModified(ZIPCODE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getZipCodeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ZIPCODE).getAttributeMdDTO();
  }
  
  public static AddressDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (AddressDTO) dto;
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
    getRequest().delete(this.getId());
  }
  
  public static com.runwaysdk.system.AddressQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.AddressQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.AddressDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
}
