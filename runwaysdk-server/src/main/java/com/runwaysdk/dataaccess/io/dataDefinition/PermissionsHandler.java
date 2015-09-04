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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class PermissionsHandler extends XMLHandler
{
  /**
   * Constructs a handler for parsing the {@link XMLTags#PERMISSIONS_TAG} and it's
   * children.
   * 
   * @param reader {@link XMLReader} stream reading the .xml document
   * @param previousHandler The {@link XMLHandler} to return control to after
   *                        parsing the {@link XMLTags#PERMISSIONS_TAG}
   * @param manager Tracks the status of the import.
   */
  public PermissionsHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);
    
    manager.enterPermissionsState();
  }
  
  @Override
  public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
  {
//    XMLHandler handler = new PermissionHandlerFactory().getHandler(localName, attributes, this, manager);
//
//    // Pass control of the parsin to the new handler
//    if (handler != null)
//    {
//      reader.setContentHandler(handler);
//      reader.setErrorHandler(handler);
//    }
  }
  
  @Override
  public void endElement(String uri, String localName, String name) throws SAXException
  {
    if(localName.equals(XMLTags.PERMISSIONS_TAG))
    {
      manager.leavingCurrentState();
      reader.setContentHandler(previousHandler);
      reader.setContentHandler(previousHandler);
    }
  }

}
