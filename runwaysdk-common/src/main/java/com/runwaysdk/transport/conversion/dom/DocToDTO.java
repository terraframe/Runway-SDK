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

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeDecimalDTO;
import com.runwaysdk.transport.attributes.AttributeDoubleDTO;
import com.runwaysdk.transport.attributes.AttributeEncryptionDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeFloatDTO;
import com.runwaysdk.transport.attributes.AttributeHashDTO;
import com.runwaysdk.transport.attributes.AttributeIntegerDTO;
import com.runwaysdk.transport.attributes.AttributeLongDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeSymmetricDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;
import com.runwaysdk.util.Base64;

public abstract class DocToDTO
{

  /**
   * The root Element containing the DTO
   */
  private Element           rootElement;

  protected ClientRequestIF clientRequest;

  /**
   * Constructor to set the root containing the DTO
   * 
   * @param root
   */
  public DocToDTO(ClientRequestIF clientRequest, Element rootElement)
  {
    this.rootElement = rootElement;

    this.clientRequest = clientRequest;
  }

  /**
   * Returns the source root Node
   * 
   * @return
   */
  protected Node getRootElement()
  {
    return rootElement;
  }

  protected AttributeDTO setAttribute(Node attribute)
  {
    // map the node name (the AttributeDTO.class.getSimpleName()) to a handler
    String nodeName = attribute.getNodeName();

    AttributeDTOHandler handler = null; // the attribute handler to set
                                        // attribute DOM

    // enumeration
    if (nodeName.equals(AttributeEnumerationDTO.class.getSimpleName()))
    {
      handler = new AttributeEnumerationDTOHandler(attribute);
    }
    // struct
    else if (nodeName.equals(AttributeStructDTO.class.getSimpleName()))
    {
      handler = new AttributeStructDTOHandler(this.clientRequest, attribute);
    }
    // decimal, double, float
    else if (nodeName.equals(AttributeDecimalDTO.class.getSimpleName()) || nodeName.equals(AttributeDoubleDTO.class.getSimpleName()) || nodeName.equals(AttributeFloatDTO.class.getSimpleName()))
    {
      handler = new AttributeDecDTOHandler(attribute);
    }
    // integer, long
    else if (nodeName.equals(AttributeIntegerDTO.class.getSimpleName()) || nodeName.equals(AttributeLongDTO.class.getSimpleName()))
    {
      handler = new AttributeNumberDTOHandler(attribute);
    }
    // hash, symmetric
    else if (nodeName.equals(AttributeHashDTO.class.getSimpleName()) || nodeName.equals(AttributeSymmetricDTO.class.getSimpleName()))
    {
      handler = new AttributeEncryptionDTOHandler(attribute);
    }
    // character
    else if (nodeName.equals(AttributeCharacterDTO.class.getSimpleName()))
    {
      handler = new AttributeCharacterDTOHandler(attribute);
    }
    // boolean
    else if (nodeName.equals(AttributeBooleanDTO.class.getSimpleName()))
    {
      handler = new AttributeBooleanDTOHandler(attribute);
    }
    // blob
    else if (nodeName.equals(AttributeBlobDTO.class.getSimpleName()))
    {
      handler = new AttributeBlobDTOHandler(attribute);
    }
    // term
    else if (nodeName.equals(AttributeTermDTO.class.getSimpleName()))
    {
      handler = new AttributeTermDTOHandler(attribute);
    }
    // reference
    else if (nodeName.equals(AttributeReferenceDTO.class.getSimpleName()))
    {
      handler = new AttributeReferenceDTOHandler(attribute);
    }
    // boolean, text
    else
    {
      handler = new AttributeDTOHandler(attribute);
    }

    return handler.getAttribute();
  }

  /**
   * Inner class to handle AttributeDTOs
   */
  private class AttributeDTOHandler
  {
//    protected Node         attribute;

    /**
     * Direct reference to the attribute metadata node (for convenience).
     */
    protected Node         metadata;

    /**
     * Direct reference to the node that contains properties of the attribute
     * (for convenience).
     */
    protected Node         properties;

    protected AttributeDTO dest;

    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeDTOHandler(Node attribute)
    {
      try
      {
        this.metadata = ( (Node) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA.getLabel(), attribute, XPathConstants.NODE) );

        this.properties = (Node) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_PROPERTIES.getLabel(), attribute, XPathConstants.NODE);
      }
      catch (XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: " + ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }

      dest = null; // this is created in getAttribute()
    }

    /**
     * Sets the metadata on an attribute. Note that this was intended for use in
     * getAttribute() only. Refer to AttributeDTOHandler.getAttribute(). Also
     * note that each subclass of AttributeDTOHandler must override this class
     * to provide the correct CommonAttributeFacade method.
     * 
     * @return
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setAttributeMetadata(metadata, properties, dest.getAttributeMdDTO());
    }

    /**
     * Returns the constructed Node representing an attribute.
     * 
     * @return
     */
    protected AttributeDTO getAttribute()
    {
      try
      {
        String name = (String) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_NAME.getLabel(), properties, XPathConstants.STRING);
        String type = (String) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_TYPE.getLabel(), properties, XPathConstants.STRING);

