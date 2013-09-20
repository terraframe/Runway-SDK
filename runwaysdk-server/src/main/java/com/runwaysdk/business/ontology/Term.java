/**
 * 
 */
package com.runwaysdk.business.ontology;

import java.util.List;

import com.runwaysdk.business.Business;

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
abstract public class Term extends Business
{
  private static final long serialVersionUID = -2009350279143212154L;
  
//  public boolean isDirectAncestorOf(Term child);
//  public boolean isRecursiveAncestorOf(Term child);
//
//  public boolean isDirectDescendentOf(Term parent);
//  public boolean isRecursiveDescendentOf(Term parent);
  
  protected OntologyOptimizationStrategyIF strategy;
  
  public Term() {
    this.strategy = getStrategy();
  }
  
  abstract public OntologyOptimizationStrategyIF getStrategy();
  
  /**
   * Returns the unique id of this term.
   * @return
   */
  public String getId() {
    return "";
  }
  
  /**
   * Copys all relevant data to the given term, making the argument a clone of this.
   * 
   * @param term
   */
  public void copyTo(Term term) {
    
  }
  
  /**
   * Returns all direct ancestor terms.
   * 
   * @return
   */
  public List<Term> getDirectAncestors() {
    return strategy.getDirectAncestors(this);
  }
  
  /**
   * Returns all direct descendant terms.
   * 
   * @return
   */
  public List<Term> getDirectDescendants() {
    return strategy.getDirectDescendants(this);
  }
  
  /**
   * Returns all ancestor terms, recursively.
   * 
   * @return
   */
  public List<Term> getAllAncestors() {
    return strategy.getAllAncestors(this);
  }
  
  /**
   * Returns all descendant terms, recursively.
   * @return
   */
  public List<Term> getAllDescendants() {
    return strategy.getAllDescendants(this);
  }
  
}
