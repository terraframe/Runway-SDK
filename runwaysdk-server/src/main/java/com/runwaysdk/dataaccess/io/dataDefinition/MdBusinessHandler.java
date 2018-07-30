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

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdBusinessHandler extends MdEntityHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdBusinessHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
    this.addHandler(XMLTags.MD_METHOD_TAG, new MdMethodHandler(manager));
    this.addHandler(XMLTags.STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.STUB_SOURCE_TAG, MdClassInfo.STUB_SOURCE));
    this.addHandler(XMLTags.DTO_STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.DTO_STUB_SOURCE_TAG, MdClassInfo.DTO_STUB_SOURCE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdBusinessDAO mdBusinessDAO = (MdBusinessDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdBusinessDAO != null && this.getManager().isCreated(mdBusinessDAO.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdBusinessDAO to import, if this is a create then a new instance
    // of MdBusinessDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdBusinessDAO mdBusinessDAO = this.createMdBusiness(localName, name);

    this.importMdBusiness(mdBusinessDAO, localName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdBusinessDAO.definesType()))
    {
      mdBusinessDAO.apply();

      this.getManager().addMapping(name, mdBusinessDAO.getId());
    }

    context.setObject(MdTypeInfo.CLASS, mdBusinessDAO);
  }

  private final MdBusinessDAO createMdBusiness(String localName, String name)
  {
    if (localName.equals(XMLTags.MD_TERM_TAG))
    {
      return (MdTermDAO) this.getManager().getEntityDAO(MdTermInfo.CLASS, name).getEntityDAO();
    }

    return (MdBusinessDAO) this.getManager().getEntityDAO(MdBusinessInfo.CLASS, name).getEntityDAO();
  }

  /**
   * Creates an MdBusinessDAO from the parse of the class tag attributes.
   * 
   * @param mdBusinessDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdBusinessDAO from the parse of the class tag attributes.
   */
  private final void importMdBusiness(MdBusinessDAO mdBusinessDAO, String localName, Attributes attributes)
  {
    if (localName.equals(XMLTags.ENUMERATION_MASTER_TAG))
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
    ImportManager.setValue(mdBusinessDAO, MdEntityInfo.HAS_DETERMINISTIC_IDS, attributes, XMLTags.HAS_DETERMINISTIC_ID);

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
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdBusinessDAO.definesType());
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

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.ENUMERATION_MASTER_TAG) || localName.equals(XMLTags.MD_BUSINESS_TAG) || localName.equals(XMLTags.MD_TERM_TAG))
    {
      MdBusinessDAO mdBusinessDAO = (MdBusinessDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdBusinessDAO.definesType());
    }
  }
}
