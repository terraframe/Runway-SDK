/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.business;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeHashDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeSymmetricDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.metadata.TypeMd;

public class ComponentDTOFacade
{

  /**
   * Instantiates ViewDTO (not type safe)
   * 
   * @param type
   * @clientRequest clientRequest
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return ExceptionDTO (not type safe)
   */
  public static ViewDTO buildViewDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    ViewDTO viewDTO = new ViewDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, viewDTO);
    return viewDTO;
  }

  /**
   * Instantiates ViewDTO
   * 
   * @param clientRequest
   * @param type
   * @return ViewDTO
   */
  public static ViewDTO buildViewDTO(ClientRequestIF clientRequest, String type)
  {
    return new ViewDTO(clientRequest, type);
  }

  /**
   * Instantiates UtilDTO (not type safe)
   * 
   * @param type
   * @clientRequest clientRequest
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return ExceptionDTO (not type safe)
   */
  public static UtilDTO buildUtilDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    UtilDTO utilDTO = new UtilDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, utilDTO);
    return utilDTO;
  }

  /**
   * Instantiates UtilDTO
   * 
   * @param clientRequest
   * @param type
   * @return UtilDTO
   */
  public static UtilDTO buildUtilDTO(ClientRequestIF clientRequest, String type)
  {
    return new UtilDTO(clientRequest, type);
  }

  /**
   * Instantiates SmartExceptionDTO
   * 
   * @param clientRequest
   * @return SmartExceptionDTO
   */
  public static SmartExceptionDTO buildSmartExceptionDTO(ClientRequestIF clientRequest)
  {
    return new SmartExceptionDTO(clientRequest);
  }

  /**
   * Instantiates SmartExceptionDTO
   * 
   * @param excepctionDTO
   * @return SmartExceptionDTO
   */
  public static SmartExceptionDTO buildSmartExceptionDTO(ExceptionDTO exceptionDTO)
  {
    return new SmartExceptionDTO(exceptionDTO);
  }

  /**
   * Instantiates ProblemDTO
   * 
   * @param clientRequest
   * @param type
   * @return ProblemDTO
   */
  public static ProblemDTO buildProblemDTO(ClientRequestIF clientRequest, String type)
  {
    return new ProblemDTO(clientRequest, type);
  }

  /**
   * Instantiates WarningDTO
   * 
   * @param clientRequest
   * @param type
   * @return WarningDTO
   */
  public static WarningDTO buildWarningDTO(ClientRequestIF clientRequest, String type)
  {
    return new WarningDTO(clientRequest, type);
  }

  /**
   * Instantiates InformationDTO
   * 
   * @param clientRequest
   * @param type
   * @return InformationDTO
   */
  public static InformationDTO buildInformationDTO(ClientRequestIF clientRequest, String type)
  {
    return new InformationDTO(clientRequest, type);
  }

  /**
   * Instantiates ExceptionDTO (not type safe)
   * 
   * @clientRequest clientRequest
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return ExceptionDTO (not type safe)
   */
  public static ExceptionDTO buildExceptionDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    ExceptionDTO exceptionDTO = new ExceptionDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, exceptionDTO);
    return exceptionDTO;
  }

  /**
   * Instantiates ProblemDTO (not type safe)
   * 
   * @param type
   * @clientRequest clientRequest
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return ProblemDTO (not type safe)
   */
  public static ProblemDTO buildProblemDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    ProblemDTO problemDTO = new ProblemDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, problemDTO);
    return problemDTO;
  }

  /**
   * Instantiates WarningDTO (not type safe)
   * 
   * @param type
   * @clientRequest clientRequest
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return WarningDTO (not type safe)
   */
  public static WarningDTO buildWarningDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    WarningDTO warningDTO = new WarningDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, warningDTO);
    return warningDTO;
  }

  /**
   * Instantiates InformationDTO (not type safe)
   * 
   * @param type
   * @clientRequest clientRequest
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return InformationDTO (not type safe)
   */
  public static InformationDTO buildInformationDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    InformationDTO informationDTO = new InformationDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, informationDTO);
    return informationDTO;
  }

  /**
   * Instantiates BusinessDTO (not type safe)
   * 
   * @clientRequest clientRequest
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return BusinessDTO (not type safe)
   */
  public static BusinessDTO buildBusinessDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString, boolean userHasCurrentLock)
  {
    BusinessDTO businessDTO = new BusinessDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, businessDTO);
    businessDTO.setLockedByCurrentUser(userHasCurrentLock);
    return businessDTO;
  }

  /**
   * Instantiates ValueObjectDTO
   * 
   * @clientRequest clientRequest
   * @param type
   * @param attributeMap
   * @param readable
   * @return ValueObjectDTO
   */
  public static ValueObjectDTO buildValueObjectDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean readable)
  {
    ValueObjectDTO valueObjectDTO = new ValueObjectDTO(clientRequest, type, attributeMap);
    setCommonProperties(readable, "", valueObjectDTO);
    return valueObjectDTO;
  }

  /**
   * Instantiates BusinessDTO
   * 
   * @param clientRequest
   * @param type
   * @return BusinessDTO
   */
  public static BusinessDTO buildBusinessDTO(ClientRequestIF clientRequest, String type)
  {
    return new BusinessDTO(clientRequest, type);
  }

  /**
   * Instantiates RelationshipDTO (not type safe)
   * 
   * @clientRequest clientRequest
   * @param type
   * @param attributeMap
   * @param parentId
   * @param childId
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param userHasCurrentLock
   * @return RelationshipDTO (not type safe)
   */
  public static RelationshipDTO buildRelationshipDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, String parentId, String childId, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString, boolean userHasCurrentLock)
  {
    RelationshipDTO relationshipDTO = new RelationshipDTO(clientRequest, type, attributeMap, parentId, childId);
    setCommonProperties(newInstance, readable, writable, modified, toString, relationshipDTO);
    relationshipDTO.setLockedByCurrentUser(userHasCurrentLock);
    return relationshipDTO;
  }

  /**
   * Instantiates RelationshipDTO (not type safe)
   * 
   * @param clientRequest
   * @param type
   * @param parentId
   * @param childId
   * @return RelationshipDTO (not type safe)
   */
  public static RelationshipDTO buildRelationshipDTO(ClientRequestIF clientRequest, String type, String parentId, String childId)
  {
    return new RelationshipDTO(clientRequest, type, parentId, childId);
  }

  /**
   * Instantiates StructDTO (not type safe)
   * 
   * @clientRequest clientRequest
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return StructDTO (not type safe)
   */
  public static StructDTO buildStructDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    StructDTO structDTO = new StructDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, structDTO);
    return structDTO;
  }

  /**
   * Instantiates StructDTO (not type safe)
   * 
   * @clientRequest clientRequest
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return StructDTO (not type safe)
   */
  public static LocalStructDTO buildLocalStructDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified, String toString)
  {
    LocalStructDTO localStructDTO = new LocalStructDTO(clientRequest, type, attributeMap);
    setCommonProperties(newInstance, readable, writable, modified, toString, localStructDTO);
    return localStructDTO;
  }

  /**
   * Instantiates StructDTO (not type safe)
   * 
   * @param clientRequest
   * @param type
   * @return StructDTO (not type safe)
   */
  public static StructDTO buildLocalStructDTO(ClientRequestIF clientRequest, String type)
  {
    return new LocalStructDTO(clientRequest, type);
  }

  /**
   * Instantiates StructDTO (not type safe)
   * 
   * @param clientRequest
   * @param type
   * @param attributeMap
   * @param typeMd
   * @return StructDTO (not type safe)
   */
  public static StructDTO buildStructDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, TypeMd typeMd)
  {
    StructDTO structDTO = new StructDTO(clientRequest, type, attributeMap);
    structDTO.setMd(typeMd);
    return structDTO;
  }

  /**
   * Instantiates StructDTO (not type safe)
   * 
   * @param clientRequest
   * @param type
   * @return StructDTO (not type safe)
   */
  public static StructDTO buildStructDTO(ClientRequestIF clientRequest, String type)
  {
    return new StructDTO(clientRequest, type);
  }

  /**
   * 
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param toString
   * @param entityDTO
   */
  private static void setCommonProperties(boolean newInstance, boolean readable, boolean writable, boolean modified, String toString, MutableDTO mutableDTO)
  {
    mutableDTO.setNewInstance(newInstance);
    mutableDTO.setReadable(readable);
    mutableDTO.setWritable(writable);
    mutableDTO.setModified(modified);
    mutableDTO.setToString(toString);
  }

  /**
   * 
   * @param readable
   * @param toString
   * @param valueObjectDTO
   */
  private static void setCommonProperties(boolean readable, String toString, ValueObjectDTO valueObjectDTO)
  {
    valueObjectDTO.setReadable(readable);
    valueObjectDTO.setToString(toString);
  }

  public static AttributeDTO getAttributeDTO(ComponentDTOIF componentDTOIF, String attributeName)
  {
    if (componentDTOIF instanceof ComponentDTO)
    {
      return ( (ComponentDTO) componentDTOIF ).getAttributeDTO(attributeName);
    }
    else if (componentDTOIF instanceof SmartExceptionDTO)
    {
      return ( (SmartExceptionDTO) componentDTOIF ).getAttributeDTO(attributeName);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a HashDTO representing the attribute with the specified name.
   * 
   * @param componentDTO
   * @param hashName
   * @return
   */
  public static AttributeHashDTO getAttributeHashDTO(ComponentDTO componentDTO, String hashName)
  {
    return componentDTO.getAttributeHashDTO(hashName);
  }

  /**
   * Returns an EnumerationDTO representing the attribute with the specified
   * name.
   * 
   * @param componentDTO
   * @param enumName
   * @return
   */
  public static AttributeEnumerationDTO getAttributeEnumerationDTO(ComponentDTO componentDTO, String enumName)
  {
    return componentDTO.getAttributeEnumerationDTO(enumName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeNumberDTO getAttributeNumberDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeNumberDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeBooleanDTO getAttributeBooleanDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeBooleanDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeDecDTO getAttributeDecDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeDecDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeSymmetricDTO getAttributeSymmetricDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeSymmetricDTO(attributeName);
  }

  /**
   * 
   * @param relationshipDTO
   * @param parentId
   * @param childId
   */
  public static void setParentAndChildOnRelationshipDTO(RelationshipDTO relationshipDTO, String parentId, String childId)
  {
    relationshipDTO.setParentId(parentId);
    relationshipDTO.setChildId(childId);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeDTO getAttributeDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeCharacterDTO getAttributeCharacterDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeCharacterDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeStructDTO getAttributeStructDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeStructDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeReferenceDTO getAttributeReferenceDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeReferenceDTO(attributeName);
  }

  /**
   * 
   * @param componentDTO
   * @param attributeName
   * @return
   */
  public static AttributeTermDTO getAttributeTermDTO(ComponentDTO componentDTO, String attributeName)
  {
    return componentDTO.getAttributeTermDTO(attributeName);
  }

  /**
   * Builds a ClassQueryDTO.
   * 
   * @param type
   * @return ClassQueryDTO.
   */
  public static ClassQueryDTO instantiateTypeSafeQueryDTO(String type)
  {
    String queryDTOtype = type + TypeGeneratorInfo.QUERY_DTO_SUFFIX;
    ClassQueryDTO typeSafeQueryDTO = null;

    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(queryDTOtype);
      Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
      constructor.setAccessible(true);

      typeSafeQueryDTO = (ClassQueryDTO) constructor.newInstance(type);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage());
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage());
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage());
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage());
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage());
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage());
    }

    return typeSafeQueryDTO;
  }

  /**
   * Builds a BusinessQueryDTO.
   * 
   * @param type
   * @return BusinessQueryDTO.
   */
  public static BusinessQueryDTO buildBusinessQueryDTO(String type)
  {
    return new BusinessQueryDTO(type);
  }

  /**
   * Builds a RelationshipQueryDTO.
   * 
   * @param type
   * @return RelationshipQueryDTO.
   */
  public static RelationshipQueryDTO buildRelationshipQueryDTO(String type)
  {
    return new RelationshipQueryDTO(type);
  }

  /**
   * Builds a StructQueryDTO.
   * 
   * @param type
   * @return StructQueryDTO.
   */
  public static StructQueryDTO buildStructQueryDTO(String type)
  {
    return new StructQueryDTO(type);
  }

  /**
   * Builds a ViewQueryDTO.
   * 
   * @param type
   * @return ViewQueryDTO.
   */
  public static ViewQueryDTO buildViewQueryDTO(String type)
  {
    return new ViewQueryDTO(type);
  }

  /**
   * Adds an item to the result set.
   * 
   * @param componentDTOIF
   */
  public static void addResultItemToQueryDTO(ComponentQueryDTO componentQueryDTO, ComponentDTOIF componentDTOIF)
  {
    componentQueryDTO.addResultItem(componentDTOIF);
  }

  /**
   * Adds a result set to this QueryDTO. The specified result set will overwrite
   * the previous result set stored in this QueryDTO
   * 
   * @param resultSet
   */
  public static void addResultSetToQueryDTO(ComponentQueryDTO componentQueryDTO, List<? extends ComponentDTOIF> resultSet)
  {
    componentQueryDTO.setResultSet(resultSet);
  }

  /**
   * Adds an attribute to this QueryDTO that the query type defines.
   * 
   * @param name
   * @param name
   * @return
   */
  public static void addAttributeQueryDTO(ComponentQueryDTO componentQueryDTO, AttributeDTO attributeDTO)
  {
    componentQueryDTO.addAttribute(attributeDTO);
  }

  /**
   * Sets the attributeDTOs on the given ComponentDTO to reference the metadata
   * on the given attributes.
   * 
   * @param componentDTO
   * @param foreignAttributeDTOMap
   */
  public static void setDefinedAttributeMetadata(ComponentDTO componentDTO, ComponentQueryDTO componentQueryDTO)
  {
    componentDTO.setDefinedAttributeMetadata(componentQueryDTO.getDefinedAttributes());
  }

  /**
   * Creates a {@link SmartExceptionDTO} from the given {@link ExceptionDTO}.
   * 
   * @param exceptionDTO
   */
  public static SmartExceptionDTO newSmartExceptionDTO(ExceptionDTO exceptionDTO)
  {
    return new SmartExceptionDTO(exceptionDTO);
  }

  /**
   * Creates a map with the key being the id of the MdAttribute that defines the
   * value, the AttributeDTO.
   * 
   * @param dto
   * @return
   */
  public static Map<String, AttributeDTO> mapMdAttributeIdToAttributeDTOs(ComponentDTOIF dto)
  {
    Map<String, AttributeDTO> attributes = new HashMap<String, AttributeDTO>();

    for (String name : dto.getAttributeNames())
    {
      AttributeDTO attr = getAttributeDTO(dto, name);
      attributes.put(attr.getAttributeMdDTO().getId(), attr);
    }

    return attributes;
  }
}
