/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeLocalDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;

public abstract class JSONToDTO
{

  private String        sessionId;

  private Locale        locale;

  private FormatFactory factory;

  /**
   * Default constructor.
   */
  public JSONToDTO(String sessionId, Locale locale)
  {
    super();
    this.sessionId = sessionId;
    this.locale = locale;
    this.factory = AbstractFormatFactory.getFormatFactory();
  }

  protected String getSessionId()
  {
    return this.sessionId;
  }

  protected Locale getLocale()
  {
    return this.locale;
  }

  /**
   * Creates and sets the values on an AttributeDTO given a JSONObject that
   * contains the attribute information. The created AttributeDTO is returned.
   */
  protected AttributeDTO setAttribute(JSONObject attribute) throws JSONException
  {
    AttributeDTOHandler handler = null; // the attribute handler to set the
    // attribute JSON

    String type = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_TYPE.getLabel());

    if (type.equals(MdAttributeEnumerationInfo.CLASS))
    {
      handler = new AttributeEnumerationDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeMultiReferenceInfo.CLASS))
    {
      handler = new AttributeMultiReferenceDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeMultiTermInfo.CLASS))
    {
      handler = new AttributeMultiTermDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeLocalCharacterInfo.CLASS))
    {
      handler = new AttributeLocalDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeLocalTextInfo.CLASS))
    {
      handler = new AttributeLocalDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeStructInfo.CLASS))
    {
      handler = new AttributeStructDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeDateTimeInfo.CLASS))
    {
      handler = new AttributeDateTimeDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeDateInfo.CLASS))
    {
      handler = new AttributeDateDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeTimeInfo.CLASS))
    {
      handler = new AttributeTimeDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeIntegerInfo.CLASS))
    {
      handler = new AttributeIntegerDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeLongInfo.CLASS))
    {
      handler = new AttributeLongDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeDecimalInfo.CLASS))
    {
      handler = new AttributeDecimalDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeDoubleInfo.CLASS))
    {
      handler = new AttributeDoubleDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeFloatInfo.CLASS))
    {
      handler = new AttributeFloatDTOHandler(attribute);
    }
    else if (type.equals(MdAttributeBooleanInfo.CLASS))
    {
      handler = new AttributeBooleanDTOHandler(attribute);
    }
    else
    {
      // Fallback converter
      // TODO does this need PluginIF support?
      handler = new AttributeDTOHandler(attribute);
    }

    return handler.getAttribute();
  }

  /**
   * Inner class to handle AttributeDTOs
   */
  private class AttributeDTOHandler
  {
    protected JSONObject attribute;

    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeDTOHandler(JSONObject attribute)
    {
      this.attribute = attribute;
    }

    /**
     * Returns the constructed Node representing an attribute. Note that
     * attribute metadata isn't copied. This is because we don't care about the
     * server having the metadata since it's only needed by the client. Default
     * metadata values will be used instead.
     * 
     * @return
     */
    protected AttributeDTO getAttribute() throws JSONException
    {
      String name = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_NAME.getLabel());
      String type = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_TYPE.getLabel());
      String value = getValue();
      boolean readable = Boolean.parseBoolean(attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_READABLE.getLabel()));
      boolean writable = Boolean.parseBoolean(attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_WRITABLE.getLabel()));
      boolean modified = Boolean.parseBoolean(attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_MODIFIED.getLabel()));

      return AttributeDTOFactory.createAttributeDTO(name, type, value, readable, writable, modified);
    }

    /**
     * 
     * @return
     * @throws JSONException
     */
    protected String getValue() throws JSONException
    {
      if (attribute.isNull(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel()))
      {
        return null;
      }
      return attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
    }
  }

  private class AttributeIntegerDTOHandler extends AttributeDTOHandler
  {
    private AttributeIntegerDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.transport.conversion.json.JSONToDTO.AttributeDTOHandler
     * #getValue()
     */
    @Override
    protected String getValue() throws JSONException
    {
      try
      {
        Integer value = attribute.getInt(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
        return value.toString();        
//        return factory.getFormat(Integer.class).format(value, locale);
      }
      catch (JSONException e)
      {
        return super.getValue();
      }
    }
  }

  private class AttributeLongDTOHandler extends AttributeDTOHandler
  {
    private AttributeLongDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.transport.conversion.json.JSONToDTO.AttributeDTOHandler
     * #getValue()
     */
    @Override
    protected String getValue() throws JSONException
    {
      try
      {
        Long value = attribute.getLong(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
        return value.toString();        
//        return factory.getFormat(Long.class).format(value, locale);
      }
      catch (JSONException e)
      {
        return super.getValue();
      }
    }
  }

  private class AttributeDecimalDTOHandler extends AttributeDTOHandler
  {
    private AttributeDecimalDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.transport.conversion.json.JSONToDTO.AttributeDTOHandler
     * #getValue()
     */
    @Override
    protected String getValue() throws JSONException
    {
      try
      {
        BigDecimal value = new BigDecimal(attribute.getDouble(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel()));
        return value.toString();        
//        return factory.getFormat(BigDecimal.class).format(value, locale);
      }
      catch (JSONException e)
      {
        return super.getValue();
      }
    }
  }

  private class AttributeDoubleDTOHandler extends AttributeDTOHandler
  {
    private AttributeDoubleDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.transport.conversion.json.JSONToDTO.AttributeDTOHandler
     * #getValue()
     */
    @Override
    protected String getValue() throws JSONException
    {
      try
      {
        Double value = attribute.getDouble(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
        return value.toString();
//        return factory.getFormat(Double.class).format(value, locale);
      }
      catch (JSONException e)
      {
        return super.getValue();
      }
    }
  }

  private class AttributeFloatDTOHandler extends AttributeDTOHandler
  {
    private AttributeFloatDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.transport.conversion.json.JSONToDTO.AttributeDTOHandler
     * #getValue()
     */
    @Override
    protected String getValue() throws JSONException
    {
      try
      {
        Float value = new Float(attribute.getDouble( ( JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel() )));
        return value.toString();        
//        return factory.getFormat(Float.class).format(value, locale);
      }
      catch (JSONException e)
      {
        return super.getValue();
      }
    }
  }

  private class AttributeBooleanDTOHandler extends AttributeDTOHandler
  {
    private AttributeBooleanDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.transport.conversion.json.JSONToDTO.AttributeDTOHandler
     * #getValue()
     */
    @Override
    protected String getValue() throws JSONException
    {
      try
      {
        Boolean value = attribute.getBoolean(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
        return factory.getFormat(Boolean.class).format(value, locale);
      }
      catch (JSONException e)
      {
        return super.getValue();
      }
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
    private AttributeEnumerationDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * Sets the Enumeration attribute, which includes creating a DOM
     * representation of all enum values.
     */
    protected AttributeDTO getAttribute() throws JSONException
    {
      AttributeEnumerationDTO attributeEnumerationDTO = (AttributeEnumerationDTO) super.getAttribute();

      JSONObject values = attribute.getJSONObject(JSON.ENUMERATION_ENUM_NAMES.getLabel());

      Iterator<?> iter = values.keys();
      while (iter.hasNext())
      {
        // we only need the enum name
        String enumName = (String) iter.next();
        attributeEnumerationDTO.addEnumItem(enumName);
      }

      return attributeEnumerationDTO;
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
    private AttributeMultiReferenceDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * Sets the MultiReference attribute, which includes creating a DOM
     * representation of all enum values.
     */
    protected AttributeDTO getAttribute() throws JSONException
    {
      AttributeMultiReferenceDTO attributeMultiReferenceDTO = (AttributeMultiReferenceDTO) super.getAttribute();

      Object value = attribute.get(JSON.MULTI_REFERENCE_ITEM_IDS.getLabel());

      if (value instanceof JSONObject)
      {
        JSONObject values = (JSONObject) value;

        Iterator<?> iter = values.keys();
        while (iter.hasNext())
        {
          String itemId = (String) iter.next();
          attributeMultiReferenceDTO.addItem(itemId);
        }
      }
      else if (value instanceof JSONArray)
      {
        JSONArray values = (JSONArray) value;

        for (int i = 0; i < values.length(); i++)
        {
          String itemId = values.getString(i);
          attributeMultiReferenceDTO.addItem(itemId);
        }
      }
      else
      {
        throw new JSONException("Unsupported value for JSON attribute [" + JSON.MULTI_REFERENCE_ITEM_IDS.getLabel() + "]");
      }

      return attributeMultiReferenceDTO;
    }
  }

  /**
   * Sets the metadata for AttributeMultiTermDTOs
   */
  private class AttributeMultiTermDTOHandler extends AttributeMultiReferenceDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeMultiTermDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }
  }

  /**
   * Sets the metadata for AttributeDateDTOs
   */
  private class AttributeDateDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeDateDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * @throws JSONException
     */
    protected String getValue() throws JSONException
    {
      String oldValue = super.getValue();
      if (JSONUtil.isNull(oldValue))
      {
        return "";
      }
      else
      {
        Date date = factory.getFormat(Date.class).parse(oldValue, locale);
        String newValue = MdAttributeDateUtil.getTypeUnsafeValue(date);

        return newValue;
      }
    }
  }

  /**
   * Sets the metadata for AttributeDateTimeDTOs
   */
  private class AttributeDateTimeDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeDateTimeDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * Sets the Struct attribute, which includes creating a DOM to represent the
     * MdStruct it represents
     * 
     * @throws JSONException
     */
    protected String getValue() throws JSONException
    {
      String oldValue = super.getValue();
      if (JSONUtil.isNull(oldValue))
      {
        return "";
      }
      else
      {
        Date date = factory.getFormat(Date.class).parse(oldValue, locale);
        String newValue = MdAttributeDateTimeUtil.getTypeUnsafeValue(date);

        return newValue;
      }
    }
  }

  /**
   * Sets the metadata for AttributeDateTimeDTOs
   */
  private class AttributeTimeDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeTimeDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * Sets the Struct attribute, which includes creating a DOM to represent the
     * MdStruct it represents
     * 
     * @throws JSONException
     */
    protected String getValue() throws JSONException
    {
      String oldValue = super.getValue();
      if (JSONUtil.isNull(oldValue))
      {
        return "";
      }
      else
      {
        Date date = factory.getFormat(Date.class).parse(oldValue, locale);
        String newValue = MdAttributeTimeUtil.getTypeUnsafeValue(date);

        return newValue;
      }
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
    private AttributeStructDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * Sets the Struct attribute, which includes creating a DOM to represent the
     * MdStruct it represents
     * 
     * @throws JSONException
     */
    protected AttributeDTO getAttribute() throws JSONException
    {
      AttributeStructDTO attributeStructDTO = (AttributeStructDTO) super.getAttribute();

      if (attribute.has(JSON.STRUCT_STRUCT_DTO.getLabel()) && !attribute.isNull(JSON.STRUCT_STRUCT_DTO.getLabel()))
      {
        JSONObject struct = attribute.getJSONObject(JSON.STRUCT_STRUCT_DTO.getLabel());

        StructDTO structDTO = (StructDTO) JSONUtil.getComponentDTOFromJSON(sessionId, getLocale(), struct.toString());
        attributeStructDTO.setStructDTO(structDTO);
      }
      else
      {
        attributeStructDTO.setStructDTO(null);
      }

      return attributeStructDTO;
    }
  }

  /**
   * Sets the metadata for AttributeLocalDTOs
   */
  private class AttributeLocalDTOHandler extends AttributeStructDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeLocalDTOHandler(JSONObject attribute)
    {
      super(attribute);
    }

    /**
     * Sets the Struct attribute, which includes creating a DOM to represent the
     * MdStruct it represents
     * 
     * @throws JSONException
     */
    protected AttributeDTO getAttribute() throws JSONException
    {
      AttributeLocalDTO attributeStructDTO = (AttributeLocalDTO) super.getAttribute();

      if (attribute.has(JSON.STRUCT_STRUCT_DTO.getLabel()) && !attribute.isNull(JSON.STRUCT_STRUCT_DTO.getLabel()))
      {
        JSONObject struct = attribute.getJSONObject(JSON.STRUCT_STRUCT_DTO.getLabel());

        LocalStructDTO structDTO = (LocalStructDTO) JSONUtil.getComponentDTOFromJSON(sessionId, getLocale(), struct.toString());
        attributeStructDTO.setStructDTO(structDTO);
      }
      else
      {
        attributeStructDTO.setStructDTO(null);
      }

      return attributeStructDTO;
    }
  }
}
