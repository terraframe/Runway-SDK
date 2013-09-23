package com.runwaysdk.business.ontology;

import java.util.List;

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
public interface OntologyOptimizationStrategyIF
{
  /**
   * Rebuilds the entire allpaths table.
   */
  public void initialize();
  
  public void shutdown();
  
  /**
   * Returns all direct ancestor terms.
   * 
   * @return
   */
  public List<Term> getDirectAncestors(Term child);
  
  /**
   * Returns all direct descendant terms.
   * 
   * @return
   */
  public List<Term> getDirectDescendants(Term parent);
  
  /**
   * Returns all ancestor terms, recursively.
   * 
   * @return
   */
  public List<Term> getAllAncestors(Term child);
  
  /**
   * Returns all descendant terms, recursively.
   * @return
   */
  public List<Term> getAllDescendants(Term parent);

  /**
   * Copies the child Term and its subtree beneath the given parent Term. The
   * child Term must already exist for this to work.
   * 
   * @param parent
   * @param child
   */
  public void copyTerm(Term parent, Term child);
  
  /**
   * Deletes the leaf node from the all paths.
   * This can only be called if isSingleLeaf()
   * returns true; otherwise, the data could be
   * corrupt.
   * 
   * @param term
   */
  public void deleteLeaf(Term term);

  /**
   * Checks if the given Term is a leaf, meaning
   * it has no children and one or less parents (orphans
   * are allowed for cleanup).
   * 
   * @param term
   * @return
   */
  public boolean isLeaf(Term term);
  
}