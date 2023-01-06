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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;

public class MdMethodPermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdMethodPermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.AbstractPermissionHandler.OperationHandler#onStartElement(java.lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    MdTypeDAOIF mdType = (MdTypeDAOIF) context.getObject(MdTypeInfo.CLASS);

    String methodName = attributes.getValue(XMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE);
    MdMethodDAOIF mdMethod = mdType.getMdMethod(methodName);

    context.setObject(MetadataInfo.CLASS, mdMethod);
  }
}
