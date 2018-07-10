/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.mapping;

import static com.runwaysdk.gis.constants.GeoserverProperties.getGeoserverGWCDir;
import static com.runwaysdk.gis.constants.GeoserverProperties.getGeoserverSLDDir;
import static com.runwaysdk.gis.constants.GeoserverProperties.getLocalPath;
import static com.runwaysdk.gis.constants.GeoserverProperties.getPublisher;
import static com.runwaysdk.gis.constants.GeoserverProperties.getReader;
import static com.runwaysdk.gis.constants.GeoserverProperties.getStore;
import static com.runwaysdk.gis.constants.GeoserverProperties.getWorkspace;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.gis.mapping.gwc.SeedRequest;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.ConfigurationException;
import com.runwaysdk.util.FileIO;

import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

@SuppressWarnings("deprecation")
public class GeoserverFacade extends GeoserverFacadeBase
{
  private static final long  serialVersionUID = 162768295;

  public static final int    SRS_CODE         = 4326;

  public static final String SRS              = "EPSG:" + SRS_CODE;

  public static final String GEOM_COLUMN      = "geom";

  public static int          MINX_INDEX       = 0;

  public static int          MINY_INDEX       = 1;

  public static int          MAXX_INDEX       = 2;

  public static int          MAXY_INDEX       = 3;

  private static Log         log              = LogFactory.getLog(GeoserverFacade.class);

  /**
   * Checks if a given File is a cache directory for the workspace.
   */
  private static class CacheFilter implements FileFilter
  {
    @Override
    public boolean accept(File file)
    {
      return file.isDirectory() && file.getName().startsWith(getWorkspace());
    }
  }
  
  public static void refresh()
  {
    if(getPublisher().reload())
    {
      log.info("Reloaded geoserver.");
    }
    else
    {
      log.warn("Failed to reload geoserver.");
    }
  }
  
  public static void removeStore()
  {
    if(getPublisher().removeDatastore(getWorkspace(), getStore(), true))
    {
      log.info("Removed the datastore ["+getStore()+"].");
    }
    else
    {
      log.warn("Failed to remove the datastore ["+getStore()+"].");
    }
  }

  public static void removeWorkspace()
  {
    if(getPublisher().removeWorkspace(getWorkspace(), true))
    {
      log.info("Removed the workspace ["+getWorkspace()+"].");
    }
    else
    {
      log.warn("Failed to remove the workspace ["+getWorkspace()+"].");
    }
  }
  
  public static void publishWorkspace()
  {
    try
    {
      if(getPublisher().createWorkspace(getWorkspace(), new URI(getLocalPath())))
      {
        log.info("Created the workspace ["+getWorkspace()+"].");
      }
      else
      {
        log.warn("Failed to create the workspace ["+getWorkspace()+"].");
      }
    }
    catch (URISyntaxException e)
    {
      throw new ConfigurationException("The URI ["+getLocalPath()+"] is invalid.", e);
    }
  }
  
  /**
   * FIXME could not find another API call to do this, but one must exist
   * that isn't deprecated. Look again later.
   */
  public static void publishStore()
  {
    GSPostGISDatastoreEncoder encoder = new GSPostGISDatastoreEncoder();
    encoder.setDatabase(DatabaseProperties.getDatabaseName());
    encoder.setUser(DatabaseProperties.getUser());
    encoder.setPassword(DatabaseProperties.getPassword());
    encoder.setName(getStore());
    encoder.setHost(DatabaseProperties.getServerName());
    encoder.setPort(DatabaseProperties.getPort());
    encoder.setSchema(DatabaseProperties.getNamespace());
    encoder.setNamespace(getWorkspace());
    encoder.setEnabled(true);
    
    if(getPublisher().createPostGISDatastore(getWorkspace(), encoder))
    {
      log.info("Published the store ["+getStore()+"].");
    }
    else
    {
      log.warn("Failed to publish the store ["+getStore()+"].");
    }
  }
  
  /**
   * Checks if the given style exists in geoserver.
   * 
   * @param styleName
   * @return
   */
  public static boolean styleExists(String styleName)
  {
    return getReader().getSLD(styleName) != null;
  }

  /**
   * Checks if the cache directory exists. This method does not check what tiles
   * or zoom levels have been cached.
   * 
   * @param cacheName
   * @return
   */
  public static boolean cacheExists(String cacheName)
  {
    String cacheDir = getGeoserverGWCDir() + cacheName;
    File cache = new File(cacheDir);
    return cache.exists();
  }

  /**
   * Gets all layers declared in Geoserver for the workspace.
   * 
   * @return
   */
  public static List<String> getLayers()
  {
    return getReader().getLayers().getNames();
  }

  /**
   * Gets all styles declared in Geoserver for the workspace.
   */
  public static List<String> getStyles()
  {
    return getReader().getStyles().getNames();
  }

