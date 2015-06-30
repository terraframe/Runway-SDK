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
package com.runwaysdk.business.generation.view;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;

/**
 * Event fired only when generating content on {@link MdRelationshipDAOIF}
 * 
 * @author Justin Smethie
 */
public interface RelationshipEventIF
{
  /**
   * @return The parent {@link MdBusinessDAOIF} of the {@link MdRelationshipDAOIF}
   */
  public MdBusinessDAOIF getParent();

  /**
   * @return The child {@link MdBusinessDAOIF} of the {@link MdRelationshipDAOIF}
   */
  public MdBusinessDAOIF getChild();
}
