/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.mvc.conversion;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.mvc.JsonConfiguration;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class ComponentDTOIFToBasicJSON extends DTOToBasicJSON
{
  /**
   * The source ComponentDTO
   */
  private ComponentDTOIF componentDTOIF;

  /**
   * The destination JSONObject
   */
  private JSONObject     json;

  /**
   * Constructor to set the source ComponentDTO.
   * 
   * @param configuration
   *
   * @param componentDTO
   */
  protected ComponentDTOIFToBasicJSON(ComponentDTOIF componentDTOIF, JsonConfiguration configuration)
  {
    super(configuration);

    this.componentDTOIF = componentDTOIF;
    this.json = new JSONObject();
  }

  /**
   * Returns the source ComponentDTOIF
   * 
   * @return
   */
  protected ComponentDTOIF getComponentDTO()
  {
    return componentDTOIF;
  }

  /**
   * Returns the destination JSONObject.
   *
   * @return
   */
  protected final JSONObject getJSON()
  {
    return json;
  }

  /**
   * Sets all properties on the json object.
   *
   * @throws JSONException
   */
  protected void setProperties() throws JSONException
  {
    // id
    json.put(ComponentInfo.ID, componentDTOIF.getId());

    // type
    json.put(ComponentInfo.TYPE, componentDTOIF.getType());
  }

  /**
   * Returns the JSONObject that has been populated with values from the source
   * ComponentDTO.
   *
   * @return
   * @throws JSONException
   */
  public final JSONObject populate() throws JSONException
  {
    // null check. Returns the unpopulated (empty hashmap) JSONObject
    if (componentDTOIF == null)
    {
      return json;
    }

    setProperties();

    serializeAttributes();

    return json;
  }

  /**
   * Sets the attributes and their metadata on the JSON object
   *
   * @throws JSONException
   */
  private void serializeAttributes() throws JSONException
  {
    for (String name : componentDTOIF.getAttributeNames())
    {
      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(componentDTOIF, name);

      if (isValid(attributeDTO))
      {
        Object attribute = serializeAttribute(attributeDTO);

        if (attribute != null)
        {
          json.put(name, attribute);
        }
      }
    }

  }

  public boolean isValid(AttributeDTO attributeDTO)
  {
    String name = attributeDTO.getName();

    if (this.getConfiguration().supports(this.componentDTOIF.getClass()))
    {
      if (this.getConfiguration().exclude(attributeDTO.getName()))
      {
        return false;
      }
    }

    return ! ( attributeDTO.getAttributeMdDTO().isSystem() || name.equals(ComponentInfo.KEY) );
  }

  /**
   * Given an ComponentDTOIF, this method returns a concrete subclass of
   * ComponentDTOToJSON that can convert the concrete ComponentDTOIF correctly.
   *
   * @param componentDTOIF
   * @param configuration
   * @return
   */
  public static ComponentDTOIFToBasicJSON getConverter(ComponentDTOIF componentDTOIF, JsonConfiguration configuration)
  {
    if (componentDTOIF instanceof BusinessDTO)
    {
      return new BusinessDTOToBasicJSON((BusinessDTO) componentDTOIF, configuration);
    }
    else if (componentDTOIF instanceof RelationshipDTO)
    {
      return new RelationshipDTOToBasicJSON((RelationshipDTO) componentDTOIF, configuration);
    }
    else if (componentDTOIF instanceof LocalStructDTO)
    {
      return new LocalStructDTOToBasicJSON((LocalStructDTO) componentDTOIF, configuration);
    }
    else if (componentDTOIF instanceof StructDTO)
    {
      return new StructDTOToBasicJSON((StructDTO) componentDTOIF, configuration);
    }
    else if (componentDTOIF instanceof ValueObjectDTO)
    {
      return new ValueObjectDTOToBasicJSON((ValueObjectDTO) componentDTOIF, configuration);
    }
    else if (componentDTOIF instanceof ViewDTO)
    {
      return new ViewDTOToBasicJSON((ViewDTO) componentDTOIF, configuration);
    }
    else if (componentDTOIF instanceof UtilDTO)
    {
      return new UtilDTOToBasicJSON((UtilDTO) componentDTOIF, configuration);
    }
    else
    {
      String error = "Cannot convert JSON into EntityDTO";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      return null;
    }
  }

}
