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
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public interface HandlerFactoryIF
{
  /**
   * Creates a new handler to parse the given tag which is a child of the {@link XMLTags#RUNWAY_TAG}.
   *
   * @param localName Name of the tag to parse
   * @param attributes Attributes of the tag
   * @param reader {@link XMLReader} stream reading the .xml document
   * @param previousHandler The {@link XMLHandler} to return control to after the tag
   * @param manager Tracks the status of the import.
   * @return A new handler to parse the given tag.
   */
  public XMLHandler getHandler(String localName, Attributes attributes, XMLReader reader, XMLHandler handler, ImportManager manager);
}
