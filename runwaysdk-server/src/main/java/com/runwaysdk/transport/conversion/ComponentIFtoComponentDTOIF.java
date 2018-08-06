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
package com.runwaysdk.transport.conversion;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.RunwayException;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.Information;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.business.Problem;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.View;
import com.runwaysdk.business.Warning;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.attributes.AttributeDateDTO;
import com.runwaysdk.transport.attributes.AttributeDateTimeDTO;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeHashDTO;
import com.runwaysdk.transport.attributes.AttributeIndicatorDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeSymmetricDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.attributes.AttributeTimeDTO;
import com.runwaysdk.transport.conversion.business.BusinessToBusinessDTO;
import com.runwaysdk.transport.conversion.business.InformationToInformationDTO;
import com.runwaysdk.transport.conversion.business.LocalStructToStructDTO;
import com.runwaysdk.transport.conversion.business.ProblemToProblemDTO;
import com.runwaysdk.transport.conversion.business.RelationshipToRelationshipDTO;
import com.runwaysdk.transport.conversion.business.SmartExceptionToExceptionDTO;
import com.runwaysdk.transport.conversion.business.StructToStructDTO;
import com.runwaysdk.transport.conversion.business.UtilToUtilDTO;
import com.runwaysdk.transport.conversion.business.ViewToViewDTO;
import com.runwaysdk.transport.conversion.business.WarningToWarningDTO;
import com.runwaysdk.transport.metadata.ServerAttributeFacade;
import com.runwaysdk.transport.metadata.TypeMd;

public abstract class ComponentIFtoComponentDTOIF
{
  /**
   * The source of information to be copied.
   */
  private ComponentIF componentIF;

  /**
   * The sessionId requesting this conversion.
   */
  private String      sessionId;

  /**
   * Flag denoting if this.entity is type-safe.
   */
  private boolean     typeSafe;

  /**
   * True if metadata is included with the DTO, false otherwise.
   */
  private boolean     convertMetaData;

  /**
   * Constructor to use when the ComponentIF parameter is to be converted into
   * an ComponentDTOIF.
   * 
   * @param sessionId
   * @param componentIF
   * @param convertMetaData
   */
  public ComponentIFtoComponentDTOIF(String sessionId, ComponentIF componentIF, boolean convertMetaData)
  {
    this.sessionId = sessionId;

    this.typeSafe = false;
    this.componentIF = componentIF;
    this.convertMetaData = convertMetaData;
  }

  /**
   * Returns the sessionId used by the request that initiated the conversion.
   * 
   * @return
   */

  protected String getSessionId()
  {
    return this.sessionId;
  }

  /**
   * Returns the component that is being converted into a DTO.
   * 
   * @return component that is being converted into a DTO.
   */
  protected ComponentIF getComponentIF()
  {
    return this.componentIF;
  }

  /**
   * Sets the componentIF that will be converted into a DTO.
   * 
   * @param componentIF
   */
  protected void setComponetIF(ComponentIF componentIF)
  {
    this.componentIF = componentIF;
  }

  /**
   * True if the metadata should be included in the DTO, false otherwise.
   * 
   * @return metadata should be included in the DTO, false otherwise.
   */
  protected boolean convertMetaData()
  {
    return this.convertMetaData;
  }

  /**
   * True if the componentIF being converted is type safe, false otherwise.
   * 
   * @return componentIF being converted is type safe, false otherwise.
   */
  protected boolean getTypeSafe()
  {
    return this.typeSafe;
  }

  /**
   * Indicates if the componentIF is type safe or not.
   * 
   * @param typeSafe
   */
  protected void setTypeSafe(boolean typeSafe)
  {
    this.typeSafe = typeSafe;
  }

  /**
   * Creates and populates an ComponentDTO based on the provided ComponentIF
   * when this object was constructed. The created ComponentDTO is returned.
   * 
   * @return
   */
  public ComponentDTOIF populate()
  {
    // write all attributes
    Map<String, AttributeDTO> attributeDTOmap = this.setAttributes();

    boolean typeRead = this.checkReadAccess();

    boolean typeWrite = this.checkWriteAccess();

    ComponentIF input = this.getComponentIF();
    boolean isModified = this.getIsModified();

    ComponentDTOIF componentDTO = factoryMethod(attributeDTOmap, input.isNew(), typeRead, typeWrite, isModified);

    return componentDTO;
  }

