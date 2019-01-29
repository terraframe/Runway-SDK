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
package com.runwaysdk.system.metadata.ontology;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.InitializationStrategyIF;
import com.runwaysdk.business.ontology.QualifiedOntologyEntry;
import com.runwaysdk.business.ontology.QualifiedOntologyEntryIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class GeoEntitySolrOntologyStrategy extends SolrOntolgyStrategy implements OntologyStrategyIF
{
  private static Logger logger = LoggerFactory.getLogger(GeoEntitySolrOntologyStrategy.class);
  
  static class Location
  {
    private String              universal;

    private Map<String, String> labels;

    public Location(String universal)
    {
      this.universal = universal;
      this.labels = new HashMap<String, String>(5);
    }

    public void add(String oid, String label)
    {
      this.labels.put(oid, label);
    }

    public String getUniversal()
    {
      return universal;
    }

    public Map<String, String> getLabels()
    {
      return labels;
    }
  }

  public static final String ORIGINAL_CHILD = "original_child";

  public static final String CHILD_OID       = "child_oid";

  public static final String PARENT_OID      = "parent_oid";

  public static final String LABEL          = "label";

  public static final String SYNONYM        = "synonym";

  public static final String SYNONYM_ID     = "synonym_id";

  public static final String UNIVERSAL      = "universal";

  public GeoEntitySolrOntologyStrategy()
  {
    super(GeoEntity.CLASS);
  }

  @Override
  public void initialize(String relationshipType, InitializationStrategyIF strategy)
  {
    if (this.isInitialized())
    {
      return;
    }
    
    logger.info("Initializing GeoEntitySolrOntologyStrategy");

    SolrCommand command = this.getCommand();
    HttpSolrClient client = command.getClient();

    StringBuilder builder = new StringBuilder();
    builder.append("WITH RECURSIVE quick_paths (original_child) AS (");
    builder.append("  SELECT li.child_oid AS original_child, li.child_oid, li.parent_oid, ge.universal, gdl.default_locale AS label, sy.oid AS synonym_id, sdl.default_locale AS synonym");
    builder.append("  FROM located_in AS li");
    builder.append("  JOIN geo_entity AS ge ON ge.oid = li.parent_oid");
    builder.append("  JOIN geo_entity_display_label AS gdl ON gdl.oid = ge.display_label");
    builder.append("  LEFT OUTER JOIN synonym_relationship AS sr ON ge.oid = sr.parent_oid");
    builder.append("  LEFT OUTER JOIN rwsynonym AS sy ON sy.oid = sr.child_oid");
    builder.append("  LEFT OUTER JOIN synonym_display_label AS sdl ON sdl.oid = sy.display_label");
    builder.append("  UNION");
    builder.append("  SELECT li.child_oid AS original_child, li.child_oid, li.child_oid AS parent_oid, ge.universal, gdl.default_locale AS label, sy.oid AS synonym_id, sdl.default_locale AS synonym");
    builder.append("  FROM located_in AS li");
    builder.append("  JOIN geo_entity AS ge ON ge.oid = li.child_oid");
    builder.append("  JOIN geo_entity_display_label AS gdl ON gdl.oid = ge.display_label");
    builder.append("  LEFT OUTER JOIN synonym_relationship AS sr ON ge.oid = sr.parent_oid");
    builder.append("  LEFT OUTER JOIN rwsynonym AS sy ON sy.oid = sr.child_oid");
    builder.append("  LEFT OUTER JOIN synonym_display_label AS sdl ON sdl.oid = sy.display_label");
    builder.append("  UNION");
    builder.append("  SELECT original_child, l.child_oid, l.parent_oid, ge.universal, gdl.default_locale, sy.oid, sdl.default_locale");
    builder.append("  FROM located_in AS l");
    builder.append("  JOIN geo_entity AS ge ON ge.oid = l.parent_oid");
    builder.append("  JOIN geo_entity_display_label AS gdl ON gdl.oid = ge.display_label");
    builder.append("  LEFT OUTER JOIN synonym_relationship AS sr ON ge.oid = sr.parent_oid");
    builder.append("  LEFT OUTER JOIN rwsynonym AS sy ON sy.oid = sr.child_oid");
    builder.append("  LEFT OUTER JOIN synonym_display_label AS sdl ON sdl.oid = sy.display_label");
    builder.append("  INNER JOIN quick_paths ON (l.child_oid  = quick_paths.parent_oid)");
    builder.append(")");
    builder.append("  SELECT *");
    builder.append("  FROM quick_paths");
    builder.append("  order by original_child, parent_oid");

    try
    {
      ResultSet results = null;

      try
      {
        logger.info("GeoEntitySolrOntologyStrategy Start Query: " + System.currentTimeMillis());

        results = Database.query(builder.toString());

        logger.info("GeoEntitySolrOntologyStrategy Finished Query: " + System.currentTimeMillis());

        String prevId = null;
        Map<String, Set<String>> relationships = new HashMap<>();
        Map<String, Location> locations = new HashMap<>();

        long count = 0;

        while (results.next())
        {
          String oid = results.getString(ORIGINAL_CHILD);
          String parentOid = results.getString(PARENT_OID);
          String childOid = results.getString(CHILD_OID);
          String label = results.getString(LABEL);
          String synonym = results.getString(SYNONYM);
          String synonymId = results.getString(SYNONYM_ID);
          String universal = results.getString(UNIVERSAL);

          if (prevId != null && !prevId.equals(oid))
          {
            // Process Entry

            List<List<QualifiedOntologyEntryIF>> paths = this.getPaths(prevId, relationships, locations);

            for (List<QualifiedOntologyEntryIF> path : paths)
            {
              JSONArray array = new JSONArray();

              for (QualifiedOntologyEntryIF p : path)
              {
                array.put(this.relationship(p));
              }

              // Create a new editable SolrInputDocument
              SolrInputDocument updateDoc = new SolrInputDocument();
              updateDoc.addField(ENTITY, prevId);
              updateDoc.addField(RELATIONSHIPS, this.serialize(array));
              updateDoc.addField(QUALIFIER, locations.get(prevId).getUniversal());

              client.add(updateDoc);
            }

            Location location = locations.get(prevId);

            Map<String, String> labels = location.getLabels();
            Set<Entry<String, String>> entries = labels.entrySet();

            for (Entry<String, String> entry : entries)
            {
              QualifiedOntologyEntry qu = new QualifiedOntologyEntry(entry.getKey(), entry.getValue(), location.getUniversal());

              JSONArray array = new JSONArray();
              array.put(this.relationship(qu));

              SolrInputDocument updateDoc = new SolrInputDocument();
              updateDoc.addField(ENTITY, prevId);
              updateDoc.addField(RELATIONSHIPS, this.serialize(array));
              updateDoc.addField(QUALIFIER, location.getUniversal());

              client.add(updateDoc);
            }

            // Reset entries
            relationships = new HashMap<>();
            locations = new HashMap<>();

            logger.info("GeoEntitySolrOntologyStrategy Processed entity [" + ( count++ ) + "]: " + System.currentTimeMillis());
          }

          // Just add a synonym
          if (synonym != null && synonym.length() > 0)
          {
            locations.putIfAbsent(parentOid, new Location(universal));

            locations.get(parentOid).add(synonymId, synonym);
          }

          if (label != null && label.length() > 0)
          {
            locations.putIfAbsent(parentOid, new Location(universal));

            locations.get(parentOid).add(parentOid, label);
          }

          if (!childOid.equals(parentOid))
          {
            relationships.putIfAbsent(childOid, new TreeSet<String>());
            relationships.get(childOid).add(parentOid);
          }

          prevId = oid;
        }

        logger.info("GeoEntitySolrOntologyStrategy Finished Index: " + System.currentTimeMillis());

        command.doIt();
      }
      finally
      {
        if (results != null)
        {
          results.close();
        }
      }
    }
    catch (SQLException | JSONException | IOException | SolrServerException e)
    {
      throw new ProgrammingErrorException(e);
    }
    
    logger.info("GeoEntitySolrOntologyStrategy finished initializing.");
  }

  private List<List<QualifiedOntologyEntryIF>> getPaths(String entityId, Map<String, Set<String>> relationships, Map<String, Location> locations)
  {
    List<List<QualifiedOntologyEntryIF>> paths = new LinkedList<>();
    Set<String> parentOids = relationships.get(entityId);

    if (parentOids != null && parentOids.size() > 0)
    {
      for (String parentOid : parentOids)
      {
        List<List<QualifiedOntologyEntryIF>> pp = this.getPaths(parentOid, relationships, locations);

        paths.addAll(pp);
      }
    }
    else
    {
      // End case
      paths.add(new LinkedList<QualifiedOntologyEntryIF>());
    }

    /*
     * Add self to all of the paths
     */
    Location location = locations.get(entityId);
    Map<String, String> labels = location.getLabels();
    Set<Entry<String, String>> entries = labels.entrySet();

    LinkedList<List<QualifiedOntologyEntryIF>> union = new LinkedList<>();

    for (Entry<String, String> entry : entries)
    {
      QualifiedOntologyEntry qu = new QualifiedOntologyEntry(entry.getKey(), entry.getValue(), location.getUniversal());

      for (List<QualifiedOntologyEntryIF> path : paths)
      {
        LinkedList<QualifiedOntologyEntryIF> sPath = new LinkedList<QualifiedOntologyEntryIF>(path);
        sPath.add(0, qu);

        union.add(sPath);
      }
    }

    return union;
  }

}
