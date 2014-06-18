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



public class TermAndRel
{
  public final static String CLASS = "com.runwaysdk.business.ontology.TermAndRel";
  private Term term;
  private String relationshipType;
  private String relationshipId;
  
  public TermAndRel(Term term, String relationshipType, String relationshipId) {
    this.term = term;
    this.relationshipType = relationshipType;
    this.relationshipId = relationshipId;
  }
  
  /**
   * @return the term
   */
  public Term getTerm()
  {
    return term;
  }
  
  /**
   * @param term the term to set
   */
  public void setTerm(Term term)
  {
    this.term = term;
  }
  
  /**
   * @return the relationshipType
   */
  public String getRelationshipType()
  {
    return relationshipType;
  }
  
  /**
   * @param relationshipType the relationshipType to set
   */
  public void setRelationshipType(String relationshipType)
  {
    this.relationshipType = relationshipType;
  }
  
  /**
   * @return the relationshipId
   */
  public String getRelationshipId()
  {
    return relationshipId;
  }

  /**
   * @param relationshipId the relationshipId to set
   */
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
}
