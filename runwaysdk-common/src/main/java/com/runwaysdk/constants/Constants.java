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
/*
 * Created on Apr 29, 2005
 */
package com.runwaysdk.constants;

/**
 * Defines constant values used by the DataAccess layer. This prevents special
 * string values from being hard coded throughout the code.
 * 
 * @author nathan
 * 
 * @version $Revision: 1.54 $
 * @since 1.4
 */
public class Constants
{
  /**
   * Runwaysdk Common jar (the name of this jar should never be renamed by
   * developers, hence it is not in a properties file)
   */
  public static final String RUNWAYSDK_COMMON_JAR          = "runwaysdk-common.jar";

  /**
   * Runwaysdk Server jar (the name of this jar should never be renamed by
   * developers, hence it is not in a properties file)
   */
  public static final String RUNWAYSDK_SERVER_JAR          = "runwaysdk-server.jar";

  /**
   * Runwaysdk Client jar (the name of this jar should never be renamed by
   * developers, hence it is not in a properties file)
   */
  public static final String RUNWAYSDK_CLIENT_JAR          = "runwaysdk-client.jar";

  /**
   * System package
   */
  public static final String ROOT_PACKAGE                  = "com.runwaysdk";

  public static final String SYSTEM_PACKAGE                = ROOT_PACKAGE + ".system";

  public static final String METADATA_PACKAGE              = SYSTEM_PACKAGE + ".metadata";

  public static final String ONTOLOGY_PACKAGE              = METADATA_PACKAGE + ".ontology";

  public static final String TRANSACTION_PACKAGE           = SYSTEM_PACKAGE + ".transaction";

  public static final String SYSTEM_BUSINESS_PACKAGE       = ROOT_PACKAGE + ".business";

  public static final String GENERATION_BUSINESS_PACKAGE   = SYSTEM_BUSINESS_PACKAGE + ".generation";

  public static final String FACADE_PACKAGE                = ROOT_PACKAGE + ".facade";

  public static final String SCHEDULER_PACKAGE                = SYSTEM_PACKAGE + ".scheduler";

  public static final String DATAACCESS_PACKAGE            = ROOT_PACKAGE + ".dataaccess";

  public static final String DATAACCESS_METADATA_PACKAGE   = DATAACCESS_PACKAGE + ".metadata";

  public static final String DATAACCESS_IO_PACKAGE         = DATAACCESS_PACKAGE + ".io";

  public static final String DATAACCESS_ATTRIBUTES_PACKAGE = DATAACCESS_PACKAGE + ".attributes";

  public static final String DATABASE_PACKAGE              = DATAACCESS_PACKAGE + ".database";

  public static final String TRANSPORT_PACKAGE             = ROOT_PACKAGE + ".transport";

  public static final String CONVERSION_PACKAGE            = TRANSPORT_PACKAGE + ".conversion";

  public static final String WEB_PACKAGE                   = ROOT_PACKAGE + ".web";

  public static final String JSON_PACKAGE                  = WEB_PACKAGE + ".json";

  public static final String CONTROLLER_PACKAGE            = ROOT_PACKAGE + ".controller";

  public static final String REQUEST_PACKAGE               = ROOT_PACKAGE + ".request";

  public static final String SESSION_PACKAGE               = ROOT_PACKAGE + ".session";

  public static final String NATHAN_USER_ID                = "000000000000000000000000000001310060";

  public static final String TIME_FORMAT                   = "HH:mm:ss";

  public static final String DATE_FORMAT                   = "yyyy-MM-dd";

  public static final String DATETIME_FORMAT               = "yyyy-MM-dd HH:mm:ss";

  public static final String DATETIME_WITH_TIMEZONE_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";
}
