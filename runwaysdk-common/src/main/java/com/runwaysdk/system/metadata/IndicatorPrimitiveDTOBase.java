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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 332510031)
public abstract class IndicatorPrimitiveDTOBase extends com.runwaysdk.system.metadata.IndicatorElementDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.IndicatorPrimitive";
  private static final long serialVersionUID = 332510031;
  
  protected IndicatorPrimitiveDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected IndicatorPrimitiveDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String INDICATORFUNCTION = "indicatorFunction";
  public static java.lang.String MDATTRIBUTEPRIMITIVE = "mdAttributePrimitive";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.IndicatorAggregateFunctionDTO> getIndicatorFunction()
  {
    return (java.util.List<com.runwaysdk.system.metadata.IndicatorAggregateFunctionDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.IndicatorAggregateFunctionDTO.CLASS, getEnumNames(INDICATORFUNCTION));
  }
  
  public java.util.List<String> getIndicatorFunctionEnumNames()
  {
    return getEnumNames(INDICATORFUNCTION);
  }
  
  public void addIndicatorFunction(com.runwaysdk.system.metadata.IndicatorAggregateFunctionDTO enumDTO)
  {
    addEnumItem(INDICATORFUNCTION, enumDTO.toString());
  }
  
  public void removeIndicatorFunction(com.runwaysdk.system.metadata.IndicatorAggregateFunctionDTO enumDTO)
  {
    removeEnumItem(INDICATORFUNCTION, enumDTO.toString());
  }
  
  public void clearIndicatorFunction()
  {
    clearEnum(INDICATORFUNCTION);
  }
  
  public boolean isIndicatorFunctionWritable()
  {
    return isWritable(INDICATORFUNCTION);
  }
  
  public boolean isIndicatorFunctionReadable()
  {
    return isReadable(INDICATORFUNCTION);
  }
  
  public boolean isIndicatorFunctionModified()
  {
    return isModified(INDICATORFUNCTION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getIndicatorFunctionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(INDICATORFUNCTION).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributePrimitiveDTO getMdAttributePrimitive()
  {
    if(getValue(MDATTRIBUTEPRIMITIVE) == null || getValue(MDATTRIBUTEPRIMITIVE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributePrimitiveDTO.get(getRequest(), getValue(MDATTRIBUTEPRIMITIVE));
    }
  }
  
  public String getMdAttributePrimitiveId()
  {
    return getValue(MDATTRIBUTEPRIMITIVE);
  }
  
  public void setMdAttributePrimitive(com.runwaysdk.system.metadata.MdAttributePrimitiveDTO value)
  {
    if(value == null)
    {
      setValue(MDATTRIBUTEPRIMITIVE, "");
    }
    else
    {
      setValue(MDATTRIBUTEPRIMITIVE, value.getOid());
    }
  }
  
  public boolean isMdAttributePrimitiveWritable()
  {
    return isWritable(MDATTRIBUTEPRIMITIVE);
  }
  
  public boolean isMdAttributePrimitiveReadable()
  {
    return isReadable(MDATTRIBUTEPRIMITIVE);
  }
  
  public boolean isMdAttributePrimitiveModified()
  {
    return isModified(MDATTRIBUTEPRIMITIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdAttributePrimitiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDATTRIBUTEPRIMITIVE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.IndicatorPrimitiveDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.IndicatorPrimitiveDTO) dto;
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
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.system.metadata.IndicatorPrimitiveQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.IndicatorPrimitiveQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.IndicatorPrimitiveDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.IndicatorPrimitiveDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.IndicatorPrimitiveDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.IndicatorPrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.IndicatorPrimitiveDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.IndicatorPrimitiveDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.IndicatorPrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
