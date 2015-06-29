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
package com.runwaysdk.controller;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.transport.metadata.AttributeDateMdDTO;
import com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeTimeMdDTO;

public class ParameterFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  private Class<?>                     clazz;

  private ClientRequestIF              request;

  public ParameterFactory(Class<?> clazz, ClientRequestIF request)
  {
    this.clazz = clazz;
    this.request = request;
  }

  /**
   * @param mutableDTO
   *          Object upon which the attribute was being parsed
   * @param attributeDTO
   *          Metadata for the attribute which was being parsed
   * @param locale
   *          The request Locale
   * @param value
   *          String represenation of the invalid value
   * 
   * @return Returns the appropriate AttributeParseException when an error
   *         occurs while parsing a value for a MutableDTO
   */
  public ParseProblemDTO getException(MutableDTO mutableDTO, AttributeMdDTO attributeDTO, Locale locale, String value)
  {
    if (clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Short.class))
    {
      return new IntegerParseExceptionDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (clazz.equals(Float.class) || clazz.equals(Double.class))
    {
      return new DecimalParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (clazz.equals(Boolean.class))
    {
      return new BooleanParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (attributeDTO instanceof AttributeDateMdDTO)
    {
      return new DateParseProblemDTO(mutableDTO, attributeDTO, locale, value, Constants.DATE_FORMAT);
    }
    else if (attributeDTO instanceof AttributeDateTimeMdDTO)
    {
      return new DateTimeParseProblemDTO(mutableDTO, attributeDTO, locale, value, Constants.DATETIME_FORMAT);
    }
    else if (attributeDTO instanceof AttributeTimeMdDTO)
    {
      return new DateParseProblemDTO(mutableDTO, attributeDTO, locale, value, Constants.TIME_FORMAT);
    }
    else if (attributeDTO instanceof AttributeReferenceMdDTO)
    {
      AttributeReferenceMdDTO referenceMdDTO = (AttributeReferenceMdDTO) attributeDTO;
      
      String label = referenceMdDTO.getReferencedDisplayLabel();
            
      // Inorder to create a more usefull error message try to retrieve the dto
      // from the server. With the dto we can use the dto.toString() value in
      // the error message.
      try
      {
        if (value != null && !value.equals(""))
        {
          value = this.request.get(value).toString();
        }
        
        return new ReferenceParseProblemDTO(mutableDTO, attributeDTO, locale, value, label);
      }
      catch (Exception e)
      {
        return new ReferenceParseProblemDTO(mutableDTO, attributeDTO, locale, null, label);
      }

    }

    for (PluginIF pluginIF : pluginMap.values())
    {
      ParseProblemDTO e = pluginIF.getException(clazz, mutableDTO, attributeDTO, locale, value);

      if (e != null)
      {
        return e;
      }
    }

    return new CharacterParseProblemDTO(mutableDTO, attributeDTO, locale, value);
  }

  /**
   * @param name
   *          The name of the parameter being parsed
   * @param locale
   *          The request Locale
   * @param value
   *          String representation of the invalid value
   * 
   * @return Returns the appropriate AttributeParseException when an error
   *         occurs while parsing a standalone value.
   */
  public AttributeNotificationDTO getException(String name, Locale locale, String value)
  {
    if (clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Short.class))
    {
      return new IntegerParseExceptionDTO(name, locale, value);
    }
    else if (clazz.equals(Float.class) || clazz.equals(Double.class))
    {
      return new DecimalParseProblemDTO(name, locale, value);
    }
    else if (clazz.equals(Boolean.class))
    {
      return new BooleanParseProblemDTO(name, locale, value);
    }
    else if (clazz.equals(Date.class))
    {
      return new DateTimeParseProblemDTO(name, locale, value, Constants.DATETIME_FORMAT);
    }

    for (PluginIF pluginIF : pluginMap.values())
    {
      AttributeNotificationDTO dto = pluginIF.getException(clazz, name, locale, value);

      if (dto != null)
      {
        return dto;
      }
    }

    return new CharacterParseProblemDTO(name, locale, value);
  }

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public ParseProblemDTO getException(Class<?> c, MutableDTO mutableDTO, AttributeMdDTO attributeDTO, Locale locale, String value);

    public AttributeNotificationDTO getException(Class<?> c, String name, Locale locale, String value);
  }

}
