/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;


public class StructAttributeHandler extends XMLHandler
{
  /**
   *  The current EntityDAO in scope
   */
  private EntityDAO current;
      
  /**
   * The name of the attriubte
   */
  private String structName;

  
  /**
   * Creates an instance of a MdAttributeEnumeration.
   * @param attributes The attributes of the instance tag
   * @param reader The XMLReader stream
   * @param previousHandler The XMLHandler in which control was passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param current The EntityDAO which defines the attribute-multiple id mapping     
   */
  public StructAttributeHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, EntityDAO current) throws SAXException
  {
    super(reader, previousHandler, manager);
    
    this.current = current;    
    this.structName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
  }
    
  /**
   * Parses the instance_tag tag and the include all tag
   * Inherited from ContentHandler
   * (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.ATTRIBUTE_TAG))
    {
      importInstanceValue(attributes);
    }
    else if(localName.equals(XMLTags.ATTRIBUTE_ENUMERATION_TAG))
    {
      AttributeEnumerationHandler sHandler = new AttributeEnumerationHandler(attributes, reader, this, manager, (ElementDAO) current, structName);
      reader.setContentHandler(sHandler);
      reader.setErrorHandler(sHandler);
    }
    else if(localName.equals(XMLTags.ATTRIBUTE_STRUCT_TAG))
    {
      StructAttributeHandler cHandler = new StructAttributeHandler(attributes, reader, this, manager, current);
      reader.setContentHandler(cHandler);
      reader.setErrorHandler(cHandler);
    }
  }
  
  /**
   * Add an attribute value to an instance StructDAO.
   * @param attributes The attributes of an instance_value tag
   */
  private void importInstanceValue(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    String value = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_VALUE_ATTRIBUTE);
    
    current.setStructValue(structName, name, value);
  }
    
  /**
   * When the composition is closed:
   * Returns parsing control back to the Handler which passed control
   * 
   * Inherits from ContentHandler
   *  (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if(localName.equals(XMLTags.ATTRIBUTE_STRUCT_TAG))
    {
      reader.setContentHandler( previousHandler );
      reader.setErrorHandler( previousHandler );
    }
  }
}