  /**
   * Checks if the ComponentIF has been modified (i.e., have any of the
   * attributes changed).
   * 
   * @return
   */
  protected abstract boolean getIsModified();

  /**
   * Checks if an individual attribute has been modified.
   * 
   * @param name
   * @return
   */
  protected abstract boolean getIsModified(String name);

  /**
   * Returns true if the user bound to the session has permission to read this
   * object, false otherwise.
   * 
   * @return false if the user bound to the session has permission to read this
   *         object, false otherwise.
   */
  protected abstract boolean checkReadAccess();

  /**
   * Returns true if the user bound to the session has permission to write to
   * this object, false otherwise.
   * 
   * @return false if the user bound to the session has permission to write to
   *         this object, false otherwise.
   */
  protected abstract boolean checkWriteAccess();

  /**
   * Instantiates the proper ComponentDTOIF class (not type safe)
   * 
   * @param sessionId
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return proper ComponentDTOIF class (not type safe)
   */
  protected abstract ComponentDTOIF factoryMethod(Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable, boolean modified);

  /**
   * Returns all MdAttributes that are defined by the type of the object being
   * converted.
   * 
   * @return MdAttributes that are defined by the type of the object being
   *         converted.
   */
  protected abstract List<? extends MdAttributeDAOIF> getDefinedMdAttributes();

