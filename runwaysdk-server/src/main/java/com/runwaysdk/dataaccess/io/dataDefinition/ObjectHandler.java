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

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;


/**
 * Parses instance tags and adds instances of a defined Class to the database
 * @author Justin Smethie
 * @date 6/02/06
 */
public class ObjectHandler extends XMLHandler
{
  /**
   *  The current EntityDAO in scope
   */
  private EntityDAO current;

  /**
   *  The XML key of each instance
   */
  private String key;

  /**
   * The fully qualified name of the MdBusiness being instaited
   */
  private String type;

  /**
   * Creates an instance of a BusinessDAO.
   * @param attributes The attributes of the instance tag
   * @param reader The XMLReader stream
   * @param previousHandler The XMLHandler in which control was passed from
   * @param manager ImportManager which provides communication between handlers for a single import
   */
  public ObjectHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    importInstance(attributes);

    current = manager.getEntityDAO(type, key).getEntityDAO();
    
    String newKey = attributes.getValue(XMLTags.NEW_KEY_ATTRIBUTE);
    
    if(newKey != null)
    {
      key = newKey;
    }
    
    current.getAttribute(ComponentInfo.KEY).setValue(key);    
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
    else if(localName.equals(XMLTags.ATTRIBUTE_REFERENCE_TAG))
    {
      importInstanceRef(attributes);
    }
    else if(localName.equals(XMLTags.ATTRIBUTE_ENUMERATION_TAG))
    {
      AttributeEnumerationHandler sHandler = new AttributeEnumerationHandler(attributes, reader, this, manager, current);
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
   * Creates an instance of an object from the parse of the instance tag attributes
   * @param attributes The attributes of an instance tag
   */
  private final void importInstance(Attributes attributes)
  {
    key = attributes.getValue(XMLTags.KEY_ATTRIBUTE);
    type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    //Ensure that the class being referenced has already been defined
    if(!MdTypeDAO.isDefined(type))
    {
      String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.ENUMERATION_MASTER_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };
      SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, type, (current != null ? current.getKey() : ""));
    }    
  }

  /**
   * Add an attribute value to an instance BusinessDAO
   * @param attributes The attributes of an instance_value tag
   */
  private void importInstanceValue(Attributes attributes)
  {
    String attribute = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    String value = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_VALUE_ATTRIBUTE);

    current.setValue(attribute, value);
  }

  private void importInstanceRef(Attributes attributes)
  {
    String attributeRefName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    String referenceKey = attributes.getValue(XMLTags.KEY_ATTRIBUTE);

    MdAttributeDAOIF mdAttributeDAOIF = current.getAttributeIF(attributeRefName).getMdAttribute();

    if (!(mdAttributeDAOIF instanceof MdAttributeReferenceDAOIF))
    {
      String errMsg = "The attribute ["+mdAttributeDAOIF.definesAttribute()+"] on type ["+this.type+"] is not a reference attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttributeDAOIF.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttributeDAOIF, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeReferenceDAOIF mdAttributeReferenceDAOIF = (MdAttributeReferenceDAOIF)mdAttributeDAOIF;
    String referenceType = mdAttributeReferenceDAOIF.getReferenceMdBusinessDAO().definesType();

    String id = "";

    try
    {
      id = EntityDAO.getIdFromKey(referenceType, referenceKey);
    }
    catch(DataNotFoundException e)
    {
      if (!(referenceType.equals(this.type) && referenceKey.equals(key)))
      {
        SearchCriteriaIF criteria = new EntitySearchCriteria(referenceType, referenceKey, XMLTags.OBJECT_TAG);

        SearchHandler.searchEntity(manager, criteria, current.getKey());
      }
    }

    if (id.equals(""))
    {
      id = EntityDAO.getIdFromKey(referenceType, referenceKey);
    }

    current.setValue(attributeRefName, id);

  }

  /**
   * When the instance tag is closed:
   * Returns parsing control back to the Handler which passed control
   * Adds the instance BusinessDAO to the database
   * Adds a new XML puesdo id, database id mapping to the collection
   *
   * Inherits from ContentHandler
   *  (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if(localName.equals(XMLTags.OBJECT_TAG))
    {
      reader.setContentHandler( previousHandler );
      reader.setErrorHandler( previousHandler );

      //Ensure that the instance has not already been added into the database
      if (manager.isCreateState())
      {
        try
        {
          EntityDAO.getIdFromKey(this.type, this.key);
        }
        catch (DataNotFoundException e)
        {
          current.apply();
        }
      }
      else
      {
        current.apply();
      }
    }
  }
}
