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

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdRelationshipHandler extends MdEntityHandler
{
  /**
   * The MdRelationshipDAO created by the metadata
   */
  protected MdRelationshipDAO mdRelationshipDAO;

  /**
   * Constructor - Creates a MdRelationshipDAO and sets the parameters according
   * to the attributes parse
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
  public MdRelationshipHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagName)
  {
    super(attributes, reader, previousHandler, manager, tagName);

    String key = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    createMdRelationship(tagName, key);

    importMdRelationship(attributes);
  }

  // Used to process the end of the tag, since the creation of the
  // mdrelationship has been cached.
  protected MdRelationshipHandler(MdRelationshipDAO mdRelationship, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(null, reader, previousHandler, manager, null);
    this.mdRelationshipDAO = mdRelationship;
  }

  /**
   * 
   * @return
   */
  protected MdRelationshipDAO getMdEntityDAO()
  {
    return this.mdRelationshipDAO;
  }

  /**
   * Creates a new instance of a MdRelationshipDAO. Depending on the given tag,
   * the MdRelationshipDAO created is either a MdTree, a MdGraph, or a
   * MdRelationshipDAO.
   * 
   * @param type
   *          The xml tag corresponding to the type of MdRelationshipDAO to
   *          create
   */
  private final void createMdRelationship(String type, String key)
  {
    if (type.equals(XMLTags.MD_RELATIONSHIP_TAG))
    {
      mdRelationshipDAO = (MdRelationshipDAO) manager.getEntityDAO(MdRelationshipInfo.CLASS, key).getEntityDAO();
    }
    else if (type.equals(XMLTags.MD_TREE_TAG))
    {
      mdRelationshipDAO = (MdRelationshipDAO) manager.getEntityDAO(MdTreeInfo.CLASS, key).getEntityDAO();
    }
    else if (type.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG))
    {
      mdRelationshipDAO = (MdTermRelationshipDAO) manager.getEntityDAO(MdTermRelationshipInfo.CLASS, key).getEntityDAO();
    }
    else
    {
      mdRelationshipDAO = (MdRelationshipDAO) manager.getEntityDAO(MdGraphInfo.CLASS, key).getEntityDAO();
    }
  }

  /**
   * Parses the parent, child, and attributes tags Inherited from ContentHandler
   * (non-Javadoc)
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

    // Do not parse the child tags if this has already been created in this
    // import
    if (manager.isCreated(mdRelationshipDAO.definesType()))
    {
      return;
    }

    if (localName.equals(XMLTags.PARENT_TAG))
    {
      importParent(attributes);
    }
    else if (localName.equals(XMLTags.CHILD_TAG))
    {
      importChild(attributes);
    }
    else
    {
      // Ensure that the MdRelationshipDAO has been applied
      if (!mdRelationshipDAO.isAppliedToDB())
      {
        mdRelationshipDAO.apply();
      }

      if (localName.equals(XMLTags.ATTRIBUTES_TAG))
      {
        // Delegate control to a AttributesHandler
        MdAttributeHandler aHandler = new MdAttributeHandler(attributes, reader, this, manager, mdRelationshipDAO);
        reader.setContentHandler(aHandler);
        reader.setErrorHandler(aHandler);
      }
      else if (localName.equals(XMLTags.MD_METHOD_TAG))
      {
        MdMethodHandler aHandler = new MdMethodHandler(attributes, reader, this, manager, mdRelationshipDAO);
        reader.setContentHandler(aHandler);
        reader.setErrorHandler(aHandler);
      }
      else if (localName.equals(XMLTags.STUB_SOURCE_TAG))
      {
        SourceHandler handler = new SourceHandler(reader, this, manager, mdRelationshipDAO, MdRelationshipInfo.STUB_SOURCE);
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
      }
      else if (localName.equals(XMLTags.DTO_STUB_SOURCE_TAG))
      {
        SourceHandler handler = new SourceHandler(reader, this, manager, mdRelationshipDAO, MdRelationshipInfo.DTO_STUB_SOURCE);
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
      }
    }
  }

  /**
   * Set the parent values of the relationshiop
   * 
   * @param attributes
   *          The attributes of an parent tag
   */
  protected void importParent(Attributes attributes) throws SAXException
  {
    String parentType = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    // Make sure that the class being reference has already been defined
    if (!MdTypeDAO.isDefined(parentType))
    {
      String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };
      SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, parentType, mdRelationshipDAO.definesType());
    }

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(parentType);
    mdRelationshipDAO.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, mdBusinessIF.getId());
    ImportManager.setLocalizedValue(mdRelationshipDAO, MdRelationshipInfo.PARENT_DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.PARENT_CARDINALITY, attributes, XMLTags.CARDINALITY_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.PARENT_METHOD, attributes, XMLTags.RELATIONSHIP_METHOD_TAG);
  }

  /**
   * Set the child parameters of the attributes
   * 
   * @param attributes
   *          The attributes of an child tag
   */
  protected void importChild(Attributes attributes) throws SAXException
  {
    String childType = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    // Make sure that the class being reference has already been defined
    if (!MdTypeDAO.isDefined(childType))
    {
      String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };
      SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, childType, mdRelationshipDAO.definesType());
    }

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(childType);

    mdRelationshipDAO.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdBusinessIF.getId());
    ImportManager.setLocalizedValue(mdRelationshipDAO, MdRelationshipInfo.CHILD_DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.CHILD_CARDINALITY, attributes, XMLTags.CARDINALITY_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.CHILD_METHOD, attributes, XMLTags.RELATIONSHIP_METHOD_TAG);
  }

  /**
   * Creates an MdRelationshipDAO from the parse of the class tag attributes.
   * 
   * @param attributes
   *          The attributes of an class tag
   * @return MdRelationshipDAO from the parse of the class tag attributes.
   */
  private final void importMdRelationship(Attributes attributes)
  {
    // Import the required attributes
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdRelationshipDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdRelationshipDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdRelationshipDAO, MdTypeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdRelationshipDAO, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdElementInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdElementInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.COMPOSITION, attributes, XMLTags.COMPOSITION_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.ENFORCE_SITE_MASTER, attributes, XMLTags.ENFORCE_SITE_MASTER_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdEntityInfo.HAS_DETERMINISTIC_IDS, attributes, XMLTags.HAS_DETERMINISTIC_ID);

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
        String[] search_tags = { XMLTags.MD_RELATIONSHIP_TAG, XMLTags.MD_TERM_RELATIONSHIP_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdRelationshipDAO.definesType());
      }

      MdRelationshipDAOIF superMdRelationship = MdRelationshipDAO.getMdRelationshipDAO(extend);
      mdRelationshipDAO.setValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP, superMdRelationship.getId());
    }
    else
    {
      // Only set the publish flag if this defines a MdRelationshipDAO which is
      // the
      // root of its hierarchy
      ImportManager.setValue(mdRelationshipDAO, MdBusinessInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    }

    if (cacheAlgorithm != null)
    {
      // Change to an everything caching algorithm
      if (cacheAlgorithm.equals(XMLTags.EVERYTHING_ENUMERATION))
      {
        mdRelationshipDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      }
      // Change to a nonthing caching algorithm
      else if (cacheAlgorithm.equals(XMLTags.NOTHING_ENUMERATION))
      {
        mdRelationshipDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      }
    }

    String tableName = attributes.getValue(XMLTags.ENTITY_TABLE);
    if (tableName != null)
    {
      mdRelationshipDAO.setTableName(tableName);
    }

    String generateController = attributes.getValue(XMLTags.GENERATE_CONTROLLER);

    if (generateController != null)
    {
      mdRelationshipDAO.setGenerateMdController(new Boolean(generateController));
    }

    // If this is a MdTermRelationship then import the assocation type attribute
    if (mdRelationshipDAO instanceof MdTermRelationshipDAO)
    {
      String associationType = attributes.getValue(XMLTags.ASSOCIATION_TYPE_ATTRIBUTE);

      if (associationType != null)
      {
        if (associationType.equals(XMLTags.RELATIONSHIP_OPTION))
        {
          mdRelationshipDAO.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
        }
        else if (associationType.equals(XMLTags.TREE_OPTION))
        {
          mdRelationshipDAO.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getId());
        }
        else if (associationType.equals(XMLTags.GRAPH_OPTION))
        {
          mdRelationshipDAO.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.GRAPH.getId());
        }
      }
    }
  }

  /**
   * When the relationship tag is closed: Returns parsing control back to the
   * Handler which passed control
   * 
   * When the child tag is closed Apply the relationship tag to the database
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.MD_RELATIONSHIP_TAG) || localName.equals(XMLTags.MD_TREE_TAG) || localName.equals(XMLTags.MD_GRAPH_TAG) || localName.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG))
    {
      if (!manager.isCreated(mdRelationshipDAO.definesType()))
      {
        mdRelationshipDAO.apply();
        manager.addMapping(mdRelationshipDAO.definesType(), mdRelationshipDAO.getId());
      }

      // Make sure the name has not already been defined
      manager.endImport(mdRelationshipDAO.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }
}