  /**
   * Removes the style defined in Geoserver, including the .sld and .xml file
   * artifacts.
   * 
   * @param styleName
   *          The name of the style to delete.
   */
  public static void removeStyle(String styleName)
  {
    if (styleExists(styleName))
    {
      if (getPublisher().removeStyle(styleName, true))
      {
        log.info("Removed the SLD [" + styleName + "].");
      }
      else
      {
        log.warn("Failed to remove the SLD [" + styleName + "].");
      }

      // There are problems with Geoserver not removing the SLD artifacts,
      // so make sure those are gone
      String stylePath = getGeoserverSLDDir();

      // remove the sld
      File sld = new File(stylePath + styleName + ".sld");
      if (sld.exists())
      {
        boolean deleted = sld.delete();
        if (deleted)
        {
          log.info("Deleted the file [" + sld + "].");
        }
        else
        {
          log.warn("Failed to delete the file [" + sld + "].");
        }
      }
      else
      {
        log.info("The file [" + sld + "] does not exist.");
      }

      // remove the xml
      File xml = new File(stylePath + styleName + ".xml");
      if (xml.exists())
      {
        boolean deleted = sld.delete();
        if (deleted)
        {
          log.info("Deleted the file [" + xml + "].");
        }
        else
        {
          log.warn("Failed to delete the file [" + xml + "].");
        }
      }
      else
      {
        log.info("The file [" + xml + "] does not exist.");
      }

    }
  }

  /**
   * Publishes the SQL of the given name with the XML body.
   * 
   * @param styleName
   * @param body
   */
  public static void publishStyle(String styleName, String body)
  {
    if (styleExists(styleName))
    {
      log.info("The style [" + styleName + "] already exists.");
    }
    else
    {
      if (getPublisher().publishStyle(body, styleName))
      {
        log.info("Published the SLD [" + styleName + "].");
      }
      else
      {
        log.warn("Failed to publish the SLD [" + styleName + "].");
      }
    }
  }

  /**
   * Adds a database view and publishes the layer if necessary.
   * 
   * @param layer
   */
  public static boolean publishLayer(String layer, String styleName)
  {

    // create the layer if it does not exist
    if (layerExists(layer))
    {
      log.info("The layer [" + layer + "] already exists in geoserver.");
      return true;
    }
    else
    {
      double[] bbox = getBBOX(layer);

      double minX = bbox[MINX_INDEX];
      double minY = bbox[MINY_INDEX];
      double maxX = bbox[MAXX_INDEX];
      double maxY = bbox[MAXY_INDEX];

      GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
      fte.setEnabled(true);
      fte.setName(layer);
      fte.setSRS(SRS);
      fte.setTitle(layer);
      fte.addKeyword(layer);
      fte.setNativeBoundingBox(minX, minY, maxX, maxY, SRS);
      fte.setLatLonBoundingBox(minX, minY, maxX, maxY, SRS);

      GSLayerEncoder le = new GSLayerEncoder();
      le.setDefaultStyle(styleName);
      le.setEnabled(true);

      if (getPublisher().publishDBLayer(getWorkspace(), getStore(), fte, le))
      {
        log.info("Created the layer [" + layer + "] in geoserver.");
        return true;
      }
      else
      {
        log.warn("Failed to create the layer [" + layer + "] in geoserver.");
        return false;
      }
    }
  }

  public static void publishSQLView(String viewName, String sql)
  {
    if (viewExists(viewName))
    {
      log.info("The database view [" + viewName + "] already exists.");
    }
    else
    {
      Database.createView(viewName, sql);
      log.info("Created the database view [" + viewName + "].");
    }
  }

  /**
   * Removes the database view that is associated with a layer.
   * 
   * @param viewName
   */
  public static void removeSQLView(String viewName, String sql)
  {
    // Remove the database view
    if (viewExists(viewName))
    {
      try
      {
        Database.dropView(viewName, sql, false);
        log.info("Removed the database view [" + viewName + "].");
      }
      catch (Throwable t)
      {
        // log the error but continue to allow further processing
        log.error("Failed to remove the database view [" + viewName + "].");
      }
    }
  }

  /**
   * Removes the layer from geoserver.
   * 
   * @param layer
   * @return
   */
  public static void removeLayer(String layer)
  {
    String workspace = getWorkspace();
    if (getPublisher().removeLayer(workspace, layer))
    {
      log.info("Removed the layer for [" + layer + "].");
    }
    else
    {
      log.warn("Failed to remove the layer for [" + layer + "].");
    }
  }

  public static void publishCache(String layer)
  {
    SeedRequest request = new SeedRequest(layer);
    if (request.doRequest())
    {
      log.info("Started seeding layer [" + layer + "].");
    }
    else
    {
      log.warn("Could not seed layer [" + layer + "]. Response code [" + request.getCode() + "].");
    }
  }

