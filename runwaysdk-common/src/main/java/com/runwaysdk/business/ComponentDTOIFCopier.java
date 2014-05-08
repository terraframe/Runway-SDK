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
package com.runwaysdk.business;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;

public abstract class ComponentDTOIFCopier
{
  /**
   * The source ComponentDTOIF
   */
  protected ComponentDTOIF source;

  /**
   * The destination ComponentDTOIF
   */
  protected ComponentDTOIF dest;

  /**
   * The clientRequest to use for type-safe conversion. This can be null if this.typeSafe == false.
   */
  private ClientRequestIF clientRequest;

  @SuppressWarnings("unused")
  private boolean typeSafeObject;

  private boolean typeSafeAttributes;

  /**
   *
   * @clientRequest clientRequest
   * @param source
   * @param dest
   * @param typeSafeObject
   * @param typeSafeAttributes
   */
  protected ComponentDTOIFCopier(ClientRequestIF clientRequest, ComponentDTOIF source, ComponentDTOIF dest, boolean typeSafeObject, boolean typeSafeAttributes)
  {
    this.clientRequest = clientRequest;
    this.source = source;
    this.dest = dest;
    this.typeSafeObject = typeSafeObject;
    this.typeSafeAttributes = typeSafeAttributes;

    if(typeSafeObject && clientRequest == null)
    {
      String error = "A ClientRequestIF object cannot be null when doing a type-safe conversion.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
    }
  }

  /**
   * Copies the values from the source ComponentDTOIF into the destination ComponentDTOIF
   * and returns the destination ComponentDTOIF;
   *
   * @return
   */
  protected ComponentDTOIF copy()
  {
    Map<String, AttributeDTO> attributeMap = copyAttributes();

    dest.copyProperties(source, attributeMap);

    return dest;
  }


  /**
   * Copies the attributes from the source to the destination.
   */
  protected Map<String, AttributeDTO> copyAttributes()
  {
    Map<String, AttributeDTO> attributeDTOmap = new HashMap<String, AttributeDTO>();

    for(String attribute : source.getAttributeNames())
    {
      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(source, attribute).clone();

      attributeDTOmap.put(attributeDTO.getName(), attributeDTO);

      // type-safe applies to Struct and Enum attributes
      if(attributeDTO instanceof AttributeStructDTO)
      {
        AttributeStructDTO attributeStructDTO = (AttributeStructDTO)attributeDTO;
        StructDTO sourceStructDTO = attributeStructDTO.getStructDTO();

        // create a type-safe StructDTO for the AttributeStructDTO (if applicable)
        if(typeSafeAttributes)
        {
          // copy the values from the old StructDTO into the new, type-safe StructDTO
          // The attributes of the struct should be type safe as well.
          StructDTO safe = (StructDTO)EntityDTOCopier.create( clientRequest, sourceStructDTO, typeSafeAttributes, typeSafeAttributes);

          // set the type-safe struct
          attributeStructDTO.setStructDTO(safe);
        }
        else
        {
          attributeStructDTO.setStructDTO((StructDTO) ConversionFacade.createGenericCopy(sourceStructDTO));
        }
      }
    }

    return attributeDTOmap;
  }

  /**
   * Copies the values from one DTO a new one.
   *
   * @param source
   * @param typeSafeObject true if the struct and enum values should be type-safe. False is they should be generic.
   * @param typeSafeAttributes
   * @return
   */
  public static ComponentDTOIF create(ClientRequestIF clientRequest, ComponentDTOIF source, boolean typeSafeObject, boolean typeSafeAttributes)
  {
    ComponentDTOIFCopier copier = null;
    if(source instanceof BusinessDTO)
    {
      copier = new BusinessDTOCopier(clientRequest, (BusinessDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof RelationshipDTO)
    {
      copier = new RelationshipDTOCopier(clientRequest, (RelationshipDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof LocalStructDTO)
    {
      copier = new LocalStructDTOCopier(clientRequest,  (LocalStructDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof StructDTO)
    {
      copier = new StructDTOCopier(clientRequest,  (StructDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof ViewDTO)
    {
      copier = new ViewDTOCopier(clientRequest,  (ViewDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof UtilDTO)
    {
      copier = new UtilDTOCopier(clientRequest,  (UtilDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof SmartExceptionDTOIF)
    {
      copier = new SmartExceptionDTOCopier(clientRequest,  (SmartExceptionDTOIF) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof ProblemDTO)
    {
      copier = new ProblemDTOCopier(clientRequest,  (ProblemDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof WarningDTO)
    {
      copier = new WarningDTOCopier(clientRequest,  (WarningDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof InformationDTO)
    {
      copier = new InformationDTOCopier(clientRequest,  (InformationDTO) source, typeSafeObject, typeSafeAttributes);
    }
    else
    {
      String error = "Cannot copy DTO because it is of an invalid type: "+source;
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
    }

    return copier.copy();
  }

  /**
   * Copies the values from one DTO to another. The dest ComponentDTOIF is returned (the reference is updated).
   *
   * @param source
   * @param dest
   * @param typeSafeObject true if the struct and enum values should be type-safe. False is they should be generic.
   * @param typeSafeAttributes
   * @return
   */
  public static ComponentDTOIF copy(ClientRequestIF clientRequest, ComponentDTOIF source, ComponentDTOIF dest, boolean typeSafeObject, boolean typeSafeAttributes)
  {
    ComponentDTOIFCopier copier = null;
    if(source instanceof BusinessDTO)
    {
      copier = new BusinessDTOCopier(clientRequest, (BusinessDTO) source, (BusinessDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof RelationshipDTO)
    {
      copier = new RelationshipDTOCopier(clientRequest, (RelationshipDTO) source, (RelationshipDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof StructDTO)
    {
      copier = new StructDTOCopier(clientRequest, (StructDTO) source, (StructDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof UtilDTO)
    {
      copier = new UtilDTOCopier(clientRequest, (UtilDTO) source, (UtilDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof ViewDTO)
    {
      copier = new ViewDTOCopier(clientRequest, (ViewDTO) source, (ViewDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof SmartExceptionDTOIF)
    {
      copier = new SmartExceptionDTOCopier(clientRequest, (SmartExceptionDTOIF) source, (SmartExceptionDTOIF) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof ProblemDTO)
    {
      copier = new ProblemDTOCopier(clientRequest, (ProblemDTO) source, (ProblemDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof WarningDTO)
    {
      copier = new WarningDTOCopier(clientRequest, (WarningDTO) source, (WarningDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else if(source instanceof InformationDTO)
    {
      copier = new InformationDTOCopier(clientRequest, (InformationDTO) source, (InformationDTO) dest, typeSafeObject, typeSafeAttributes);
    }
    else
    {
      String error = "Cannot copy DTO because it is of an invalid type.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      return null;
    }

    return copier.copy();
  }
}
