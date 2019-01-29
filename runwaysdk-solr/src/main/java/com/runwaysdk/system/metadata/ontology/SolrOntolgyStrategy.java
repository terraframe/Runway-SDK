/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.metadata.ontology;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CursorMarkParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.DeleteStrategyProviderIF;
import com.runwaysdk.business.ontology.OntologyEntry;
import com.runwaysdk.business.ontology.OntologyEntryIF;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.InitializationStrategyIF;
import com.runwaysdk.business.ontology.QualifiedOntologyEntry;
import com.runwaysdk.business.ontology.QualifiedOntologyEntryIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy.SortOrder;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.Cache;

public class SolrOntolgyStrategy implements OntologyStrategyIF
{
  private static Logger logger        = LoggerFactory.getLogger(GeoEntitySolrOntologyStrategy.class);

  public static String  LABEL         = "label";

  public static String  OID           = "oid";

  public static String  ENTITY        = "entity";

  public static String  QUALIFIER     = "qualifier";

  public static String  RELATIONSHIPS = "relationships";

  private String        termClass;

  private String        sortAttriubte;

  public SolrOntolgyStrategy(String termClass)
  {
    this(termClass, null);
  }

  public SolrOntolgyStrategy(String termClass, String sortAttribute)
  {
    this.termClass = termClass;
    this.sortAttriubte = sortAttribute;
  }

  @Override
  public boolean isInitialized()
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();

      SolrQuery query = new SolrQuery();
      query.setQuery("*:*");
      query.setFields(OID);

      QueryResponse response = client.query(query);
      SolrDocumentList list = response.getResults();

      return ( list.getNumFound() > 0 );
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
  
  @Override
  public void initialize(String relationshipType)
  {
    this.initialize(relationshipType, null);
  }

