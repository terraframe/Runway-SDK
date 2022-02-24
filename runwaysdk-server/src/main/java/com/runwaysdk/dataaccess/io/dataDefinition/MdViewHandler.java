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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;

public class MdViewHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdViewHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
    this.addHandler(XMLTags.MD_METHOD_TAG, new MdMethodHandler(manager));
    this.addHandler(XMLTags.STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.STUB_SOURCE_TAG, MdClassInfo.STUB_SOURCE));
    this.addHandler(XMLTags.DTO_STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.DTO_STUB_SOURCE_TAG, MdClassInfo.DTO_STUB_SOURCE));
    this.addHandler(XMLTags.QUERY_STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.QUERY_STUB_SOURCE_TAG, MdViewInfo.QUERY_STUB_SOURCE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdViewDAO mdView = (MdViewDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdView != null && this.getManager().isCreated(mdView.definesType()))
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
    // Get the MdView to import, if the action is 'create' then a new instance
    // of MdView is used.
    MdViewDAO mdView = (MdViewDAO) this.getManager().getEntityDAO(MdViewInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdView(mdView, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdView.definesType()))
    {
      mdView.apply();

      this.getManager().addMapping(mdView.definesType(), mdView.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdView);
  }

  /**
   * Creates an MdException from the parse of the mdException attributes.
   * 
   * @param mdView
   *          TODO
   * @param attributes
   *          The attributes of the mdException tag
   */
  private final void importMdView(MdViewDAO mdView, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdView.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdView.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdView, MdViewInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdView, MdViewInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdView, MdViewInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdView, MdViewInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdView, MdViewInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdView, MdViewInfo.GENERATE_SOURCE, attributes, XMLTags.GENERATE_SOURCE);
    ImportManager.setValue(mdView, MdViewInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdView, MdViewInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_VIEW_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdView.definesType());
      }

      mdView.setValue(MdViewInfo.SUPER_MD_VIEW, MdViewDAO.getMdViewDAO(extend).getOid());
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
    MdViewDAO mdView = (MdViewDAO) context.getObject(MdTypeInfo.CLASS);

    this.getManager().endImport(mdView.definesType());
  }
}
