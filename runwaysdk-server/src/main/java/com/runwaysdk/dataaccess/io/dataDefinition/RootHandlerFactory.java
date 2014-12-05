/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class RootHandlerFactory implements HandlerFactoryIF
{

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#getHandler(
   * java.lang.String, org.xml.sax.Attributes, org.xml.sax.XMLReader,
   * com.runwaysdk.dataaccess.io.XMLHandler,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  public XMLHandler getHandler(String localName, Attributes attributes, XMLReader reader, XMLHandler handler, ImportManager manager)
  {
    if (localName.equals(XMLTags.DELETE_TAG))
    {
      return new DeleteHandler(reader, handler, manager);
    }
    else if (localName.equals(XMLTags.UPDATE_TAG))
    {
      return new UpdateHandler(reader, handler, manager);
    }
    else if (localName.equals(XMLTags.CREATE_TAG))
    {
      return new CreateHandler(reader, handler, manager);
    }
    else if (localName.equals(XMLTags.CREATE_OR_UPDATE_TAG))
    {
      return new CreateOrUpdateHandler(reader, handler, manager);
    }
    else if (localName.equals(XMLTags.PERMISSIONS_TAG))
    {
      return new PermissionsHandler(reader, handler, manager);
    }

    return null;
  }

}
