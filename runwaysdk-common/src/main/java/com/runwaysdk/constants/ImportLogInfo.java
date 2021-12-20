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

public interface ImportLogInfo extends BusinessInfo
{
  /**
   * Class <code>ImportLog</code>.
   */
  public static final String CLASS                     = Constants.TRANSACTION_PACKAGE+".ImportLog";
  
  /**uence
   * OID of the metadata that defines this class.
   */
   public static final String ID_VALUE                 = "9f304884-1242-33a5-ae8a-5cccf500003a";
  
   /**
    * Source site.
    */
   public static final String SOURCE_SITE              = "sourceSite";

   /**
    * Export sequence.
    */
   public static final String LAST_EXPORT_SEQUENCE     = "lastExportSeq";

  
}
