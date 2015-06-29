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

public interface ElementInfo extends EntityInfo
{
  /**
   * The Class. Unlike other CLASS constants, this does <i>not</i>
   * correlate directly with the database. This class is extended by generated
   * classes (which do have database counterparts), but Element.java
   * exists only as part of the bridge between layers.
   */
  public static final String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".Element";

  public static final String ID_VALUE          = "0000000000000000000000000000001100000000000000000000000000000001";

  public static final String TABLE             = "elements";

  /**
   * Name of the attribute that specifies a reference to the user who created
   * the component.
   */
  public static final String CREATED_BY       = "createdBy";
  /**
   * Name of the attribute that specifies when the component was created.
   */
  public static final String CREATE_DATE      = "createDate";
  /**
   * Name of the attribute that specifies a reference to the user who last
   * updated the component.
   */
  public static final String LAST_UPDATED_BY  = "lastUpdatedBy";
  /**
   * Name of the attribute that specifies when the component was last updated.
   */
  public static final String LAST_UPDATE_DATE = "lastUpdateDate";
  /**
   * Name of the attribute that specifies a reference to the user who has a lock
   * on this component.
   */
  public static final String LOCKED_BY        = "lockedBy";
  /**
   * Name of the attribute user that owns the object.
   */
  public static final String OWNER            = "owner";
  /**
   * The sequence number for this MdEntity
   */
  public static final String SEQUENCE         = "seq";

  /**
   * Name of the attribute that specifies the domain of the element
   */
  public static final String DOMAIN = "entityDomain";
}
