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
package com.runwaysdk.transport.metadata;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.conversion.dom.Elements;

/**
 * Builds the metadata for an attribute enumeration.
 */
public class CommonAttributeEnumerationMdBuilder extends CommonAttributeMdBuilder
{

  /**
   * Flag denoting if this enumeration supports multiple item select or not.
   */
  private boolean selectMultiple;

  /**
   * Map of enum names and display labels.
   * Key: name of the enum item
   * Value: display label of the item.
   */
  private Map<String, String> enumNameMap;

  private String referencedMdEnumeration;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeEnumerationMdBuilder(AttributeEnumerationMdDTO source, AttributeEnumerationMdDTO dest)
  {
    super(source, dest);
    selectMultiple = source.selectMultiple();
    enumNameMap = source.getEnumItems();
    referencedMdEnumeration = source.getReferencedMdEnumeration();
  }

  /**
   *
   * @param metadata
   * @param dest
   */
  protected CommonAttributeEnumerationMdBuilder(Node metadata, Node properties, AttributeEnumerationMdDTO dest)
  {
    super(metadata, properties, dest);

    this.enumNameMap = new HashMap<String, String>();

    try
    {
      this.selectMultiple = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_METADATA_SELECT_MULTIPLE.getLabel(), metadata, XPathConstants.STRING));
      this.referencedMdEnumeration = (String)ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_METADATA_REFERENCED_MD_ENUMERATION.getLabel(), metadata, XPathConstants.STRING);

      Node enumItems = (Node)ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_METADATA_ENUM_ITEMS.getLabel(), metadata, XPathConstants.NODE);

      NodeList enumItemList = (NodeList)ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_METADATA_ENUM_ITEM.getLabel(), enumItems, XPathConstants.NODESET);

      for (int i=0; i<enumItemList.getLength(); i++)
      {
        Node enumItemNode = enumItemList.item(i);
        Node enumNameNode = (Node)ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_METADATA_ENUM_NAME.getLabel(), enumItemNode, XPathConstants.NODE);
        Node enumDisplayLabel = (Node)ConversionFacade.getXPath().evaluate(Elements.ENUMERATION_METADATA_ENUM_DISPLAY_LABEL.getLabel(), enumItemNode, XPathConstants.NODE);

        this.enumNameMap.put(enumNameNode.getTextContent(), enumDisplayLabel.getTextContent());
      }
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }

  /**
   * Builds the metadata.
   */
  protected void build()
  {
    super.build();

    AttributeEnumerationMdDTO destSafe = (AttributeEnumerationMdDTO) dest;

    destSafe.setSelectMultiple(selectMultiple);

    destSafe.setReferencedMdEnumeration(referencedMdEnumeration);

    destSafe.setEnumItems(enumNameMap);
  }

}
