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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.JSON;
import com.runwaysdk.transport.conversion.json.ComponentDTOIFToJSON;

public class TermAndRelDTO implements ToJSONIF
{
  public static final String CLASS = "com.runwaysdk.business.ontology.TermAndRel"; 
  
  public static final String concatChar = ";"; // The code breaks if this char is contained within the relationship oid or the business oid.
  
  private TermDTO term;
  private String relationshipType;
  private String relationshipId;
  private Boolean canTermCreateChildren;
  
  public TermAndRelDTO(TermDTO term, String relationshipType, String relationshipId) {
    this.term = term;
    this.relationshipType = relationshipType;
    this.relationshipId = relationshipId;
  }
  
  public void setCanTermCreateChildren(Boolean b) {
    canTermCreateChildren = b;
  }
  
  /**
   * JSON Serialization toJSON
   */
  public JSONObject toJSON() {
    JSONObject json;
    
    try
    {
      json = new JSONObject();
      json.put(JSON.DTO_TYPE.getLabel(), CLASS);
      
      JSONObject termJson;
      
      ComponentDTOIFToJSON componentDTOToJSON = ComponentDTOIFToJSON.getConverter(this.term);
      termJson = componentDTOToJSON.populate();
      
      // We need to know whether or not we can create GeoEntities under this term.
      if (canTermCreateChildren != null) {
        termJson.put("canCreateChildren", canTermCreateChildren);
      }
      
      json.put("term", termJson);
      
      json.put("relType", this.relationshipType);
      json.put("relId", this.relationshipId);
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
    
    return json;
  }
  
  /**
   * @return the term
   */
  public TermDTO getTerm()
  {
    return term;
  }
  
  /**
   * @param term the term to set
   */
  public void setTerm(TermDTO term)
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
  
  @Override
  public String toString() {
    return "TermAndRelDTO(TermDTO = [" + this.term.toString() + "])";
  }
}
