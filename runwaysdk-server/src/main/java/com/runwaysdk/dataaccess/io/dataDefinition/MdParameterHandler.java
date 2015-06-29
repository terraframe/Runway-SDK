/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;
import com.runwaysdk.dataaccess.metadata.Type;

public class MdParameterHandler extends XMLHandler
{
  private MdParameterDAO mdParameter;

  public MdParameterHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, ParameterMarker marker)
  {
    super(reader, previousHandler, manager);

    // Get the MdBusiness to import, if this is a create then a new instance of
    // MdBusiness is imported
    mdParameter = (MdParameterDAO) manager.getEntityDAO(MdParameterInfo.CLASS, MdParameterDAO.buildKey(marker.getEnclosingMdTypeDAO().definesType(), marker.getName(), attributes.getValue(XMLTags.NAME_ATTRIBUTE))).getEntityDAO();

    importMdParameter(attributes, manager, marker);

    mdParameter.apply();
  }

  private final void importMdParameter(Attributes attributes, ImportManager manager, ParameterMarker marker)
  {
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, marker.getId());

    if (attributes.getValue(XMLTags.TYPE_ATTRIBUTE) != null)
    {
      Type type = new Type(attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

      // Ensure that the type has been defined in the core if it is not a
      // primitive
      if (type.isDefinedType() && !MdTypeDAO.isDefined(type.getRootType()))
      {
        SearchHandler.searchEntity(manager, XMLTags.TYPE_TAGS, XMLTags.NAME_ATTRIBUTE, type.getRootType(), mdParameter.getParameterName());
      }

      mdParameter.setValue(MdParameterInfo.TYPE, type.getType());
    }

    ImportManager.setValue(mdParameter, MdParameterInfo.NAME, attributes, XMLTags.NAME_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdParameter, MdParameterInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdParameter, MdParameterInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdParameter, MdParameterInfo.ORDER, attributes, XMLTags.PARAMETER_ORDER_ATTRIBUTE);
  }

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.MD_PARAMETER_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
