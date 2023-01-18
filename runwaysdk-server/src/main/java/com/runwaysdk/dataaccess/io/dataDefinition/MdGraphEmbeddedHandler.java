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

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.graph.MdEmbeddedGraphClassInfo;
import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDAO;

public class MdGraphEmbeddedHandler extends MdGraphClassHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   */
  public MdGraphEmbeddedHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
  }

  /**
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.
   * runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdEmbeddedGraphClassDAO mdEmbeddedDAO = (MdEmbeddedGraphClassDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdEmbeddedDAO != null && this.getManager().isCreated(mdEmbeddedDAO.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /**
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java
   * .lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdEmbeddedGraphClassDAO to import, if this is a create then a new instance
    // of MdEmbeddedGraphClassDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdEmbeddedGraphClassDAO mdEmbeddedDAO = this.createMdEmbedded(localName, name);

    this.importMdEmbeddedGraphClass(mdEmbeddedDAO, localName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdEmbeddedDAO.definesType()))
    {
      mdEmbeddedDAO.apply();

      this.getManager().addMapping(name, mdEmbeddedDAO.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdEmbeddedDAO);
  }

  protected MdEmbeddedGraphClassDAO createMdEmbedded(String localName, String name)
  {
    return (MdEmbeddedGraphClassDAO) this.getManager().getEntityDAO(MdEmbeddedGraphClassInfo.CLASS, name).getEntityDAO();
  }

  protected String getTag()
  {
    return XMLTags.MD_GRAPH_EMBEDDED_TAG;
  }

  /**
   * Creates an MdEmbeddedGraphClassDAO from the parse of the class tag attributes.
   * 
   * @param mdEmbeddedDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdEmbeddedGraphClassDAO from the parse of the class tag attributes.
   */
  private final void importMdEmbeddedGraphClass(MdEmbeddedGraphClassDAO mdEmbeddedDAO, String localName, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdEmbeddedDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdEmbeddedDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdEmbeddedDAO, MdEmbeddedGraphClassInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdEmbeddedDAO, MdEmbeddedGraphClassInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdEmbeddedDAO, MdEmbeddedGraphClassInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdEmbeddedDAO, MdEmbeddedGraphClassInfo.GENERATE_SOURCE, attributes, XMLTags.GENERATE_SOURCE);
    ImportManager.setValue(mdEmbeddedDAO, MdEmbeddedGraphClassInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdEmbeddedDAO, MdEmbeddedGraphClassInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { getTag() };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdEmbeddedDAO.definesType());
      }

      MdEmbeddedGraphClassDAOIF mdEmbeddedIF = MdEmbeddedGraphClassDAO.getMdEmbeddedGraphClassDAO(extend);
      mdEmbeddedDAO.setValue(MdEmbeddedGraphClassInfo.SUPER_MD_RELATIONSHIP, mdEmbeddedIF.getOid());
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
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(getTag()))
    {
      MdEmbeddedGraphClassDAO mdEmbeddedDAO = (MdEmbeddedGraphClassDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdEmbeddedDAO.definesType());
    }
  }
}
