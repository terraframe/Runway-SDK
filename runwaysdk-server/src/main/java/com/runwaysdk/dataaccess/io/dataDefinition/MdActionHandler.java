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

import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;

public class MdActionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdActionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.MD_PARAMETER_TAG, new MdParameterHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.MdEntityHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    MdTypeDAO mdType = (MdTypeDAO) context.getObject(MdTypeInfo.CLASS);

    // Get the MdBusiness to import, if this is a create then a new instance of
    // MdBusiness is imported
    MdActionDAO mdAction = (MdActionDAO) this.getManager().getEntityDAO(MdActionInfo.CLASS, mdType.definesType() + "." + attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();
    importMdAction(mdAction, mdType, attributes);
    mdAction.apply();

    context.setObject(ParameterMarker.class.getName(), mdAction);
  }

  /**
   * Creates an MdMethod from the parse of the class tag attributes
   * 
   * @param mdAction
   *          TODO
   * @param mdType
   *          The mdType that defines the mdMethod
   * @param attributes
   *          The attributes of an class tag
   */
  private final void importMdAction(MdActionDAO mdAction, MdTypeDAO mdType, Attributes attributes)
  {
    mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdType.getId());

    ImportManager.setValue(mdAction, MdActionInfo.NAME, attributes, XMLTags.NAME_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdAction, MdActionInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdAction, MdActionInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdAction, MdActionInfo.IS_POST, attributes, XMLTags.IS_POST_ATTRIBUTES);
    ImportManager.setValue(mdAction, MdActionInfo.IS_QUERY, attributes, XMLTags.IS_QUERY_ATTRIBUTES);
  }
}
