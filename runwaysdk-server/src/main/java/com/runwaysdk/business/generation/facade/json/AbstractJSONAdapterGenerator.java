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
package com.runwaysdk.business.generation.facade.json;

import java.util.List;

import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.generation.AbstractServerGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.facade.FacadeBaseGenerator;
import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public abstract class AbstractJSONAdapterGenerator extends AbstractServerGenerator
{

  public AbstractJSONAdapterGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName)
  {
    super(mdTypeDAOIF, fileName);
  }
  
  protected void writeParameterConversion(MdParameterDAOIF mdParameter)
  {
    Type parameterType = mdParameter.getParameterType();
    String name = mdParameter.getParameterName();    
    String facadeName = JSONFacade.class.getName();
    String facadeMethod = "getObjectFromJSON";
    String classType = parameterType.getGenericDTOJavaClass();
    String genericType = parameterType.getGenericDTOType();

    String convertString = genericType + " _" + name + " = (" + genericType + ") " + facadeName + "." +
                           facadeMethod + "(sessionId, \"" + classType + "\", " + name + ");";    

    getWriter().writeLine(convertString);
  } 
  
  protected void writeMethodBody(Type returnType, MdMethodDAOIF mdMethod, List<MdParameterDAOIF> list, String methodName)
  {
    String facadeName = FacadeBaseGenerator.getGeneratedName((MdFacadeDAOIF) this.getMdTypeDAOIF());
    String callString = "sessionId" + GenerationUtil.buildCallParameter(list, "_", false);
    String methodCall = facadeName + "." + methodName + "Base(" + callString + ");";
    
    getWriter().openBracket();

    getWriter().writeLine(JSONReturnObject.class.getName()+" returnJSON = new "+JSONReturnObject.class.getName()+"();");
    
    // Declare the return object
    if(!returnType.isVoid())
    {
      getWriter().writeLine("");
      getWriter().writeLine(returnType.getGenericDTOType() + " _output;");
    }
    
    getWriter().writeLine("try");
    getWriter().openBracket();
    
    for(MdParameterDAOIF mdParameter : list)
    {
      writeParameterConversion(mdParameter);  
    }
    
    if(list.size() > 0)
    {
      getWriter().writeLine("");
    }
   
    if(!returnType.isVoid())
    {
      getWriter().writeLine("_output = " + methodCall);
    }
    else
    {
      getWriter().writeLine(methodCall);        
    }
    
    getWriter().closeBracket();
    
    // START - MessageExceptionDTO block
    getWriter().writeLine("catch("+MessageExceptionDTO.class.getName()+" me)");
    getWriter().openBracket();
    getWriter().writeLine("returnJSON.extractMessages(me);");
    
    if(!returnType.isVoid())
    {
      getWriter().writeLine("_output = ("+returnType.getGenericDTOType()+") me.getReturnObject();");
    }
    
    getWriter().closeBracket();
    // END - MessageExceptionDTO block
    
    getWriter().writeLine("");
    
    if(!returnType.isVoid())
    {
      getWriter().writeLine(Object.class.getName()+" _jsonOutput = " + JSONFacade.class.getName() + ".getJSONFromObject(sessionId, _output);");
      getWriter().writeLine("returnJSON.setReturnValue(_jsonOutput);");
    }
    
    getWriter().writeLine("return returnJSON.toString();");
    getWriter().closeBracket();
  }

}
