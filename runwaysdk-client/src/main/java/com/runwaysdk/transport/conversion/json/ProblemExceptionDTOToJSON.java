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
package com.runwaysdk.transport.conversion.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.constants.AttributeProblemDTOInfo;
import com.runwaysdk.constants.EmptyValueProblemDTOInfo;
import com.runwaysdk.constants.ImmutableAttributeProblemDTOInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.ProblemExceptionDTOInfo;
import com.runwaysdk.constants.RunwayProblemDTOInfo;
import com.runwaysdk.constants.SystemAttributeProblemDTOInfo;
import com.runwaysdk.controller.ParseProblemDTO;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblemDTO;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblemDTO;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblemDTO;

public class ProblemExceptionDTOToJSON
{

  
  private ProblemExceptionDTO problemExceptionDTO;
  
  public ProblemExceptionDTOToJSON(ProblemExceptionDTO e)
  {
    this.problemExceptionDTO = e;
  }
  
  public JSONObject populate() throws JSONException
  {
    JSONObject json = new JSONObject();
    
    // exception type
    json.put(JSON.DTO_TYPE.getLabel(), ProblemExceptionDTOInfo.CLASS);
    
    // localized message
    json.put(JSON.EXCEPTION_LOCALIZED_MESSAGE.getLabel(), problemExceptionDTO.getLocalizedMessage());
    
    // build the problem list
    JSONArray problemList = new JSONArray();
    
    for(ProblemDTOIF problemDTOIF : problemExceptionDTO.getProblems())
    {
      JSONObject problem;
      if(problemDTOIF instanceof ProblemDTO)
      {
        // metadata generated problems
        ProblemDTOToJSON converter = new ProblemDTOToJSON((ProblemDTO) problemDTOIF);
        problem = converter.populate();
      }
      else if(problemDTOIF instanceof AttributeProblemDTO)
      {
        // hard-coded attribute problems
        AttributeProblemDTO attributeProblemDTO = (AttributeProblemDTO) problemDTOIF;

        problem = new JSONObject();
        
        String dtoType = getAttributeProblemDTOType(attributeProblemDTO);
        String attributeDisplayLabel = attributeProblemDTO.getAttributeDisplayLabel();
        String attributeName = attributeProblemDTO.getAttributeName();
        String attributeId = attributeProblemDTO.getAttributeId();
        String componentId = attributeProblemDTO.getComponentId();
        String definingType = attributeProblemDTO.getDefiningType();
        String definingTypeDisplayLabel = attributeProblemDTO.getDefiningTypeDisplayLabel();
        
        problem.put(JSON.DTO_TYPE.getLabel(), dtoType);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_DISPLAY_LABEL.getLabel(), attributeDisplayLabel);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_NAME.getLabel(), attributeName);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_ID.getLabel(), attributeId);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_COMPONENT_ID.getLabel(), componentId);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_DEFINING_TYPE.getLabel(), definingType);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_DEFINING_TYPE_DISPLAY_LABEL.getLabel(), definingTypeDisplayLabel);
      }
      else if(problemDTOIF instanceof ParseProblemDTO)
      {
        // hard-coded attribute problems
        ParseProblemDTO parseProblemDTO = (ParseProblemDTO) problemDTOIF;

        problem = new JSONObject();
        
        problem.put(JSON.DTO_TYPE.getLabel(), AttributeProblemDTOInfo.CLASS);
        String attributeDisplayLabel = parseProblemDTO.getAttributeDisplayLabel();
        String attributeName = parseProblemDTO.getAttributeName();
        String attributeId = parseProblemDTO.getAttributeId();
        String componentId = parseProblemDTO.getComponentId();
        String definingType = parseProblemDTO.getDefiningType();
        String definingTypeDisplayLabel = parseProblemDTO.getDefiningTypeDisplayLabel();
        
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_DISPLAY_LABEL.getLabel(), attributeDisplayLabel);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_NAME.getLabel(), attributeName);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_ID.getLabel(), attributeId);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_COMPONENT_ID.getLabel(), componentId);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_DEFINING_TYPE.getLabel(), definingType);
        problem.put(JSON.ATTRIBUTE_PROBLEM_DTO_DEFINING_TYPE_DISPLAY_LABEL.getLabel(), definingTypeDisplayLabel);
      }
      else
      {
        // hard-coded runway problems
        problem = new JSONObject();
        problem.put(JSON.DTO_TYPE.getLabel(), RunwayProblemDTOInfo.CLASS);
      }
      
      // dev message
      problem.put(JSON.EXCEPTION_DEVELOPER_MESSAGE.getLabel(), problemDTOIF.getDeveloperMessage());
      
      problem.put(JSON.EXCEPTION_LOCALIZED_MESSAGE.getLabel(), problemDTOIF.getMessage());
      
      problemList.put(problem);
    }
    
    json.put(JSON.PROBLEM_EXCEPTION_DTO_PROBLEM_LIST.getLabel(), problemList);
    

    return json;
  }
  
  /**
   * Returns the DTO type for a given AttributeProblemDTO.
   * 
   * @param attributeProblemDTO
   * @return
   */
  private String getAttributeProblemDTOType(AttributeProblemDTO attributeProblemDTO)
  {
    // FIXME make this list dynamic
    if(attributeProblemDTO instanceof EmptyValueProblemDTO)
    {
      return EmptyValueProblemDTOInfo.CLASS;
    }
    else if(attributeProblemDTO instanceof ImmutableAttributeProblemDTO)
    {
      return ImmutableAttributeProblemDTOInfo.CLASS;
    }
    else if(attributeProblemDTO instanceof SystemAttributeProblemDTO)
    {
      return SystemAttributeProblemDTOInfo.CLASS;
    }
    else
    {
      return AttributeProblemDTOInfo.CLASS;
    }
  }
}
