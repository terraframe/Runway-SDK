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
package com.runwaysdk.mvc.conversion;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.mvc.JsonConfiguration;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDateDTO;
import com.runwaysdk.transport.attributes.AttributeDateTimeDTO;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeIntegerDTO;
import com.runwaysdk.transport.attributes.AttributeLongDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeTextDTO;
import com.runwaysdk.transport.attributes.AttributeTimeDTO;
import com.runwaysdk.util.DateUtilities;

public abstract class DTOToBasicJSON
{
  private JsonConfiguration configuration;

  /**
   * Default constructor
   */
  protected DTOToBasicJSON(JsonConfiguration configuration)
  {
    this.configuration = configuration;
  }

  protected JsonConfiguration getConfiguration()
  {
    return configuration;
  }

  /**
   * Sets attribute values on a JSONObject and returns it. Null is returned if
   * the attribute is not supported (i.e., blob)
   * 
   * @param attributeDTO
   * @return
   */
  protected Object serializeAttribute(AttributeDTO attributeDTO) throws JSONException
  {
    AttributeDTOHandler handler = null; // the attribute handler to set the
    // attribute JSON
    if (attributeDTO instanceof AttributeEnumerationDTO)
    {
      handler = new AttributeEnumerationDTOHandler((AttributeEnumerationDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeMultiReferenceDTO)
    {
      handler = new AttributeMultiReferenceDTOHandler((AttributeMultiReferenceDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeStructDTO)
    {
      handler = new AttributeStructDTOHandler((AttributeStructDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeDecDTO)
    {
      handler = new AttributeDecDTOHandler((AttributeDecDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeNumberDTO)
    {
      handler = new AttributeNumberDTOHandler((AttributeNumberDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeCharacterDTO)
    {
      handler = new AttributeCharacterDTOHandler((AttributeCharacterDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeTextDTO)
    {
      handler = new AttributeTextDTOHandler((AttributeTextDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeBlobDTO)
    {
      // do nothing. Blobs aren't represented in JSON
      return null;
    }
    else if (attributeDTO instanceof AttributeBooleanDTO)
    {
      handler = new AttributeBooleanDTOHandler((AttributeBooleanDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeReferenceDTO)
    {
      handler = new AttributeReferenceDTOHandler((AttributeReferenceDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeDateTimeDTO)
    {
      handler = new AttributeDateTimeDTOHandler((AttributeDateTimeDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeDateDTO)
    {
      handler = new AttributeDateDTOHandler((AttributeDateDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeTimeDTO)
    {
      handler = new AttributeTimeDTOHandler((AttributeTimeDTO) attributeDTO);
    }

    if (handler != null)
    {
      return handler.getAttribute();
    }

    return null;
  }

  /**
   * Inner class to handle AttributeDTOs
   */
  private abstract class AttributeDTOHandler
  {
    protected AttributeDTO attributeDTO;

    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeDTOHandler(AttributeDTO attributeDTO)
    {
      this.attributeDTO = attributeDTO;
    }

    /**
     * Returns the constructed Node representing an attribute.
     * 
     * @return
     */
    protected abstract Object getAttribute();

    protected Object stringValue(String value)
    {
      if (value != null && value.length() > 0)
      {
        return value;
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * Handles boolean values such that an actual javascript boolean is added to
   * the json, not a string representing the boolean
   * <code>MdAttributeBooleanInfo.TRUE</code> or
   * <code>MdAttributeBooleanInfo.FALSE</code> value.
   * 
   * @author justin
   */
  private class AttributeBooleanDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeBooleanDTO
     */
    private AttributeBooleanDTOHandler(AttributeBooleanDTO attributeBooleanDTO)
    {
      super(attributeBooleanDTO);
    }

    @Override
    protected Object getAttribute()
    {
      // set the value as a boolean, not as a string
      String value = attributeDTO.getValue();

      if (value != null && !value.equals(""))
      {
        if (value.equals("1") || value.equals("0"))
        {
          value = com.runwaysdk.constants.MdAttributeBooleanUtil.convertIntToString(value);
        }

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
        {
          return new Boolean(value);
        }
        else
        {
          return value;
        }
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * Inner class to handle AttributeNumberDTOs
   */
  private class AttributeNumberDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeNumberDTOHandler(AttributeNumberDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected Object getAttribute()
    {
      String value = attributeDTO.getValue();
      if (value != null && !value.equals(""))
      {
        // int and long must be converted into a long
        if (attributeDTO instanceof AttributeIntegerDTO || attributeDTO instanceof AttributeLongDTO)
        {
          long valueL = Long.parseLong(attributeDTO.getValue());

          return Long.valueOf(valueL);
        }

        // decimal, double, and float must be converted into a double
        if (attributeDTO instanceof AttributeDecDTO)
        {
          double valueD = Double.parseDouble(attributeDTO.getValue());
          return Double.valueOf(valueD);
        }
      }

      return null;
    }
  }

  /**
   * Sets the metadata for AttributeDecDTOs
   */
  private class AttributeDecDTOHandler extends AttributeNumberDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeDecDTOHandler(AttributeDecDTO attributeDTO)
    {
      super(attributeDTO);
    }

  }

  /**
   * Sets the metadata for AttributeEnumerationDTOs
   */
  private class AttributeEnumerationDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeEnumerationDTOHandler(AttributeEnumerationDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the Enumeration attribute, which includes creating a DOM
     * representation of all enum values.
     */
    protected Object getAttribute()
    {
      AttributeEnumerationDTO attributeEnumerationDTO = (AttributeEnumerationDTO) attributeDTO;
      List<String> enumNames = attributeEnumerationDTO.getEnumNames();

      if (enumNames.size() == 0)
      {
        return null;
      }
      else if (enumNames.size() == 1)
      {
        return enumNames.get(0);
      }
      else
      {
        JSONArray enumNamesJSON = new JSONArray();

        for (String enumName : enumNames)
        {
          enumNamesJSON.put(enumName);
        }

        return enumNamesJSON;
      }
    }
  }

  /**
   * Sets the metadata for AttributeMultiReferenceDTOs
   */
  private class AttributeMultiReferenceDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeMultiReferenceDTOHandler(AttributeMultiReferenceDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the MultiReference attribute, which includes creating a DOM
     * representation of all item ids.
     */
    protected Object getAttribute()
    {
      // container array to hold all item ids
      JSONArray itemIdsJSON = new JSONArray();

      AttributeMultiReferenceDTO attributeMultiReferenceDTO = (AttributeMultiReferenceDTO) attributeDTO;

      List<String> itemIds = attributeMultiReferenceDTO.getItemIds();
      for (String itemId : itemIds)
      {
        itemIdsJSON.put(itemId);
      }

      return itemIds;
    }
  }

  /**
   * Sets the metadata for AttributeCharacterDTOs
   */
  private class AttributeCharacterDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeCharacterDTOHandler(AttributeCharacterDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected Object getAttribute()
    {
      return this.stringValue(attributeDTO.getValue());
    }
  }

  private class AttributeTextDTOHandler extends AttributeDTOHandler
  {
    private AttributeTextDTOHandler(AttributeTextDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected Object getAttribute()
    {
      return this.stringValue(attributeDTO.getValue());
    }
  }

  /**
   * Sets the metadata for AttributeStructDTOs
   */
  private class AttributeStructDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeStructDTOHandler(AttributeStructDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the Struct attribute.
     * 
     * @throws JSONException
     */
    protected Object getAttribute()
    {
      try
      {
        AttributeStructDTO attributeStructDTO = (AttributeStructDTO) attributeDTO;
        StructDTO structDTO = attributeStructDTO.getStructDTO();

        if (structDTO != null)
        {
          if (structDTO instanceof LocalStructDTO)
          {
            String value = ( (LocalStructDTO) structDTO ).getValue();

            return stringValue(value);
          }
          else
          {
            StructDTOToBasicJSON structDTOToJSON = new StructDTOToBasicJSON(structDTO, configuration);
            return structDTOToJSON.populate();
          }
        }

        return null;
      }
      catch (JSONException e)
      {
        throw new RuntimeException(e);
      }
    }
  }

  private class AttributeReferenceDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeReferenceDTOHandler(AttributeReferenceDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the MultiReference attribute, which includes creating a DOM
     * representation of all item ids.
     */
    protected Object getAttribute()
    {
      // container array to hold all item ids
      AttributeReferenceDTO attributeReferenceDTO = (AttributeReferenceDTO) attributeDTO;

      return attributeReferenceDTO.getValue();
    }
  }

  private class AttributeDateTimeDTOHandler extends AttributeDTOHandler
  {
    private AttributeDateTimeDTOHandler(AttributeDateTimeDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected Object getAttribute()
    {
      String value = this.attributeDTO.getValue();
      if (value != null && value.trim().length() != 0)
      {
        Date date = MdAttributeDateTimeUtil.getTypeSafeValue(value);
        String isoDate = DateUtilities.formatISO8601(date);

        return isoDate;
      }

      return null;
    }
  }

  private class AttributeDateDTOHandler extends AttributeDTOHandler
  {
    private AttributeDateDTOHandler(AttributeDateDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected Object getAttribute()
    {
      String value = this.attributeDTO.getValue();
      if (value != null && value.trim().length() != 0)
      {
        Date date = MdAttributeDateUtil.getTypeSafeValue(value);
        String isoDate = DateUtilities.formatISO8601(date);

        return isoDate;
      }

      return null;
    }
  }

  private class AttributeTimeDTOHandler extends AttributeDTOHandler
  {
    private AttributeTimeDTOHandler(AttributeTimeDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected Object getAttribute()
    {
      String value = this.attributeDTO.getValue();
      if (value != null && value.trim().length() != 0)
      {
        Date date = MdAttributeTimeUtil.getTypeSafeValue(value);
        String isoDate = DateUtilities.formatISO8601(date);

        return isoDate;
      }

      return null;
    }
  }
}
