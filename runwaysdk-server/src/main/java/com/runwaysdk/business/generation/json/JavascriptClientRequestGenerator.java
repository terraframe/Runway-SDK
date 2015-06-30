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

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;

public class JavascriptClientRequestGenerator extends TypeJSGenerator
{

  /**
   * Constructor that takes in the MdFacadeIF object to generate
   * a javascript clientRequest.
   *
   * @param mdFacadeIF
   */
  public JavascriptClientRequestGenerator(String sessionId, MdFacadeDAOIF mdFacadeIF)
  {
    super(sessionId, mdFacadeIF);
  }

  @Override
  protected MdFacadeDAOIF getMdTypeIF()
  {
    return (MdFacadeDAOIF) super.getMdTypeIF();
  }

  @Override
  protected String getParent()
  {
    return null;
  }

  @Override
  protected List<Declaration> getConstants()
  {
    return null;
  }

  @Override
  protected List<Declaration> getInstanceMethods()
  {
    return null;
  }

  @Override
  protected List<Declaration> getStaticMethods()
  {
    MdFacadeDAOIF mdFacadeIF = getMdTypeIF();

    List<Declaration> methods = new LinkedList<Declaration>();

    for(MdMethodDAOIF mdMethod : mdFacadeIF.getMdMethodsOrdered())
    {
      Declaration method = this.newDeclaration();

      String methodName = mdMethod.getValue(MdMethodInfo.NAME);
      List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();

      String parameters = GenerationUtil.buildJSONParameters(list);

      method.writeln(methodName + " : function(" + parameters + ")");
      method.openBracketLn();

      method.write("var params = ");
      method.openBracketLn();
      method.write("'method' : '"+methodName+"'");
      if(list.size() > 0)
      {
        method.writeln(",");
      }
      else
        method.writeln("");

      int count = 0;
      for(MdParameterDAOIF parameter : list)
      {
        String mapping = "'"+parameter.getParameterName()+"' : "+JSON.RUNWAY_DTO_GET_JSON.getLabel()+"("+parameter.getParameterName()+")";
        if(count != (list.size() -1))
        {
          mapping += ",";
        }
        method.writeln(mapping);
        count++;
      }

      method.closeBracketLn();

      // add the clientRequest as a parameter
      String generic = mdFacadeIF.getPackage() + "." + MdFacadeInfo.JSON_GENERIC_ADAPTER_PREPEND
        + getMdTypeIF().getTypeName() + MdFacadeInfo.JSON_GENERIC_ADAPTER_SUFFIX;

      method.writeln("params['"+MdFacadeInfo.JSON_ADAPTER_NAMESPACE+"'] = '"+generic+"';");
      method.writeln(JSON.RUNWAY_FACADE.getLabel()+"._methodWrapper(clientRequest, params);");
      method.closeBracket();

      methods.add(method);
    }

    return methods;
  }

  @Override
  protected String getClassName()
  {
    return getMdTypeIF().definesType();
  }

  @Override
  protected boolean isAbstract()
  {
    return false;
  }
}
