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
package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.PluginIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

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
   * <b>Precondition:</b> mdAttributeDAOIF != null <br/>
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
  public static Attribute createAttribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingType)
  {
    Attribute attribute = null;

    if (mdAttributeDAOIF instanceof MdAttributeUUIDDAOIF)
    {
      attribute = new AttributeUUID(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeIntegerDAOIF)
    {
      attribute = new AttributeInteger(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeLongDAOIF)
    {
      attribute = new AttributeLong(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDoubleDAOIF)
    {
      attribute = new AttributeDouble(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDecimalDAOIF)
    {
      attribute = new AttributeDecimal(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeFloatDAOIF)
    {
      attribute = new AttributeFloat(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeBooleanDAOIF)
    {
      attribute = new AttributeBoolean(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDateDAOIF)
    {
      attribute = new AttributeDate(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeDateTimeDAOIF)
    {
      attribute = new AttributeDateTime(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeTimeDAOIF)
    {
      attribute = new AttributeTime(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeCharacterDAOIF)
    {
      attribute = new AttributeCharacter(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeTextDAOIF)
    {
      attribute = new AttributeText(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeReferenceDAOIF)
    {
      attribute = new AttributeReference(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeEnumerationDAOIF)
    {
      attribute = new AttributeEnumeration(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeClassificationDAOIF)
    {
      attribute = new AttributeClassification(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeGraphRefDAOIF)
    {
      attribute = new AttributeGraphRef(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeEmbeddedDAOIF)
    {
      MdAttributeEmbeddedDAOIF mdAttributeEmbeddedDAOIF = (MdAttributeEmbeddedDAOIF) mdAttributeDAOIF;

      String embeddedType = ( (MdGraphClassDAOIF) mdAttributeEmbeddedDAOIF.getEmbeddedMdClassDAOIF() ).definesType();

      VertexObjectDAO vertexObjectDAO = VertexObjectDAO.newInstance(embeddedType);

      if (mdAttributeDAOIF instanceof MdAttributeLocalCharacterEmbeddedDAOIF)
      {
        attribute = new AttributeLocalCharacterEmbedded(mdAttributeDAOIF, definingType);
      }
      else
      {
        attribute = new AttributeEmbedded(mdAttributeDAOIF, definingType);
      }

      attribute.setValue(vertexObjectDAO);
    }

    if (attribute == null)
    {
      ServiceLoader<GraphAttributeFactoryIF> loader = ServiceLoader.load(GraphAttributeFactoryIF.class);

      try
      {
        Iterator<GraphAttributeFactoryIF> i = loader.iterator();

        while (i.hasNext())
        {
          GraphAttributeFactoryIF factory = i.next();

          attribute = factory.createGraphAttribute(mdAttributeDAOIF, definingType);

          if (attribute != null)
          {
            break;
          }
        }
      }
      catch (ServiceConfigurationError serviceError)
      {
        throw new ProgrammingErrorException(serviceError);
      }
    }

    if (attribute == null)
    {
      String error = "[" + mdAttributeDAOIF.getType() + "] is not a recognized attribute type.";

      throw new AttributeException(error);
    }

    return attribute;
  }
}
