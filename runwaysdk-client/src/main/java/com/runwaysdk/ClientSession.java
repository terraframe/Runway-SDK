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
package com.runwaysdk;

import java.util.Locale;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.request.ClientRequestManager;
import com.runwaysdk.request.ConnectionLabel;

public class ClientSession
{
  /**
   * The label of the clientRequest
   */
  private ConnectionLabel connectionLabel;

  /**
   * Id of the session.
   */
  private String          sessionId;

  private Locale[]        locales;

  private boolean         isPublicUser = false;

  private boolean         isLoggedIn   = false;

  /**
   * Creates a session on the server that is logged in as the anonymous public
   * user.
   *
   * @param locales
   *          the locale of the session
   */
  protected ClientSession(Locale[] locales)
  {
    this.connectionLabel = ClientRequestManager.getDefaultConnection();

    this.locales = locales;

    ClientRequestIF clientRequest = ClientRequest.getRequest(this, this.locales);
    copyClientRequestAttributes(clientRequest);
  }

  /**
   * Creates a session on the server that is logged in as the anonymous public
   * user.
   *
   * @param locales
   *          the locale of the session
   */
  public static ClientSession createAnonymousSession(Locale[] locales)
  {
    return new ClientSession(locales);
  }

  /**
   * Creates a session on the server located at the given label that is logged
   * in as the anonymous public user.
   *
   * @param connectionLabel
   * @param locales
   *          the locale of the session
   */
  protected ClientSession(ConnectionLabel connectionLabel, Locale[] locales)
  {
    this.connectionLabel = connectionLabel;

    this.locales = locales;

    ClientRequestIF clientRequest = ClientRequest.getRequest(this, this.locales);
    copyClientRequestAttributes(clientRequest);
  }

  /**
   * Creates a session on the server located at the given label that is logged
   * in as the anonymous public user.
   *
   * @param label
   * @param locales
   *          the locale of the session
   */
  public static ClientSession createAnonymousSession(String label, Locale[] locales)
  {
    ConnectionLabel connectionLabel = ClientRequestManager.getConnection(label);

    return new ClientSession(connectionLabel, locales);
  }

  /**
   * Connects to an existing session with the given session id.
   *
   * @param sessionId
   */
  protected ClientSession(String sessionId, Locale[] locales)
  {
    this.connectionLabel = ClientRequestManager.getDefaultConnection();

    this.locales = locales;

    ClientRequestIF clientRequest = ClientRequest.getRequest(this, sessionId);

    this.copyClientRequestAttributes(clientRequest);
    this.setLoginStatus(false, true);
  }

  /**
   * Connects to an existing session with the given session id.
   *
   * @param sessionId
   */
  public static ClientSession getExistingSession(String sessionId, Locale[] locales)
  {
    return new ClientSession(sessionId, locales);
  }

  /**
   * Connects to an existing session with the given session id at the end point
   * with the given label.
   *
   * @param connectionLabel
   * @param sessionId
   */
  protected ClientSession(ConnectionLabel connectionLabel, String sessionId, Locale[] locales)
  {
    this.connectionLabel = connectionLabel;

    this.locales = locales;

    ClientRequestIF clientRequest = ClientRequest.getRequest(this, sessionId);
    copyClientRequestAttributes(clientRequest);
  }

  /**
   * Connects to an existing session with the given session id at the end point
   * with the given label.
   *
   * @param label
   * @param sessionId
   */
  public static ClientSession getExistingSession(String label, String sessionId, Locale[] locales)
  {
    ConnectionLabel connectionLabel = ClientRequestManager.getConnection(label);

    return new ClientSession(connectionLabel, sessionId, locales);
  }

  /**
   * Creates a session on the server for the given user with the given password.
   *
   * @param userName
   * @param password
   * @param locales
   */
  protected ClientSession(String userName, String password, Locale[] locales)
  {
    this.connectionLabel = ClientRequestManager.getDefaultConnection();

    this.locales = locales;

    ClientRequestIF clientRequest = ClientRequest.getRequest(this, userName, password, this.locales);
    copyClientRequestAttributes(clientRequest);
  }

