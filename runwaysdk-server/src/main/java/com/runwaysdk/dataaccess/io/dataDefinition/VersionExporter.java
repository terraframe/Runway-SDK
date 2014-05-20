/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;

import com.runwaysdk.constants.XMLConstants;

public class VersionExporter extends SAXExporter
{

  public VersionExporter(String fileName, String schemaLocation, ExportMetadata metadata)
  {
    super(fileName, schemaLocation, metadata);
  }

  @Override
  public void open()
  {
    HashMap<String, String> attributes = new HashMap<String, String>();

    attributes.put("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE);
    attributes.put("xsi:noNamespaceSchemaLocation", schemaLocation);

    writer.openEscapedTag(XMLTags.VERSION_TAG, attributes);
    writer.openTag(XMLTags.DO_IT_TAG);
  }

  @Override
  public void close()
  {
    // Close the doIt tag
    writer.closeTag();

    // Open and close the undoIt tag to make the file conform to Version.xsd
    writer.openTag(XMLTags.UNDO_IT_TAG);
    writer.closeTag();

    // Close the version tag
    writer.closeTag();
  }

  public static void export(String file, String schema, ExportMetadata metadata)
  {
    new VersionExporter(file, schema, metadata).export();
  }

}
