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

import com.runwaysdk.dataaccess.io.ImportManager;

public interface TagHandlerIF
{

  /**
   * Delegation of SAX start element event
   * 
   * @param localName
   * @param attributes
   * @param context
   */
  public void onStartElement(String localName, Attributes attributes, TagContext context);

  /**
   * Delegation of SAX end element event
   * 
   * @param localName
   * @param attributes
   * @param context
   */
  public void onEndElement(String uri, String localName, String name, TagContext context);

  /**
   * Delegation of SAX characters event
   * 
   * @param localName
   * @param attributes
   * @param context
   */
  public void characters(char[] ch, int start, int length, TagContext context);

  /**
   * @return Unique identifier of
   */
  public String getKey();

  /**
   * Indicates if the handler will modify the manager state based upon the localName of the tag
   * 
   * @param localName
   * @return
   */
  public boolean modifiesState(String localName);

  /**
   * @return Manager being used in the import
   */
  public ImportManager getManager();
}
