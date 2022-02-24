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
package com.runwaysdk.constants;

public class WebXmlMappings
{
  /**
   * The Runway namespace used with mappings in the web.file for JEE deploy
   * environments. This namespace is used to prepend paths to filters, servlets,
   * and other resources.
   */
  public static final String RUNWAY_NAMESPACE = "Runway";
  
  public static final String SECURE_UPLOAD_ENDPOINT = RUNWAY_NAMESPACE + "/SecureFileUploadServlet";
  
  public static final String SECURE_DOWNLOAD_ENDPOINT = RUNWAY_NAMESPACE + "/SecureFileDownloadServlet";
  
  public static final String WEB_UPLOAD_ENDPOINT = RUNWAY_NAMESPACE + "/WebFileUploadServlet";
  
  public static final String WEB_DOWNLOAD_ENDPOINT = RUNWAY_NAMESPACE + "/WebFileDownloadServlet";
}