  /**
   * Copies the attributes from the Element to the ElementDTO
   */
  private Map<String, AttributeDTO> setAttributes()
  {
    Map<String, AttributeDTO> attributeDTOmap = new LinkedHashMap<String, AttributeDTO>();

    // add the attributes
    List<? extends MdAttributeDAOIF> definedAttributes = this.getDefinedMdAttributes();
    for (MdAttributeDAOIF mdAttribute : definedAttributes)
    {
      // skip special or non-visible attributes
      if (mdAttribute.getGetterVisibility() != VisibilityModifier.PUBLIC)
        continue;

      AttributeDTO attributeDTO;

      MdAttributeConcreteDAOIF mdAttributeConcreteIF = mdAttribute.getMdAttributeConcrete();

      if (mdAttributeConcreteIF instanceof MdAttributeCharacterDAOIF)
      {
        attributeDTO = getAttributeCharacter(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeEnumerationDAOIF)
      {
        attributeDTO = getAttributeEnumeration(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        attributeDTO = getAttributeMultiReference(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeStructDAOIF)
      {
        attributeDTO = getAttributeStruct(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeSymmetricDAOIF)
      {
        attributeDTO = getAttributeSymmetric(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeHashDAOIF)
      {
        attributeDTO = getAttributeHash(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeBlobDAOIF)
      {
        attributeDTO = getAttributeBlob(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeDecDAOIF)
      {
        attributeDTO = getAttributeDec(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
      {
        attributeDTO = getAttributeNumber(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeTermDAOIF)
      {
        attributeDTO = getAttributeTerm(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeReferenceDAOIF)
      {
        attributeDTO = getAttributeReference(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeDateDAOIF)
      {
        attributeDTO = getAttributeDate(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeTimeDAOIF)
      {
        attributeDTO = getAttributeTime(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeDateTimeDAOIF)
      {
        attributeDTO = getAttributeDateTime(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeBooleanDAOIF)
      {
        attributeDTO = getAttributeBoolean(mdAttribute);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeIndicatorDAOIF)
      {
        attributeDTO = getAttributeIndicator(mdAttribute);
      }
      else
      {
        attributeDTO = getAttribute(mdAttribute);
      }

      attributeDTOmap.put(attributeDTO.getName(), attributeDTO);
    }

    return attributeDTOmap;
  }

  /**
   * Sets an attribute given an mdAttribute.
   * 
   * @param mdAttributeIF
   */
  private AttributeDateDTO getAttributeDate(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    String value = "";
    Object oValue = invokeGetter(mdAttributeIF);

    if (oValue != null && oValue instanceof Date)
    {
      value = MdAttributeDateUtil.getTypeUnsafeValue((Date) oValue);
    }
    else if (oValue == null)
    {
      value = "";
    }
    else
    {
      value = (String) oValue;
    }

    AttributeDateDTO attributeDateDTO = (AttributeDateDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeDateDTO.getAttributeMdDTO());
    }

    return attributeDateDTO;
  }

  /**
   * Sets an attribute given an mdAttribute.
   * 
   * @param mdAttributeIF
   */
  private AttributeTimeDTO getAttributeTime(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    String value = "";
    Object oValue = invokeGetter(mdAttributeIF);

    if (oValue != null && oValue instanceof Date)
    {
      value = MdAttributeTimeUtil.getTypeUnsafeValue((Date) oValue);
    }
    else if (oValue == null)
    {
      value = "";
    }
    else
    {
      value = (String) oValue;
    }

    AttributeTimeDTO attributeTimeDTO = (AttributeTimeDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeTimeDTO.getAttributeMdDTO());
    }

    return attributeTimeDTO;
  }

  /**
   * Sets an attribute given an mdAttribute.
   * 
   * @param mdAttributeIF
   */
  private AttributeDateTimeDTO getAttributeDateTime(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    String value = "";
    Object oValue = invokeGetter(mdAttributeIF);

    if (oValue != null && oValue instanceof Date)
    {
      value = MdAttributeDateTimeUtil.getTypeUnsafeValue((Date) oValue);
    }
    else if (oValue == null)
    {
      value = "";
    }
    else
    {
      value = (String) oValue;
    }

    AttributeDateTimeDTO attributeDateTimeDTO = (AttributeDateTimeDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeDateTimeDTO.getAttributeMdDTO());
    }

    return attributeDateTimeDTO;
  }

  /**
   * Sets an attribute given an mdAttribute.
   * 
   * @param mdAttributeIF
   */
  private AttributeDTO getAttribute(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    Object oValue = invokeGetter(mdAttributeIF);
    Object value = null;
    if (oValue != null)
    {
      value = oValue.toString();
    }

    AttributeDTO attributeDTO = createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeDTO.getAttributeMdDTO());
    }
    return attributeDTO;
  }

  /**
   * Sets an attribute given an mdAttribute.
   * 
   * @param mdAttributeIF
   */
  private AttributeBooleanDTO getAttributeBoolean(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    Object oValue = invokeGetter(mdAttributeIF);
    String value = null;
    if (oValue != null)
    {
      value = oValue.toString();
    }

    AttributeBooleanDTO attributeDTO = (AttributeBooleanDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setBooleanMetadata(mdAttributeIF, attributeDTO.getAttributeMdDTO());
    }
    return attributeDTO;
  }

  /**
   * Sets an attribute given an mdAttribute.
   * 
   * @param mdAttributeIF
   */
  private AttributeIndicatorDTO getAttributeIndicator(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);
    
    Object oValue = invokeGetter(mdAttributeIF);
    String value = null;
    if (oValue != null)
    {
      value = oValue.toString();
    }
    
    AttributeIndicatorDTO attributeDTO = (AttributeIndicatorDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);
    
    if (this.convertMetaData())
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeDTO.getAttributeMdDTO());
    }
    
    return attributeDTO;
  }
  
  private AttributeSymmetricDTO getAttributeSymmetric(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    // symmetric attributes have empty values for security reasons.
    AttributeSymmetricDTO attributeSymmetricDTO = (AttributeSymmetricDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), "", readable, writable);

    // set the encryption method
    if (this.convertMetaData())
    {
      ServerAttributeFacade.setEncryptionMetadata(mdAttributeIF, attributeSymmetricDTO.getAttributeMdDTO());
    }

    return attributeSymmetricDTO;
  }

  private AttributeDecDTO getAttributeDec(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    Object oValue = invokeGetter(mdAttributeIF);
    String value = "";
    if (oValue != null)
    {
      value = oValue.toString();
    }

    AttributeDecDTO attributeDecDTO = (AttributeDecDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setDecMetadata(mdAttributeIF, attributeDecDTO.getAttributeMdDTO());
    }

    return attributeDecDTO;
  }

  private AttributeNumberDTO getAttributeNumber(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    Object oValue = invokeGetter(mdAttributeIF);
    String value = "";
    if (oValue != null)
    {
      value = oValue.toString();
    }

    AttributeNumberDTO attributeNumberDTO = (AttributeNumberDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setNumberMetadata(mdAttributeIF, attributeNumberDTO.getAttributeMdDTO());
    }

    return attributeNumberDTO;
  }

  private AttributeCharacterDTO getAttributeCharacter(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    Object oValue = invokeGetter(mdAttributeIF);
    String value = "";
    if (oValue != null)
    {
      value = oValue.toString();
    }

    AttributeCharacterDTO attributeCharacterDTO = (AttributeCharacterDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setCharacterMetadata(mdAttributeIF, attributeCharacterDTO.getAttributeMdDTO());
    }

    return attributeCharacterDTO;
  }

  private AttributeHashDTO getAttributeHash(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = false; // the read value will always be an false for
    // security reasons
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    // the value will always be an empty string for security reasons
    AttributeHashDTO attributeHashDTO = (AttributeHashDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), "", readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setEncryptionMetadata(mdAttributeIF, attributeHashDTO.getAttributeMdDTO());
    }

    return attributeHashDTO;
  }

  private AttributeBlobDTO getAttributeBlob(MdAttributeDAOIF mdAttributeIF)
  {
    // if this entity is a new instance, set the set_id to an empty value
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    Object oValue = invokeGetter(mdAttributeIF);
    byte[] value = new byte[0];
    if (oValue instanceof String)
    {
      if (oValue != null && !mdAttributeIF.isSystem())
      {
        value = ( (String) oValue ).getBytes();
      }
    }
    else if (oValue != null && !mdAttributeIF.isSystem())
    {
      value = (byte[]) oValue;
    }

    AttributeBlobDTO attributeBlobDTO = (AttributeBlobDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), "", readable, writable);

    // only set the blob value if the attribute is readable
    // otherwise, it will retain the empty value "" (or byte[0]) as set in the
    // line above
    if (readable)
    {
      attributeBlobDTO.setValue(value);
    }

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, attributeBlobDTO.getAttributeMdDTO());
    }

    return attributeBlobDTO;
  }

  private AttributeEnumerationDTO getAttributeEnumeration(MdAttributeDAOIF mdAttributeIF)
  {
    // if this entity is a new instance, set the set_id to an empty value
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    AttributeEnumerationDTO attributeEnumerationDTO;

    if (this.getComponentIF().isNew() || !readable)
    {
      attributeEnumerationDTO = (AttributeEnumerationDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), "", readable, writable);
    }
    else
    {
      attributeEnumerationDTO = (AttributeEnumerationDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), this.getComponentIF().getValue(attributeName), readable, writable);
    }

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setEnumerationMetadata(mdAttributeIF, attributeEnumerationDTO.getAttributeMdDTO());
    }

