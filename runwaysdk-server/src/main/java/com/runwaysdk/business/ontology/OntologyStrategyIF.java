/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

public interface OntologyStrategyIF
{
  /**
   * @return
   */
  public boolean isInitialized();

  /**
   * Initialize the strategy and prepare for usage.
   */
  public void initialize(String relationshipType);
  
  /**
   * Initialize the strategy and prepare for usage.
   */
  public void initialize(String relationshipType, InitializationStrategyIF strategy);

  /**
   * Prepare for deletion.
   */
  public void shutdown();

  /**
   * Prepare for deletion.
   */
  public void shutdown(String relationshipType);
  
  /**
   * Reset the strategy back to the initialized state, clearing any and all
   * cached data. The default behavior is to call shutdown then initialize.
   */
  public void reinitialize(String relationshipType);
  
  /**
   * Reset the strategy back to the initialized state, clearing any and all
   * cached data. The default behavior is to call shutdown then initialize.
   */
  public void reinitialize(String relationshipType, InitializationStrategyIF strategy);

  /**
   * Copies the child Term and its subtree beneath the given parent Term. The
   * child Term must already exist for this to work.
   * 
   * @param parent
   * @param child
   */
  public void addLink(Term parent, Term child, String relationshipType);
  
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
  public OIterator<Term> getAllAncestors(Term term, String relationshipType);

  /**
   * Returns all children of the given term, including children of children.
   * 
   * @param term
   * @param relationshipType
   */
  public OIterator<Term> getAllDescendants(Term term, String relationshipType);
  
  /**
   * Returns the parent(s) of the given term.
   * 
   * @param term
   * @param relat
   */
  public OIterator<Term> getDirectAncestors(Term term, String relationshipType);
  
  /**
   * Returns the children of the given term. Page number starts at page 1. A page number of 0 means to return all records.
   * 
   */
  public OIterator<Term> getDirectDescendants(Term term, String relationshipType);
  
  /**
   * Adds a new term
   * 
   * @param term
   * @param relationshipType
   */
  public void add(Term term, String relationshipType);
  
  /**
   * Removes only this term from the strategy, but does not delete it. Also does not touch children.
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
  
  /**
   * Used to provide additional (typically transient) configuration parameters to the strategy, immediately after it has been created (but not initialized).
   * 
   * @param termClass The CLASS of the term that this strategy is associated with.
   */
  public void configure(String termClass);
  
  public DeleteStrategyProviderIF getDeleteStrategyProvider(Term deleteRoot, String relationshipType);
  
  public void addSynonym(Term term, OntologyEntryIF synonym);
  
  public void updateSynonym(OntologyEntryIF synonym);
  
  public void removeSynonym(OntologyEntryIF  synonym);
  
  public void updateLabel(Term term, String label);
}
