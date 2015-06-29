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
package com.runwaysdk.business.rbac;

import java.util.Set;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.BusinessDAOIF;


public interface SDutyDAOIF extends BusinessDAOIF
{
  /**
   * The type SDutyDAO class.
   */
  public static final String CLASS = Constants.SYSTEM_PACKAGE + "."+"SDuty";
  
  /**
   * The name of the ssdName attribute of the SDutyDAO table
   */
  public static final String SDNAME = "sdName";
  
  /**
   * The name of the ssdCardinality attribute of the SDutyDAO table
   */
  public static final String SDCARDINALITY = "sdCardinality";
  
  /**
   * The type of the ConflictingRoles relationship
   */
  public static final String SDCONFLICTINGROLES = Constants.SYSTEM_PACKAGE + "."+"ConflictingRoles";
  
  public String getSSDSetName();

  /**
   * Returns the cardinality of a SSD Set
   * 
   * @return The cardinality of the SSD set
   */
  public int getSSDSetCardinality();
  
  /**
   * Returns a set of roles associated with a SSD Set
   * 
   * @return A set of all roles in the SSD set
   */
  public Set<RoleDAOIF> SSDSetRoles();
  
  /**
   * Return true if an singleactor is below the cardinality of a given SSD set
   * 
   * @param singleActorIF The singleactor to validate
   * @return If an singleactor is below the cardinality of a given SSD set
   */
  public boolean checkSSD(SingleActorDAOIF singleActorIF);
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public SDutyDAO getBusinessDAO();
}
