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
package com.runwaysdk.web;

import java.util.Locale;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.runwaysdk.ClientSession;
import com.runwaysdk.request.ClientRequestManager;
import com.runwaysdk.request.ConnectionLabel;

public class WebClientSession extends ClientSession implements HttpSessionBindingListener
{
  /**
   * Creates a session on the server that is logged in as the anonymous public user.
   *
   * @param locales the locale of the session
   */
  protected WebClientSession(Locale[] locales)
  {
    super(locales);
  }

  /**
   * Creates a session on the server that is logged in as the anonymous public user.
   *
   * @param locales the locale of the session
   */
  public static WebClientSession createAnonymousSession(Locale[] locales)
  {
    return new WebClientSession(locales);
  }

  /**
   * Creates a session on the server located at the given label that is logged in as the anonymous public user.
   *
   * @param connectionLabel
   * @param locales the locale of the session
   */
  protected WebClientSession(ConnectionLabel connectionLabel, Locale[] locales)
  {
    super(connectionLabel, locales);
  }

  /**
   * Creates a session on the server located at the given label that is logged in as the anonymous public user.
   *
   * @param label
   * @param locales the locale of the session
   */
  public static WebClientSession createAnonymousSession(String label, Locale[] locales)
  {
    ConnectionLabel connectionLabel = ClientRequestManager.getConnection(label);

    return new WebClientSession(connectionLabel, locales);
  }

  /**
   * Connects to an existing session with the given session oid.
   *
   * @param sessionId
   */
  protected WebClientSession(String sessionId, Locale[] locales)
  {
    super(sessionId, locales);
  }

  /**
   * Connects to an existing session with the given session oid.
   *
   * @param sessionId
   */
  public static WebClientSession getExistingSession(String sessionId, Locale[] locales)
  {
    return new WebClientSession(sessionId, locales);
  }

  /**
   * Connects to an existing session with the given session oid at the end point with the given label.
   *
   * @param connectionLabel
   * @param sessionId
   */
  protected WebClientSession(ConnectionLabel connectionLabel, String sessionId, Locale[] locales)
  {
    super(connectionLabel, sessionId, locales);
  }

  /**
   * Connects to an existing session with the given session oid at the end point with the given label.
   *
   * @param label
   * @param sessionId
   */
  public static WebClientSession getExistingSession(String label, String sessionId, Locale[] locales)
  {
    ConnectionLabel connectionLabel = ClientRequestManager.getConnection(label);
    return new WebClientSession(connectionLabel, sessionId, locales);
  }

  /**
   * Creates a session on the server for the given user with the given password.
   *
   * @param userName
   * @param password
   * @param locales
   */
  protected WebClientSession(String userName, String password, Locale[] locales)
  {
    super(userName, password, locales);
  }

  /**
   * Creates a session on the server for the given user with the given password.
   *
   * @param userName
   * @param password
   * @param locales
   */
  public static WebClientSession createUserSession(String userName, String password, Locale[] locales)
  {
    return new WebClientSession(userName, password, locales);
  }

  /**
   * Creates a session on the server for the given user with the given password.
   *
   * @param connectionLabel
   * @param userName
   * @param password
   * @param locales
   */
  protected WebClientSession(ConnectionLabel connectionLabel, String userName, String password, Locale[] locales)
  {
    super(connectionLabel, userName, password, locales);
  }

  /**
   * Creates a session on the server located at the given label for the given user with the given password.
   *
   * @param label
   * @param userName
   * @param password
   * @param locales
   */
  public static WebClientSession createUserSession(String label, String userName, String password, Locale[] locales)
  {
    ConnectionLabel connectionLabel = ClientRequestManager.getConnection(label);
    return new WebClientSession(connectionLabel, userName, password, locales);
  }

  /**
   * This method does not do anything.
   */
  public void valueBound(HttpSessionBindingEvent event) {}

  /**
   * When the HttpSession object expires, this method is called and logs out the session
   */
  public void valueUnbound(HttpSessionBindingEvent event)
  {
    this.logout();
  }

}
