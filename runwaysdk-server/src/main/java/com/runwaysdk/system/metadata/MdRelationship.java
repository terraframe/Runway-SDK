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
package com.runwaysdk.system.metadata;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;

public class MdRelationship extends MdRelationshipBase
{
  private static final long serialVersionUID = 1240203381783L;
  
  public MdRelationship()
  {
    super();
  }
  
  public static MdRelationship getMdRelationship(String type)
  {
    return (MdRelationship) BusinessFacade.get(MdRelationshipDAO.getMdRelationshipDAO(type));
  }
}
