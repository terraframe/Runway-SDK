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
package com.runwaysdk.dataaccess.io.instance;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;


/**
 * Parses selection tags to instianted an AttributeEnumeration with multiple reference to other instances.
 * Makes use of the tempelate pattern to deal with differences when the AttributeEnumeration belongs
 * to an EntityDAO and an AttributeStruct.  This class control behavior when the AttributeEnumeration
 * belongs to an EntityDAO.
 * 
 * @author Justin Smethie
 */
public class ElementEnumerationHandler extends EnumerationHandler
{ 
  /**
   * The AttributeEnumeration which is being imported.
   */
  private AttributeEnumeration attribute = null;
 
  /**
   * Creates an instance of a MdAttributeEnumeration.
   * @param reader The XMLReader stream
   * @param previousHandler The XMLHandler in which control was passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param current The EntityDAO which defines the attribute-multiple oid mapping     
   * @param attributes The attributes of the instance tag
   */
  public ElementEnumerationHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, EntityDAO current, Attributes attributes) throws SAXException
  {
    super(reader, previousHandler, manager);
    
    String attributeName = attributes.getValue(XMLTags.ATTRIBUTE_TAG);
    
    this.attribute = (AttributeEnumeration) current.getAttribute(attributeName);
    // Clear out all items, as the XML contains the new state of this enumeration
    this.attribute.clearItems();
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.io.instance.SelectorHandler#addItem(java.lang.String)
   */
  protected void addItem(String value)
  {
    attribute.addItem(value);
  }

}
