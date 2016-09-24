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
package com.runwaysdk.business.ontology;

import com.runwaysdk.query.OIterator;

public class DefaultStrategy implements OntologyStrategyIF
{
  protected String termClass;
  
  public static class Singleton
  {
    public static DefaultStrategy INSTANCE = new DefaultStrategy();
  }

  /**
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#isInitialized(java.lang
   * .String)
   */
  @Override
  public boolean isInitialized()
  {
    return true;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#initialize()
   */
  @Override
  public void initialize(String relationshipType)
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

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#reinitialize()
   */
  @Override
  public void reinitialize(String relationshipType)
  {
    shutdown();
    initialize(relationshipType);
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
//    List<Term> retList = new ArrayList<Term>();
//    List<Term> ancestors = getDirectAncestors(term, relationshipType);
//    retList.addAll(ancestors);
//
//    for (Iterator<Term> i = ancestors.iterator(); i.hasNext();)
//    {
//      retList.addAll(getAllAncestors(i.next(), relationshipType));
//    }
//
//    return retList;
    
    throw new UnsupportedOperationException("Implement Me!");
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDecentants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public OIterator<Term> getAllDescendants(Term term, String relationshipType)
  {
//    List<Term> retList = new ArrayList<Term>();
//    List<Term> descendants = getDirectDescendants(term, relationshipType);
//    retList.addAll(descendants);
//
//    for (Iterator<Term> i = descendants.iterator(); i.hasNext();)
//    {
//      retList.addAll(this.getAllDescendants(i.next(), relationshipType));
//    }
//
//    return retList;
    
    throw new UnsupportedOperationException("Implement Me!");
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
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   * .business.ontology.Term, java.lang.String)
   */
  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    // NO OP
  }

  /**
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   * .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   * java.lang.String)
   */
  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    // NO OP
  }

  /**
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   * .ontology.Term, java.lang.String)
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

  @Override
  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
