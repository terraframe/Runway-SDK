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
package com.runwaysdk.dataaccess.io.instance;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;


/**
 * Parses selection tags to instianted an AttributeEnumeration with multiple reference to other instances.
 * Makes use of the tempelate pattern to deal with differences when the AttributeEnumeration belongs
 * to an EntityDAO and an AttributeStruct.
 * 
 * @author Justin Smethie
 * @date 06/09/06
 */
public abstract class EnumerationHandler extends XMLHandler
{
  /**
   * Creates an instance of a MdAttributeEnumeration.
   * @param reader The XMLReader stream
   * @param previousHandler The XMLHandler in which control was passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param current The EntityDAO which defines the attribute-multiple oid mapping     
   * @param attributes The attributes of the instance tag
   */
  public EnumerationHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager) throws SAXException
  {
    super(reader, previousHandler, manager);    
  }
    
  /**
   * Parses the instance_ref tag
   * Inherited from ContentHandler
   * (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if(localName.equals(XMLTags.VALUE_TAG))
    {
      String value = attributes.getValue(XMLTags.ATTRIBUTE_VALUE_TAG);

      this.addItem(value);
    }
  }
  
  /**
   * When the selection is closed:
   * Returns parsing control back to the Handler which passed control
   * 
   * Inherits from ContentHandler
   *  (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if(localName.equals(XMLTags.ENUMERATION_TAG))
    {
      reader.setContentHandler( previousHandler );
      reader.setErrorHandler( previousHandler );
    }
  }
  
  /**
   * Adds an item to the AttributeEnumeration.
   *  
   * @param value A reference to BusinessDAO to be included in the AttributeEnumeration.
   */
  protected abstract void addItem(String value);
  

}
