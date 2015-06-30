/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.business.View;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.facade.FacadeUtil;

public class ViewJSGenerator extends SessionJSGenerator
{

  public ViewJSGenerator(String sessionId, MdViewDAOIF mdViewIF)
  {
    super(sessionId, mdViewIF);
  }
  
  @Override
  protected MdViewDAOIF getMdTypeIF()
  {
    return (MdViewDAOIF) super.getMdTypeIF();
  }

  @Override
  protected Declaration getInitialize()
  {
    Declaration method = this.newDeclaration();
    
    MdViewDAOIF mdView = getMdTypeIF();
    
    method.writeln("initialize : function(obj)");
    method.openBracketLn();

    if(!mdView.isAbstract())
    {
      String definesType = mdView.definesType();
      
      View util = (View) BusinessFacade.newSessionComponent(definesType);
      ViewDTO newInstance = (ViewDTO) FacadeUtil.populateComponentDTOIF(getSessionId(), util, true);
      
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
    MdViewDAOIF mdView = getMdTypeIF();
    
    MdViewDAOIF superEntity = mdView.getSuperClass();
    if(superEntity == null)
    {
      return ViewDTO.CLASS;
    }
    else
    {
      return superEntity.definesType();
    }
  }

}
