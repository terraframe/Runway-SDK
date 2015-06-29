/**
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
 */
package com.runwaysdk.transport.conversion.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.constants.JSON;

public abstract class ClassQueryDTOToJSON extends ComponentQueryDTOToJSON
{

  protected ClassQueryDTOToJSON(ClassQueryDTO queryDTO, boolean typeSafe)
  {
    super(queryDTO, typeSafe);
  }
  
  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    // classes
    addClasses();
  }
  
  @Override
  protected ClassQueryDTO getComponentQueryDTO()
  {
    return (ClassQueryDTO) super.getComponentQueryDTO();
  }
  
  /**
   * Adds the classes to the json.
   * 
   * @throws JSONException
   */
  private void addClasses() throws JSONException
  {
    ClassQueryDTO queryDTO = getComponentQueryDTO();
    
    JSONArray classes = new JSONArray();

    for(String type : queryDTO.getClassTypes())
    {
      JSONObject classObj = new JSONObject();
      classObj.put(JSON.CLASS_QUERY_DTO_CLASS_TYPE.getLabel(), type);
      
      // add all super classes
      JSONArray superClasses = new JSONArray();
      for(String superType : queryDTO.getSuperClassTypes(type))
      {
        superClasses.put(superType);
      }
      classObj.put(JSON.CLASS_QUERY_DTO_CLASS_SUPERCLASSES.getLabel(), superClasses);
      
      classes.put(classObj);
    }
    
    JSONObject json = getJSON();
    json.put(JSON.CLASS_QUERY_DTO_CLASSES.getLabel(), classes);
  }
  

}
