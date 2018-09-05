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


public interface MetadataInfo extends BusinessInfo
{
  
  /**
   * OID.
   */
  public static final String ID_VALUE  = "bf60d4f1-7eb8-3fda-9f9e-3cb7c5750058";  
  
  /**
   * Class MetaData.
   */
  public static final String CLASS                       = Constants.METADATA_PACKAGE+".Metadata";
  /**
   * Name of the attribute that indicates if the database can be removed or not.
   */
  public static final String REMOVE                      = "remove";
  /**
   * Name of the attribute that describes this metadata.
   */
  public static final String DESCRIPTION                 = "description";

}
