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
package com.runwaysdk.dataaccess.attributes.tranzient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.AttributeException;

public class AttributeFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * Returns an Attribute object of the appropriate sub class for the given
   * DataAccess attribute class.
   * 
   * <br/>
   * <b>Precondition:</b> attributeType != null <br/>
   * <b>Precondition:</b> !attributeType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeType is a concrete sub class of
   * Constants.MD_ATTRIBUTE <br/>
   * <b>Precondition:</b> attributeName != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("") <br/>
   * <b>Precondition:</b> definingType != null <br/>
   * <b>Precondition:</b> !definingType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeValue != null <br/>
   * <b>Postcondition:</b> return value may not be null
   * 
   * @param attributeType
   * @param mdAttributeKey
   *          key of the defining metadata.
   * @param attributeName
   * @param definingType
   * @param attributeValue
   * 
   * @return Attribute object of the appropriate sub class for the given
   *         DataAccess attribute type
   */
  public static Attribute createAttribute(String attributeType, String mdAttributeKey, String attributeName, String definingType, Object attributeValue)
  {
    Attribute attribute = null;
    if (attributeType.equals(MdAttributeIntegerInfo.CLASS))
    {
      attribute = new AttributeInteger(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeLongInfo.CLASS))
    {
      attribute = new AttributeLong(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeFloatInfo.CLASS))
    {
      attribute = new AttributeFloat(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDoubleInfo.CLASS))
    {
      attribute = new AttributeDouble(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDecimalInfo.CLASS))
    {
      attribute = new AttributeDecimal(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeCharacterInfo.CLASS))
    {
      attribute = new AttributeCharacter(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeTextInfo.CLASS))
    {
      attribute = new AttributeText(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeClobInfo.CLASS))
    {
      attribute = new AttributeClob(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeFileInfo.CLASS))
    {
      attribute = new AttributeFile(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeBooleanInfo.CLASS))
    {
      attribute = new AttributeBoolean(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeTimeInfo.CLASS))
    {
      attribute = new AttributeTime(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDateInfo.CLASS))
    {
      attribute = new AttributeDate(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      attribute = new AttributeDateTime(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeReferenceInfo.CLASS))
    {
      attribute = new AttributeReference(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeTermInfo.CLASS))
    {
      attribute = new AttributeTerm(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeEnumerationInfo.CLASS))
    {
      attribute = new AttributeEnumeration(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeMultiReferenceInfo.CLASS))
    {
      attribute = new AttributeMultiReference(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeMultiTermInfo.CLASS))
    {
      attribute = new AttributeMultiTerm(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeHashInfo.CLASS))
    {
      attribute = new AttributeHash(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeSymmetricInfo.CLASS))
    {
      attribute = new AttributeSymmetric(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeBlobInfo.CLASS))
    {
      attribute = new AttributeBlob(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeStructInfo.CLASS))
    {
      if (attributeValue instanceof StructDAO)
      {
        StructDAO structDAO = (StructDAO) attributeValue;
        attribute = new AttributeStruct(attributeName, mdAttributeKey, definingType, structDAO.getId(), structDAO);
      }
      // only call for new instances
      else
      {
        attribute = new AttributeStruct(attributeName, mdAttributeKey, definingType, (String) attributeValue);
      }
    }
    else if (attributeType.equals(MdAttributeLocalCharacterInfo.CLASS))
    {
      if (attributeValue instanceof StructDAO)
      {
        StructDAO structDAO = (StructDAO) attributeValue;
        attribute = new AttributeLocalCharacter(attributeName, mdAttributeKey, definingType, structDAO.getId(), structDAO);
      }
      // only call for new instances
      else
      {
        attribute = new AttributeLocalCharacter(attributeName, mdAttributeKey, definingType, (String) attributeValue);
      }
    }
    else if (attributeType.equals(MdAttributeLocalTextInfo.CLASS))
    {
      if (attributeValue instanceof StructDAO)
      {
        StructDAO structDAO = (StructDAO) attributeValue;
        attribute = new AttributeLocalText(attributeName, mdAttributeKey, definingType, structDAO.getId(), structDAO);
      }
      // only call for new instances
      else
      {
        attribute = new AttributeLocalText(attributeName, mdAttributeKey, definingType, (String) attributeValue);
      }
    }

    if (attribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.createAttribute(attributeType, mdAttributeKey, attributeName, definingType, attributeValue);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "[" + attributeType + "] is not a recognized attribute type.";
      throw new AttributeException(error);
    }

    return attribute;
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Attribute createAttribute(String attributeType, String mdAttributeKey, String attributeName, String definingType, Object attributeValue);
  }
}
