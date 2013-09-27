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

import com.runwaysdk.business.Business;

abstract public class Term extends Business
{
  private static final long serialVersionUID = -2009350279143212154L;
  
//  public boolean isDirectAncestorOf(Term child);
//  public boolean isRecursiveAncestorOf(Term child);
//
//  public boolean isDirectDescendentOf(Term parent);
//  public boolean isRecursiveDescendentOf(Term parent);
  
  protected OntologyStrategyIF strategy;
  
  public Term() {
    this.strategy = getStrategy();
    strategy.initialize();
  }
  
  abstract public OntologyStrategyIF getStrategy();
  
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
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getDirectAncestors(String relationshipType) {
    return strategy.getDirectAncestors(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescendants(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getDirectDescendants(String relationshipType) {
    return strategy.getDirectDescendants(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getAllAncestors(String relationshipType) {
    return strategy.getAllAncestors(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getAllDescendants(String relationshipType) {
    return strategy.getAllDescendants(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isLeaf(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public boolean isLeaf(String relationshipType) {
    return strategy.isLeaf(this, relationshipType);
  }
  
  /**
   * Performs a deep copy of this term to the specified parent.
   * 
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#copyTerm(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public void copyTerm(Term parent, String relationshipType) {
    strategy.copyTerm(parent, this, relationshipType);
  }
}
