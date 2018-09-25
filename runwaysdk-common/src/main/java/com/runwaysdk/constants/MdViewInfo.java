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

public interface MdViewInfo extends MdSessionInfo
{
  /**
   * Class MdViewInfo.
   */
  public static final String CLASS                  = Constants.METADATA_PACKAGE+".MdView";
  
  /**
   * OID.
   */
  public static final String ID_VALUE               = "0ce9c17c-c794-3a95-9e68-6fca1600003a"; 

  /**
   * Super defining class.
   */
  public static final String SUPER_MD_VIEW          = "superMdView";
  
  /**
   * Query Base Class source code.
   */
  public static final String QUERY_BASE_SOURCE      = "queryBaseSource";
  
  /**
   * Query Base Class bytecode.
   */
  public static final String QUERY_BASE_CLASS       = "queryBaseClass";

  /**
   * Query Stub Class source code.
   */
  public static final String QUERY_STUB_SOURCE      = "queryStubSource";
  
  /**
   * Query Stub Class bytecode.
   */
  public static final String QUERY_STUB_CLASS       = "queryStubClass";
  
  /**
   * Query DTO Class source code.
   */
  public static final String QUERY_DTO_SOURCE      = "queryDTOsource";
  
  /**
   * Query DTO Class bytecode.
   */
  public static final String QUERY_DTO_CLASS       = "queryDTOclass";
}
