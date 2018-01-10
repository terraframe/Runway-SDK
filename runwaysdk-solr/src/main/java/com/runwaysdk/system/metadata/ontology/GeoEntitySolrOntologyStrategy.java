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

import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.QualifiedOntologyEntry;
import com.runwaysdk.business.ontology.QualifiedOntologyEntryIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class GeoEntitySolrOntologyStrategy extends SolrOntolgyStrategy implements OntologyStrategyIF
{
  static class Location
  {
    private String              universal;

    private Map<String, String> labels;

    public Location(String universal)
    {
      this.universal = universal;
      this.labels = new HashMap<String, String>(5);
    }

    public void add(String id, String label)
    {
      this.labels.put(id, label);
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

  public static final String CHILD_ID       = "child_id";

  public static final String PARENT_ID      = "parent_id";

  public static final String LABEL          = "label";

  public static final String SYNONYM        = "synonym";

  public static final String SYNONYM_ID     = "synonym_id";

  public static final String UNIVERSAL      = "universal";

  public GeoEntitySolrOntologyStrategy()
  {
    super(GeoEntity.CLASS);
  }

  @Override
  public void initialize(String relationshipType)
  {
    if (this.isInitialized())
    {
      return;
    }
    
    SolrCommand command = this.getCommand();
    HttpSolrClient client = command.getClient();

    StringBuilder builder = new StringBuilder();
    builder.append("WITH RECURSIVE quick_paths (original_child) AS (");
    builder.append("  SELECT li.child_id AS original_child, li.child_id, li.parent_id, ge.universal, gdl.default_locale AS label, sy.id AS synonym_id, sdl.default_locale AS synonym");
    builder.append("  FROM located_in AS li");
    builder.append("  JOIN geo_entity AS ge ON ge.id = li.parent_id");
    builder.append("  JOIN geo_entity_display_label AS gdl ON gdl.id = ge.display_label");
    builder.append("  LEFT OUTER JOIN synonym_relationship AS sr ON ge.id = sr.parent_id");
    builder.append("  LEFT OUTER JOIN rwsynonym AS sy ON sy.id = sr.child_id");
    builder.append("  LEFT OUTER JOIN synonym_display_label AS sdl ON sdl.id = sy.display_label");
    builder.append("  UNION");
    builder.append("  SELECT li.child_id AS original_child, li.child_id, li.child_id AS parent_id, ge.universal, gdl.default_locale AS label, sy.id AS synonym_id, sdl.default_locale AS synonym");
    builder.append("  FROM located_in AS li");
    builder.append("  JOIN geo_entity AS ge ON ge.id = li.child_id");
    builder.append("  JOIN geo_entity_display_label AS gdl ON gdl.id = ge.display_label");
    builder.append("  LEFT OUTER JOIN synonym_relationship AS sr ON ge.id = sr.parent_id");
    builder.append("  LEFT OUTER JOIN rwsynonym AS sy ON sy.id = sr.child_id");
    builder.append("  LEFT OUTER JOIN synonym_display_label AS sdl ON sdl.id = sy.display_label");
    builder.append("  UNION");
    builder.append("  SELECT original_child, l.child_id, l.parent_id, ge.universal, gdl.default_locale, sy.id, sdl.default_locale");
    builder.append("  FROM located_in AS l");
    builder.append("  JOIN geo_entity AS ge ON ge.id = l.parent_id");
    builder.append("  JOIN geo_entity_display_label AS gdl ON gdl.id = ge.display_label");
    builder.append("  LEFT OUTER JOIN synonym_relationship AS sr ON ge.id = sr.parent_id");
    builder.append("  LEFT OUTER JOIN rwsynonym AS sy ON sy.id = sr.child_id");
    builder.append("  LEFT OUTER JOIN synonym_display_label AS sdl ON sdl.id = sy.display_label");
    builder.append("  INNER JOIN quick_paths ON (l.child_id  = quick_paths.parent_id)");
    builder.append(")");
    builder.append("  SELECT *");
    builder.append("  FROM quick_paths");
    builder.append("  order by original_child, parent_id");

    try
    {
      ResultSet results = null;

      try
      {
        System.out.println("Start Query: " + System.currentTimeMillis());
        
        results = Database.query(builder.toString());
        
        System.out.println("Finished Query: " + System.currentTimeMillis());        

        String prevId = null;
        Map<String, Set<String>> relationships = new HashMap<>();
        Map<String, Location> locations = new HashMap<>();
        
        long count = 0;

        while (results.next())
        {
          String id = results.getString(ORIGINAL_CHILD);
          String parentId = results.getString(PARENT_ID);
          String childId = results.getString(CHILD_ID);
          String label = results.getString(LABEL);
          String synonym = results.getString(SYNONYM);
          String synonymId = results.getString(SYNONYM_ID);
          String universal = results.getString(UNIVERSAL);

          if (prevId != null && !prevId.equals(id))
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

            // Reset entries
            relationships = new HashMap<>();
            locations = new HashMap<>();
            
            System.out.println("Processed entity [" + (count++) + "]: " + System.currentTimeMillis());            
          }

          // Just add a synonym
          if (synonym != null && synonym.length() > 0)
          {
            locations.putIfAbsent(parentId, new Location(universal));

            locations.get(parentId).add(synonymId, synonym);
          }

          if (label != null && label.length() > 0)
          {
            locations.putIfAbsent(parentId, new Location(universal));

            locations.get(parentId).add(parentId, label);
          }

          if (!childId.equals(parentId))
          {
            relationships.putIfAbsent(childId, new TreeSet<String>());
            relationships.get(childId).add(parentId);
          }

          prevId = id;
        }
        
        
        System.out.println("Finished Index: " + System.currentTimeMillis());        
        
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
  }

  private List<List<QualifiedOntologyEntryIF>> getPaths(String entityId, Map<String, Set<String>> relationships, Map<String, Location> locations)
  {
    List<List<QualifiedOntologyEntryIF>> paths = new LinkedList<>();
    Set<String> parentIds = relationships.get(entityId);

    if (parentIds != null && parentIds.size() > 0)
    {
      for (String parentId : parentIds)
      {
        List<List<QualifiedOntologyEntryIF>> pp = this.getPaths(parentId, relationships, locations);
        
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
