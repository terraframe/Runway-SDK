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
package com.runwaysdk.business.generation.json;

import org.apache.commons.lang.StringEscapeUtils;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.facade.FacadeUtil;

public class UtilJSGenerator extends SessionJSGenerator
{

  public UtilJSGenerator(String sessionId, MdUtilDAOIF mdUtil)
  {
    super(sessionId, mdUtil);
  }
  
  @Override
  protected MdUtilDAOIF getMdTypeIF()
  {
    return (MdUtilDAOIF) super.getMdTypeIF();
  }

  
  @Override
  protected Declaration getInitialize()
  {
    Declaration method = this.newDeclaration();   
    
    MdUtilDAOIF mdUtil = getMdTypeIF();
    
    method.writeln("initialize : function(obj)");
    method.openBracketLn();

    if(!mdUtil.isAbstract())
    {
      String definesType = mdUtil.definesType();
      
      Util util = (Util) BusinessFacade.newSessionComponent(definesType);
      UtilDTO newInstance = (UtilDTO) FacadeUtil.populateComponentDTOIF(getSessionId(), util, true);
      
      // get the JSON string, but escape all single quotes because the new instance is held
      // in a string wrapped by single quotes (double quotes are used internally within the json)
      String jsonNewInstance = StringEscapeUtils.escapeJavaScript(JSONFacade.getJSONFromComponentDTO(newInstance).toString());
      
      method.writeln("if(obj == null)");
      method.openBracketLn();
      method.writeln("var json = '"+jsonNewInstance+"';");
      method.writeln("obj = "+JSON.RUNWAY_DTO_GET_OBJECT.getLabel()+"(json);");
      method.closeBracketLn();
    }
    
    method.writeln("this.$initialize(obj);");
    method.closeBracket();
    
    return method;
  }

  @Override
  protected String getParent()
  {
    MdUtilDAOIF mdUtil = getMdTypeIF();
    
    MdUtilDAOIF superEntity = mdUtil.getSuperClass();
    if(superEntity == null)
    {
      return UtilDTO.CLASS;
    }
    else
    {
      return superEntity.definesType();
    }
  }

}
