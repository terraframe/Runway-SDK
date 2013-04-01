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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1291642825)
public abstract class MdAttributeSymmetricDTOBase extends com.runwaysdk.system.metadata.MdAttributeEncryptionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeSymmetric";
  private static final long serialVersionUID = -1291642825;
  
  protected MdAttributeSymmetricDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeSymmetricDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String SECRETKEYSIZE = "secretKeySize";
  public static java.lang.String SYMMETRICMETHOD = "symmetricMethod";
  public Integer getSecretKeySize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(SECRETKEYSIZE));
  }
  
  public void setSecretKeySize(Integer value)
  {
    if(value == null)
    {
      setValue(SECRETKEYSIZE, "");
    }
    else
    {
      setValue(SECRETKEYSIZE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isSecretKeySizeWritable()
  {
    return isWritable(SECRETKEYSIZE);
  }
  
  public boolean isSecretKeySizeReadable()
  {
    return isReadable(SECRETKEYSIZE);
  }
  
  public boolean isSecretKeySizeModified()
  {
    return isModified(SECRETKEYSIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSecretKeySizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SECRETKEYSIZE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.SymmetricOptionsDTO> getSymmetricMethod()
  {
    return (java.util.List<com.runwaysdk.system.metadata.SymmetricOptionsDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.SymmetricOptionsDTO.CLASS, getEnumNames(SYMMETRICMETHOD));
  }
  
  public java.util.List<String> getSymmetricMethodEnumNames()
  {
    return getEnumNames(SYMMETRICMETHOD);
  }
  
  public void addSymmetricMethod(com.runwaysdk.system.metadata.SymmetricOptionsDTO enumDTO)
  {
    addEnumItem(SYMMETRICMETHOD, enumDTO.toString());
  }
  
  public void removeSymmetricMethod(com.runwaysdk.system.metadata.SymmetricOptionsDTO enumDTO)
  {
    removeEnumItem(SYMMETRICMETHOD, enumDTO.toString());
  }
  
  public void clearSymmetricMethod()
  {
    clearEnum(SYMMETRICMETHOD);
  }
  
  public boolean isSymmetricMethodWritable()
  {
    return isWritable(SYMMETRICMETHOD);
  }
  
  public boolean isSymmetricMethodReadable()
  {
    return isReadable(SYMMETRICMETHOD);
  }
  
  public boolean isSymmetricMethodModified()
  {
    return isModified(SYMMETRICMETHOD);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getSymmetricMethodMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(SYMMETRICMETHOD).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeSymmetricDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeSymmetricDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeSymmetricQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeSymmetricQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeSymmetricDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeSymmetricDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeSymmetricDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeSymmetricDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeSymmetricDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeSymmetricDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeSymmetricDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