  @Override
  public void initialize(String relationshipType, InitializationStrategyIF strategy)
  {
    if (this.isInitialized())
    {
      return;
    }

    logger.info("Initializing SolrOntolgyStrategy");

    try
    {
      SolrCommand command = this.getCommand();
      HttpSolrClient client = command.getClient();

      QueryFactory factory = new QueryFactory();
      BusinessQuery query = factory.businessQuery(this.termClass);

      if (this.sortAttriubte != null)
      {
        query.ORDER_BY(query.getS(this.sortAttriubte), SortOrder.ASC);
      }

      long total = query.getCount();
      long count = 0;

      OIterator<Business> it = query.getIterator();

      Cache<String, List<List<QualifiedOntologyEntryIF>>> cache = new Cache<String, List<List<QualifiedOntologyEntryIF>>>(1000);

      try
      {
        while (it.hasNext())
        {
          // if (count % 50 == 0)
          // {
          logger.info("SolrOntolgyStrategy Processing:" + count + "/" + total + " " + System.currentTimeMillis());
          // }

          Term term = (Term) it.next();

          List<List<QualifiedOntologyEntryIF>> paths = this.getPaths(term, relationshipType, cache);

          for (List<QualifiedOntologyEntryIF> path : paths)
          {
            JSONArray relationships = new JSONArray();

            for (QualifiedOntologyEntryIF p : path)
            {
              relationships.put(this.relationship(p));
            }

            // Create a new editable SolrInputDocument
            SolrInputDocument updateDoc = new SolrInputDocument();
            updateDoc.addField(ENTITY, term.getOid());
            updateDoc.addField(RELATIONSHIPS, this.serialize(relationships));
            updateDoc.addField(QUALIFIER, term.getQualifier());

            client.add(updateDoc);
          }

          JSONArray relationships = new JSONArray();
          relationships.put(this.relationship(term));

          SolrInputDocument updateDoc = new SolrInputDocument();
          updateDoc.addField(ENTITY, term.getOid());
          updateDoc.addField(RELATIONSHIPS, this.serialize(relationships));
          updateDoc.addField(QUALIFIER, term.getQualifier());

          client.add(updateDoc);

          count++;
        }
      }
      finally
      {
        it.close();
      }

      command.doIt();
    }
    catch (SolrServerException | IOException | JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    logger.info("SolrOntolgyStrategy finished initializing.");
  }

  @Override
  public void shutdown()
  {
    // Do nothing
  }

  @Override
  public void shutdown(String relationshipType)
  {
    // NO OP
  }

  @Override
  public void reinitialize(String relationshipType, InitializationStrategyIF strategy)
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();
      client.deleteByQuery(OID + ":*");

      this.initialize(relationshipType, strategy);
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void reinitialize(String relationshipType)
  {
    this.reinitialize(relationshipType, null);
  }

  @Override
  public void addLink(Term parent, Term child, String relationshipType)
  {
    try
    {
      SolrCommand command = this.getCommand();
      HttpSolrClient client = command.getClient();

      List<List<QualifiedOntologyEntryIF>> paths = this.getPaths(parent, relationshipType, new Cache<>(20));

      // Add new old paths
      SolrQuery query = new SolrQuery();
      query.setQuery(RELATIONSHIPS + ":*" + ClientUtils.escapeQueryChars(child.getOid()) + "*");
      query.setFields(ENTITY, RELATIONSHIPS, QUALIFIER);
      query.setRows(5000);
      query.addSort("oid", ORDER.asc); // Pay attention to this line
      String cursorMark = CursorMarkParams.CURSOR_MARK_START;
      boolean done = false;

      while (!done)
      {
        query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
        QueryResponse response = client.query(query);
        String nextCursorMark = response.getNextCursorMark();

        SolrDocumentList list = response.getResults();

        Iterator<SolrDocument> iterator = list.iterator();

        while (iterator.hasNext())
        {
          SolrDocument document = iterator.next();
          String value = (String) document.getFieldValue(RELATIONSHIPS);
          JSONArray relationships = this.deserialize(value);
          JSONArray base = new JSONArray();

          boolean found = false;

          for (int i = 0; i < relationships.length(); i++)
          {
            JSONObject relationship = relationships.getJSONObject(i);

            if (!found)
            {
              base.put(relationship);
            }

            if (relationship.getString(OID).equals(child.getOid()))
            {
              found = true;
            }
          }

          for (List<QualifiedOntologyEntryIF> path : paths)
          {
            JSONArray clone = new JSONArray(base.toString());

            for (QualifiedOntologyEntryIF p : path)
            {
              clone.put(this.relationship(p));
            }

            // Create a new editable SolrInputDocument
            SolrInputDocument updateDoc = new SolrInputDocument();
            updateDoc.addField(ENTITY, (String) document.get(ENTITY));
            updateDoc.addField(RELATIONSHIPS, this.serialize(clone));
            updateDoc.addField(QUALIFIER, (String) document.get(QUALIFIER));

            client.add(updateDoc);
          }
        }

        if (cursorMark.equals(nextCursorMark))
        {
          done = true;
        }
        cursorMark = nextCursorMark;
      }

      command.doIt();
    }
    catch (SolrServerException | IOException | JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private List<List<QualifiedOntologyEntryIF>> getPaths(Term term, String relationshipType, Cache<String, List<List<QualifiedOntologyEntryIF>>> cache)
  {
    List<List<QualifiedOntologyEntryIF>> paths = new LinkedList<>();
    List<Term> parents = term.getDirectAncestors(relationshipType).getAll();

    if (parents.size() > 0)
    {
      for (Term parent : parents)
      {
        if (!cache.containsKey(parent.getOid()))
        {
          cache.put(parent.getOid(), this.getPaths(parent, relationshipType, cache));
        }

        List<List<QualifiedOntologyEntryIF>> pp = cache.get(parent.getOid());

        for (List<QualifiedOntologyEntryIF> p : pp)
        {
          p.add(0, term);
        }

        paths.addAll(pp);
      }
    }
    else
    {
      LinkedList<QualifiedOntologyEntryIF> path = new LinkedList<QualifiedOntologyEntryIF>();
      path.add(term);

      // End case
      paths.add(path);
    }

    /*
     * Add all the synonym paths
     */
    List<OntologyEntryIF> synonyms = term.getSynonyms();

    List<List<QualifiedOntologyEntryIF>> sPaths = new LinkedList<>();

    for (OntologyEntryIF synonym : synonyms)
    {
      QualifiedOntologyEntry entry = new QualifiedOntologyEntry(synonym.getOid(), synonym.getLabel(), term.getQualifier());

      for (List<QualifiedOntologyEntryIF> path : paths)
      {
        LinkedList<QualifiedOntologyEntryIF> sPath = new LinkedList<QualifiedOntologyEntryIF>(path);
        sPath.remove(0);
        sPath.add(0, entry);

        sPaths.add(sPath);
      }
    }

    LinkedList<List<QualifiedOntologyEntryIF>> union = new LinkedList<>();
    union.addAll(paths);
    union.addAll(sPaths);

    return union;
  }

  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    // Do nothing

    return false;
  }

  @Override
  public OIterator<Term> getAllAncestors(Term term, String relationshipType)
  {
    // Do nothing

    return null;
  }

  @Override
  public OIterator<Term> getAllDescendants(Term term, String relationshipType)
  {
    // Do nothing

    return null;
  }

  @Override
  public OIterator<Term> getDirectAncestors(Term term, String relationshipType)
  {
    // Do nothing

    return null;
  }

  @Override
  public OIterator<Term> getDirectDescendants(Term term, String relationshipType)
  {
    // Do nothing

    return null;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   *      .ontology.Term, java.lang.String)
   */
  @Override
  public void add(Term term, String relationshipType)
  {
    try
    {
      SolrCommand command = this.getCommand();
      HttpSolrClient client = command.getClient();

      List<OntologyEntryIF> entries = term.getSynonyms();
      entries.add(0, term);

      String qualifier = term.getQualifier();

      for (OntologyEntryIF entry : entries)
      {
        JSONArray relationships = new JSONArray();
        relationships.put(this.relationship(qualifier, entry.getLabel(), entry.getOid()));

        SolrInputDocument document = new SolrInputDocument();
        document.addField(ENTITY, term.getOid());
        document.addField(RELATIONSHIPS, this.serialize(relationships));
        document.addField(QUALIFIER, term.getQualifier());

        client.add(document);
      }

      command.doIt();
    }
    catch (SolrServerException | IOException | JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();
      String qText = ClientUtils.escapeQueryChars(term.getOid());

      client.deleteByQuery(RELATIONSHIPS + ":*" + qText + "*");

      command.doIt();
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();
      String qText = ClientUtils.escapeQueryChars(parent.getOid());

      SolrQuery query = new SolrQuery();
      query.setQuery(ENTITY + ":" + term.getOid() + " AND " + RELATIONSHIPS + ":*" + qText + "*");
      query.setFields(OID, RELATIONSHIPS, QUALIFIER);
      query.setRows(5000);
      query.addSort("oid", ORDER.asc); // Pay attention to this line
      String cursorMark = CursorMarkParams.CURSOR_MARK_START;
      boolean done = false;

      while (!done)
      {
        query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
        QueryResponse response = client.query(query);
        String nextCursorMark = response.getNextCursorMark();

        SolrDocumentList list = response.getResults();

        Iterator<SolrDocument> iterator = list.iterator();

        while (iterator.hasNext())
        {
          SolrDocument document = iterator.next();
          String value = (String) document.getFieldValue(RELATIONSHIPS);
          JSONArray relationships = this.deserialize(value);
          JSONArray update = new JSONArray();

          boolean found = false;

          for (int i = 0; i < relationships.length(); i++)
          {
            JSONObject relationship = relationships.getJSONObject(i);

            if (relationship.getString(OID).equals(parent.getOid()))
            {
              found = true;
            }

            if (!found)
            {
              update.put(relationship);
            }
          }

          // Create a new editable SolrInputDocument
          SolrInputDocument updateDoc = new SolrInputDocument();
          updateDoc.addField(OID, (String) document.get(OID));
          updateDoc.addField(RELATIONSHIPS, this.serialize(update));
          updateDoc.addField(QUALIFIER, (String) document.get(QUALIFIER));

          client.add(updateDoc);
        }

        if (cursorMark.equals(nextCursorMark))
        {
          done = true;
        }
        cursorMark = nextCursorMark;
      }

      command.doIt();
    }
    catch (SolrServerException | IOException | JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void configure(String termClass)
  {
    // Do nothing
  }

  @Override
  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType)
  {
    // Do nothing

    return null;
  }

  @Override
  public void addSynonym(Term term, OntologyEntryIF synonym)
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();
      SolrQuery query = new SolrQuery();
      query.setQuery(RELATIONSHIPS + ":*" + ClientUtils.escapeQueryChars(term.getOid()) + "*");
      query.setFields(ENTITY, RELATIONSHIPS, QUALIFIER);
      query.setRows(5000);
      query.addSort("oid", ORDER.asc); // Pay attention to this line
      String cursorMark = CursorMarkParams.CURSOR_MARK_START;
      boolean done = false;

      while (!done)
      {
        query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
        QueryResponse response = client.query(query);
        String nextCursorMark = response.getNextCursorMark();

        SolrDocumentList list = response.getResults();

        Iterator<SolrDocument> iterator = list.iterator();

        while (iterator.hasNext())
        {
          SolrDocument document = iterator.next();
          String value = (String) document.getFieldValue(RELATIONSHIPS);
          JSONArray relationships = this.deserialize(value);

          for (int i = 0; i < relationships.length(); i++)
          {
            JSONObject relationship = relationships.getJSONObject(i);

            if (relationship.getString(OID).equals(term.getOid()))
            {
              relationship.put(OID, synonym.getOid());
              relationship.put(LABEL, synonym.getLabel());
            }
          }

          // Create a new editable SolrInputDocument
          SolrInputDocument clone = new SolrInputDocument();
          clone.addField(ENTITY, (String) document.get(ENTITY));
          clone.addField(RELATIONSHIPS, this.serialize(relationships));
          clone.addField(QUALIFIER, (String) document.get(QUALIFIER));

          client.add(clone);
        }
        if (cursorMark.equals(nextCursorMark))
        {
          done = true;
        }
        cursorMark = nextCursorMark;
      }

      command.doIt();
    }
    catch (SolrServerException | IOException | JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void updateSynonym(OntologyEntryIF synonym)
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();

      SolrQuery query = new SolrQuery();
      query.setQuery(RELATIONSHIPS + ":*" + ClientUtils.escapeQueryChars(synonym.getOid()) + "*");
      query.setFields(OID, RELATIONSHIPS, QUALIFIER);
      query.setRows(5000);
      query.addSort("oid", ORDER.asc); // Pay attention to this line
      String cursorMark = CursorMarkParams.CURSOR_MARK_START;
      boolean done = false;

      while (!done)
      {
        query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
        QueryResponse response = client.query(query);
        String nextCursorMark = response.getNextCursorMark();

        SolrDocumentList list = response.getResults();

        Iterator<SolrDocument> iterator = list.iterator();

        while (iterator.hasNext())
        {
          SolrDocument document = iterator.next();
          String value = (String) document.getFieldValue(RELATIONSHIPS);
          JSONArray relationships = this.deserialize(value);

          for (int i = 0; i < relationships.length(); i++)
          {
            JSONObject relationship = relationships.getJSONObject(i);

            if (relationship.getString(OID).equals(synonym.getOid()))
            {
              relationship.put(LABEL, synonym.getLabel());
            }
          }

          // Create a new editable SolrInputDocument
          SolrInputDocument updateDoc = new SolrInputDocument();
          updateDoc.addField(OID, (String) document.get(OID));
          updateDoc.addField(RELATIONSHIPS, this.serialize(relationships));
          updateDoc.addField(QUALIFIER, (String) document.get(QUALIFIER));

          client.add(updateDoc);
        }

        if (cursorMark.equals(nextCursorMark))
        {
          done = true;
        }
        cursorMark = nextCursorMark;
      }

      command.doIt();
    }
    catch (SolrServerException | IOException | JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void removeSynonym(OntologyEntryIF synonym)
  {
    try
    {
      SolrCommand command = this.getCommand();
      SolrClient client = command.getClient();
      String qText = ClientUtils.escapeQueryChars(synonym.getOid());

      client.deleteByQuery(RELATIONSHIPS + ":*" + qText + "*");

      command.doIt();
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void updateLabel(Term term, String label)
  {
    this.updateSynonym(new OntologyEntry(label, term.getOid()));
  }

  public SolrCommand getCommand()
  {
    TransactionState state = TransactionState.getCurrentTransactionState();
    List<Command> commands = state.getNotUndoableCommandList();

    for (Command command : commands)
    {
      if (command instanceof SolrCommand)
      {
        return (SolrCommand) command;
      }
    }

    HttpSolrClient client = new HttpSolrClient.Builder(SolrProperties.getUrl()).build();
    return new SolrCommand(client);
  }

  public JSONObject relationship(QualifiedOntologyEntryIF t) throws JSONException
  {
    String qualifier = t.getQualifier();

    JSONObject relationship = new JSONObject();
    relationship.put(LABEL, t.getLabel());
    relationship.put(OID, t.getOid());

    if (qualifier != null)
    {
      relationship.put(QUALIFIER, qualifier);
    }

    return relationship;
  }

  public JSONObject relationship(String qualifier, String label, String oid) throws JSONException
  {
    JSONObject relationship = new JSONObject();
    relationship.put(LABEL, label);
    relationship.put(OID, oid);

    if (qualifier != null)
    {
      relationship.put(QUALIFIER, qualifier);
    }
    return relationship;
  }

  public String serialize(JSONArray array) throws JSONException
  {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < array.length(); i++)
    {
      JSONObject object = array.getJSONObject(i);

      if (i != 0)
      {
        builder.append("####");
      }

      if (object.has(QUALIFIER))
      {
        builder.append( ( (String) object.get(QUALIFIER) ) + "%%%");
      }

      builder.append( ( (String) object.get(LABEL) ));
      builder.append("###" + ( (String) object.get(OID) ));
    }

    return builder.toString();
  }

  public JSONArray deserialize(String value) throws JSONException
  {
    JSONArray array = new JSONArray();

    String[] tokens = value.split("####");

    for (String token : tokens)
    {
      String[] split = token.split("###");
      String label = split[0];
      String oid = split[1];
      String qualifier = null;

      if (label.contains("%%%"))
      {
        String[] lSplit = label.split("%%%");

        qualifier = lSplit[0];
        label = lSplit[1];
      }

      array.put(this.relationship(qualifier, label, oid));
    }

    return array;
  }
}
