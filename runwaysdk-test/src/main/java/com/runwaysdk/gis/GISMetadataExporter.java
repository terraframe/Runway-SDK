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
package com.runwaysdk.gis;

import java.util.List;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.io.XMLExporter;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class GISMetadataExporter
{
  @Request
  public static void main(String[] args) throws Throwable
  {
    try
    {
      if (args.length != 2)
      {
        String errMsg = "Two arguments are required for GISMetadataExporter:\n" + "  1) metadata XSD file path\n" + "  2) metadata XML file path";
        throw new CoreException(errMsg);
      }

      String schemaFile = args[0];
      String exportFile = args[1];

      gisMetadata(schemaFile, exportFile);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  @Request
  public static void gisMetadata(String schemaFile, String exportFile) throws Throwable
  {
    XMLExporter exporter = new XMLExporter(schemaFile);

    List<MdEntityDAOIF> rootEntityList = MdEntityDAO.getRootEntities();

    for (MdEntityDAOIF mdEntityIF : rootEntityList)
    {
      QueryFactory queryFactory = new QueryFactory();
      EntityQuery entityQuery = queryFactory.entityQueryDAO(mdEntityIF);

      OIterator<? extends ComponentIF> componentIterator = entityQuery.getIterator();
      while (componentIterator.hasNext())
      {
        EntityDAOIF component = (EntityDAOIF) componentIterator.next();

        if (isGISMetadata(component) && !isUniversalMetadata(component))
        {
          exporter.add(component);
        }
      }
    }

    exporter.writeToFile(exportFile);
  }

  // private static void export(String oid, Set<String> all)
  // {
  // if (!all.contains(oid))
  // {
  // EntityDAOIF entity = EntityDAO.get(oid);
  //
  // if (!isRunwayMetadata(entity))
  // {
  // all.add(oid);
  //
  // if (entity instanceof BusinessDAOIF)
  // {
  // BusinessDAOIF businessDAO = (BusinessDAOIF) entity;
  // List<RelationshipDAOIF> children = businessDAO.getAllChildren();
  //
  // for (RelationshipDAOIF child : children)
  // {
  // export(child.getOid(), all);
  // }
  //
  // List<RelationshipDAOIF> parents = businessDAO.getAllParents();
  //
  // for (RelationshipDAOIF parent : parents)
  // {
  // export(parent.getOid(), all);
  // }
  // }
  // else if (entity instanceof RelationshipDAOIF)
  // {
  // RelationshipDAOIF relationship = (RelationshipDAOIF) entity;
  //
  // export(relationship.getParentOid(), all);
  // export(relationship.getChildOid(), all);
  // }
  // }
  // }
  // }

  private static boolean isGISMetadata(EntityDAOIF component)
  {
    return component.getSiteMaster().equals("www.runwaysdk-gis.com") || component.getKey().startsWith(GISConstants.GIS_METADATA_PACKAGE);
  }

  private static boolean isUniversalMetadata(EntityDAOIF component)
  {
    return component.getKey().startsWith(GISConstants.GEO_PACKAGE);
  }
}
