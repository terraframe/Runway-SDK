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
package com.runwaysdk.constants;

public class ClientConstants
{

  /**
   * Client Request Class.
   */
  public static final String CLIENT_REQUEST_CLASS     = Constants.ROOT_PACKAGE+".ClientSession";

  /**
   * JSP Fetcher Class.
   */
  public static final String JSP_FETCHER_CLASS        = Constants.CONTROLLER_PACKAGE+".JSPFetcher";

  /**
   * ConnectionLabelFacade Class.
   */
  public static final String CONNECTION_LABEL_FACADE_CLASS  = Constants.REQUEST_PACKAGE+".ConnectionLabelFacade";

  /**
   * ConnectionLabel Class.
   */
  public static final String CONNECTION_LABEL_CLASS    = Constants.REQUEST_PACKAGE+".ConnectionLabel";

  /**
   * ConnectionLabel.TYPE Class.
   */
  public static final String CONNECTION_LABEL_TYPE_CLASS  = Constants.REQUEST_PACKAGE+".ConnectionLabel.Type";

  /**
   * The client request.
   */
  public static final String CLIENTREQUEST             = "RUNWAY_ClientRequest";

  /**
   * The session session.
   */
  public static final String CLIENTSESSION             = "RUNWAY_ClientSession";

  /**
   * DTO of the user that is currently logged into the session.
   */
  public static final String CURRENTUSER               = "RUNWAY_CurrentUser";

  /**
   * Name of the type on which an attribute file is defined.
   */
  public static final String VAULT_ATTRIBUTE_FILE_TYPE = "RUNWAY_VaultAttributeFileType";

  /**
   * Name of the attribute file.
   */
  public static final String VAULT_ATTRIBUTE_FILE_NAME = "RUNWAY_VaultAttributeFileName";

}
