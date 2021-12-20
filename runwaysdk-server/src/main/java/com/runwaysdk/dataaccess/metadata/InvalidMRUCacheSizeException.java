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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.ServerExceptionMessageLocalizer;

/**
 * Thrown when an error occured regarding valid cache sizes defined by an MdBusiness
 * using Most Recently Used Caching.
 */
public class InvalidMRUCacheSizeException extends MetadataException
{
  /**
   * Auto-generated serial oid.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The MdBusiness involved in the Exception
   */
  private MdBusinessDAO mdBusiness;
  
  /**
   * Constructor.
   * 
   * @param devMessage
   * @param mdBusiness
   */
  public InvalidMRUCacheSizeException(String devMessage, MdBusinessDAO mdBusiness)
  {
    super(devMessage);
    this.mdBusiness = mdBusiness;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.invalidMRUCacheSizeException(this.getLocale(), this.mdBusiness);
  }
  
}
