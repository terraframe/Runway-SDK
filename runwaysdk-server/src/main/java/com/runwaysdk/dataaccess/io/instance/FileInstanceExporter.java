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
package com.runwaysdk.dataaccess.io.instance;

import java.util.List;

import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.query.ComponentQuery;

public class FileInstanceExporter extends InstanceExporter
{
  /**
   * Constructor Creates an xml file called file_name
   *
   * @param fileName
   *            The location of the file to write
   * @param schemaLocation
   *            The location of the schema file
   * @param _exportOnlyModifiedAttributes
   *             True if only modified attributes should be exported, false otherwise.
   */
  private FileInstanceExporter(String fileName, String schemaLocation, boolean _exportOnlyModifiedAttributes)
  {
    super(new FileMarkupWriter(fileName), schemaLocation, _exportOnlyModifiedAttributes);
  }

  /**
   * Exports the instances specified by a list of queries to an xml file. If a
   * file of the specified filename already exists then the file is overwritten.
   *
   * @param fileName
   *            The name of the xml file to create.
   * @param schemaLocation
   *            The location of the schema
   * @param queries
   *            List of queriers which specify the instances to export
   * @param _exportOnlyModifiedAttributes
   *             True if only modified attributes should be exported, false otherwise.
   */
  public static void export(String fileName, String schemaLocation, List<ComponentQuery> queries, boolean _exportOnlyModifiedAttributes)
  {
    FileInstanceExporter exporter = new FileInstanceExporter(fileName, schemaLocation, _exportOnlyModifiedAttributes);

    exporter.open();
    exporter.export(queries);
    exporter.close();
  }
}
