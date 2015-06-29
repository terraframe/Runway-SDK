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
package com.runwaysdk.transport.conversion.json;

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
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class ComponentDTOIFToJSON extends DTOToJSON
{
  /**
   * The source ComponentDTO
   */
  private ComponentDTOIF componentDTOIF;

  /**
   * The destination JSONObject
   */
  private JSONObject json;

  /**
   * Constructor to set the source ComponentDTO.
   *
   * @param componentDTO
   */
  protected ComponentDTOIFToJSON(ComponentDTOIF componentDTOIF)
  {
    this.componentDTOIF = componentDTOIF;
    this.json = new JSONObject();
  }

  /**
   * Constructor to set the source ComponentDTO and destination JSONObject.
   *
   * @param componentDTO
   * @param json
   */
  protected ComponentDTOIFToJSON(ComponentDTOIF componentDTOIF, JSONObject json)
  {
    this.componentDTOIF = componentDTOIF;
    this.json = json;
  }

  /**
   * Returns the source ComponentDTOIF
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
    json.put(JSON.ENTITY_DTO_ID.getLabel(), componentDTOIF.getId());

    // type
    json.put(JSON.COMPONENT_DTO_TYPE.getLabel(), componentDTOIF.getType());

    // readable
    json.put(JSON.ENTITY_DTO_READABLE.getLabel(), componentDTOIF.isReadable());

    // toString
    json.put(JSON.ENTITY_DTO_TO_STRING.getLabel(), componentDTOIF.toString());

    // dto type
    String dtoType = getDTOType();
    json.put(JSON.DTO_TYPE.getLabel(), dtoType);

    // TypeMd
    setTypeMd();
  }

  /**
   * Sets the TypeMd values on the json.
   */
  protected void setTypeMd() throws JSONException
  {
    JSONObject typeMd = new JSONObject();
    typeMd.put(JSON.TYPE_MD_DISPLAY_LABEL.getLabel(), componentDTOIF.getMd().getDisplayLabel());
    typeMd.put(JSON.TYPE_MD_DESCRIPTION.getLabel(), componentDTOIF.getMd().getDescription());
    typeMd.put(JSON.TYPE_MD_ID.getLabel(), componentDTOIF.getMd().getId());

    json.put(JSON.TYPE_MD.getLabel(), typeMd);
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
    if(componentDTOIF == null)
    {
      return json;
    }

    setProperties();

    setAttributes();

    return json;
  }

  /**
   * Sets the attributes and their metadata on the JSON object
   *
   * @throws JSONException
   */
  private void setAttributes() throws JSONException
  {
    JSONObject attributeMap = new JSONObject(); // Map to hold all attributes

    for (String name : componentDTOIF.getAttributeNames())
    {
      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(componentDTOIF, name);

      JSONObject attribute = setAttribute(attributeDTO);
      if(attribute != null)
      {
        // add the attribute to the attributeMap
        attributeMap.put(name, attribute);
      }
    }

    json.put(JSON.ENTITY_DTO_ATTRIBUTE_MAP.getLabel(), attributeMap);
  }

  /**
   * Subclasses must override this to return the generic DTO type
   * so that the destination JSON knows what type of DTO it is.
   *
   * @return
   */
  protected abstract String getDTOType();

  /**
   * Given an ComponentDTOIF, this method returns a concrete subclass of ComponentDTOToJSON
   * that can convert the concrete ComponentDTOIF correctly.
   *
   * @param componentDTOIF
   * @return
   */
  public static ComponentDTOIFToJSON getConverter(ComponentDTOIF componentDTOIF)
  {
    if (componentDTOIF instanceof BusinessDTO)
    {
      return new BusinessDTOToJSON((BusinessDTO)componentDTOIF);
    }
    else if(componentDTOIF instanceof RelationshipDTO)
    {
      return new RelationshipDTOToJSON((RelationshipDTO)componentDTOIF);
    }
    else if(componentDTOIF instanceof LocalStructDTO)
    {
      return new LocalStructDTOToJSON((LocalStructDTO)componentDTOIF);
    }
    else if(componentDTOIF instanceof StructDTO)
    {
      return new StructDTOToJSON((StructDTO)componentDTOIF);
    }
    else if(componentDTOIF instanceof ValueObjectDTO)
    {
      return new ValueObjectDTOToJSON((ValueObjectDTO)componentDTOIF);
    }
    else if(componentDTOIF instanceof ViewDTO)
    {
      return new ViewDTOToJSON((ViewDTO)componentDTOIF);
    }
    else if(componentDTOIF instanceof UtilDTO)
    {
      return new UtilDTOToJSON((UtilDTO)componentDTOIF);
    }
    else
    {
      String error = "Cannot convert JSON into EntityDTO";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      return null;
    }
  }

}
