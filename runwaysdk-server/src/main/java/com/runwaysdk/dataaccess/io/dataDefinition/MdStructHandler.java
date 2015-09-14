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
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;

/**
 * Handles attributes and elements within the enum_class and standalone tag. See datatype.xsd as a reference.
 * 
 * @author Justin Smethie
 * @date 6/01/06
 */
public class MdStructHandler extends MdEntityHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdStructHandler(ImportManager manager)
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
    MdStructDAO mdStruct = (MdStructDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdStruct != null && this.getManager().isCreated(mdStruct.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    MdStructDAO mdStructDAO = (MdStructDAO) this.getManager().getEntityDAO(MdStructInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();
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

    // Make sure the name has not already been defined
    if (!this.getManager().isCreated(mdStructDAO.definesType()))
    {
      mdStructDAO.apply();

      this.getManager().addMapping(mdStructDAO.definesType(), mdStructDAO.getId());
    }

    context.setObject(MdTypeInfo.CLASS, mdStructDAO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    MdStructDAO mdStructDAO = (MdStructDAO) context.getObject(MdTypeInfo.CLASS);

    // Make sure the name has not already been defined
    if (!this.getManager().isCreated(mdStructDAO.definesType()))
    {
      this.getManager().addImportedType(mdStructDAO.definesType());
    }
  }
}
