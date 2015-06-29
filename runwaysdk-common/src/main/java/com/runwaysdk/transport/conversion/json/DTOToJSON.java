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

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeClobDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDateDTO;
import com.runwaysdk.transport.attributes.AttributeDateTimeDTO;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEncryptionDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeIntegerDTO;
import com.runwaysdk.transport.attributes.AttributeLongDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeMultiTermDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.attributes.AttributeTextDTO;
import com.runwaysdk.transport.attributes.AttributeTimeDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.util.DateUtilities;

public abstract class DTOToJSON
{

  /**
   * Default constructor
   */
  protected DTOToJSON()
  {
    super();
  }

  /**
   * Sets attribute values on a JSONObject and returns it. Null is returned if
   * the attribute is not supported (i.e., blob)
   * 
   * @param attributeDTO
   * @return
   */
  protected JSONObject setAttribute(AttributeDTO attributeDTO) throws JSONException
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
    else if (attributeDTO instanceof AttributeMultiTermDTO)
    {
      handler = new AttributeMultiTermDTOHandler((AttributeMultiTermDTO) attributeDTO);
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
    else if (attributeDTO instanceof AttributeEncryptionDTO)
    {
      handler = new AttributeEncryptionDTOHandler((AttributeEncryptionDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeCharacterDTO)
    {
      handler = new AttributeCharacterDTOHandler((AttributeCharacterDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeTextDTO)
    {
      handler = new AttributeTextDTOHandler((AttributeTextDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeClobDTO)
    {
      handler = new AttributeClobDTOHandler((AttributeClobDTO) attributeDTO);
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
    else if (attributeDTO instanceof AttributeTermDTO)
    {
      handler = new AttributeTermDTOHandler((AttributeTermDTO) attributeDTO);
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
    else
    {
      handler = new AttributeDTOHandler(attributeDTO);
    }

    return handler.getAttribute();
  }

  /**
   * Inner class to handle AttributeDTOs
   */
  private class AttributeDTOHandler
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
     * Returns the constructed Map representing attribute metadata. Subclasses
     * should override this to add extra metadata.
     * 
     * @return
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = new JSONObject();

      AttributeMdDTO mdDTO = attributeDTO.getAttributeMdDTO();

      String displayLabel = mdDTO.getDisplayLabel();
      String description = mdDTO.getDescription();
      boolean required = mdDTO.isRequired();
      boolean immutable = mdDTO.isImmutable();
      String id = mdDTO.getId();
      boolean system = mdDTO.isSystem();
      String name = mdDTO.getName();

      // display label
      metadata.put(JSON.ATTRIBUTE_METADATA_DISPLAY_LABEL.getLabel(), displayLabel);

      // description
      metadata.put(JSON.ATTRIBUTE_METADATA_DESCRIPTION.getLabel(), description);

      // required
      metadata.put(JSON.ATTRIBUTE_METADATA_REQUIRED.getLabel(), required);

      // immutable
      metadata.put(JSON.ATTRIBUTE_METADATA_IMMUTABLE.getLabel(), immutable);

      // id
      metadata.put(JSON.ATTRIBUTE_METADATA_ID.getLabel(), id);

      // name
      metadata.put(JSON.ATTRIBUTE_METADATA_NAME.getLabel(), name);

      // system
      metadata.put(JSON.ATTRIBUTE_METADATA_SYSTEM.getLabel(), system);

      return metadata;
    }

    /**
     * Returns the constructed Node representing an attribute.
     * 
     * @return
     */
    protected JSONObject getAttribute() throws JSONException
    {
      JSONObject attribute = new JSONObject();

      String name = attributeDTO.getName();
      String type = attributeDTO.getType();

      String dtoType = attributeDTO.getClass().getName();
      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_DTO_TYPE.getLabel(), dtoType);

      boolean readable = attributeDTO.isReadable();
      boolean writable = attributeDTO.isWritable();
      boolean modified = attributeDTO.isModified();

      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_NAME.getLabel(), name);
      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_TYPE.getLabel(), type);

      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_READABLE.getLabel(), readable);
      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_WRITABLE.getLabel(), writable);
      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_MODIFIED.getLabel(), modified);

      setValue(attribute);

      JSONObject metadata = setMetadata();
      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_ATTRIBUTE_MD_DTO.getLabel(), metadata);

      return attribute;
    }

    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = attributeDTO.getValue();

      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), value);
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
    protected JSONObject setMetadata() throws JSONException
    {

      JSONObject json = super.setMetadata();

      AttributeBooleanDTO booleanDTO = (AttributeBooleanDTO) this.attributeDTO;

      String pos = booleanDTO.getAttributeMdDTO().getPositiveDisplayLabel();
      json.put(JSON.BOOLEAN_POSITIVE_DISPLAY_LABEL.getLabel(), pos);

      String neg = booleanDTO.getAttributeMdDTO().getNegativeDisplayLabel();
      json.put(JSON.BOOLEAN_NEGATIVE_DISPLAY_LABEL.getLabel(), neg);

      return json;
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      // set the value as a boolean, not as a string
      String value = attributeDTO.getValue();
      if (value != null && !value.equals(""))
      {
        boolean bValue = Boolean.parseBoolean(value);
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), bValue);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
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

    /**
     * Sets the metadata for AttributeNumberDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeNumberMdDTO mdDTO = ( (AttributeNumberDTO) attributeDTO ).getAttributeMdDTO();
      boolean rejectZero = mdDTO.rejectZero();
      boolean rejectNegative = mdDTO.rejectNegative();
      boolean rejectPositive = mdDTO.rejectPositive();

      // reject zero
      metadata.put(JSON.NUMBER_METADATA_REJECT_ZERO.getLabel(), rejectZero);

      // reject negative
      metadata.put(JSON.NUMBER_METADATA_REJECT_NEGATIVE.getLabel(), rejectNegative);

      // reject positive
      metadata.put(JSON.NUMBER_METADATA_REJECT_POSITIVE.getLabel(), rejectPositive);

      return metadata;
    }

    /**
     * Converts the attribute value into the proper number type so the
     * JSONObject can parse them correctly into JSON (so the numbers aren't
     * strings).
     */
    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = attributeDTO.getValue();
      if (value != null && !value.equals(""))
      {
        // int and long must be converted into a long
        if (attributeDTO instanceof AttributeIntegerDTO || attributeDTO instanceof AttributeLongDTO)
        {
          long valueL = Long.parseLong(attributeDTO.getValue());
          attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), valueL);
        }
        // decimal, double, and float must be converted into a double
        if (attributeDTO instanceof AttributeDecDTO)
        {
          double valueD = Double.parseDouble(attributeDTO.getValue());
          attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), valueD);
        }
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
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

    /**
     * Sets the metadata for AttributeDecDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeDecMdDTO mdDTO = ( (AttributeDecDTO) attributeDTO ).getAttributeMdDTO();
      int totalLength = mdDTO.getTotalLength();
      int decimalLength = mdDTO.getDecimalLength();

      // total length
      metadata.put(JSON.DEC_METADATA_TOTAL_LENGTH.getLabel(), totalLength);

      // decimal length
      metadata.put(JSON.DEC_METADATA_DECIMAL_LENGTH.getLabel(), decimalLength);

      return metadata;
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
     * Sets the metadata for AttributeEnumerationDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeEnumerationMdDTO mdDTO = ( (AttributeEnumerationDTO) attributeDTO ).getAttributeMdDTO();

      boolean selectMultiple = mdDTO.selectMultiple();

      // referenced mdenumeration
      metadata.put(JSON.ENUMERATION_METADATA_REFERENCED_MD_ENUMERATION.getLabel(), mdDTO.getReferencedMdEnumeration());

      // select multiple
      metadata.put(JSON.ENUMERATION_METADATA_SELECT_MULTIPLE.getLabel(), selectMultiple);

      // enum values
      JSONObject enumNamesJSON = new JSONObject();
      List<String> enumNames = mdDTO.getEnumNames();
      for (String enumName : enumNames)
      {
        enumNamesJSON.put(enumName, mdDTO.getEnumDisplayLabel(enumName));
      }

      metadata.put(JSON.ENUMERATION_METADATA_ENUM_NAMES.getLabel(), enumNamesJSON);

      return metadata;
    }

    /**
     * Sets the Enumeration attribute, which includes creating a DOM
     * representation of all enum values.
     */
    protected JSONObject getAttribute() throws JSONException
    {
      JSONObject attribute = super.getAttribute();

      // container array to hold all enum values
      JSONArray enumNamesJSON = new JSONArray();

      AttributeEnumerationDTO attributeEnumerationDTO = (AttributeEnumerationDTO) attributeDTO;
      List<String> enumNames = attributeEnumerationDTO.getEnumNames();
      for (String enumName : enumNames)
      {
        enumNamesJSON.put(enumName);
      }

      attribute.put(JSON.ENUMERATION_ENUM_NAMES.getLabel(), enumNamesJSON);

      return attribute;
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
     * Sets the metadata for AttributeMultiReferenceDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeMultiReferenceMdDTO mdDTO = ( (AttributeMultiReferenceDTO) attributeDTO ).getAttributeMdDTO();

      String referencedMdBusiness = mdDTO.getReferencedMdBusiness();

      // referenced mdbusiness
      metadata.put(JSON.REFERENCE_METADATA_REFERENCED_MD_BUSINESS.getLabel(), referencedMdBusiness);

      return metadata;
    }

    /**
     * Sets the MultiReference attribute, which includes creating a DOM
     * representation of all item ids.
     */
    protected JSONObject getAttribute() throws JSONException
    {
      JSONObject attribute = super.getAttribute();

      // container array to hold all item ids
      JSONArray itemIdsJSON = new JSONArray();

      AttributeMultiReferenceDTO attributeMultiReferenceDTO = (AttributeMultiReferenceDTO) attributeDTO;
      List<String> itemIds = attributeMultiReferenceDTO.getItemIds();
      for (String itemId : itemIds)
      {
        itemIdsJSON.put(itemId);
      }

      attribute.put(JSON.MULTI_REFERENCE_ITEM_IDS.getLabel(), itemIdsJSON);

      return attribute;
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
    private AttributeMultiTermDTOHandler(AttributeMultiTermDTO attributeDTO)
    {
      super(attributeDTO);
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

    /**
     * Sets the metadata for AttributeCharacterDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeCharacterMdDTO mdDTO = ( (AttributeCharacterDTO) attributeDTO ).getAttributeMdDTO();

      int size = mdDTO.getSize();

      metadata.put(JSON.CHARACTER_METADATA_SIZE.getLabel(), size);

      return metadata;
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = attributeDTO.getValue();
      if (value != null)
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), value);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
    }
  }

  private class AttributeTextDTOHandler extends AttributeDTOHandler
  {
    private AttributeTextDTOHandler(AttributeTextDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = attributeDTO.getValue();
      if (value != null)
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), value);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
    }
  }

  private class AttributeClobDTOHandler extends AttributeDTOHandler
  {
    private AttributeClobDTOHandler(AttributeClobDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = attributeDTO.getValue();
      if (value != null)
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), value);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
    }
  }

  /**
   * Sets the metadata for AttributeEncryptionDTOs
   */
  private class AttributeEncryptionDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeEncryptionDTOHandler(AttributeEncryptionDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeEncryptionDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeEncryptionMdDTO mdDTO = ( (AttributeEncryptionDTO) attributeDTO ).getAttributeMdDTO();

      String encryptionMethod = mdDTO.getEncryptionMethod();

      metadata.put(JSON.ENCRYPTION_METADATA_ENCRYPTION_METHOD.getLabel(), encryptionMethod);

      return metadata;
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      // don't set values for encryption
      attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), "");
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
     * Sets the metadata for AttributeEnumerationDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeStructMdDTO mdDTO = ( (AttributeStructDTO) attributeDTO ).getAttributeMdDTO();

      String definingMdBusiness = mdDTO.getDefiningMdStruct();

      // defining mdstruct
      metadata.put(JSON.STRUCT_METADATA_DEFINING_MDSTRUCT.getLabel(), definingMdBusiness);

      return metadata;
    }

    /**
     * Sets the Struct attribute.
     * 
     * @throws JSONException
     */
    protected JSONObject getAttribute() throws JSONException
    {
      JSONObject attribute = super.getAttribute();

      AttributeStructDTO attributeStructDTO = (AttributeStructDTO) attributeDTO;

      StructDTO structDTO = attributeStructDTO.getStructDTO();
      if (structDTO != null)
      {
        if (structDTO instanceof LocalStructDTO)
        {
          LocalStructDTOToJSON structDTOToJSON = new LocalStructDTOToJSON((LocalStructDTO) structDTO);
          attribute.put(JSON.STRUCT_STRUCT_DTO.getLabel(), structDTOToJSON.populate());
        }
        else
        {
          StructDTOToJSON structDTOToJSON = new StructDTOToJSON(structDTO);
          attribute.put(JSON.STRUCT_STRUCT_DTO.getLabel(), structDTOToJSON.populate());
        }
      }
      else
      {
        attribute.put(JSON.STRUCT_STRUCT_DTO.getLabel(), JSONObject.NULL);
      }

      return attribute;
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
     * Sets the metadata for AttributeReferenceDTOs
     */
    protected JSONObject setMetadata() throws JSONException
    {
      JSONObject metadata = super.setMetadata();

      AttributeReferenceMdDTO mdDTO = ( (AttributeReferenceDTO) attributeDTO ).getAttributeMdDTO();

      String referencedMdBusiness = mdDTO.getReferencedMdBusiness();

      // referenced mdbusiness
      metadata.put(JSON.REFERENCE_METADATA_REFERENCED_MD_BUSINESS.getLabel(), referencedMdBusiness);

      return metadata;
    }
  }

  private class AttributeTermDTOHandler extends AttributeReferenceDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    private AttributeTermDTOHandler(AttributeTermDTO attributeDTO)
    {
      super(attributeDTO);
    }
  }

  private class AttributeDateTimeDTOHandler extends AttributeDTOHandler
  {
    private AttributeDateTimeDTOHandler(AttributeDateTimeDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = this.attributeDTO.getValue();
      if (value != null && value.trim().length() != 0)
      {
        Date date = MdAttributeDateTimeUtil.getTypeSafeValue(value);
        String isoDate = DateUtilities.formatISO8601(date);

        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), isoDate);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
    }
  }

  private class AttributeDateDTOHandler extends AttributeDTOHandler
  {
    private AttributeDateDTOHandler(AttributeDateDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = this.attributeDTO.getValue();
      if (value != null && value.trim().length() != 0)
      {
        Date date = MdAttributeDateUtil.getTypeSafeValue(value);
        String isoDate = DateUtilities.formatISO8601(date);

        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), isoDate);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
    }
  }

  private class AttributeTimeDTOHandler extends AttributeDTOHandler
  {
    private AttributeTimeDTOHandler(AttributeTimeDTO attributeDTO)
    {
      super(attributeDTO);
    }

    @Override
    protected void setValue(JSONObject attribute) throws JSONException
    {
      String value = this.attributeDTO.getValue();
      if (value != null && value.trim().length() != 0)
      {
        Date date = MdAttributeTimeUtil.getTypeSafeValue(value);
        String isoDate = DateUtilities.formatISO8601(date);

        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), isoDate);
      }
      else
      {
        attribute.put(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel(), JSONObject.NULL);
      }
    }
  }
}
