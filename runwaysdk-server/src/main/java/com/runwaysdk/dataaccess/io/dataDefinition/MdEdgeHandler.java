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

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class MdEdgeHandler extends MdGraphClassHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdEdgeHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new MdAttributeHandler(manager));
    // this.addHandler(XMLTags.MD_METHOD_TAG, new MdMethodHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.
   * runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdEdgeDAO mdEdgeDAO = (MdEdgeDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdEdgeDAO != null && this.getManager().isCreated(mdEdgeDAO.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
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
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdEdgeDAO to import, if this is a create then a new instance
    // of MdEdgeDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdEdgeDAO mdEdgeDAO = this.createMdEdge(localName, name);

    this.importMdEdge(mdEdgeDAO, localName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdEdgeDAO.definesType()))
    {
      mdEdgeDAO.apply();

      this.getManager().addMapping(name, mdEdgeDAO.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdEdgeDAO);
  }

  private final MdEdgeDAO createMdEdge(String localName, String name)
  {
    return (MdEdgeDAO) this.getManager().getEntityDAO(MdEdgeInfo.CLASS, name).getEntityDAO();
  }

  /**
   * Creates an MdEdgeDAO from the parse of the class tag attributes.
   * 
   * @param mdEdgeDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdEdgeDAO from the parse of the class tag attributes.
   */
  private final void importMdEdge(MdEdgeDAO mdEdgeDAO, String localName, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdEdgeDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdEdgeDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdEdgeDAO, MdEdgeInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdEdgeDAO, MdEdgeInfo.DB_CLASS_NAME, attributes, XMLTags.DATABASE_CLASS_NAME);
    ImportManager.setValue(mdEdgeDAO, MdEdgeInfo.GENERATE_SOURCE, attributes, XMLTags.GENERATE_SOURCE);
    ImportManager.setValue(mdEdgeDAO, MdEdgeInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdEdgeDAO, MdEdgeInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setValue(mdEdgeDAO, MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, attributes, XMLTags.ENABLE_CHANGE_OVER_TIME);

    // Import optional reference attributes
    String parent = attributes.getValue(XMLTags.PARENT_TAG);

    if (parent != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(parent))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_VERTEX_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, parent, mdEdgeDAO.definesType());
      }

      MdVertexDAOIF mdVertexIF = MdVertexDAO.getMdVertexDAO(parent);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdVertexIF.getOid());
    }

    String child = attributes.getValue(XMLTags.CHILD_TAG);

    if (child != null)
    {
      // Ensure the child class has already been defined in the database
      if (!MdTypeDAO.isDefined(child))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_VERTEX_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, child, mdEdgeDAO.definesType());
      }

      MdVertexDAOIF mdVertexIF = MdVertexDAO.getMdVertexDAO(child);
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdVertexIF.getOid());
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
    if (localName.equals(XMLTags.MD_VERTEX_TAG))
    {
      MdEdgeDAO mdEdgeDAO = (MdEdgeDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdEdgeDAO.definesType());
    }
  }
}
