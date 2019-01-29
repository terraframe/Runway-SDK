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
package com.runwaysdk.business.ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.OIterator;

public class DefaultStrategy implements OntologyStrategyIF
{
  protected String termClass;

  // private String relationshipType;

  public static class Singleton
  {
    public static DefaultStrategy INSTANCE = new DefaultStrategy();
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isInitialized(java.lang
   *      .String)
   */
  @Override
  public boolean isInitialized()
  {
    return true;
  }
  
  @Override
  public void initialize(String relationshipType)
  {
    // NO OP
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#initialize()
   */
  @Override
  public void initialize(String relationshipType, InitializationStrategyIF strategy)
  {
    // NO OP
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#shutdown()
   */
  @Override
  public void shutdown()
  {
    // NO OP
  }

  @Override
  public void shutdown(String relationshipType)
  {
    // NO OP
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#reinitialize()
   */
  @Override
  public void reinitialize(String relationshipType, InitializationStrategyIF strategy)
  {
    shutdown();
    initialize(relationshipType, strategy);
  }
  
  @Override
  public void reinitialize(String relationshipType)
  {
    this.reinitialize(relationshipType, null);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#addLink(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public void addLink(Term parent, Term child, String relationshipType)
  {
    // NO OP
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isLeaf(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    return !term.getChildren(relationshipType).hasNext();
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public OIterator<Term> getAllAncestors(Term term, String relationshipType)
  {
    List<Term> retList = new ArrayList<Term>();
    List<Term> ancestors = getDirectAncestors(term, relationshipType).getAll();
    retList.addAll(ancestors);

    for (Iterator<Term> i = ancestors.iterator(); i.hasNext();)
    {
      retList.addAll(getAllAncestors(i.next(), relationshipType).getAll());
    }

    return new InMemoryObjectIterator<Term>(retList);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDecentants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public OIterator<Term> getAllDescendants(Term term, String relationshipType)
  {
    List<Term> retList = new ArrayList<Term>();
    List<Term> descendants = getDirectDescendants(term, relationshipType).getAll();
    retList.addAll(descendants);

    for (Iterator<Term> i = descendants.iterator(); i.hasNext();)
    {
      retList.addAll(this.getAllDescendants(i.next(), relationshipType).getAll());
    }

    return new InMemoryObjectIterator<Term>(retList);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @SuppressWarnings("unchecked")
  @Override
  public OIterator<Term> getDirectAncestors(Term term, String relationshipType)
  {
    return (OIterator<Term>) term.getParents(relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescentents(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @SuppressWarnings("unchecked")
  @Override
  public OIterator<Term> getDirectDescendants(Term term, String relationshipType)
  {
    return (OIterator<Term>) term.getChildren(relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   *      .business.ontology.Term, java.lang.String)
   */
  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    // NO OP
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   *      .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   *      java.lang.String)
   */
  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    // NO OP
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   *      .ontology.Term, java.lang.String)
   */
  @Override
  public void add(Term term, String relationshipType)
  {
    // NO OP
  }

  @Override
  public void configure(String termClass)
  {
    this.termClass = termClass;
  }

  private class InMemoryObjectIterator<T> implements OIterator<T>
  {
    List<T>     list;

    Iterator<T> it;

    private InMemoryObjectIterator(List<T> list)
    {
      this.list = list;
      it = this.list.iterator();
    }

    @Override
    public Iterator<T> iterator()
    {
      return it;
    }

    @Override
    public T next()
    {
      return it.next();
    }

    @Override
    public void remove()
    {
      it.remove();
    }

    @Override
    public boolean hasNext()
    {
      return it.hasNext();
    }

    @Override
    public void close()
    {

    }

    @Override
    public List<T> getAll()
    {
      return list;
    }

  }

  public class DefaultDeleteStrategyProvider implements DeleteStrategyProviderIF
  {
    private String relationshipType;

    private DefaultDeleteStrategyProvider(Term deleteRoot, String relationshipType)
    {
      this.relationshipType = relationshipType;
    }

    @Override
    public boolean isTermAlreadyProcessed(Term child, Stack<Term> s, String tempTableName)
    {
      int count = 0;

      ResultSet resultSet = Database.selectFromWhere("count(*)", tempTableName, Term.TEMP_TERM_ID_COL + " = '" + child.getOid() + "'");
      try
      {
        if (resultSet.next())
        {
          count = resultSet.getInt("count");
        }
      }
      catch (SQLException sqlEx1)
      {
        Database.throwDatabaseException(sqlEx1);
      }
      finally
      {
        try
        {
          java.sql.Statement statement = resultSet.getStatement();
          resultSet.close();
          statement.close();
        }
        catch (SQLException sqlEx2)
        {
          Database.throwDatabaseException(sqlEx2);
        }
      }

      return count > 0 && this.doesAncestorHaveMultipleParents(child, s);
    }

    @Override
    public boolean doesAncestorHaveMultipleParents(Term child, Stack<Term> s)
    {
      List<Term> ancestors = DefaultStrategy.this.getDirectAncestors(child, relationshipType).getAll();

      if (ancestors.size() > 1)
      {
        return true;
      }

      for (Iterator<Term> i = ancestors.iterator(); i.hasNext();)
      {
        boolean doesParent = doesAncestorHaveMultipleParents(i.next(), s);

        if (doesParent)
        {
          return true;
        }
      }

      return false;
    }
  }

  @Override
  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType)
  {
    return new DefaultDeleteStrategyProvider(deleteRoot, relationshipType);
  }

  @Override
  public void addSynonym(Term term, OntologyEntryIF synonym)
  {
    // Do nothing
  }

  @Override
  public void updateSynonym(OntologyEntryIF synonym)
  {
    // Do nothing
  }

  @Override
  public void removeSynonym(OntologyEntryIF synonym)
  {
    // Do nothing
  }

  @Override
  public void updateLabel(Term term, String label)
  {
    // Do nothing
  }
}
