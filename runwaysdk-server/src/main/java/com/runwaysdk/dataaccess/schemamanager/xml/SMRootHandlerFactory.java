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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;

public class SMRootHandlerFactory
{

  public AbstractSchemaHandler getHandler(XMLReader reader, DefaultHandler handler, ImportManager manager, String tag, Attributes attributes)
  {
    if (! ( manager instanceof MergeSchema ))
    {
      throw new UnsupportedOperationException("The import manager has to be an instance of Schema in order to create handlers");
    }

    MergeSchema schema = (MergeSchema) manager;

    if (tag.equals(XMLTags.CREATE_TAG))
    {
      return new SMCreateGroupHandler(reader, handler, schema);
    }

    else if (tag.equals(XMLTags.UPDATE_TAG))
    {
      return new SMUpdateHandler(reader, handler, schema);
    }

    else if (tag.equals(XMLTags.DELETE_TAG))
    {
      return new SMDeleteHandler(reader, handler, schema);
    }

    else if (tag.equals(XMLTags.PERMISSIONS_TAG))
    {
      return new SMPermissionHandler(reader, handler, schema);
    }

    else
      return null;
  }

}