  /**
   * Returns a list of all cache directories.
   * 
   * @return
   */
  public static File[] getCaches()
  {
    File cacheRoot = new File(getGeoserverGWCDir());
    return cacheRoot.listFiles(new CacheFilter());
  }
  
  public static void removeCache(File cache)
  {
    if (cache.exists())
    {
      try
      {
        FileIO.deleteDirectory(cache);

        if (cache.exists())
        {
          log.info("Deleted the cache [" + cache + "].");
        }
        else
        {
          log.warn("Failed to delete the cache [" + cache + "].");
        }
      }
      catch (IOException e)
      {
        log.error("Error deleting the cache [" + cache + "].", e);
      }
    }
    else
    {
      log.info("The cache [" + cache + "] does not exist.");
    }
  }

  /**
   * Removes all cache files and directories.
   * 
   * @param cacheName
   */
  public static void removeCache(String cacheName)
  {
    String cacheDir = getGeoserverGWCDir() + cacheName;
    removeCache(new File(cacheDir));
  }

  /**
   * Checks if the given layer exists in Geoserver.
   * 
   * @param layer
   * @return
   */
  public static boolean layerExists(String layer)
  {
    return getReader().getLayer(layer) != null;
  }

  /**
   * Checks if the given layer has a database view.
   * 
   * @param layer
   * @return
   */
  public static boolean viewExists(String viewName)
  {
    // create the view if the view exists in the pg metadata
    ValueQuery check = new ValueQuery(new QueryFactory());

    check.SELECT(check.aSQLAggregateInteger("ct", "count(*)"));
    check.FROM("pg_views", "pg_views");
    check.WHERE(check.aSQLCharacter("viewname", "viewname").EQ(viewName));

    OIterator<? extends ValueObject> iter = check.getIterator();
    try
    {
      ValueObject vo = iter.next();
      if (Integer.valueOf(vo.getValue("ct")) == 1)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    finally
    {
      iter.close();
    }
  }

  /**
   * Calculates the bounding box of a specific layer.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public static double[] getBBOX(String viewName)
  {
    List<String> view = new LinkedList<String>();
    view.add(viewName);

    return getBBOX(view);
  }

  /**
   * Calculates the bounding box of all the layers.
   * 
   * @param views
   * @return double[] {minx, miny, maxx, maxy}
   */
  public static double[] getBBOX(List<String> views)
  {
    // collect all the views and extend the bounding box
    ValueQuery union = new ValueQuery(new QueryFactory());
    if (views.size() == 1)
    {
      String view = views.get(0);
      union.SELECT(union.aSQLClob(GEOM_COLUMN, GEOM_COLUMN, GEOM_COLUMN));
      union.FROM(view, view);
    }
    else if (views.size() > 1)
    {
      ValueQuery[] unionVQs = new ValueQuery[views.size()];
      for (int i = 0; i < unionVQs.length; i++)
      {
        String view = views.get(i);
        ValueQuery vq = new ValueQuery(union.getQueryFactory());
        vq.SELECT(vq.aSQLClob(GEOM_COLUMN, GEOM_COLUMN, GEOM_COLUMN));
        vq.FROM(view, view);

        unionVQs[i] = vq;
      }

      union.UNION_ALL(unionVQs);
    }
    else
    {
      // TODO throw better exception or a message
      throw new ProgrammingErrorException("The map has no layers");
    }

    ValueQuery collected = new ValueQuery(union.getQueryFactory());
    collected.SELECT(collected.aSQLAggregateClob("collected", "st_collect(" + GEOM_COLUMN + ")",
        "collected"));
    collected.FROM("(" + union.getSQL() + ")", "unioned");

    ValueQuery outer = new ValueQuery(union.getQueryFactory());
    outer.SELECT(union.aSQLAggregateDouble("minx", "st_xmin(collected)"), union.aSQLAggregateDouble(
        "miny", "st_ymin(collected)"), union.aSQLAggregateDouble("maxx", "st_xmax(collected)"), union
        .aSQLAggregateDouble("maxy", "st_ymax(collected)"));

    outer.FROM("(" + collected.getSQL() + ")", "collected");

    OIterator<? extends ValueObject> iter = outer.getIterator();
    ValueObject o;
    try
    {
      o = iter.next();
    }
    finally
    {
      iter.close();
    }

    double[] bbox = new double[4];
    bbox[MINX_INDEX] = Double.parseDouble(o.getValue("minx"));
    bbox[MINY_INDEX] = Double.parseDouble(o.getValue("miny"));
    bbox[MAXX_INDEX] = Double.parseDouble(o.getValue("maxx"));
    bbox[MAXY_INDEX] = Double.parseDouble(o.getValue("maxy"));

    return bbox;
  }

  /**
   * Reapplys all maps and creates database views if they do not exist.
   * GeoServer is notified of the changes.
   */
  @Authenticate
  public static void initializeGeoServer()
  {
    // FIXME Is this needed for the project?
//    Initializer.init();
  }

}

