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

import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;

public class MdTableHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdTableHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.
   * runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String qName)
  {
    MdTableDAO mdTableDAO = (MdTableDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdTableDAO != null && this.getManager().isCreated(mdTableDAO.definesType()))
    {
      return false;
    }

    return super.supports(context, qName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java
   * .lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    // Get the MdTableDAO to import, if this is a create then a new instance
    // of MdTableDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdTableDAO mdTableDAO = this.createMdTable(qName, name);

    this.importMdTable(mdTableDAO, qName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdTableDAO.definesType()))
    {
      mdTableDAO.apply();

      this.getManager().addMapping(name, mdTableDAO.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdTableDAO);
  }

  private final MdTableDAO createMdTable(String qName, String name)
  {
    return (MdTableDAO) this.getManager().getEntityDAO(MdTableInfo.CLASS, name).getEntityDAO();
  }

  /**
   * Creates an MdTableDAO from the parse of the class tag attributes.
   * 
   * @param mdTableDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdTableDAO from the parse of the class tag attributes.
   */
  private final void importMdTable(MdTableDAO mdTableDAO, String qName, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdTableDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdTableDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdTableDAO, MdTableInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdTableDAO, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdTableDAO, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdTableDAO, MdTableInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdTableDAO, MdTableInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    String tableName = attributes.getValue(XMLTags.ENTITY_TABLE);
    if (tableName != null)
    {
      mdTableDAO.setValue(MdTableInfo.TABLE_NAME, tableName);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.
   * lang.String, java.lang.String, java.lang.String,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onEndElement(String uri, String qName, String name, TagContext context)
  {
    if (qName.equals(XMLTags.MD_TABLE_TAG))
    {
      MdTableDAO mdTableDAO = (MdTableDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdTableDAO.definesType());
    }
  }
}
