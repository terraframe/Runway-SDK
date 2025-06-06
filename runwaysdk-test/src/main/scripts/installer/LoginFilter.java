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
package ${domain}$;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.generation.loader.;
import com.runwaysdk.web.ServletUtility;
import com.runwaysdk.web.WebClientSession;

public class LoginFilter implements Filter, 
{
  private FilterConfig filterConfig;

  public void init(FilterConfig filterConfig) throws ServletException
  {
    this.filterConfig = filterConfig;
  }

  public void destroy()
  {
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpSession session = httpReq.getSession();
    WebClientSession clientSession = (WebClientSession)session.getAttribute(ClientConstants.CLIENTSESSION);
    
    if (clientSession==null)
    {
      Locale[] locales = ServletUtility.getLocales(httpReq);
      clientSession = WebClientSession.createAnonymousSession(locales);
      session.setAttribute(ClientConstants.CLIENTSESSION, clientSession);
    }

    // Create a request object for this request
    ClientRequestIF clientRequest = clientSession.getRequest();
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    chain.doFilter(req, res);
  }
}
