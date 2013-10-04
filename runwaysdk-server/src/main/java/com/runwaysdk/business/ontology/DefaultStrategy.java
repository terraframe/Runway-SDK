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
package com.runwaysdk.business.ontology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.runwaysdk.business.Business;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.metadata.MdTerm;

public class DefaultStrategy implements OntologyStrategyIF
{
  public static class Singleton {
    public static DefaultStrategy INSTANCE = new DefaultStrategy();
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#initialize()
   */
  @Override
  public void initialize()
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
  public void reinitialize()
  {
    shutdown();
    initialize();
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#copyTerm(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public void copyTerm(Term parent, Term child, String relationshipType)
  {
    Term childCopy;
    try
    {
      childCopy = child.getClass().newInstance();
      child.copyTo(childCopy);
      childCopy.apply();
      
      parent.addChild(childCopy, relationshipType).apply();
      
      OIterator<? extends Business> children = child.getChildren(relationshipType);
      while (children.hasNext()) {
        copyTerm(childCopy, (Term) children.next(), relationshipType);
      }
    }
    catch (InstantiationException e)
    {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isLeaf(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    return !term.getChildren(relationshipType).hasNext();
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public List<Term> getAllAncestors(Term term, String relationshipType)
  {
    List<Term> retList = new ArrayList<Term>();
    List<Term> ancestors = getDirectAncestors(term, relationshipType);
    retList.addAll(ancestors);
    
    for (Iterator<Term> i = ancestors.iterator(); i.hasNext();) {
      retList.addAll(getAllAncestors(i.next(), relationshipType));
    }
    
    return retList;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDecentants(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  @Override
  public List<Term> getAllDescendants(Term term, String relationshipType)
  {
    List<Term> retList = new ArrayList<Term>();
    List<Term> descendants = getDirectDescendants(term, relationshipType);
    retList.addAll(descendants);
    
    for (Iterator<Term> i = descendants.iterator(); i.hasNext();) {
      retList.addAll(getAllDescendants(i.next(), relationshipType));
    }
    
    return retList;
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Term> getDirectAncestors(Term term, String relationshipType)
  {
    return (List<Term>) term.getParents(relationshipType).getAll();
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescentents(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Term> getDirectDescendants(Term term, String relationshipType)
  {
    return (List<Term>) term.getChildren(relationshipType).getAll();
  }

}
