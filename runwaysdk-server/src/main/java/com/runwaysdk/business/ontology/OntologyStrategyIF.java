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

import java.util.List;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.query.OrderBy.SortOrder;

public interface OntologyStrategyIF
{
  /**
   * @param relationshipType
   * @return
   */
  public boolean isInitialized(String relationshipType);

  /**
   * Initialize the strategy and prepare for usage.
   */
  public void initialize(String relationshipType);

  /**
   * Prepare for deletion.
   */
  public void shutdown();

  /**
   * Reset the strategy back to the initialized state, clearing any and all
   * cached data. The default behavior is to call shutdown then initialize.
   */
  public void reinitialize(String relationshipType);

  /**
   * Copies the child Term and its subtree beneath the given parent Term. The
   * child Term must already exist for this to work.
   * 
   * @param parent
   * @param child
   */
  public Relationship addLink(Term parent, Term child, String relationshipType);

  /**
   * Returns true if the term is a leaf node. Leaf nodes have no children.
   * 
   * @param term
   * @param relat
   */
  public boolean isLeaf(Term term, String relationshipType);

  /**
   * Returns all parents of the given term, including parents of parents.
   * 
   * @param term
   * @param relat
   */
  public List<Term> getAllAncestors(Term term, String relationshipType);

  /**
   * Returns all children of the given term, including children of children.
   * 
   * @param term
   * @param relationshipType
   */
  public List<Term> getAllDescendants(Term term, String relationshipType);
  
  /**
   * Returns the parent(s) of the given term.
   * 
   * @param term
   * @param relat
   */
  public List<Term> getDirectAncestors(Term term, String relationshipType);

  /**
   * Returns the children of the given term. Page number starts at page 1. A page number of 0 means to return all records.
   * 
   */
  public List<Term> getDirectDescendants(Term term, String relationshipType);
  
  /**
   * Adds a new term
   * 
   * @param term
   * @param relationshipType
   */
  public void add(Term term, String relationshipType);

  /**
   * Completely removes a node from the tree.
   * 
   * @param term
   * @param relationshipType
   */
  public void removeTerm(Term term, String relationshipType);

  /**
   * Removes a relationship between two nodes.
   * 
   * @param term
   * @param relationshipType
   */
  public void removeLink(Term parent, Term term, String relationshipType);
}
