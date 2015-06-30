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

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;

/**
 * Handles create tags
 * 
 * @author Aritra
 * 
 */
public class SMCreateHandlerFactory
{

  public AbstractSchemaHandler getHandler(String localName, Attributes attributes, XMLReader reader, AbstractSchemaHandler handler, ImportManager manager)
  {
    if (! ( manager instanceof MergeSchema ))
    {
      throw new UnsupportedOperationException("The import manager has to be an instance of Schema in order to create handlers");
    }

    MergeSchema schema = (MergeSchema) manager;
    if (SMXMLTags.isClassTag(localName))
    {
      return new SMClassHandler(schema, reader, handler, localName, attributes);
    }
    else if (SMXMLTags.isRelationshipTag(localName))
    {
      return new SMRelationshipHandler(attributes, reader, handler, schema, localName);
    }
    else if (SMXMLTags.isInstanceElementTag(localName))
    {
      return new InstanceElementHandler(attributes, reader, handler, schema, localName);
    }
    else if (localName.equals(XMLTags.MD_ENUMERATION_TAG))
    {
      return new SMEnumerationHandler(attributes, reader, handler, schema, localName);
    }
    // Everything else did not have their semantics preserved
    else if (localName.equals(XMLTags.MD_INDEX_TAG))
    {
      return new SchemaIndexHandler(attributes, reader, handler, schema, localName);
    }
    else
      return null;

  }

}
