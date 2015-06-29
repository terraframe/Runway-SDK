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

public interface MdClassInfo extends MdTypeInfo
{
  /**
   * Class MdClassInfo.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdClass";

  /**
   * ID.
   */
  public static final String ID_VALUE  = "20070920NM000000000000000000000100000000000000000000000000000001";  
  
  /**
   * A published class has a representation in the DTO layer.
   */
  public static final String PUBLISH                   = "publish";
  /**
   * Name of the attribute that stores the stub .class blob.
   */
  public static final String STUB_CLASS                = "stubClass";
  /**
   * Name of the attribute that stores the stub .java clob.
   */
  public static final String STUB_SOURCE               = "stubSource";
  /**
   * Name of the attribute that stores the dto stub .class blob
   */
  public static final String DTO_STUB_CLASS            = "stubDTOclass";
  /**
   * Name of the attribute that stores the dto stub .java clob
   */
  public static final String DTO_STUB_SOURCE           = "stubDTOsource";
}
