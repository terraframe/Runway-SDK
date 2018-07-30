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
package com.runwaysdk.web.json;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runwaysdk.ClientRequest;
import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.FacadeMethods;
import com.runwaysdk.constants.JSONClientRequestConstants;
import com.runwaysdk.transport.conversion.json.JSONExceptionDTO;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;
import com.runwaysdk.web.ServletUtility;
import com.runwaysdk.web.WebClientSession;

public class JSONControllerServlet extends HttpServlet
{
  /**
   * Auto-generated id
   */
  private static final long serialVersionUID = -3892528595585482859L;

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  {
    String error = "Cannot use GET requests, only POST requests.";
    ServletException ex = new ServletException(error);
    JSONExceptionDTO wrapper = new JSONRunwayExceptionDTO(ex);
    res.setStatus(500);
    res.getWriter().write(wrapper.getJSON());
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  {
    ClientSession clientSession = (ClientSession) req.getSession().getAttribute(ClientConstants.CLIENTSESSION);
    Locale[] locales = ServletUtility.getLocales(req);

    // Ensures there is always a clientRequest object
    if (clientSession == null)
    {
      clientSession = WebClientSession.createAnonymousSession(locales);
    }

    String json = getOutputFromMethod(req, res, clientSession);

    // we use print instead of write here so the output stream will take care of
    // the encoding, which is set in the JS filter
    // res.getOutputStream().print(json);

    res.setContentType("application/json");
    res.getOutputStream().write(json.getBytes("UTF-8"));
  }

  /**
   * Gets the output JSON from every method call.
   *
   * @param req
   * @param clientSession
   * @return
   */
  private String getOutputFromMethod(HttpServletRequest req, HttpServletResponse res, ClientSession clientSession)
  {
    try
    {
      // the method to invoke
      String method = req.getParameter(JSONClientRequestConstants.METHOD.getName());

      // Copy the parameters map to a different HashMap so that we can modify it
      // if we need to.
      Map<String, String[]> parameters = new HashMap<String, String[]>(req.getParameterMap());

      // Replace the string null to an actual null value.
      Set<String> keys = (Set<String>) parameters.keySet();
      for (String key : keys)
      {
        String[] value = parameters.get(key);

        if (value[0].equals("\0null\0"))
        {
          parameters.put(key, new String[] { null });
        }
      }

      ClientRequestIF clientRequest = (ClientRequestIF) clientSession.getRequest();

      // Set the request object for the request
      String sessionId = clientRequest.getSessionId();

      Locale[] locales = ServletUtility.getLocales(req);

      /* START LOGIN/LOGOUT METHODS */
      if (method.equals(FacadeMethods.LOGIN.getName()))
      {
        String username = ( (String[]) parameters.get(JSONClientRequestConstants.USERNAME.getName()) )[0];
        String password = ( (String[]) parameters.get(JSONClientRequestConstants.PASSWORD.getName()) )[0];

        clientSession = WebClientSession.createUserSession(username, password, locales);
        req.getSession().setAttribute(ClientConstants.CLIENTSESSION, clientSession);

        clientRequest = (ClientRequest) clientSession.getRequest();
        req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

        sessionId = clientRequest.getSessionId();

        JSONReturnObject returnJSON = new JSONReturnObject();
        returnJSON.setWarnings(clientRequest.getWarnings());
        returnJSON.setInformation(clientRequest.getInformation());
        returnJSON.setReturnValue(sessionId);

        String json = returnJSON.toString();
        return json;
      }

      else if (method.equals(FacadeMethods.LOGIN_ANONYMOUS.getName()))
      {
        clientSession = WebClientSession.createAnonymousSession(locales);
        req.getSession().setAttribute(ClientConstants.CLIENTSESSION, clientSession);

        clientRequest = (ClientRequest) clientSession.getRequest();
        req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

        sessionId = clientRequest.getSessionId();

        JSONReturnObject returnJSON = new JSONReturnObject();
        returnJSON.setWarnings(clientRequest.getWarnings());
        returnJSON.setInformation(clientRequest.getInformation());
        returnJSON.setReturnValue(sessionId);

        String json = returnJSON.toString();
        return json;
      }
      else if (method.equals(FacadeMethods.CHANGE_LOGIN.getName()))
      {
        String username = ( (String[]) parameters.get(JSONClientRequestConstants.USERNAME.getName()) )[0];
        String password = ( (String[]) parameters.get(JSONClientRequestConstants.PASSWORD.getName()) )[0];

        ClientRequestIF tempClientRequest = clientSession.changeLogin(username, password);

        JSONReturnObject returnJSON = new JSONReturnObject();
        returnJSON.setWarnings(tempClientRequest.getWarnings());
        returnJSON.setInformation(tempClientRequest.getInformation());

        String json = returnJSON.toString();
        return json;
      }
      else if (method.equals(FacadeMethods.LOGOUT.getName()))
      {
        ClientRequestIF tempClientRequest = clientSession.logout();
        req.getSession().removeAttribute(ClientConstants.CLIENTSESSION);
        req.removeAttribute(ClientConstants.CLIENTREQUEST);

        JSONReturnObject returnJSON = new JSONReturnObject();
        returnJSON.setWarnings(tempClientRequest.getWarnings());
        returnJSON.setInformation(tempClientRequest.getInformation());

        String json = returnJSON.toString();
        return json;
      }
      /* END LOGIN/LOGOUT METHODS */
      else if (method.equals(FacadeMethods.GET_SESSION_USER.getName()))
      {
        String json = JSONControllerGeneric.getSessionUser(sessionId);
        return json;
      }
      else if (method.equals(FacadeMethods.IMPORT_TYPES.getName()))
      {
        String json = JSONControllerGeneric.importTypes(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.CHECK_ADMIN_SCREEN_ACCESS.getName()))
      {
        String json = JSONControllerGeneric.checkAdminScreenAccess(sessionId);
        return json;
      }
      else if (method.equals(FacadeMethods.NEW_MUTABLE.getName()))
      {
        String json = JSONControllerGeneric.newEntity(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.NEW_BUSINESS.getName()))
      {
        String json = JSONControllerGeneric.newBusiness(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.CREATE_SESSION_COMPONENT.getName()))
      {
        String json = JSONControllerGeneric.createSessionComponent(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.CREATE_BUSINESS.getName()))
      {
        String json = JSONControllerGeneric.createBusiness(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.NEW_STRUCT.getName()))
      {
        String json = JSONControllerGeneric.newStruct(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.CREATE_STRUCT.getName()))
      {
        String json = JSONControllerGeneric.createStruct(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.CREATE_RELATIONSHIP.getName()))
      {
        String json = JSONControllerGeneric.createRelationship(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.UPDATE.getName()))
      {
        String json = JSONControllerGeneric.update(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.DELETE.getName()))
      {
        String json = JSONControllerGeneric.delete(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_INSTANCE.getName()))
      {
        String json = JSONControllerGeneric.get(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_QUERY.getName()))
      {
        String json = JSONControllerGeneric.getQuery(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.ADD_PARENT.getName()))
      {
        String json = JSONControllerGeneric.addParent(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.DELETE_PARENT.getName()))
      {
        String json = JSONControllerGeneric.deleteParent(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.ADD_CHILD.getName()))
      {
        String json = JSONControllerGeneric.addChild(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.DELETE_CHILD.getName()))
      {
        String json = JSONControllerGeneric.deleteChild(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.DELETE_CHILDREN.getName()))
      {
        String json = JSONControllerGeneric.deleteChildren(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.DELETE_PARENTS.getName()))
      {
        String json = JSONControllerGeneric.deleteParents(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_CHILDREN.getName()))
      {
        String json = JSONControllerGeneric.getChildren(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_CHILD_RELATIONSHIPS.getName()))
      {
        String json = JSONControllerGeneric.getChildRelationships(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_PARENTS.getName()))
      {
        String json = JSONControllerGeneric.getParents(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_PARENT_RELATIONSHIPS.getName()))
      {
        String json = JSONControllerGeneric.getParentRelationships(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.LOCK.getName()))
      {
        String json = JSONControllerGeneric.lock(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.UNLOCK.getName()))
      {
        String json = JSONControllerGeneric.unlock(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GRANT_TYPE_PERMISSION.getName()))
      {
        // overloaded inside
        String json = JSONControllerGeneric.grantTypePermission(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GRANT_METHOD_PERMISSION.getName()))
      {
        // overloaded inside
        String json = JSONControllerGeneric.grantMethodPermission(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GRANT_ATTRIBUTE_PERMISSION.getName()))
      {
        // overloaded inside
        String json = JSONControllerGeneric.grantAttributePermission(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.REVOKE_ATTRIBUTE_PERMISSION.getName()))
      {
        String json = JSONControllerGeneric.revokeAttributePermission(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.REVOKE_TYPE_PERMISSION.getName()))
      {
        String json = JSONControllerGeneric.revokeTypePermission(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.REVOKE_METHOD_PERMISSION.getName()))
      {
        String json = JSONControllerGeneric.revokeMethodPermission(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.QUERY_BUSINESSES.getName()))
      {
        String json = JSONControllerGeneric.queryBusinesses(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.QUERY_VIEWS.getName()))
      {
        String json = JSONControllerGeneric.queryViews(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.QUERY_STRUCTS.getName()))
      {
        String json = JSONControllerGeneric.queryStructs(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.QUERY_ENTITIES.getName()))
      {
        String json = JSONControllerGeneric.queryEntities(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.QUERY_RELATIONSHIPS.getName()))
      {
        String json = JSONControllerGeneric.queryRelationships(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_ENUMERATION.getName()))
      {
        String json = JSONControllerGeneric.getEnumeration(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_ENUMERATIONS.getName()))
      {
        String json = JSONControllerGeneric.getEnumerations(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_ALL_ENUMERATIONS.getName()))
      {
        String json = JSONControllerGeneric.getAllEnumerations(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.GET_TERM_ALL_CHILDREN.getName()))
      {
        String json = JSONControllerGeneric.getTermAllChildren(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.MOVE_BUSINESS.getName()))
      {
        String json = JSONControllerGeneric.moveBusiness(sessionId, parameters);
        return json;
      }
      else if (method.equals(FacadeMethods.INVOKE_METHOD.getName()))
      {
        try
        {
          String json = JSONControllerGeneric.invokeMethod(sessionId, parameters);
          return json;
        }
        catch (IllegalAccessException e)
        {
          throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
          throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e)
        {
          throw new RuntimeException(e);
        }
      }
      else
      {
        // Bad call. Nothing to invoke.
        String text = "Attempt to invoke unknown method '" + method + "'. This method must exist as part of the if/else chain in JSONControllerServlet.";
        throw new RuntimeException(text);
      }
    }
    catch (JSONExceptionDTO e)
    {
      res.setStatus(500);
      String json = e.getJSON();
      return json;
    }
    catch (Throwable e)
    {
      JSONRunwayExceptionDTO ex = new JSONRunwayExceptionDTO(e);
      res.setStatus(500);
      String json = ex.getJSON();
      return json;
    }
  }
}
