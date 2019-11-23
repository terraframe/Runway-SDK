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
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.graph.ChangeFrequency;

public class MdVertexHandler extends MdGraphClassHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdVertexHandler(ImportManager manager)
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
    MdVertexDAO mdVertexDAO = (MdVertexDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdVertexDAO != null && this.getManager().isCreated(mdVertexDAO.definesType()))
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
    // Get the MdVertexDAO to import, if this is a create then a new instance
    // of MdVertexDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdVertexDAO mdVertexDAO = this.createMdVertex(localName, name);

    this.importMdVertex(mdVertexDAO, localName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdVertexDAO.definesType()))
    {
      mdVertexDAO.apply();

      this.getManager().addMapping(name, mdVertexDAO.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdVertexDAO);
  }

  protected MdVertexDAO createMdVertex(String localName, String name)
  {
    return (MdVertexDAO) this.getManager().getEntityDAO(MdVertexInfo.CLASS, name).getEntityDAO();
  }

  protected String getTag()
  {
    return XMLTags.MD_VERTEX_TAG;
  }

  /**
   * Creates an MdVertexDAO from the parse of the class tag attributes.
   * 
   * @param mdVertexDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdVertexDAO from the parse of the class tag attributes.
   */
  private final void importMdVertex(MdVertexDAO mdVertexDAO, String localName, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdVertexDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdVertexDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdVertexDAO, MdVertexInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdVertexDAO, MdVertexInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdVertexDAO, MdVertexInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdVertexDAO, MdVertexInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdVertexDAO, MdVertexInfo.GENERATE_SOURCE, attributes, XMLTags.GENERATE_SOURCE);
    ImportManager.setValue(mdVertexDAO, MdVertexInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    ImportManager.setValue(mdVertexDAO, MdVertexInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setValue(mdVertexDAO, MdVertexInfo.ENABLE_CHANGE_OVER_TIME, attributes, XMLTags.ENABLE_CHANGE_OVER_TIME);

    String frequency = attributes.getValue(XMLTags.FREQUENCY);

    if (frequency != null)
    {
      mdVertexDAO.addItem(MdVertexInfo.FREQUENCY, ChangeFrequency.valueOf(frequency).getOid());
    }

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
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdVertexDAO.definesType());
      }

      MdVertexDAOIF mdVertexIF = MdVertexDAO.getMdVertexDAO(extend);
      mdVertexDAO.setValue(MdVertexInfo.SUPER_MD_VERTEX, mdVertexIF.getOid());
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
      MdVertexDAO mdVertexDAO = (MdVertexDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdVertexDAO.definesType());
    }
  }
}
