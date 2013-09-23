/**
 * 
 */
package com.runwaysdk.business.ontology;

import java.util.List;

import com.runwaysdk.business.Relationship;

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
abstract public class AbstractOntologyStrategy
{
  abstract public void initialize();
  
  abstract public void shutdown();
  
  public void reinitialize() {
    shutdown();
    initialize();
  }
  
  /**
   * Copies the child Term and its subtree beneath the given parent Term. The
   * child Term must already exist for this to work.
   * 
   * @param parent
   * @param child
   */
  abstract public void copyTerm(Term parent, Term child, String relationshipType);
  
  /**
   * Returns true if the term is a leaf node. Leaf nodes have no children.
   * 
   * @param term
   * @param relat
   */
  abstract public boolean isLeaf(Term term, String relationshipType);
  
//  abstract public void newTermRelationship(Term term1, Term term2, TermRelationship relat);
//  
//  abstract public void deleteTermRelationship(Term term1, Term term2, TermRelationship relat);
  
  /**
   * Returns all parents of the given term, including parents of parents.
   * 
   * @param term
   * @param relat
   */
  abstract public List<Term> getAllAncestors(Term term, String relationshipType);
  
  /**
   * Returns all children of the given term, including children of children.
   * 
   * @param term
   * @param relat
   */
  abstract public List<Term> getAllDescendants(Term term, String relationshipType);
  
  /**
   * Returns the parent(s) of the given term.
   * 
   * @param term
   * @param relat
   */
  abstract public List<Term> getDirectAncestors(Term term, String relationshipType);
  
  /**
   * Returns the children of the given term.
   * 
   * @param term
   * @param relat
   */
  abstract public List<Term> getDirectDescendants(Term term, String relationshipType);
}
