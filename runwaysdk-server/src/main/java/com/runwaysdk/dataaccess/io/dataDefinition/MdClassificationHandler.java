/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.graph.MdClassificationDAO;

public class MdClassificationHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdClassificationHandler(ImportManager manager)
  {
    super(manager);
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
    MdClassificationDAO mdVertexDAO = (MdClassificationDAO) context.getObject(MdTypeInfo.CLASS);

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
    // Get the MdClassificationDAO to import, if this is a create then a new
    // instance
    // of MdClassificationDAO is imported
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdClassificationDAO mdVertexDAO = this.createMdClassification(localName, name);

    this.importMdClassification(mdVertexDAO, localName, attributes);

    // Make sure the class has not already been defined
    if (!this.getManager().isCreated(mdVertexDAO.definesType()))
    {
      mdVertexDAO.apply();

      this.getManager().addMapping(name, mdVertexDAO.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdVertexDAO);
  }

  protected MdClassificationDAO createMdClassification(String localName, String name)
  {
    return (MdClassificationDAO) this.getManager().getEntityDAO(MdClassificationInfo.CLASS, name).getEntityDAO();
  }

  protected String getTag()
  {
    return XMLTags.MD_VERTEX_TAG;
  }

  /**
   * Creates an MdClassificationDAO from the parse of the class tag attributes.
   * 
   * @param mdClassificationDAO
   *          TODO
   * @param attributes
   *          The attributes of an class tag
   * @return MdClassificationDAO from the parse of the class tag attributes.
   */
  private final void importMdClassification(MdClassificationDAO mdClassificationDAO, String localName, Attributes attributes)
  {
    // Import the required attributes and Breakup the type into a package and
    // name
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdClassificationDAO.setValue(MdClassificationInfo.TYPE_NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdClassificationDAO.setValue(MdClassificationInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdClassificationDAO, MdClassificationInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdClassificationDAO, MdClassificationInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdClassificationDAO, MdClassificationInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

    // Import optional reference attributes
    String rootOid = attributes.getValue(MdClassificationInfo.ROOT);

    if (rootOid != null && rootOid.length() > 0)
    {
      VertexObjectDAOIF root = VertexObjectDAO.get(mdClassificationDAO.getReferenceMdVertexDAO(), rootOid);

      mdClassificationDAO.setValue(MdClassificationInfo.ROOT, root.getOid());
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
      MdClassificationDAO mdClassificationDAO = (MdClassificationDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdClassificationDAO.definesType());
    }
  }
}
