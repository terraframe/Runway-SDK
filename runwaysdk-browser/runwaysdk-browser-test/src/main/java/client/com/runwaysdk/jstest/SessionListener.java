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
package com.runwaysdk.jstest;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.web.WebClientSession;

public class SessionListener implements HttpSessionListener
{

  public void sessionCreated(HttpSessionEvent arg0)
  {
  }

  public void sessionDestroyed(HttpSessionEvent arg0)
  {
    HttpSession session = arg0.getSession();
    // process which logs the user out.
    WebClientSession clientSession = (WebClientSession)session.getAttribute(ClientConstants.CLIENTSESSION);
    if (clientSession != null)
    {
      clientSession.logout();
    }
    session.removeAttribute(ClientConstants.CLIENTSESSION);
    session.invalidate();
  }
}