    // set the enumeration items
    if (readable)
    {
      this.setAttributeEnumerationNames(mdAttributeIF, attributeEnumerationDTO);
    }

    return attributeEnumerationDTO;
  }

  /**
   * Sets enumeration item names on the given
   * <code>AttributeEnumerationDTO</code>
   * 
   * @param mdAttributeIF
   * @param attributeEnumerationDTO
   */
  protected abstract void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO);

  @SuppressWarnings("unchecked")
  private AttributeMultiReferenceDTO getAttributeMultiReference(MdAttributeDAOIF mdAttributeIF)
  {
    // if this entity is a new instance, set the set_id to an empty value
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    AttributeMultiReferenceDTO attributeMultiReferenceDTO;

    if (this.getComponentIF().isNew() || !readable)
    {
      attributeMultiReferenceDTO = (AttributeMultiReferenceDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), "", readable, writable);
    }
    else
    {
      attributeMultiReferenceDTO = (AttributeMultiReferenceDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), this.getComponentIF().getValue(attributeName), readable, writable);
    }

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setMultiReferenceMetadata(mdAttributeIF, attributeMultiReferenceDTO.getAttributeMdDTO());
    }

    Collection<Object> items = (Collection<Object>) this.invokeGetter(mdAttributeIF);

    for (Object item : items)
    {
      if (item instanceof Business)
      {
        attributeMultiReferenceDTO.addItem( ( (Business) item ).getOid());
      }
      else
      {
        attributeMultiReferenceDTO.addItem((String) item);
      }

    }

    return attributeMultiReferenceDTO;
  }

  private AttributeStructDTO getAttributeStruct(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    // add the struct to the EntityDTO
    String value;
    if (readable)
    {
      value = this.getComponentIF().getValue(attributeName);
    }
    else
    {
      value = "";
    }

    AttributeStructDTO attributeStructDTO = (AttributeStructDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    // get the StructDTO to add values directly
    if (this.convertMetaData())
    {
      ServerAttributeFacade.setStructMetadata(mdAttributeIF, attributeStructDTO.getAttributeMdDTO());
    }

    Struct struct = null;

    Object oValue = invokeGetter(mdAttributeIF);

    if (oValue != null)
    {
      if (oValue instanceof StructDAO)
      {
        struct = BusinessFacade.typeUnsafeStruct((StructDAO) oValue);
      }
      else
      {
        struct = (Struct) oValue;
      }
    }

    if (readable && struct != null)
    {
      StructToStructDTO structToStructDTO;

      if (struct instanceof LocalStruct)
      {
        structToStructDTO = new LocalStructToStructDTO(getSessionId(), (LocalStruct) struct, readable, this.convertMetaData);
      }
      else
      {
        structToStructDTO = new StructToStructDTO(getSessionId(), struct, readable, this.convertMetaData);
      }

      StructDTO structDTO = (StructDTO) structToStructDTO.populate();

      attributeStructDTO.setStructDTO(structDTO);
    }
    else if (struct == null)
    {
      attributeStructDTO.setStructDTO(null);
    }
    else
    {
      StructDTO structDTO = new StructDTO(null, struct.getType(), new HashMap<String, AttributeDTO>());

      Locale locale = Session.getCurrentLocale();

      MdStructDAOIF mdStructIF = MdStructDAO.getMdStructDAO(struct.getType());
      structDTO.setMd(new TypeMd(mdStructIF.getDisplayLabel(locale), mdStructIF.getDescription(locale), mdStructIF.getOid(), mdStructIF.isGenerateSource()));
      attributeStructDTO.setStructDTO(structDTO);
    }

    return attributeStructDTO;

  }

  private AttributeReferenceDTO getAttributeReference(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    // DTOs only hold the reference oid, not the object itself. Don't
    // invoke the type-safe getter because we only need the oid.
    String value = this.getComponentIF().getValue(attributeName);

    AttributeReferenceDTO attributeReferenceDTO = (AttributeReferenceDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setReferenceMetadata(mdAttributeIF, attributeReferenceDTO.getAttributeMdDTO());
    }

    return attributeReferenceDTO;
  }

  private AttributeTermDTO getAttributeTerm(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    boolean readable = hasAttributeReadAccess(mdAttributeIF);
    boolean writable = hasAttributeWriteAccess(mdAttributeIF);

    // DTOs only hold the Term oid, not the object itself. Don't
    // invoke the type-safe getter because we only need the oid.
    String value = this.getComponentIF().getValue(attributeName);

    AttributeTermDTO attributeTermDTO = (AttributeTermDTO) createAttribute(attributeName, mdAttributeIF.getMdAttributeConcrete().getType(), value, readable, writable);

    if (this.convertMetaData())
    {
      ServerAttributeFacade.setTermMetadata(mdAttributeIF, attributeTermDTO.getAttributeMdDTO());
    }

    return attributeTermDTO;
  }

  /**
   * Creates the actual attribute on the EntityDTO. Also performs a read check
   * and sets the value to an empty string if read permissions are not enabled.
   * 
   * @param attributeName
   * @param type
   * @param value
   * @param readable
   * @param writable
   * @return AttributeDTO
   */
  private AttributeDTO createAttribute(String attributeName, String type, Object value, boolean readable, boolean writable)
  {
    if (!readable && !attributeName.equals(ComponentInfo.OID))
    {
      value = "";
    }

    boolean isModified = this.getIsModified(attributeName);
    return AttributeDTOFactory.createAttributeDTO(attributeName, type, value, readable, writable, isModified);
  }

  /**
   * Checks permissions on an attribute to see if it can be read or not.
   * 
   * @param sessionId
   * @param mdAttribute
   *          The MdAttributeIF object to check for write permission on.
   * @return true if the attribute can be read, false otherwise.
   */
  protected abstract boolean hasAttributeReadAccess(MdAttributeDAOIF mdAttribute);

  /**
   * Checks permissions on an attribute to see if it can be edited or not.
   * 
   * @param mdAttribute
   *          The MdAttributeIF object to check for write permission on.
   * @return true if attribute can be edited, false otherwise.
   */
  protected abstract boolean hasAttributeWriteAccess(MdAttributeDAOIF mdAttribute);

  /**
   * Invokes the type-safe getter on an object.
   * 
   * @return
   */
  private Object invokeGetter(MdAttributeDAOIF mdAttributeIF)
  {
    Object value = null;
    String getter = null;
    Class<?> clazz = null;

    if (!this.getTypeSafe())
    {
      return this.getComponentIF().getObjectValue(mdAttributeIF.definesAttribute());
    }
    else if (!mdAttributeIF.getGenerateAccessor())
    {
      return this.getComponentIF().getValue(mdAttributeIF.definesAttribute());
    }
    else
    {
      MdTypeDAOIF mdType = MdTypeDAO.getMdTypeDAO(this.getComponentIF().getType());

      if (!mdType.isGenerateSource())
      {
        return this.getComponentIF().getObjectValue(mdAttributeIF.definesAttribute());
      }
    }

    getter = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());
    clazz = LoaderDecorator.load(mdAttributeIF.definedByClass().definesType());

    try
    {
      value = clazz.getMethod(getter).invoke(this.getComponentIF());
    }
    catch (IllegalArgumentException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (SecurityException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (InvocationTargetException e)
    {
      if (e.getTargetException() instanceof RunwayException)
      {
        RunwayException fwEx = (RunwayException) e.getTargetException();
        throw fwEx;
      }
      else if (e.getTargetException() instanceof SmartException)
      {
        throw (SmartException) e.getTargetException();
      }
      else
      {
        throw new ProgrammingErrorException(e);
      }
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }

    return value;
  }

  /**
   * Returns the correct subclass converter to convert an Entity into an
   * EntityDTO
   * 
   * @param sessionId
   * @param entity
   * @param convertMetaData
   * @return
   */
  public static ComponentIFtoComponentDTOIF getConverter(String sessionId, ComponentIF componentIF, boolean convertMetaData)
  {
    if (componentIF instanceof Business)
    {
      return new BusinessToBusinessDTO(sessionId, (Business) componentIF, convertMetaData);
    }
    else if (componentIF instanceof Relationship)
    {
      return new RelationshipToRelationshipDTO(sessionId, (Relationship) componentIF, convertMetaData);
    }
    else if (componentIF instanceof LocalStruct)
    {
      return new LocalStructToStructDTO(sessionId, (LocalStruct) componentIF, convertMetaData);
    }
    else if (componentIF instanceof Struct)
    {
      return new StructToStructDTO(sessionId, (Struct) componentIF, convertMetaData);
    }
    else if (componentIF instanceof SmartException)
    {
      return new SmartExceptionToExceptionDTO(sessionId, (SmartException) componentIF, convertMetaData);
    }
    else if (componentIF instanceof Util)
    {
      return new UtilToUtilDTO(sessionId, (Util) componentIF, convertMetaData);
    }
    else if (componentIF instanceof View)
    {
      return new ViewToViewDTO(sessionId, (View) componentIF, convertMetaData);
    }
    else if (componentIF instanceof SmartException)
    {
      return new SmartExceptionToExceptionDTO(sessionId, (SmartException) componentIF, convertMetaData);
    }
    else if (componentIF instanceof Problem)
    {
      return new ProblemToProblemDTO(sessionId, (Problem) componentIF, convertMetaData);
    }
    else if (componentIF instanceof Warning)
    {
      return new WarningToWarningDTO(sessionId, (Warning) componentIF, convertMetaData);
    }
    else if (componentIF instanceof Information)
    {
      return new InformationToInformationDTO(sessionId, (Information) componentIF, convertMetaData);
    }
    else
    {
      String error = "Cannot convert type to ComponetDTO.";
      throw new ConversionException(error);
    }
  }

}
