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
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -2070328385)
public abstract class StateMasterDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.StateMaster";
  private static final long serialVersionUID = -2070328385;
  
  protected StateMasterDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected StateMasterDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ENTRYSTATE = "entryState";
  public static java.lang.String STATENAME = "stateName";
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.AllEntryEnumerationDTO> getEntryState()
  {
    return (java.util.List<com.runwaysdk.system.AllEntryEnumerationDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.AllEntryEnumerationDTO.CLASS, getEnumNames(ENTRYSTATE));
  }
  
  public java.util.List<String> getEntryStateEnumNames()
  {
    return getEnumNames(ENTRYSTATE);
  }
  
  public void addEntryState(com.runwaysdk.system.AllEntryEnumerationDTO enumDTO)
  {
    addEnumItem(ENTRYSTATE, enumDTO.toString());
  }
  
  public void removeEntryState(com.runwaysdk.system.AllEntryEnumerationDTO enumDTO)
  {
    removeEnumItem(ENTRYSTATE, enumDTO.toString());
  }
  
  public void clearEntryState()
  {
    clearEnum(ENTRYSTATE);
  }
  
  public boolean isEntryStateWritable()
  {
    return isWritable(ENTRYSTATE);
  }
  
  public boolean isEntryStateReadable()
  {
    return isReadable(ENTRYSTATE);
  }
  
  public boolean isEntryStateModified()
  {
    return isModified(ENTRYSTATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getEntryStateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(ENTRYSTATE).getAttributeMdDTO();
  }
  
  public String getStateName()
  {
    return getValue(STATENAME);
  }
  
  public void setStateName(String value)
  {
    if(value == null)
    {
      setValue(STATENAME, "");
    }
    else
    {
      setValue(STATENAME, value);
    }
  }
  
  public boolean isStateNameWritable()
  {
    return isWritable(STATENAME);
  }
  
  public boolean isStateNameReadable()
  {
    return isReadable(STATENAME);
  }
  
  public boolean isStateNameModified()
  {
    return isModified(STATENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStateNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STATENAME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.StateMasterDTO> getAllSink()
  {
    return (java.util.List<? extends com.runwaysdk.system.StateMasterDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.StateMasterDTO> getAllSink(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.StateMasterDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.TransitionDTO> getAllSinkRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.TransitionDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.TransitionDTO> getAllSinkRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.TransitionDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public com.runwaysdk.system.TransitionDTO addSink(com.runwaysdk.system.StateMasterDTO child)
  {
    return (com.runwaysdk.system.TransitionDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.TransitionDTO addSink(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.StateMasterDTO child)
  {
    return (com.runwaysdk.system.TransitionDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public void removeSink(com.runwaysdk.system.TransitionDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeSink(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.TransitionDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllSink()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public static void removeAllSink(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.StateMasterDTO> getAllSource()
  {
    return (java.util.List<? extends com.runwaysdk.system.StateMasterDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.StateMasterDTO> getAllSource(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.StateMasterDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.TransitionDTO> getAllSourceRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.TransitionDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.TransitionDTO> getAllSourceRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.TransitionDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public com.runwaysdk.system.TransitionDTO addSource(com.runwaysdk.system.StateMasterDTO parent)
  {
    return (com.runwaysdk.system.TransitionDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.TransitionDTO addSource(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.StateMasterDTO parent)
  {
    return (com.runwaysdk.system.TransitionDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public void removeSource(com.runwaysdk.system.TransitionDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeSource(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.TransitionDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllSource()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public static void removeAllSource(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.TransitionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.StateMasterDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.StateMasterDTO) dto;
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
  
  public static com.runwaysdk.system.StateMasterQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.StateMasterQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.StateMasterDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.StateMasterDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.StateMasterDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.StateMasterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.StateMasterDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.StateMasterDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.StateMasterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
