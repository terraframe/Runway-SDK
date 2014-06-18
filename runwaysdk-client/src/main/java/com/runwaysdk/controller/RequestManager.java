/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;

public class RequestManager
{
  /**
   * The servlet request being managed
   */
  private HttpServletRequest             req;

  /**
   * List of attribute notifications which have occured
   */
  private List<AttributeNotificationDTO> attributeNotifications;

  /**
   * List of problems which have occured
   */
  private List<ProblemDTO>               problems;

  /**
   * The clientRequest
   */
  private ClientRequestIF                clientReq;

  private ClientSession                  clientSession;

  public RequestManager(HttpServletRequest req)
  {
    HttpSession httpSession = req.getSession();

    this.req = req;
    this.attributeNotifications = new LinkedList<AttributeNotificationDTO>();
    this.problems = new LinkedList<ProblemDTO>();
    this.clientReq = (ClientRequestIF) req.getAttribute(ClientConstants.CLIENTREQUEST);
    this.clientSession = (ClientSession) httpSession.getAttribute(ClientConstants.CLIENTSESSION);

    if(this.clientSession == null)
    {
      this.clientSession = (ClientSession) req.getAttribute(ClientConstants.CLIENTSESSION);      
    }
    
    // All RequestManagers must have a ClientRequest. Thus if one doesn't exist
    // then create a new one from the ClientSession
    if (this.clientReq == null)
    {

      if (this.clientSession != null)
      {
        this.clientReq = clientSession.getRequest();
        this.req.setAttribute(ClientConstants.CLIENTREQUEST, this.clientReq);
      }
    }
  }

  public HttpServletRequest getReq()
  {
    return req;
  }

  public ClientRequestIF getClientRequest()
  {
    if (clientReq == null)
    {
      String msg = "Could not find the ClientRequest.  Either the user is not logged in, or the ClientRequest does not exist in the ServletRequest attribute [RUNWAY_ClientRequest].";
      new NullClientRequestException(msg, req.getLocale());
    }

    return clientReq;
  }
  
  public ClientRequestIF newClientRequest()
  {
    if (clientSession == null)
    {
      String msg = "Could not find the ClientRequest.  Either the user is not logged in, or the ClientRequest does not exist in the ServletRequest attribute [RUNWAY_ClientRequest].";
      new NullClientRequestException(msg, req.getLocale());
    }
    
    return clientSession.getRequest();
  }

  public List<AttributeNotificationDTO> getAttributeNotifications()
  {
    return attributeNotifications;
  }

  public void addAttributeNotificationDTO(AttributeNotificationDTO problem)
  {
    attributeNotifications.add(problem);
  }

  public List<ProblemDTO> getProblems()
  {
    return problems;
  }

  public void addProblem(ProblemDTO problem)
  {
    problems.add(problem);
  }

  public boolean hasExceptions()
  {
    return ( ( attributeNotifications.size() > 0 ) || ( problems.size() > 0 ) );
  }
}
