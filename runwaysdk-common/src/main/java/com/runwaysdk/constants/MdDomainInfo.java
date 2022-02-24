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

public interface MdDomainInfo extends MetadataInfo
{
  public static final String ID_VALUE              = "514f5b28-3c4a-3b37-b363-4fe86700003a";
  
  /**
   * Class MdBusiness.
   */
  public static final String CLASS           = Constants.METADATA_PACKAGE + ".MdDomain";

  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL   = "displayLabel";
  
  /**
   * Name of the attribute that stores the oid of the super class.
   */
  public static final String DOMAIN_NAME     = "domainName";
}
