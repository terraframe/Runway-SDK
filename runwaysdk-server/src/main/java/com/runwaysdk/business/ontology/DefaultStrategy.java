/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.business.ontology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public class DefaultStrategy implements OntologyStrategyIF
{
  public static class Singleton
  {
    public static DefaultStrategy INSTANCE = new DefaultStrategy();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#isInitialized(java.lang
   * .String)
   */
  @Override
  public boolean isInitialized(String relationshipType)
  {
    return true;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#initialize()
   */
  @Override
  public void initialize(String relationshipType)
  {

  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#shutdown()
   */
  @Override
  public void shutdown()
  {

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
  public Relationship addLink(Term parent, Term child, String relationshipType)
  {
    Relationship rel = parent.addChild(child, relationshipType);
    
    rel.apply();
    
    return rel;
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
  public List<Term> getAllAncestors(Term term, String relationshipType)
  {
    List<Term> retList = new ArrayList<Term>();
    List<Term> ancestors = getDirectAncestors(term, relationshipType);
    retList.addAll(ancestors);

    for (Iterator<Term> i = ancestors.iterator(); i.hasNext();)
    {
      retList.addAll(getAllAncestors(i.next(), relationshipType));
    }

    return retList;
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term)
   */
  @Override
  public List<TermAndRel> getAllAncestors(Term term)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDecentants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public List<Term> getAllDescendants(Term term, String relationshipType)
  {
    List<Term> retList = new ArrayList<Term>();
    List<Term> descendants = getDirectDescendants(term, relationshipType);
    retList.addAll(descendants);

    for (Iterator<Term> i = descendants.iterator(); i.hasNext();)
    {
      retList.addAll(this.getAllDescendants(i.next(), relationshipType));
    }

    return retList;
  }
  
  /**
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllDescendants
   * (com.runwaysdk.business.ontology.Term)
   */
  @Override
  public List<TermAndRel> getAllDescendants(Term term)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Term> getDirectAncestors(Term term, String relationshipType)
  {
    return (List<Term>) term.getParents(relationshipType).getAll();
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<TermAndRel> getDirectAncestors(Term term)
  {
    BusinessDAO business = (BusinessDAO) BusinessDAO.get(term.getId());
    List<RelationshipDAOIF> rels = business.getAllParents();
    
    List<TermAndRel> children = new ArrayList<TermAndRel>();
    for (RelationshipDAOIF rel : rels) {
      Term childTerm = (Term) BusinessFacade.getEntity(rel.getChildId());
      
      children.add(new TermAndRel(childTerm, rel.getType(), rel.getId()));
    }
    
    return children;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescentents(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Term> getDirectDescendants(Term term, String relationshipType)
  {
    return (List<Term>) term.getChildren(relationshipType).getAll();
  }
  
  /**
   * @see
   * com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectDescendants
   * (com.runwaysdk.business.ontology.Term)
   */
  @Override
  public List<TermAndRel> getDirectDescendants(Term term)
  {
    BusinessDAO business = (BusinessDAO) BusinessDAO.get(term.getId());
    List<RelationshipDAOIF> rels = business.getAllChildren();
    
    List<TermAndRel> children = new ArrayList<TermAndRel>();
    for (RelationshipDAOIF rel : rels) {
      Term childTerm = (Term) BusinessFacade.getEntity(rel.getChildId());
      
      children.add(new TermAndRel(childTerm, rel.getType(), rel.getId()));
    }
    
    return children;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   * .business.ontology.Term, java.lang.String)
   */
  @Override
  public void removeTerm(Term term, String relationshipType)
  {
    List<Term> parents = this.getDirectAncestors(term, relationshipType);

    for (Term parent : parents)
    {
      term.removeAllParents(parent, relationshipType);
    }

    List<Term> children = this.getDirectDescendants(term, relationshipType);

    for (Term child : children)
    {
      term.removeAllChildren(child, relationshipType);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#remove(com.runwaysdk
   * .business.ontology.Term, com.runwaysdk.business.ontology.Term,
   * java.lang.String)
   */
  @Override
  public void removeLink(Term parent, Term term, String relationshipType)
  {
    term.removeAllParents(parent, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.ontology.OntologyStrategyIF#add(com.runwaysdk.business
   * .ontology.Term, java.lang.String)
   */
  @Override
  public void add(Term term, String relationshipType)
  {
    // NO OP
  }

}
