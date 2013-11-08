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
package com.runwaysdk.transport.metadata;

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
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;

/**
 * Factory to create an AttributeMdDTO
 */
public class AttributeMdDTOFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * Factor method to create an AttributeMdDTO given the specified attribute
   * type
   * 
   * @param attributeType
   * @return
   */
  public static AttributeMdDTO createAttributeMd(String attributeType)
  {
    AttributeMdDTO attributeMdDTO = null;

    if (attributeType.equals(MdAttributeIntegerInfo.CLASS))
    {
      attributeMdDTO = new AttributeIntegerMdDTO();
    }
    else if (attributeType.equals(MdAttributeLongInfo.CLASS))
    {
      attributeMdDTO = new AttributeLongMdDTO();
    }
    else if (attributeType.equals(MdAttributeFloatInfo.CLASS))
    {
      attributeMdDTO = new AttributeFloatMdDTO();
    }
    else if (attributeType.equals(MdAttributeDoubleInfo.CLASS))
    {
      attributeMdDTO = new AttributeDoubleMdDTO();
    }
    else if (attributeType.equals(MdAttributeDecimalInfo.CLASS))
    {
      attributeMdDTO = new AttributeDecimalMdDTO();
    }
    else if (attributeType.equals(MdAttributeCharacterInfo.CLASS))
    {
      attributeMdDTO = new AttributeCharacterMdDTO();
    }
    else if (attributeType.equals(MdAttributeTextInfo.CLASS))
    {
      attributeMdDTO = new AttributeTextMdDTO();
    }
    else if (attributeType.equals(MdAttributeClobInfo.CLASS))
    {
      attributeMdDTO = new AttributeClobMdDTO();
    }
    else if (attributeType.equals(MdAttributeFileInfo.CLASS))
    {
      attributeMdDTO = new AttributeFileMdDTO();
    }
    else if (attributeType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      attributeMdDTO = new AttributeDateTimeMdDTO();
    }
    else if (attributeType.equals(MdAttributeDateInfo.CLASS))
    {
      attributeMdDTO = new AttributeDateMdDTO();
    }
    else if (attributeType.equals(MdAttributeTimeInfo.CLASS))
    {
      attributeMdDTO = new AttributeTimeMdDTO();
    }
    else if (attributeType.equals(MdAttributeStructInfo.CLASS))
    {
      attributeMdDTO = new AttributeStructMdDTO();
    }
    else if (attributeType.equals(MdAttributeLocalCharacterInfo.CLASS))
    {
      attributeMdDTO = new AttributeLocalCharacterMdDTO();
    }
    else if (attributeType.equals(MdAttributeLocalTextInfo.CLASS))
    {
      attributeMdDTO = new AttributeLocalTextMdDTO();
    }
    else if (attributeType.equals(MdAttributeBlobInfo.CLASS))
    {
      attributeMdDTO = new AttributeBlobMdDTO();
    }
    else if (attributeType.equals(MdAttributeReferenceInfo.CLASS))
    {
      attributeMdDTO = new AttributeReferenceMdDTO();
    }
    else if (attributeType.equals(MdAttributeTermInfo.CLASS))
    {
      attributeMdDTO = new AttributeTermMdDTO();
    }
    else if (attributeType.equals(MdAttributeEnumerationInfo.CLASS))
    {
      attributeMdDTO = new AttributeEnumerationMdDTO();
    }
    else if (attributeType.equals(MdAttributeMultiReferenceInfo.CLASS))
    {
      attributeMdDTO = new AttributeMultiReferenceMdDTO();
    }
    else if (attributeType.equals(MdAttributeHashInfo.CLASS))
    {
      attributeMdDTO = new AttributeHashMdDTO();
    }
    else if (attributeType.equals(MdAttributeSymmetricInfo.CLASS))
    {
      attributeMdDTO = new AttributeSymmetricMdDTO();
    }
    else if (attributeType.equals(MdAttributeBooleanInfo.CLASS))
    {
      attributeMdDTO = new AttributeBooleanMdDTO();
    }

    if (attributeMdDTO == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attributeMdDTO = plugin.createAttributeMdDTO(attributeType);

        if (attributeMdDTO != null)
        {
          break;
        }
      }
    }

    if (attributeMdDTO == null)
    {
      String error = "The attribute type [" + attributeType + "] is not supported as a DTO attribute.";
      CommonExceptionProcessor.processException(ExceptionConstants.AttributeException.getExceptionClass(), error);
    }

    return attributeMdDTO;
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public AttributeMdDTO createAttributeMdDTO(String attributeType);
  }
}
