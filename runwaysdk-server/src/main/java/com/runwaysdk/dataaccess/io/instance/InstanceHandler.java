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

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;


/**
 * Parses instance tags and adds instances of a defined Class to the database
 * @author Justin Smethie
 * @date 6/02/06
 */
public class InstanceHandler extends ElementHandler
{

  /**
   * Creates an instance of a BusinessDAO    
   * 
   * @param reader The XMLReader stream
   * @param previousHandler The XMLHandler in which control was passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param attributes The attributes of the instance tag
   * @param locator The locator of the XMLReader stream
   */
  public InstanceHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes) throws SAXException
  {
    this(0L, reader, previousHandler, manager, resolver, attributes);
  }
  
  /**
   * Creates an instance of a BusinessDAO 
   * 
   * @param importSeq seqence number of the imported object, or 0 if it is not known.
   * @param reader The XMLReader stream
   * @param previousHandler The XMLHandler in which control was passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param attributes The attributes of the instance tag
   * @param locator The locator of the XMLReader stream
   */
  public InstanceHandler(Long importSeq, XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes) throws SAXException
  {
    this(importSeq, reader, previousHandler, manager, resolver, attributes, false);
  }
  
  public InstanceHandler(Long importSeq, XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes, Boolean ignoreSeq) throws SAXException
  {
    super(reader, previousHandler, manager, resolver, ignoreSeq);
            
    String type = attributes.getValue(XMLTags.TYPE_TAG);
    String databaseId = attributes.getValue(XMLTags.ID_TAG);
    
    //Import a new 'type' BusinessDAO
    //Ensure that the BusinessDAO does not already exist
    //If the BusinessDAO already exists but is not as recent then
    //overwrite the BusinessDAO with import in the BusinessDAO
    try
    {
      elementDAO = BusinessDAO.get(databaseId).getBusinessDAO();
      
      sequence = elementDAO.getSequence();
      siteMaster = elementDAO.getSiteMaster();
      
      if (ignoreSeq || (importSeq > sequence))
      {
        this.skipProcessing = false;
      }
      else
      {
        this.skipProcessing = true;
      }
    }
    catch(DataAccessException e)
    {
      elementDAO = BusinessDAO.newInstance(type);
      isNew = true;
      
      //Set the imported oid
      Attribute attribute = elementDAO.getAttribute(EntityInfo.OID);
      attribute.setValue(databaseId);
      
      this.skipProcessing = false;
    }
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.io.instance.EntityHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if(localName.equals(XMLTags.OBJECT_TAG))
    {
      super.endElement(namespaceURI, localName, fullName);
    }
  }
}
