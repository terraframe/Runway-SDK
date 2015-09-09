/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;

public class MdFacadeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  public MdFacadeHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.MD_METHOD_TAG, new MdMethodHandler(manager));
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
    MdFacadeDAO mdFacade = (MdFacadeDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdFacade != null && this.getManager().isCreated(mdFacade.definesType()))
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
    // Get the MdFacade to import, if this is a create then a new instance of MdFacade is imported
    MdFacadeDAO mdFacade = (MdFacadeDAO) this.getManager().getEntityDAO(MdFacadeInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    this.importMdFacade(mdFacade, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdFacade.definesType()))
    {
      mdFacade.apply();
      this.getManager().addMapping(mdFacade.definesType(), mdFacade.getId());
    }

    context.setObject(MdTypeInfo.CLASS, mdFacade);
  }

  /**
   * Creates an MdFacade from the parse of the class tag attributes.
   * 
   * @param mdFacade
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * 
   * @return MdFacade from the parse of the class tag attributes.
   */
  private final void importMdFacade(MdFacadeDAO mdFacade, Attributes attributes)
  {
    // Breakup the type into a package and name
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdFacade.setValue(MdFacadeInfo.NAME, BusinessDAOFactory.getClassNameFromType(name));
    mdFacade.setValue(MdFacadeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(name));

    ImportManager.setLocalizedValue(mdFacade, MdFacadeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdFacade, MdFacadeInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdFacade, MdFacadeInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdFacade, MdFacadeInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
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
    MdFacadeDAO mdFacade = (MdFacadeDAO) context.getObject(MdTypeInfo.CLASS);

    this.getManager().endImport(mdFacade.definesType());
  }
}
