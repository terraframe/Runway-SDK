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
package com.runwaysdk.jstest;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.web.WebClientSession;

public class SessionFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    throws ServletException, IOException
  {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpSession session = req.getSession();

    WebClientSession clientSession = (WebClientSession)session.getAttribute(ClientConstants.CLIENTSESSION);

    // This is a new or expired session.
    if (clientSession == null)
    {
      clientSession = WebClientSession.createUserSession(JSTestConstants.USERNAME_WITH_ALL_PERMISSIONS, 
          JSTestConstants.USER_PASSWORD_WITH_ALL_PERMISSIONS, new Locale[]{Locale.ENGLISH});
    	
      session.setAttribute(ClientConstants.CLIENTSESSION, clientSession);
      
      int secondsMaxTimeOut = (CommonProperties.getSessionTime());
      session.setMaxInactiveInterval(secondsMaxTimeOut);
    }
    
    ClientRequestIF clientRequest = clientSession.getRequest();
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);   
    
    chain.doFilter(request,response);
  }

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}
}
