package com.runwaysdk.business.ontology;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.transport.conversion.json.ComponentDTOIFToJSON;

public class TermAndRelDTO implements ToJSONIF
{
  public static final String CLASS = "com.runwaysdk.business.ontology.TermAndRel"; 
  
  public static final String concatChar = ";"; // The code breaks if this char is contained within the relationship id or the business id.
  
  private TermDTO term;
  private String relationshipType;
  private String relationshipId;
  private Boolean canTermCreateChildren;
  
  public TermAndRelDTO(TermDTO term, String relationshipType, String relationshipId) {
    this.term = term;
    this.relationshipType = relationshipType;
    this.relationshipId = relationshipId;
  }
  
  public static TermAndRelDTO fromString(ClientRequestIF clientRequest, String str) {
    String[] attrs = str.split(concatChar);
    
    TermDTO t = (TermDTO) clientRequest.get(attrs[0]);
    
    return new TermAndRelDTO(t, attrs[1], attrs[2]);
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
      json.put(JSON.DTO_TYPE.getLabel(), "com.runwaysdk.business.ontology.TermAndRel");
      
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
}
