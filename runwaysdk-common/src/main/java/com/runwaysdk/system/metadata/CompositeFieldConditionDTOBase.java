/**
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
 */
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1253937541)
public abstract class CompositeFieldConditionDTOBase extends com.runwaysdk.system.metadata.FieldConditionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.CompositeFieldCondition";
  private static final long serialVersionUID = -1253937541;
  
  protected CompositeFieldConditionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected CompositeFieldConditionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String FIRSTCONDITION = "firstCondition";
  public static java.lang.String SECONDCONDITION = "secondCondition";
  public com.runwaysdk.system.metadata.FieldConditionDTO getFirstCondition()
  {
    if(getValue(FIRSTCONDITION) == null || getValue(FIRSTCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.FieldConditionDTO.get(getRequest(), getValue(FIRSTCONDITION));
    }
  }
  
  public String getFirstConditionId()
  {
    return getValue(FIRSTCONDITION);
  }
  
  public void setFirstCondition(com.runwaysdk.system.metadata.FieldConditionDTO value)
  {
    if(value == null)
    {
      setValue(FIRSTCONDITION, "");
    }
    else
    {
      setValue(FIRSTCONDITION, value.getId());
    }
  }
  
  public boolean isFirstConditionWritable()
  {
    return isWritable(FIRSTCONDITION);
  }
  
  public boolean isFirstConditionReadable()
  {
    return isReadable(FIRSTCONDITION);
  }
  
  public boolean isFirstConditionModified()
  {
    return isModified(FIRSTCONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getFirstConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(FIRSTCONDITION).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.FieldConditionDTO getSecondCondition()
  {
    if(getValue(SECONDCONDITION) == null || getValue(SECONDCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.FieldConditionDTO.get(getRequest(), getValue(SECONDCONDITION));
    }
  }
  
  public String getSecondConditionId()
  {
    return getValue(SECONDCONDITION);
  }
  
  public void setSecondCondition(com.runwaysdk.system.metadata.FieldConditionDTO value)
  {
    if(value == null)
    {
      setValue(SECONDCONDITION, "");
    }
    else
    {
      setValue(SECONDCONDITION, value.getId());
    }
  }
  
  public boolean isSecondConditionWritable()
  {
    return isWritable(SECONDCONDITION);
  }
  
  public boolean isSecondConditionReadable()
  {
    return isReadable(SECONDCONDITION);
  }
  
  public boolean isSecondConditionModified()
  {
    return isModified(SECONDCONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSecondConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SECONDCONDITION).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.CompositeFieldConditionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.CompositeFieldConditionDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.CompositeFieldConditionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.CompositeFieldConditionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.CompositeFieldConditionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.CompositeFieldConditionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.CompositeFieldConditionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.CompositeFieldConditionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.CompositeFieldConditionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.CompositeFieldConditionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.CompositeFieldConditionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
