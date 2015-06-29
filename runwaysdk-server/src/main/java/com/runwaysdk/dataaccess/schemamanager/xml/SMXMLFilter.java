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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
/**
 * An XMLFilter designed to suppress all the start-element/end-element events appearing inside
 * the undoIt tag.
 * 
 * @author runway
 *
 */
public class SMXMLFilter extends XMLFilterImpl
{
  /**
   * The events occurring only inside this tag is passed by the present handler
   */
  private String passTag;
  
  public SMXMLFilter (String passTag) {
    this.passTag = passTag;
  }
  
  public SMXMLFilter(XMLReader reader) {
     super (reader);
  }
  private boolean isBlocked = true;

  @Override
  public void startElement(String uri, String localName, String name, Attributes atts)
      throws SAXException
  {
    
    if (localName.equals(passTag)) {
      unblock();
    }
    if (!isBlocked()) {
      super.startElement(uri, localName, name, atts);
    }
      
  }

  private boolean isBlocked()
  {
    return isBlocked;
  }

  private void unblock()
  {
    isBlocked = false;
  }

  @Override
  public void endElement(String uri, String localName, String name) throws SAXException
  {
    if (localName.equals(passTag)) {
      block();
    }
    // TODO Auto-generated method stub
    if (!isBlocked()) {
      super.endElement(uri, localName, name); 
    }
     
  }

  private void block()
  {
    isBlocked = true;
  }

}
