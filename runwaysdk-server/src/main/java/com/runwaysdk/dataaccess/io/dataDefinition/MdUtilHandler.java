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

import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;

public class MdUtilHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdUtilHandler(ImportManager manager)
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
    MdUtilDAO mdUtil = (MdUtilDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdUtil != null && this.getManager().isCreated(mdUtil.definesType()))
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
    // Get the MdUtil to import, if the action is 'create' then a new instance
    // of MdUtil is used.
    MdUtilDAO mdUtil = (MdUtilDAO) this.getManager().getEntityDAO(MdUtilInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdUtil(mdUtil, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdUtil.definesType()))
    {
      mdUtil.apply();

      this.getManager().addMapping(mdUtil.definesType(), mdUtil.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdUtil);
  }

  /**
   * Creates a MdUtil from the XML attributes.
   * 
   * @param mdUtil
   *          TODO
   * @param attributes
   *          The attributes of the mdException tag
   */
  private final void importMdUtil(MdUtilDAO mdUtil, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdUtil.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdUtil.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdUtil, MdUtilInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdUtil, MdUtilInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdUtil, MdUtilInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdUtil, MdUtilInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdUtil, MdUtilInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdUtil, MdUtilInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdUtil, MdUtilInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_UTIL_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdUtil.definesType());
      }

      mdUtil.setValue(MdUtilInfo.SUPER_MD_UTIL, MdUtilDAO.getMdUtil(extend).getOid());
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
    MdUtilDAO mdUtil = (MdUtilDAO) context.getObject(MdTypeInfo.CLASS);

    this.getManager().endImport(mdUtil.definesType());
  }
}