        Node cdata = (CDATASection) ( (Node) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_VALUE.getLabel(), properties, XPathConstants.NODE) ).getFirstChild();

        String value = "";
        if (cdata != null)
        {
          value = cdata.getTextContent();
        }
        boolean readable = Boolean.parseBoolean((String) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_READABLE.getLabel(), this.properties, XPathConstants.STRING));
        boolean writable = Boolean.parseBoolean((String) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_WRITABLE.getLabel(), this.properties, XPathConstants.STRING));
        boolean modified = Boolean.parseBoolean((String) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_MODIFIED.getLabel(), this.properties, XPathConstants.STRING));

        dest = AttributeDTOFactory.createAttributeDTO(name, type, value, readable, writable, modified);

        if (this.metadata != null)
        {
          this.setMetadata();
        }

        return dest;
      }
      catch (XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: " + ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }
      return null;
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
    protected AttributeNumberDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata for AttributeNumberDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setNumberMetadata(metadata, properties, ( (AttributeNumberDTO) dest ).getAttributeMdDTO());
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
    protected AttributeDecDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata for AttributeDecDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setDecMetadata(metadata, properties, ( (AttributeDecDTO) dest ).getAttributeMdDTO());
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
    protected AttributeEnumerationDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata for AttributeEnumerationDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setEnumerationMetadata(metadata, properties, ( (AttributeEnumerationDTO) dest ).getAttributeMdDTO());
    }

    /**
     * Sets the Enumeration attribute, which includes creating a DOM
     * representation of all enum values.
     */
    protected AttributeDTO getAttribute()
    {
      AttributeEnumerationDTO attributeEnumerationDTO = (AttributeEnumerationDTO) super.getAttribute();

      Node enumItemsNode = null;
      try
      {
        enumItemsNode = (Node) ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_ENUM_ITEMS.getLabel(), properties, XPathConstants.NODE);

        NodeList enumNameList = (NodeList) ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_ENUM_NAME.getLabel(), enumItemsNode, XPathConstants.NODESET);

        for (int i = 0; i < enumNameList.getLength(); i++)
        {
          String enumName = enumNameList.item(i).getTextContent();
          AttributeDTOFacade.addEnumItemInternal(attributeEnumerationDTO, enumName);
        }

      }
      catch (XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: " + ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }

      return attributeEnumerationDTO;
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
    protected AttributeCharacterDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata for AttributeCharacterDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setCharacterMetadata(metadata, properties, ( (AttributeCharacterDTO) dest ).getAttributeMdDTO());
    }
  }

  /**
   * Sets the metadata for AttributeBooleanDTOs
   */
  private class AttributeBooleanDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeBooleanDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata for AttributeCharacterDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setBooleanMetadata(metadata, properties, ( (AttributeBooleanDTO) dest ).getAttributeMdDTO());
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
    protected AttributeEncryptionDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata for AttributeEncryptionDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setEncryptionMetadata(metadata, properties, ( (AttributeEncryptionDTO) dest ).getAttributeMdDTO());
    }
  }

  /**
   * Sets the metadata for AttributeEncryptionDTOs
   */
  private class AttributeBlobDTOHandler extends AttributeDTOHandler
  {
    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeBlobDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Returns the constructed Node representing an attribute.
     * 
     * @return
     */
    protected AttributeDTO getAttribute()
    {
      AttributeBlobDTO attributeBlobDTO = (AttributeBlobDTO) super.getAttribute();

      try
      {
        // get the attribute info to create a new attribute
        Node cdata = ( (Node) ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_NAME.getLabel(), properties, XPathConstants.NODE) ).getFirstChild();
        byte[] value = new byte[0];
        if (cdata != null && cdata instanceof CDATASection)
        {
          value = Base64.decode(cdata.getTextContent());
          attributeBlobDTO.setValue(value);
        }

        return attributeBlobDTO;
      }
      catch (XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: " + ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }
      return null;
    }
  }

  /**
   * Sets the metadata for AttributeStructDTOs
   */
  private class AttributeStructDTOHandler extends AttributeDTOHandler
  {

    private ClientRequestIF clientRequest;

    /**
     * Constructor
     * 
     * @param attributeDTO
     */
    protected AttributeStructDTOHandler(ClientRequestIF clientRequest, Node attribute)
    {
      super(attribute);
      this.clientRequest = clientRequest;
    }

    /**
     * Sets the metadata for AttributeEnumerationDTOs
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setStructMetadata(metadata, properties, ( (AttributeStructDTO) dest ).getAttributeMdDTO());
    }

    /**
     * Sets the Struct attribute, which includes creating a DOM to represent the
     * MdStruct it represents
     */
    protected AttributeDTO getAttribute()
    {
      AttributeStructDTO attributeStructDTO = (AttributeStructDTO) super.getAttribute();

      // get the struct node from the AttributeStructDTO node
      Element structNode = null;
      try
      {
        structNode = (Element) ConversionFacade.getXPath().evaluate(Elements.STRUCT_STRUCT_DTO.getLabel(), properties, XPathConstants.NODE);
      }
      catch (XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: " + ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }

      DocToStructDTO docToStruct = new DocToStructDTO(this.clientRequest, structNode);

      attributeStructDTO.setStructDTO(docToStruct.populate());

      return attributeStructDTO;
    }
  }

  /**
   * Sets the metadata and value for an attribute reference.
   * 
   */
  private class AttributeReferenceDTOHandler extends AttributeDTOHandler
  {

    /**
     * Constructor
     * 
     * @param attribute
     */
    protected AttributeReferenceDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata.
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setReferenceMetadata(metadata, properties, ( (AttributeReferenceDTO) dest ).getAttributeMdDTO());
    }
  }

  /**
   * Sets the metadata and value for an attribute reference.
   * 
   */
  private class AttributeTermDTOHandler extends AttributeDTOHandler
  {

    /**
     * Constructor
     * 
     * @param attribute
     */
    protected AttributeTermDTOHandler(Node attribute)
    {
      super(attribute);
    }

    /**
     * Sets the metadata.
     */
    protected void setMetadata()
    {
      CommonAttributeFacade.setTermMetadata(metadata, properties, ( (AttributeTermDTO) dest ).getAttributeMdDTO());
    }
  }
}
