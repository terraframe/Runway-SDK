/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdBusinessHandler extends MdEntityHandler
{
  /**
   * The BusinessDAO created by the metadata
   */
  private MdBusinessDAO mdBusinessDAO;

  /**
   * Constructor - Creates a MdBusinessDAO BusinessDAO and sets the parameters
   * according to the attributes parse
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
   * @param tagType
   *          The type to construct. Can be either enumeration master class or a
   *          regular class.
   */
  public MdBusinessHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagType)
  {
    super(attributes, reader, previousHandler, manager, tagType);

    // Get the MdBusinessDAO to import, if this is a create then a new instance
    // of MdBusinessDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    mdBusinessDAO = createMdBusiness(tagType, name);

    importMdBusiness(tagType, attributes);

    // Make sure the class has not already been defined
    if (!manager.isCreated(mdBusinessDAO.definesType()))
    {
      mdBusinessDAO.apply();

      manager.addMapping(name, mdBusinessDAO.getId());
    }
  }

  private final MdBusinessDAO createMdBusiness(String tagType, String name)
  {
    if (tagType.equals(XMLTags.MD_TERM_TAG))
    {
      return (MdTermDAO) manager.getEntityDAO(MdTermInfo.CLASS, name).getEntityDAO();
    }

    return (MdBusinessDAO) manager.getEntityDAO(MdBusinessInfo.CLASS, name).getEntityDAO();
  }

  /**
   * 
   * @return
   */
  protected MdBusinessDAO getMdEntityDAO()
  {
    return this.mdBusinessDAO;
  }

  /**
   * Parses the attributes tag Inherited from ContentHandler (non-Javadoc)
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

    // If this object has already been created in this import
    // then parsing the attributes is not needed
    if (manager.isCreated(mdBusinessDAO.definesType()))
    {
      return;
    }

    // Delegate control to a new AttributesHandler
    if (localName.equals(XMLTags.ATTRIBUTES_TAG))
    {
      MdAttributeHandler handler = new MdAttributeHandler(attributes, reader, this, manager, mdBusinessDAO);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.MD_STATE_MACHINE_TAG))
    {
      MdStateMachineHandler handler = new MdStateMachineHandler(attributes, reader, this, manager, mdBusinessDAO);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.MD_METHOD_TAG))
    {
      MdMethodHandler handler = new MdMethodHandler(attributes, reader, this, manager, mdBusinessDAO);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdBusinessDAO, MdViewInfo.STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTags.DTO_STUB_SOURCE_TAG))
    {
      SourceHandler handler = new SourceHandler(reader, this, manager, mdBusinessDAO, MdViewInfo.DTO_STUB_SOURCE);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * Creates an MdBusinessDAO from the parse of the class tag attributes.
   * 
   * @param attributes
   *          The attributes of an class tag
   * @return MdBusinessDAO from the parse of the class tag attributes.
   */
  private final void importMdBusiness(String tagType, Attributes attributes)
  {
    if (tagType.equals(XMLTags.ENUMERATION_MASTER_TAG))
    {
      // Find the database ID of the default EnumerationMaster
      mdBusinessDAO.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
      mdBusinessDAO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    }

    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdBusinessDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdBusinessDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdBusinessDAO, MdBusinessInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdBusinessDAO, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MdElementInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MdElementInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MdBusinessInfo.CACHE_SIZE, attributes, XMLTags.CACHE_SIZE_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MdBusinessInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MdBusinessInfo.ENFORCE_SITE_MASTER, attributes, XMLTags.ENFORCE_SITE_MASTER_ATTRIBUTE);
    ImportManager.setValue(mdBusinessDAO, MdBusinessInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);
    String cacheAlgorithm = attributes.getValue(XMLTags.CACHE_ALGORITHM_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG,  XMLTags.MD_TERM_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdBusinessDAO.definesType());
      }

      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(extend);
      mdBusinessDAO.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusinessIF.getId());
    }

    if (cacheAlgorithm != null)
    {
      // Change to an everything caching algorithm
      if (cacheAlgorithm.equals(XMLTags.EVERYTHING_ENUMERATION))
      {
        mdBusinessDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      }
      // Change to a nonthing caching algorithm
      else if (cacheAlgorithm.equals(XMLTags.NOTHING_ENUMERATION))
      {
        mdBusinessDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      }
      else
      {
        mdBusinessDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId());
      }
    }

    String tableName = attributes.getValue(XMLTags.ENTITY_TABLE);
    if (tableName != null)
    {
      mdBusinessDAO.setTableName(tableName);
    }

    String generateController = attributes.getValue(XMLTags.GENERATE_CONTROLLER);
    if (generateController != null)
    {
      mdBusinessDAO.setGenerateMdController(new Boolean(generateController));
    }
  }

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.ENUMERATION_MASTER_TAG) || localName.equals(XMLTags.MD_BUSINESS_TAG) || localName.equals(XMLTags.MD_TERM_TAG))
    {
      manager.endImport(mdBusinessDAO.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
