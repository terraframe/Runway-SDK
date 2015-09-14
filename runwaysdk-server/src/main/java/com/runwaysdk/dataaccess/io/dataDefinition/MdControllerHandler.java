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

import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;

public class MdControllerHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  public MdControllerHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.MD_ACTION_TAG, new MdActionHandler(manager));
    this.addHandler(XMLTags.STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.STUB_SOURCE_TAG, MdFacadeInfo.STUB_SOURCE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdControllerDAO mdController = (MdControllerDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdController != null && this.getManager().isCreated(mdController.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.MdEntityHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdController to import, if this is a create then a new instance of MdController is imported
    MdControllerDAO mdController = (MdControllerDAO) this.getManager().getEntityDAO(MdControllerInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importMdController(mdController, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdController.definesType()))
    {
      mdController.apply();

      this.getManager().addMapping(mdController.definesType(), mdController.getId());
    }

    context.setObject(MdTypeInfo.CLASS, mdController);
  }

  /**
   * Creates an MdController from the parse of the class tag attributes.
   * 
   * @param mdController
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * 
   * @return MdController from the parse of the class tag attributes.
   */
  private final void importMdController(MdControllerDAO mdController, Attributes attributes)
  {
    // Breakup the type into a package and name
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdController.setValue(MdControllerInfo.NAME, BusinessDAOFactory.getClassNameFromType(name));
    mdController.setValue(MdControllerInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(name));

    ImportManager.setLocalizedValue(mdController, MdControllerInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdController, MdControllerInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdController, MdControllerInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdController, MdControllerInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
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
    MdControllerDAO mdController = (MdControllerDAO) context.getObject(MdTypeInfo.CLASS);

    this.getManager().endImport(mdController.definesType());
  }
}
