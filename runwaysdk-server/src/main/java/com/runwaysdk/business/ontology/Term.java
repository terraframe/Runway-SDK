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
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.system.metadata.ontology.OntologyStrategy;
import com.runwaysdk.system.metadata.ontology.StrategyState;

abstract public class Term extends Business
{
  private static final long serialVersionUID = -2009350279143212154L;
  
  protected static OntologyStrategyIF strategy;
  
  public OntologyStrategyIF getStrategy()
  {
    if(strategy == null) {
      if (getStrategyCLASS() == DefaultStrategy.CLASS) {
        strategy = new DefaultStrategy();
      }
      else {
        // check the database for existing
        String stratId = this.getMdClass().getValue(MdTermInfo.STRATEGY);
        
        if (stratId == null) {
          BusinessDAO stratDAO = BusinessDAO.newInstance(getStrategyCLASS());
          stratDAO.addItem(OntologyStrategy.STRATEGYSTATE, StrategyState.UNINITIALIZED.getId());
          stratDAO.apply();
          
          this.getMdClass().getBusinessDAO().setValue(MdTermInfo.STRATEGY, stratDAO.getId());
          
          strategy = (OntologyStrategyIF) stratDAO;
        }
        else {
          strategy = (OntologyStrategyIF) MdTerm.get(stratId);
        }
        
        strategy.initialize();
      }
    }

    return strategy;
  }
  
  public Term() {
    
  }
  
  abstract public String getStrategyCLASS();
  
  /**
   * Returns the unique id of this term.
   * @return
   */
  public String getId() {
    return "";
  }
  
//MdTermDAO mdTerm = (MdTermDAO) MdTermDAO.getMdBusinessDAO(this.getClass().getName()).getBusinessDAO();
//mdTerm.setValue(MdTermInfo.STRATEGY, state.getId());
//mdTerm.apply();
  
  /**
   * Copys all relevant data to the given term, making the argument a clone of this.
   * 
   * @param term
   */
  public void copyTo(Term term) {
    
  }
  
//public boolean isDirectAncestorOf(Term child);
//public boolean isRecursiveAncestorOf(Term child);
//
//public boolean isDirectDescendentOf(Term parent);
//public boolean isRecursiveDescendentOf(Term parent);
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getDirectAncestors(String relationshipType) {
    return getStrategy().getDirectAncestors(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescendants(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getDirectDescendants(String relationshipType) {
    return getStrategy().getDirectDescendants(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getAllAncestors(String relationshipType) {
    return getStrategy().getAllAncestors(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getAllDescendants(String relationshipType) {
    return getStrategy().getAllDescendants(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isLeaf(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public boolean isLeaf(String relationshipType) {
    return getStrategy().isLeaf(this, relationshipType);
  }
  
  /**
   * Performs a deep copy of this term to the specified parent.
   * 
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#copyTerm(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.TermRelationship)
   */
  public void copyTerm(Term parent, String relationshipType) {
    getStrategy().copyTerm(parent, this, relationshipType);
  }
}
