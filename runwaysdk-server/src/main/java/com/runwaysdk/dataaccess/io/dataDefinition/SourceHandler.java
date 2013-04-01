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

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 * Parses the {@link XMLTags#MESSAGES_TAG}, {@link XMLTags#STUB_SOURCE_TAG} and  {@link XMLTags#DTO_STUB_SOURCE_TAG} tags.
 * 
 * @author Justin Smethie
 */
public class SourceHandler extends XMLHandler
{
  /**
   * Keeps track of the text(value) which is parsed.
   */
  private StringBuffer buffer;
  
  /**
   * MdClass being imported
   */
  private MdTypeDAO mdType;
   
  private String attributeName;
  
     
  /**
   * Constructor
   * 
   * @param reader The XMLReader which reads the XML document
   * @param previousHandler The handler which control passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param mdType The MdClass of which the source attribute is being parsed
   * @param attributeName TODO
   */
  public SourceHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdTypeDAO mdType, String attributeName)
  {
    super(reader, previousHandler, manager);
    
    this.buffer = new StringBuffer();
    this.mdType = mdType;
    this.attributeName = attributeName;
  }
  
  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    //Remove all white spaces before and after the text
    String attributeValue = buffer.toString().trim();
    
    mdType.setValue(attributeName, attributeValue);    
    mdType.apply();
    
    reader.setContentHandler( previousHandler );
    reader.setErrorHandler( previousHandler );
  }
  
  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    buffer.append(new String(ch, start, length));
  }
}