  /**
   * Creates a session on the server for the given user with the given password.
   *
   * @param userName
   * @param password
   * @param locales
   */
  public static ClientSession createUserSession(String userName, String password, Locale[] locales)
  {
    return new ClientSession(userName, password, locales);
  }

  /**
   * Creates a session on the server for the given user with the given password.
   *
   * @param connectionLabel
   * @param userName
   * @param password
   * @param locales
   */
  protected ClientSession(ConnectionLabel connectionLabel, String userName, String password, Locale[] locales)
  {
    this.connectionLabel = connectionLabel;

    this.locales = locales;

    ClientRequestIF clientRequest = ClientRequest.getRequest(this, userName, password, this.locales);
    copyClientRequestAttributes(clientRequest);
  }

  /**
   * Creates a session on the server located at the given label for the given
   * user with the given password.
   *
   * @param label
   * @param userName
   * @param password
   * @param locales
   */
  public static ClientSession createUserSession(String label, String userName, String password, Locale[] locales)
  {
    ConnectionLabel connectionLabel = ClientRequestManager.getConnection(label);
    return new ClientSession(connectionLabel, userName, password, locales);
  }

  /**
   * Returns a new clientRequest object for a request.
   * 
   * @return new clientRequest object for a request.
   */
  public ClientRequestIF getRequest()
  {
    return ClientRequest.getRequest(this, this.getSessionId());
  }

  /**
   * Attempts to log a user in with the specified username and password.
   *
   * @param username
   * @param password
   * @return <code>ClientRequest</code> object for the given request.
   */
  public ClientRequestIF changeLogin(String username, String password)
  {
    ClientRequest clientRequest = (ClientRequest) this.getRequest();
    clientRequest.changeLogin(username, password);
    return clientRequest;
  }

  /**
   * Logs a user out with the specified session id.
   *
   */
  public ClientRequestIF logout()
  {
    ClientRequest clientRequest = (ClientRequest) this.getRequest();
    clientRequest.logout();
    return clientRequest;
  }

  private void copyClientRequestAttributes(ClientRequestIF clientRequest)
  {
    this.sessionId = clientRequest.getSessionId();
  }

  /**
   * Returns the session id used by the clientRequest to connect to the
   * back-end.
   * 
   * @return session id used by the clientRequest to connect to the back-end.
   */
  public synchronized String getSessionId()
  {
    return this.sessionId;
  }

  /**
   * Returns the <code>Locale</code> used by the clientRequest.
   * 
   * @return <code>Locale</code> used by the clientRequest.
   */
  public synchronized Locale[] getLocales()
  {
    return this.locales;
  }

  protected synchronized void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
  }

  public ConnectionLabel getConnectionLabel()
  {
    return this.connectionLabel;
  }

  protected synchronized void setIsPublicUser(String username)
  {
    if (username.equals(UserInfo.PUBLIC_USER_NAME))
    {
      this.setIsPublicUser(true);
    }
    else
    {
      this.setIsPublicUser(false);
    }
  }

  protected synchronized void setIsPublicUser(boolean isPublicUser)
  {
    this.isPublicUser = isPublicUser;
  }

  /**
   * Returns true if the clientRequest is logged in as the anonymouse public
   * user, false otherwise.
   * 
   * @return true if the clientRequest is logged in as the anonymouse public
   *         user, false otherwise.
   */
  protected synchronized boolean isPublicUser()
  {
    return this.isPublicUser;
  }

  protected synchronized void setIsLoggedIn(boolean isLoggedIn)
  {
    this.isLoggedIn = isLoggedIn;
  }

  /**
   * Returns true if the clientRequest is loggedIn, false otherwise.
   * 
   * @return true if the clientRequest is loggedIn, false otherwise.
   */
  protected synchronized boolean isLoggedIn()
  {
    return this.isLoggedIn;
  }

  protected synchronized void setLoginStatus(String username, boolean isLoggedIn)
  {
    this.setIsPublicUser(username);
    this.setIsLoggedIn(isLoggedIn);
  }

  protected synchronized void setLoginStatus(boolean isPublicUser, boolean isLoggedIn)
  {
    this.setIsPublicUser(isPublicUser);
    this.setIsLoggedIn(isLoggedIn);
  }
}
