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
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;

public class MdFacadePermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdFacadePermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
    this.addHandler(XMLTags.MD_METHOD_PERMISSION_TAG, new MdMethodPermissionHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    MdFacadeDAOIF mdFacade = MdFacadeDAO.getMdFacadeDAO(type);

    context.setObject(MdTypeInfo.CLASS, mdFacade);
    context.setObject(MetadataInfo.CLASS, mdFacade);
  }
}
