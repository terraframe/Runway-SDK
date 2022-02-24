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

import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;
import com.runwaysdk.dataaccess.metadata.Type;

public class MdParameterHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdParameterHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    ParameterMarker marker = (ParameterMarker) context.getObject(ParameterMarker.class.getName());

    // Get the MdBusiness to import, if this is a create then a new instance of
    // MdBusiness is imported
    String key = MdParameterDAO.buildKey(marker.getEnclosingMdTypeDAO().definesType(), marker.getName(), attributes.getValue(XMLTags.NAME_ATTRIBUTE));

    MdParameterDAO mdParameter = (MdParameterDAO) this.getManager().getEntityDAO(MdParameterInfo.CLASS, key).getEntityDAO();
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, marker.getOid());

    if (attributes.getValue(XMLTags.TYPE_ATTRIBUTE) != null)
    {
      Type type = new Type(attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

      // Ensure that the type has been defined in the core if it is not a
      // primitive
      if (type.isDefinedType() && !MdTypeDAO.isDefined(type.getRootType()))
      {
        SearchHandler.searchEntity(this.getManager(), XMLTags.TYPE_TAGS, XMLTags.NAME_ATTRIBUTE, type.getRootType(), mdParameter.getParameterName());
      }

      mdParameter.setValue(MdParameterInfo.TYPE, type.getType());
    }

    ImportManager.setValue(mdParameter, MdParameterInfo.NAME, attributes, XMLTags.NAME_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdParameter, MdParameterInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdParameter, MdParameterInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdParameter, MdParameterInfo.ORDER, attributes, XMLTags.PARAMETER_ORDER_ATTRIBUTE);

    mdParameter.apply();
  }
}
