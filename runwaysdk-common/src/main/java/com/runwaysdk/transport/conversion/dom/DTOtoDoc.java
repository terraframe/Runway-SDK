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
package com.runwaysdk.transport.conversion.dom;

import java.util.Map;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEncryptionDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.util.Base64;

public abstract class DTOtoDoc
{
  /**
   * The constructing document.
   */
  private Document document;

  /**
   * Constructor to set the constructing document.
   * 
   * @param document
   */
  public DTOtoDoc(Document document)
  {
    this.document = document;
  }

  /**
   * Returns the destination document.
   * 
   * @return
   */
  protected Document getDocument()
  {
    return document;
  }

  /**
   * True if the metadata should be included in the document, false otherwise.
   * 
   * @return metadata should be included in the document, false otherwise.
   */
  protected abstract boolean convertMetaData();

  /**
   * Populates and returns a node with the given AttributeDTO data.
   * 
   * @param attributeDTO
   * @return
   */
  protected Node setAttribute(AttributeDTO attributeDTO)
  {
    AttributeDTOHandler handler = null; // the attribute handler to set
                                        // attribute DOM

    if (attributeDTO instanceof AttributeCharacterDTO)
    {
      handler = new AttributeCharacterDTOHandler((AttributeCharacterDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeEnumerationDTO)
    {
      handler = new AttributeEnumerationDTOHandler((AttributeEnumerationDTO) attributeDTO);
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
    else if (attributeDTO instanceof AttributeBlobDTO)
    {
      handler = new AttributeBlobDTOHandler((AttributeBlobDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeTermDTO)
    {
      handler = new AttributeTermDTOHandler((AttributeTermDTO) attributeDTO);
    }
    else if (attributeDTO instanceof AttributeReferenceDTO)
    {
      handler = new AttributeReferenceDTOHandler((AttributeReferenceDTO) attributeDTO);
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
    protected AttributeDTOHandler(AttributeDTO attributeDTO)
    {
      this.attributeDTO = attributeDTO;
    }

    /**
     * Returns the constructed Node representing attribute metadata. Subclasses
     * should override this to add extra metadata.
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      AttributeMdDTO mdDTO = attributeDTO.getAttributeMdDTO();
      String displayLabel = mdDTO.getDisplayLabel();
      String description = mdDTO.getDescription();
      boolean required = mdDTO.isRequired();
      boolean immutable = mdDTO.isImmutable();
      String id = mdDTO.getId();
      boolean system = mdDTO.isSystem();

      // display label
      Element dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_DISPLAY_LABEL.getLabel());
      CDATASection cdata = document.createCDATASection(displayLabel);
      dataNode.appendChild(cdata);
      metadata.appendChild(dataNode);

      // description
      dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_DESCRIPTION.getLabel());
      cdata = document.createCDATASection(description);
      dataNode.appendChild(cdata);
      metadata.appendChild(dataNode);

      // id
      dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_ID.getLabel());
      dataNode.setTextContent(id);
      metadata.appendChild(dataNode);

      // required
      dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_REQUIRED.getLabel());
      dataNode.setTextContent(Boolean.toString(required));
      metadata.appendChild(dataNode);

      // immutable
      dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_IMMUTABLE.getLabel());
      dataNode.setTextContent(Boolean.toString(immutable));
      metadata.appendChild(dataNode);

      // system
      dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_SYSTEM.getLabel());
      dataNode.setTextContent(Boolean.toString(system));
      metadata.appendChild(dataNode);

      // generateAccessor
      boolean generateAccessor = mdDTO.getGenerateAccessor();
      dataNode = document.createElement(Elements.ATTRIBUTE_METADATA_GENERATE_ACCESSOR.getLabel());
      dataNode.setTextContent(Boolean.toString(generateAccessor));
      metadata.appendChild(dataNode);
    }

    /**
     * Returns the constructed Node representing attribute properties.
     * Subclasses should override this to add extra metadata.
     * 
     * @return
     */
    protected Node setProperties()
    {
      Element properties = document.createElement(Elements.ATTRIBUTE_PROPERTIES.getLabel());

      // set the attribute data that all attributes share
      // Get the attribute information
      String name = attributeDTO.getName();
      String type = attributeDTO.getType();
      String value = attributeDTO.getValue();
      boolean isReadable = attributeDTO.isReadable();
      boolean isWritable = attributeDTO.isWritable();
      boolean isModified = attributeDTO.isModified();

      // add the name
      Element dataNode = document.createElement(Elements.ATTRIBUTE_NAME.getLabel());
      dataNode.setTextContent(name);
      properties.appendChild(dataNode);

      // add the attribute type
      dataNode = document.createElement(Elements.ATTRIBUTE_TYPE.getLabel());
      dataNode.setTextContent(type);
      properties.appendChild(dataNode);

      // add the value
      dataNode = document.createElement(Elements.ATTRIBUTE_VALUE.getLabel());
      CDATASection cdata = setAttributeValue(value);
      dataNode.appendChild(cdata);
      properties.appendChild(dataNode);

      // add the readable value
      dataNode = document.createElement(Elements.ATTRIBUTE_READABLE.getLabel());
      dataNode.setTextContent(Boolean.toString(isReadable));
      properties.appendChild(dataNode);

      // add the writable value
      dataNode = document.createElement(Elements.ATTRIBUTE_WRITABLE.getLabel());
      dataNode.setTextContent(Boolean.toString(isWritable));
      properties.appendChild(dataNode);

      // add the modified value
      dataNode = document.createElement(Elements.ATTRIBUTE_MODIFIED.getLabel());
      dataNode.setTextContent(Boolean.toString(isModified));
      properties.appendChild(dataNode);

      return properties;
    }

    protected CDATASection setAttributeValue(String value)
    {
      return document.createCDATASection(value);
    }

    /**
     * Returns the constructed Node representing an attribute.
     * 
     * @return
     */
    protected Node getAttribute()
    {
      Element attribute = document.createElement(attributeDTO.getClass().getSimpleName());

      if (convertMetaData())
      {
        Element metadata = document.createElement(Elements.ATTRIBUTE_METADATA.getLabel());
        this.setMetadata(metadata);
        // set the attribute metadata
        attribute.appendChild(metadata);
      }

      attribute.appendChild(this.setProperties());

      return attribute;
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
    protected AttributeNumberDTOHandler(AttributeNumberDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeNumberDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeNumberMdDTO mdDTO = ( (AttributeNumberDTO) attributeDTO ).getAttributeMdDTO();
      boolean rejectZero = mdDTO.rejectZero();
      boolean rejectNegative = mdDTO.rejectNegative();
      boolean rejectPositive = mdDTO.rejectPositive();

      // reject zero
      Element dataNode = document.createElement(Elements.NUMBER_METADATA_REJECT_ZERO.getLabel());
      dataNode.setTextContent(Boolean.toString(rejectZero));
      metadata.appendChild(dataNode);

      // reject negative
      dataNode = document.createElement(Elements.NUMBER_METADATA_REJECT_NEGATIVE.getLabel());
      dataNode.setTextContent(Boolean.toString(rejectNegative));
      metadata.appendChild(dataNode);

      // reject positive
      dataNode = document.createElement(Elements.NUMBER_METADATA_REJECT_POSITIVE.getLabel());
      dataNode.setTextContent(Boolean.toString(rejectPositive));
      metadata.appendChild(dataNode);
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
    protected AttributeDecDTOHandler(AttributeDecDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeDecDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeDecMdDTO mdDTO = ( (AttributeDecDTO) attributeDTO ).getAttributeMdDTO();
      int totalLength = mdDTO.getTotalLength();
      int decimalLength = mdDTO.getDecimalLength();

      // total length
      Element dataNode = document.createElement(Elements.DEC_METADATA_TOTAL_LENGTH.getLabel());
      dataNode.setTextContent(Integer.toString(totalLength));
      metadata.appendChild(dataNode);

      // decimal length
      dataNode = document.createElement(Elements.DEC_METADATA_DECIMAL_LENGTH.getLabel());
      dataNode.setTextContent(Integer.toString(decimalLength));
      metadata.appendChild(dataNode);
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
    protected AttributeEnumerationDTOHandler(AttributeEnumerationDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeEnumerationDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeEnumerationMdDTO mdDTO = ( (AttributeEnumerationDTO) attributeDTO ).getAttributeMdDTO();

      boolean selectMultiple = mdDTO.selectMultiple();
      // select multiple
      Element dataNode = document.createElement(Elements.ENUMERATION_METADATA_SELECT_MULTIPLE.getLabel());
      dataNode.setTextContent(Boolean.toString(selectMultiple));
      metadata.appendChild(dataNode);

      // Type of the MdEnumeration
      Element typeNode = document.createElement(Elements.ENUMERATION_METADATA_REFERENCED_MD_ENUMERATION.getLabel());
      typeNode.setTextContent(mdDTO.getReferencedMdEnumeration());
      metadata.appendChild(typeNode);

      // enum values
      Element enumItems = document.createElement(Elements.ENUMERATION_METADATA_ENUM_ITEMS.getLabel());
      metadata.appendChild(enumItems);

      Map<String, String> enumNameMap = mdDTO.getEnumItems();
      for (String enumName : enumNameMap.keySet())
      {
        Element enumItemNode = document.createElement(Elements.ENUMERATION_METADATA_ENUM_ITEM.getLabel());
        enumItems.appendChild(enumItemNode);

        Element enumNameNode = document.createElement(Elements.ENUMERATION_METADATA_ENUM_NAME.getLabel());
        enumNameNode.setTextContent(enumName);
        enumItemNode.appendChild(enumNameNode);

        Element enumDisplayLabelNode = document.createElement(Elements.ENUMERATION_METADATA_ENUM_DISPLAY_LABEL.getLabel());
        enumDisplayLabelNode.setTextContent(enumNameMap.get(enumName));
        enumItemNode.appendChild(enumDisplayLabelNode);
      }
    }

    /**
     * Returns the constructed Node representing attribute properties.
     * Subclasses should override this to add extra metadata.
     * 
     * @return
     */
    protected Node setProperties()
    {
      Node properties = super.setProperties();

      // container element to hold all enum values
      Element enumItemsNode = document.createElement(Elements.ENUMERATION_ENUM_ITEMS.getLabel());
      properties.appendChild(enumItemsNode);

      AttributeEnumerationDTO attributeEnumerationDTO = (AttributeEnumerationDTO) attributeDTO;

      Map<String, String> enumMap = attributeEnumerationDTO.getEnumItems();

      for (String enumName : enumMap.keySet())
      {
        Element enumNameNode = document.createElement(Elements.ENUMERATION_ENUM_NAME.getLabel());
        enumNameNode.setTextContent(enumName);
        enumItemsNode.appendChild(enumNameNode);
      }

      return properties;
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
    protected AttributeCharacterDTOHandler(AttributeCharacterDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeCharacterDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeCharacterMdDTO mdDTO = ( (AttributeCharacterDTO) attributeDTO ).getAttributeMdDTO();

      int size = mdDTO.getSize();

      // defining mdbusiness
      Element dataNode = document.createElement(Elements.CHARACTER_METADATA_SIZE.getLabel());
      dataNode.setTextContent(Integer.toString(size));
      metadata.appendChild(dataNode);
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
    protected AttributeEncryptionDTOHandler(AttributeEncryptionDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeEncryptionDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeEncryptionMdDTO mdDTO = ( (AttributeEncryptionDTO) attributeDTO ).getAttributeMdDTO();

      String encryptionMethod = mdDTO.getEncryptionMethod();

      // defining mdbusiness
      Element dataNode = document.createElement(Elements.ENCRYPTION_METADATA_ENCRYPTION_METHOD.getLabel());
      dataNode.setTextContent(encryptionMethod);
      metadata.appendChild(dataNode);
    }
  }

  /**
   * Sets the metadata for AttributeBlobDTOs
   */
  private class AttributeBlobDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeBlobDTOHandler(AttributeBlobDTO attributeDTO)
    {
      super(attributeDTO);
    }

    protected CDATASection setAttributeValue(String value)
    {
      AttributeBlobDTO blobDTO = (AttributeBlobDTO) attributeDTO;
      String base64Data = Base64.encodeToString(blobDTO.getBlob(), false);

      return document.createCDATASection(base64Data);
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
    protected AttributeStructDTOHandler(AttributeStructDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeEnumerationDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeStructMdDTO mdDTO = ( (AttributeStructDTO) attributeDTO ).getAttributeMdDTO();

      String definingMdStruct = mdDTO.getDefiningMdStruct();

      // defining mdstruct
      Element dataNode = document.createElement(Elements.STRUCT_METADATA_DEFINING_MDSTRUCT.getLabel());
      dataNode.setTextContent(definingMdStruct);
      metadata.appendChild(dataNode);
    }

    /**
     * Returns the constructed Node representing attribute properties.
     * Subclasses should override this to add extra metadata.
     * 
     * @return
     */
    protected Node setProperties()
    {
      Node properties = super.setProperties();

      AttributeStructDTO attributeStructDTO = (AttributeStructDTO) attributeDTO;

      StructDTOtoDoc structToDoc = new StructDTOtoDoc(attributeStructDTO.getStructDTO(), document, convertMetaData());
      Node structDOM = structToDoc.populate();
      properties.appendChild(structDOM);

      return properties;
    }
  }

  /**
   * Sets the metadata for AttributeReferenceDTOs
   */
  private class AttributeReferenceDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeReferenceDTOHandler(AttributeReferenceDTO attributeDTO)
    {
      super(attributeDTO);
    }

    /**
     * Sets the metadata for AttributeEnumerationDTOs
     * 
     * @param metadata
     */
    protected void setMetadata(Element metadata)
    {
      super.setMetadata(metadata);

      AttributeReferenceMdDTO mdDTO = ( (AttributeReferenceDTO) attributeDTO ).getAttributeMdDTO();

      String referencedMdBusiness = mdDTO.getReferencedMdBusiness();

      // referenced MdBusiness
      Element dataNode = document.createElement(Elements.REFERENCE_METADATA_REFERENCED_MD_BUSINESS.getLabel());
      dataNode.setTextContent(referencedMdBusiness);
      metadata.appendChild(dataNode);
    }
  }

  /**
   * Sets the metadata for AttributeTermDTOs
   */
  private class AttributeTermDTOHandler extends AttributeReferenceDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeTermDTOHandler(AttributeTermDTO attributeDTO)
    {
      super(attributeDTO);
    }
  }
}
