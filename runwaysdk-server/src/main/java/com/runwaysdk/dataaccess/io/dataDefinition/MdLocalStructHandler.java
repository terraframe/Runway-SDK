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

import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;

public class MdLocalStructHandler extends MdEntityHandler
{
  /**
   *  The {@link MdLocalStructDAO} instance.
   */
  private MdLocalStructDAO          mdLocalStructDAO;

  /**
   * Handler Construction, creates a new MdLocalStructDAO.
   * @param attributes The XML attributes of the tag.
   * @param reader The XML parsing stream.
   * @param previousHandler The Handler in which control was passed from.
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param tagName The type to construct.  Can be either enumeration_class or standalone tag.
   */
  public MdLocalStructHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagName)
  {
    super(attributes, reader, previousHandler, manager, tagName);

    mdLocalStructDAO = (MdLocalStructDAO) manager.getEntityDAO(MdLocalStructInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();
    importLocalStruct(attributes);

    //Make sure the name has not already been defined
    if (!manager.isCreated(mdLocalStructDAO.definesType()))
    {
      mdLocalStructDAO.apply();
      manager.addMapping(mdLocalStructDAO.definesType(), mdLocalStructDAO.getId());
    }
  }

  /**
   *
   * @return
   */
  protected MdLocalStructDAO getMdEntityDAO()
  {
    return this.mdLocalStructDAO;
  }

  /**
   * Determines the actions when a attributes tag is opened
   * Inherits from ContentHandler (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }

    if (manager.isCreated(mdLocalStructDAO.definesType()))
    {
      return;
    }

    //Delegates parsing control to a AttributesHandler
    if (localName.equals(XMLTags.ATTRIBUTES_TAG))
    {
      MdAttributeHandler aHandler = new MdAttributeHandler(attributes, reader, this, manager, mdLocalStructDAO);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if(localName.equals(XMLTags.MD_METHOD_TAG))
    {
      MdMethodHandler aHandler = new MdMethodHandler(attributes, reader, this, manager, mdLocalStructDAO);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdLocalStructDAO, MdStructInfo.STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.DTO_STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdLocalStructDAO, MdStructInfo.DTO_STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * Sets the parameters of mdBusiness from the parsed attributes list
   *
   * @param attributes The attributes of an element
   */
  private final void importLocalStruct(Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdLocalStructDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdLocalStructDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdLocalStructDAO, MdStructInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdLocalStructDAO, MdStructInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdLocalStructDAO, MdStructInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdLocalStructDAO, MdBusinessInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdLocalStructDAO, MdStructInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setValue(mdLocalStructDAO, MdEntityInfo.HAS_DETERMINISTIC_IDS, attributes, XMLTags.HAS_DETERMINISTIC_ID);
  }

  /**
   * Passes back control to the previous handler when a local struct class  is parsed
   * Inherited from contentHandler
   * (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.MD_LOCAL_STRUCT_TAG))
    {
      //Make sure the name has not already been defined
      if (!manager.isCreated(mdLocalStructDAO.definesType()))
      {
        manager.addImportedType(mdLocalStructDAO.definesType());
      }

      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
