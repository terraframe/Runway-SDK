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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;

public class SMHandlerFactory
{
  public static AbstractSchemaHandler getHandler(MergeSchema schema, XMLReader reader, AbstractSchemaHandler previousHandler, String tag, Attributes attributes)
  {
    if (tag.equals(SMXMLTags.CREATE_TAG))
    {
      return new SMCreateElementHandler(schema, reader, previousHandler);
    }
    else if (SMXMLTags.isUnkeyedTag(tag))
    {
      return new UnKeyedElementHandler(schema, reader, previousHandler, tag, attributes);
    }

    return new SMKeyedElementHandler(schema, reader, previousHandler, tag, attributes);
  }

  public static AbstractSchemaHandler getUnKeyedElementHandler(MergeSchema schema, XMLReader reader, AbstractSchemaHandler previousHandler, String tag, Attributes attributes)
  {
    if (tag.equals(SMXMLTags.CREATE_TAG))
    {
      return new SMCreateElementHandler(schema, reader, previousHandler);
    }

    return new UnKeyedElementHandler(schema, reader, previousHandler, tag, attributes);
  }
}
