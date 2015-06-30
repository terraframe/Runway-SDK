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
package com.runwaysdk.constants;

public interface MdFacadeInfo extends MdTypeInfo
{

  /**
   * Class.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdFacade"; 
  
  public static final String ID_VALUE = "20070427JS000000000000000000150700000000000000000000000000000001";
  
  public static final String STUB_CLASS = "stubClass";
  
  public static final String STUB_SOURCE = "stubSource";
  
  public static final String SERVER_CLASSES = "serverClasses";
  
  public static final String COMMON_CLASSES = "commonClasses";
  
  public static final String CLIENT_CLASSES = "clientClasses";
  
  /**
   * The name the controller to which this MdFacade connects.
   */
  public static final String ADAPTER = "adapter";
  
  public static final String JSON_GENERIC_ADAPTER_PREPEND = "JSON";
  
  public static final String JSON_ADAPTER_NAMESPACE = "JSON-adapter";
  
  public static final String JSON_GENERIC_ADAPTER_SUFFIX = "GenericAdapter";
}
