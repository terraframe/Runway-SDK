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


public interface MdElementInfo extends MdEntityInfo
{
  /**
   * Class MdElement.
   */
  public static final String CLASS                     = Constants.METADATA_PACKAGE+".MdElement";
  /**
   * OID.
   */
  public static final String ID_VALUE                   = "682ae94c-a233-3857-ba2c-7e2bd800003a";

  /**
   * Name of the attribute that indicates if this type is extendable or not.
   */
  public static final String EXTENDABLE                = "extendable";
  /**
   * Name of the attribute that indicates if this type is abstract or not.
   */
  public static final String ABSTRACT                  = "isAbstract";

}
