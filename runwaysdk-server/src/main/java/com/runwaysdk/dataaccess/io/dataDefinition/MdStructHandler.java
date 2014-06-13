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

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;

/**
 * Handles attributes and elements within the enum_class and standalone tag. See
 * datatype.xsd as a reference.
 * 
 * @author Justin Smethie
 * @date 6/01/06
 */
public class MdStructHandler extends MdEntityHandler
{
  /**
   * The {@link MdStructDAO} instance.
   */
  private MdStructDAO mdStructDAO;

  /**
   * Handler Construction, creates a new MdStructDAO.
   * 
   * @param attributes
   *          The attibutes of the class tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The Handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param tagName
   *          The type to construct. Can be either enumeration master class or a
   *          regular class.
   */
  public MdStructHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagName)
  {
    super(attributes, reader, previousHandler, manager, tagName);

    mdStructDAO = (MdStructDAO) manager.getEntityDAO(MdStructInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();
    importStruct(attributes);

    // Make sure the name has not already been defined
    if (!manager.isCreated(mdStructDAO.definesType()))
    {
      mdStructDAO.apply();
      manager.addMapping(mdStructDAO.definesType(), mdStructDAO.getId());
    }
  }

  /**
   * 
   * @return
   */
  protected MdStructDAO getMdEntityDAO()
  {
    return this.mdStructDAO;
  }

  /**
   * Determines the actions when a attributes tag is opened Inherits from
   * ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }

    if (manager.isCreated(mdStructDAO.definesType()))
    {
      return;
    }

    // Delegates parsing control to a AttributesHandler
    if (localName.equals(XMLTags.ATTRIBUTES_TAG))
    {
      MdAttributeHandler aHandler = new MdAttributeHandler(attributes, reader, this, manager, mdStructDAO);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.MD_METHOD_TAG))
    {
      MdMethodHandler aHandler = new MdMethodHandler(attributes, reader, this, manager, mdStructDAO);
      reader.setContentHandler(aHandler);
      reader.setErrorHandler(aHandler);
    }
    else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdStructDAO, MdStructInfo.STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.DTO_STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdStructDAO, MdStructInfo.DTO_STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * Sets the parameters of mdBusiness from the parsed attributes list
   * 
   * @param attributes
   *          The attributes of an element
   */
  private final void importStruct(Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdStructDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdStructDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdStructDAO, MdStructInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdStructDAO, MdStructInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdStructDAO, MdStructInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdStructDAO, MdStructInfo.CACHE_SIZE, attributes, XMLTags.CACHE_SIZE_ATTRIBUTE);
    ImportManager.setValue(mdStructDAO, MdStructInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdStructDAO, MdStructInfo.ENFORCE_SITE_MASTER, attributes, XMLTags.ENFORCE_SITE_MASTER_ATTRIBUTE);
    ImportManager.setValue(mdStructDAO, MdStructInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setValue(mdStructDAO, MdEntityInfo.HAS_DETERMINISTIC_IDS, attributes, XMLTags.HAS_DETERMINISTIC_ID);

    // Import optional reference attributes
    String cacheAlgorithm = attributes.getValue(XMLTags.CACHE_ALGORITHM_ATTRIBUTE);

    if (cacheAlgorithm != null)
    {
      // Change to an everything caching algorithm
      if (cacheAlgorithm.equals(XMLTags.EVERYTHING_ENUMERATION))
      {
        mdStructDAO.addItem(MdStructInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      }
      // Change to a nonthing caching algorithm
      else if (cacheAlgorithm.equals(XMLTags.NOTHING_ENUMERATION))
      {
        mdStructDAO.addItem(MdStructInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      }
      else
      {
        mdStructDAO.addItem(MdStructInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId());
      }
    }

    String tableName = attributes.getValue(XMLTags.ENTITY_TABLE);
    if (tableName != null)
    {
      mdStructDAO.setTableName(tableName);
    }

    String generateController = attributes.getValue(XMLTags.GENERATE_CONTROLLER);
    if (generateController != null)
    {
      mdStructDAO.setGenerateMdController(new Boolean(generateController));
    }
  }

  /**
   * Passes back control to the previous handler when a enumeration_class or
   * standalone class tag is parsed Inherited from contentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.MD_STRUCT_TAG))
    {
      // Make sure the name has not already been defined
      if (!manager.isCreated(mdStructDAO.definesType()))
      {
        manager.addImportedType(mdStructDAO.definesType());
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
