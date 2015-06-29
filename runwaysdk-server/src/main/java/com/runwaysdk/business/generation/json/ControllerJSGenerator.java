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
package com.runwaysdk.business.generation.json;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.FormObjectInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;

public class ControllerJSGenerator extends TypeJSGenerator
{
  private List<MdActionDAOIF> actions;

  // FIXME generate as singleton and have static methods delegate to instance
  // methods via getInstance()
  public ControllerJSGenerator(String sessionId, MdControllerDAOIF mdTypeIF)
  {
    super(sessionId, mdTypeIF);

    actions = getMdTypeIF().getMdActionDAOsOrdered();
  }

  @Override
  protected MdControllerDAOIF getMdTypeIF()
  {
    return (MdControllerDAOIF) super.getMdTypeIF();
  }

  @Override
  protected String getClassName()
  {
    return getMdTypeIF().definesType();
  }

  @Override
  protected List<Declaration> getConstants()
  {
    return null;
  }

  protected Declaration getInitialize()
  {
    return super.getInitialize();
  }

  @Override
  protected List<Declaration> getStaticMethods()
  {
    List<Declaration> actionDecs = new LinkedList<Declaration>();

    MdControllerDAOIF controller = getMdTypeIF();

    Declaration listeners = this.newDeclaration();

    String listStr = "";
    for (int i = 0; i < actions.size(); i++)
    {
      listStr += "'" + actions.get(i).getName() + "':null";
      if (i != actions.size() - 1)
      {
        listStr += ",";
      }
    }
    listeners.write("_listeners : {" + listStr + "}");

    actionDecs.add(listeners);

    // global setter so actions can share same listener
    Declaration actionDec = this.newDeclaration();
    actionDec.writeln("setListener : function(listener)");
    actionDec.openBracketLn();
    actionDec.writeln("var listeners = " + getType() + "._listeners;");
    actionDec.writeln("for(var action in listeners)");
    actionDec.openBracketLn();
    actionDec.writeln("listeners[action] = listener;");
    actionDec.closeBracketLn();
    actionDec.closeBracket();

    actionDecs.add(actionDec);

    for (MdActionDAOIF action : actions)
    {
      String actionName = action.getName();
      String upperActionName = CommonGenerationUtil.upperFirstCharacter(actionName);
      String qualifiedAction = controller.definesType() + "." + actionName;

      // adds a listener
      actionDec = this.newDeclaration();
      actionDec.writeln(CommonGenerationUtil.SET + upperActionName + "Listener : function(listener)");
      actionDec.openBracketLn();
      actionDec.writeln(getType() + "._listeners['" + actionName + "'] = listener;");
      actionDec.closeBracket();

      actionDecs.add(actionDec);

      // removes a listener
      actionDec = this.newDeclaration();
      actionDec.writeln("remove" + upperActionName + "Listener : function()");
      actionDec.openBracketLn();
      actionDec.writeln(getType() + "._listeners['" + actionName + "'] = null;");
      actionDec.closeBracket();

      actionDecs.add(actionDec);

      // notifies a listener
      actionDec = this.newDeclaration();
      actionDec.writeln("_notify" + upperActionName + "Listener : function(params, action, actionId)");
      actionDec.openBracketLn();
      actionDec.writeln("if(" + JSON.RUNWAY_IS_FUNCTION.getLabel() + "(" + getType() + "._listeners['" + actionName + "']))");
      actionDec.openBracketLn();
      actionDec.writeln("var clientRequest = " + getType() + "._listeners['" + actionName + "'](params, action, actionId);");
      actionDec.closeBracketLn();
      actionDec.writeln("if(clientRequest != null)");
      actionDec.openBracketLn();
      actionDec.writeln("this['" + actionName + "Map'](clientRequest, params);");
      actionDec.closeBracketLn();
      actionDec.closeBracket();

      actionDecs.add(actionDec);

      // calls the action with complex objects
      actionDec = this.newDeclaration();
      List<MdParameterDAOIF> params = action.getMdParameterDAOs();
      String paramsStr = GenerationUtil.buildJSONParameters(params);
      actionDec.writeln(actionName + " : function(" + paramsStr + ")");
      actionDec.openBracketLn();

      String argString = "";
      for (int i = 0; i < params.size(); i++)
      {
        String paramName = params.get(i).getParameterName();
        argString += "'" + paramName + "':" + paramName;
        if (i != params.size() - 1)
        {
          argString += ",";
        }
      }
      actionDec.writeln("var params = {" + argString + "}");

      String actionEndpoint;
      // if(params.size() == 1 &&
      // params.get(0).getParameterType().getType().equals(FormObjectInfo.CLASS))
      if (params.size() > 0 && params.get(0).getParameterType().getType().equals(FormObjectInfo.CLASS))
      {
        actionEndpoint = qualifiedAction + MdActionInfo.FORM_OBJECT_ACTION_SUFFIX;
      }
      else
      {
        actionEndpoint = qualifiedAction + MdActionInfo.AJAX_ACTION_SUFFIX;
      }

      actionDec.writeln(JSON.RUNWAY_FACADE.getLabel() + "._controllerWrapper('" + actionEndpoint + "', clientRequest, params);");
      actionDec.closeBracket();

      actionDecs.add(actionDec);

      // calls the action with a map
      actionDec = this.newDeclaration();
      actionDec.writeln(actionName + "Map : function(clientRequest, params)");
      actionDec.openBracketLn();
      actionDec.writeln("var paramString = " + JSON.RUNWAY_CONVERT_MAP_TO_QUERY_STRING.getLabel() + "(params);");
      actionDec.writeln(JSON.RUNWAY_FACADE.getLabel() + "._controllerWrapper('" + actionEndpoint + "', clientRequest, paramString);");
      actionDec.closeBracket();

      actionDecs.add(actionDec);
    }

    return actionDecs;
  }

  @Override
  protected String getParent()
  {
    return null;
  }

  @Override
  protected List<Declaration> getInstanceMethods()
  {
    return null;
  }

  @Override
  protected boolean isAbstract()
  {
    return false;
  }

}
