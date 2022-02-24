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
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
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
   * query result attribute class.
   * 
   * <br/>
   * <b>Precondition:</b> mdAttributeIF != null <br/>
   * <b>Precondition:</b> !mdAttributeIF.trim().equals("") <br/>
   * <b>Precondition:</b> attributeName != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("") <br/>
   * <b>Precondition:</b> definingType != null <br/>
   * <b>Precondition:</b> !definingType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeValue != null <br/>
   * <b>Postcondition:</b> return value may not be null
   * 
   * @param useCache
   *          true if when this is called the cache has been initialized.
   * @param entityMdAttributeIFset
   *          all MdAttributes that were involved in constructing this
   *          attribute.
   * @return Attribute object of the appropriate sub class for the given
   *         DataAccess attribute type
   */
  public static Attribute createAttribute(MdAttributeConcreteDAOIF mdAttributeIF, String attributeName, String definingType, Object attributeValue, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {

    Attribute attribute = null;
    if (mdAttributeIF instanceof MdAttributeCharacterDAOIF)
    {
      attribute = new AttributeCharacter(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeDecimalDAOIF)
    {
      attribute = new AttributeDecimal(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeTextDAOIF)
    {
      attribute = new AttributeText(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeClobDAOIF)
    {
      attribute = new AttributeClob(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeFileDAOIF)
    {
      attribute = new AttributeFile(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeUUIDDAOIF)
    {
      attribute = new AttributeUUID(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeTermDAOIF)
    {
      attribute = new AttributeTerm(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeReferenceDAOIF)
    {
      attribute = new AttributeReference(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
    {
      attribute = new AttributeEnumeration(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF)
    {
      attribute = new AttributeMultiReference(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeHashDAOIF)
    {
      attribute = new AttributeHash(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeSymmetricDAOIF)
    {
      attribute = new AttributeSymmetric(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeBlobDAOIF)
    {
      attribute = new AttributeBlob(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeLocalCharacterDAOIF)
    {
      attribute = new AttributeLocalCharacter(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeLocalTextDAOIF)
    {
      attribute = new AttributeLocalText(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    // only call for new instances
    else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
    {
      attribute = new AttributeStruct(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    else if (mdAttributeIF instanceof MdAttributeIndicatorDAOIF)
    {
      attribute = new AttributeIndicator(attributeName, (String) attributeValue, definingType, mdAttributeIF, entityMdAttributeIFset);
    }
    
    
    if (attribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.createAttribute(mdAttributeIF, attributeName, definingType, attributeValue, entityMdAttributeIFset);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "[" + mdAttributeIF.getType() + "] is not a recognized query result attribute type.";
      throw new AttributeException(error);
    }

    return attribute;
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Attribute createAttribute(MdAttributeConcreteDAOIF mdAttributeIF, String attributeName, String definingType, Object attributeValue, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFlist);
  }
}
