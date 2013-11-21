/**
 * 
 */
package com.runwaysdk.business.ontology;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.generation.facade.json.ToJSONIF;
import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.constants.JSON;

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
public class TermAndRel implements ToJSONIF
{
  private BusinessDTO term;
  private String relationshipType;
  private String relationshipId;
  
  public TermAndRel(BusinessDTO term, String relationshipType, String relationshipId) {
    this.term = term;
    this.relationshipType = relationshipType;
    this.relationshipId = relationshipId;
  }
  
  /**
   * JSON Serialization toJSON
   */
  public JSONObject toJSON() throws JSONException {
    JSONObject json = new JSONObject();
    json.put(JSON.DTO_TYPE.getLabel(), "com.runwaysdk.business.ontology.TermAndRel");
    json.put("term", JSONFacade.getJSONFromComponentDTO(this.term));
    json.put("relType", this.relationshipType);
    json.put("relId", this.relationshipId);
    return json;
  }
  
  /**
   * @return the term
   */
  public BusinessDTO getTerm()
  {
    return term;
  }
  
  /**
   * @param term the term to set
   */
  public void setTerm(BusinessDTO term)
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
