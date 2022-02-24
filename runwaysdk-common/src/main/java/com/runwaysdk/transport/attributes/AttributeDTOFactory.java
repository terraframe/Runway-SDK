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
package com.runwaysdk.transport.attributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

/**
 * Factor to create an AttributeDTO given the proper information.
 */
public class AttributeDTOFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * Creates an AttributeDTO subclass of the appropriate type.
   * 
   * @param attributeName
   * @param type
   * @param readable
   * @param writable
   * @param modified
   * @return
   */
  public static AttributeDTO createAttributeDTO(String attributeName, String type, Object value, boolean readable, boolean writable, boolean modified)
  {
    AttributeDTO attributeDTO = null;

    if (type.equals(MdAttributeCharacterInfo.CLASS))
    {
      attributeDTO = new AttributeCharacterDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeIntegerInfo.CLASS))
    {
      attributeDTO = new AttributeIntegerDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeLongInfo.CLASS))
    {
      attributeDTO = new AttributeLongDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeFloatInfo.CLASS))
    {
      attributeDTO = new AttributeFloatDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeDoubleInfo.CLASS))
    {
      attributeDTO = new AttributeDoubleDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeFileInfo.CLASS))
    {
      attributeDTO = new AttributeFileDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeDecimalInfo.CLASS))
    {
      attributeDTO = new AttributeDecimalDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeTextInfo.CLASS))
    {
      attributeDTO = new AttributeTextDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeClobInfo.CLASS))
    {
      attributeDTO = new AttributeClobDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeDateTimeInfo.CLASS))
    {
      attributeDTO = new AttributeDateTimeDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeDateInfo.CLASS))
    {
      attributeDTO = new AttributeDateDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeTimeInfo.CLASS))
    {
      attributeDTO = new AttributeTimeDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeStructInfo.CLASS))
    {
      attributeDTO = new AttributeStructDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeLocalCharacterInfo.CLASS))
    {
      attributeDTO = new AttributeLocalCharacterDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeLocalTextInfo.CLASS))
    {
      attributeDTO = new AttributeLocalTextDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeBlobInfo.CLASS))
    {
      attributeDTO = new AttributeBlobDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeReferenceInfo.CLASS))
    {
      attributeDTO = new AttributeReferenceDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeTermInfo.CLASS))
    {
      attributeDTO = new AttributeTermDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeEnumerationInfo.CLASS))
    {
      attributeDTO = new AttributeEnumerationDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeMultiReferenceInfo.CLASS))
    {
      attributeDTO = new AttributeMultiReferenceDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeMultiTermInfo.CLASS))
    {
      attributeDTO = new AttributeMultiTermDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeHashInfo.CLASS))
    {
      attributeDTO = new AttributeHashDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeSymmetricInfo.CLASS))
    {
      attributeDTO = new AttributeSymmetricDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeBooleanInfo.CLASS))
    {
      attributeDTO = new AttributeBooleanDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeUUIDInfo.CLASS))
    {
      attributeDTO = new AttributeUUIDDTO(attributeName, (String) value, readable, writable, modified);
    }
    else if (type.equals(MdAttributeIndicatorInfo.CLASS))
    {
      attributeDTO = new AttributeIndicatorDTO(attributeName, (String) value, readable, writable, modified);
    }

    if (attributeDTO == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attributeDTO = plugin.createAttributeDTO(attributeName, type, value, readable, writable, modified);

        if (attributeDTO != null)
        {
          break;
        }

      }
    }

    if (attributeDTO == null)
    {
      String error = "The attribute type [" + type + "] is not supported as a DTO attribute.";
      CommonExceptionProcessor.processException(ExceptionConstants.AttributeException.getExceptionClass(), error);
    }

    return attributeDTO;
  }

  /**
   * Sets a reference to the given attribute metadata.
   * 
   * @param attributeDTO
   * @param attributeMdDTO
   */
  public static void setAttributeMdDTO(AttributeDTO attributeDTO, AttributeMdDTO attributeMdDTO)
  {
    attributeDTO.setAttributeMdDTO(attributeMdDTO);
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public AttributeDTO createAttributeDTO(String attributeName, String type, Object value, boolean readable, boolean writable, boolean modified);
  }
}
