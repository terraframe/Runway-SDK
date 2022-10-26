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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.transport.attributes.AttributeLocalDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.AttributeDateMdDTO;
import com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecimalMdDTO;
import com.runwaysdk.transport.metadata.AttributeDoubleMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeFloatMdDTO;
import com.runwaysdk.transport.metadata.AttributeIntegerMdDTO;
import com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeLocalMdDTO;
import com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO;
import com.runwaysdk.transport.metadata.AttributeLongMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeMultiTermMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.AttributeTimeMdDTO;

public abstract class BasicJSONToDTO
{

  private String        sessionId;

  private Locale        locale;

  private FormatFactory factory;

  /**
   * Default constructor.
   */
  public BasicJSONToDTO(String sessionId, Locale locale)
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

  protected ClientRequestIF getClientRequest()
  {
    ClientSession session = ClientSession.getExistingSession(this.getSessionId(), new Locale[] { this.getLocale() });
    ClientRequestIF request = session.getRequest();

    return request;
  }

  /**
   * Creates and sets the values on an AttributeDTO given a JSONObject that
   * contains the attribute information. The created AttributeDTO is returned.
   * 
   * @param mutableDTO
   *          TODO
   */
  protected void populate(MutableDTO mutableDTO, String attributeName, JSONObject object) throws JSONException
  {
    AttributeDTOHandler handler = null; // the attribute handler to set the
    // attribute JSON

    AttributeMdDTO attributeMd = mutableDTO.getAttributeMd(attributeName);

    if (attributeMd instanceof AttributeEnumerationMdDTO)
    {
      handler = new AttributeEnumerationDTOHandler(mutableDTO, (AttributeEnumerationMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeMultiTermMdDTO)
    {
      handler = new AttributeMultiTermDTOHandler(mutableDTO, (AttributeMultiTermMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeMultiReferenceMdDTO)
    {
      handler = new AttributeMultiReferenceDTOHandler(mutableDTO, (AttributeMultiReferenceMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeLocalCharacterMdDTO)
    {
      handler = new AttributeLocalDTOHandler(mutableDTO, (AttributeLocalCharacterMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeLocalTextMdDTO)
    {
      handler = new AttributeLocalDTOHandler(mutableDTO, (AttributeLocalTextMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeStructMdDTO)
    {
      handler = new AttributeStructDTOHandler(mutableDTO, (AttributeStructMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeDateTimeMdDTO)
    {
      handler = new AttributeDateTimeDTOHandler(mutableDTO, (AttributeDateTimeMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeDateMdDTO)
    {
      handler = new AttributeDateDTOHandler(mutableDTO, (AttributeDateMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeTimeMdDTO)
    {
      handler = new AttributeTimeDTOHandler(mutableDTO, (AttributeTimeMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeIntegerMdDTO)
    {
      handler = new AttributeIntegerDTOHandler(mutableDTO, (AttributeIntegerMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeLongMdDTO)
    {
      handler = new AttributeLongDTOHandler(mutableDTO, (AttributeLongMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeDecimalMdDTO)
    {
      handler = new AttributeDecimalDTOHandler(mutableDTO, (AttributeDecimalMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeDoubleMdDTO)
    {
      handler = new AttributeDoubleDTOHandler(mutableDTO, (AttributeDoubleMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeFloatMdDTO)
    {
      handler = new AttributeFloatDTOHandler(mutableDTO, (AttributeFloatMdDTO) attributeMd, object);
    }
    else if (attributeMd instanceof AttributeBooleanMdDTO)
    {
      handler = new AttributeBooleanDTOHandler(mutableDTO, (AttributeBooleanMdDTO) attributeMd, object);
    }
    else
    {
      // Fallback converter
      handler = new AttributeDTOHandler(mutableDTO, attributeMd, object);
    }

    handler.populate();
  }

  /**
   * Inner class to handle AttributeDTOs
   */
  private class AttributeDTOHandler
  {
    protected MutableDTO     mutableDTO;

    protected AttributeMdDTO attributeMd;

    protected JSONObject     object;

    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeDTOHandler(MutableDTO mutableDTO, AttributeMdDTO attributeMd, JSONObject object)
    {
      this.mutableDTO = mutableDTO;
      this.attributeMd = attributeMd;
      this.object = object;
    }

    protected boolean isValid(String attributeName)
    {
      return this.mutableDTO.isWritable(attributeName) && !this.attributeMd.isSystem() && this.object.has(attributeName);
    }

    /**
     * 
     * @return
     * @throws JSONException
     */
    protected void populate() throws JSONException
    {
      String attributeName = attributeMd.getName();

      if (this.isValid(attributeName))
      {
        String string = this.getValue(attributeName);

        this.mutableDTO.setValue(attributeName, string);
      }
    }

    protected String getValue(String attributeName) throws JSONException
    {
      return object.getString(attributeName);
    }
  }

  private class AttributeIntegerDTOHandler extends AttributeDTOHandler
  {
    private AttributeIntegerDTOHandler(MutableDTO mutableDTO, AttributeIntegerMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      Integer value = Integer.valueOf(object.getInt(attributeName));
      return value.toString();
    }
  }

  private class AttributeLongDTOHandler extends AttributeDTOHandler
  {
    private AttributeLongDTOHandler(MutableDTO mutableDTO, AttributeLongMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      return Long.toString(object.getLong(attributeName));
    }
  }

  private class AttributeDecimalDTOHandler extends AttributeDTOHandler
  {
    private AttributeDecimalDTOHandler(MutableDTO mutableDTO, AttributeDecimalMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      BigDecimal value = new BigDecimal(object.getDouble(attributeName));
      return value.toString();
    }

  }

  private class AttributeDoubleDTOHandler extends AttributeDTOHandler
  {
    private AttributeDoubleDTOHandler(MutableDTO mutableDTO, AttributeDoubleMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      Double value = Double.valueOf(object.getDouble(attributeName));
      return value.toString();
    }
  }

  private class AttributeFloatDTOHandler extends AttributeDTOHandler
  {
    private AttributeFloatDTOHandler(MutableDTO mutableDTO, AttributeFloatMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      Float value = new Float(object.getDouble(attributeName));
      return value.toString();
    }
  }

  private class AttributeBooleanDTOHandler extends AttributeDTOHandler
  {
    private AttributeBooleanDTOHandler(MutableDTO mutableDTO, AttributeBooleanMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      boolean value = object.getBoolean(attributeName);
      return factory.getFormat(Boolean.class).format(value, locale);
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
    private AttributeEnumerationDTOHandler(MutableDTO mutableDTO, AttributeEnumerationMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected void populate() throws JSONException
    {
      String attributeName = attributeMd.getName();

      if (this.isValid(attributeName))
      {
        Object value = this.object.get(attributeName);

        if (value instanceof String)
        {
          this.mutableDTO.addEnumItem(attributeName, (String) value);
        }
        else
        {
          JSONArray items = (JSONArray) value;

          for (int i = 0; i < items.length(); i++)
          {
            this.mutableDTO.addEnumItem(attributeName, items.getString(i));
          }
        }
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
    private AttributeMultiReferenceDTOHandler(MutableDTO mutableDTO, AttributeMultiReferenceMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected void populate() throws JSONException
    {
      String attributeName = attributeMd.getName();

      if (this.isValid(attributeName))
      {
        Object value = this.object.get(attributeName);

        if (value instanceof String)
        {
          this.mutableDTO.addMultiItem(attributeName, (String) value);
        }
        else
        {
          JSONArray items = (JSONArray) value;

          for (int i = 0; i < items.length(); i++)
          {
            this.mutableDTO.addMultiItem(attributeName, items.getString(i));
          }
        }
      }
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
    private AttributeMultiTermDTOHandler(MutableDTO mutableDTO, AttributeMultiTermMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
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
    private AttributeDateDTOHandler(MutableDTO mutableDTO, AttributeDateMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      String value = super.getValue(attributeName);

      Date date = factory.getFormat(Date.class).parse(value, locale);

      return MdAttributeDateUtil.getTypeUnsafeValue(date);
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
    private AttributeDateTimeDTOHandler(MutableDTO mutableDTO, AttributeDateTimeMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      String value = super.getValue(attributeName);

      Date date = factory.getFormat(Date.class).parse(value, locale);

      return MdAttributeDateTimeUtil.getTypeUnsafeValue(date);
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
    private AttributeTimeDTOHandler(MutableDTO mutableDTO, AttributeTimeMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected String getValue(String attributeName) throws JSONException
    {
      String value = super.getValue(attributeName);

      Date date = factory.getFormat(Date.class).parse(value, locale);

      return MdAttributeTimeUtil.getTypeUnsafeValue(date);
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
    private AttributeStructDTOHandler(MutableDTO mutableDTO, AttributeStructMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected void populate() throws JSONException
    {
      String attributeName = attributeMd.getName();

      if (this.isValid(attributeName))
      {
        JSONObject struct = this.object.getJSONObject(attributeName);

        AttributeStructDTO attributeStruct = ComponentDTOFacade.getAttributeStructDTO(mutableDTO, attributeName);
        StructDTO structDTO = attributeStruct.getStructDTO();

        new BasicJSONToStructDTO(sessionId, locale, struct).populate(structDTO);
      }
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
    private AttributeLocalDTOHandler(MutableDTO mutableDTO, AttributeLocalMdDTO attributeMd, JSONObject object)
    {
      super(mutableDTO, attributeMd, object);
    }

    @Override
    protected void populate() throws JSONException
    {
      String attributeName = attributeMd.getName();

      if (this.isValid(attributeName))
      {
        String value = this.getValue(attributeName);

        AttributeLocalDTO attributeLocal = (AttributeLocalDTO) ComponentDTOFacade.getAttributeStructDTO(mutableDTO, attributeName);
        LocalStructDTO localStruct = (LocalStructDTO) attributeLocal.getStructDTO();
        localStruct.setValue(value);
      }
    }
  }
}
