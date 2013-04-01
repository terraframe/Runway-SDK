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
package com.runwaysdk.dataaccess.io.instance;

import java.util.List;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.io.StringMarkupWriter;
import com.runwaysdk.query.ComponentQuery;

public class StringInstanceExporter extends InstanceExporter
{
  /**
   * Constructor Writes serialized XML to a string.
   *
   * @param schemaLocation
   *            The location of the schema file
   * @param _exportOnlyModifiedAttributes
   *             True if only modified attributes should be exported, false otherwise.
   */
  private StringInstanceExporter(String schemaLocation, boolean _exportOnlyModifiedAttributes)
  {
    super(new StringMarkupWriter(), schemaLocation, _exportOnlyModifiedAttributes);
  }

  /**
   * Returns a reference to the markup writer.
   * @return reference to the markup writer.
   */
  protected StringMarkupWriter getWriter()
  {
    return (StringMarkupWriter)super.getWriter();
  }

  /**
   * return the string serialized XML of the EntityDAO;
   */
  public String toString()
  {
    return this.getWriter().getOutput();
  }

  /**
   * Exports the instances specified by a list of queries to XML.
   *
   * @param schemaLocation
   *            The location of the schema
   * @param queries
   *            List of queriers which specify the instances to export
   * @param _exportOnlyModifiedAttributes
   *             True if only modified attributes should be exported, false otherwise.
   * @return string of serialized XML
   */
  public static String export(String schemaLocation, List<ComponentQuery> queries, boolean _exportOnlyModifiedAttributes)
  {
    StringInstanceExporter exporter = new StringInstanceExporter(schemaLocation, _exportOnlyModifiedAttributes);

    exporter.open();
    exporter.export(queries);
    exporter.close();

    return exporter.toString();
  }

  /**
   * Serializes the given instance
   *
   * @param schemaLocation
   *            The location of the schema
   * @param entityDAOIF
   *            entity to serialize into XML
   * @param _exportOnlyModifiedAttributes
   *             True if only modified attributes should be exported, false otherwise.
   */
  public static String export(String schemaLocation, EntityDAOIF entityDAOIF, boolean _exportOnlyModifiedAttributes)
  {
    StringInstanceExporter exporter = new StringInstanceExporter(schemaLocation, _exportOnlyModifiedAttributes);
    exporter.export(entityDAOIF);

    return exporter.toString();
  }
}
