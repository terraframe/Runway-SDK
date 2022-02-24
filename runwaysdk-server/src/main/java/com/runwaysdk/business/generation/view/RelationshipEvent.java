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
package com.runwaysdk.business.generation.view;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;

/**
 * Concrete implementation of {@link RelationshipEventIF}
 * 
 * @author Justin Smethie
 */
public class RelationshipEvent implements RelationshipEventIF
{
  /**
   * Parent {@link MdBusinessDAOIF} of the {@link MdRelationshipDAOIF}
   */
  private MdBusinessDAOIF parent;

  /**
   * Child {@link MdBusinessDAOIF} of the {@link MdRelationshipDAOIF}
   */
  private MdBusinessDAOIF child;

  /**
   * @param parent
   *          Parent {@link MdBusinessDAOIF} of the {@link MdRelationshipDAOIF}
   * @param child
   *          Child {@link MdBusinessDAOIF} of the {@link MdRelationshipDAOIF}
   */
  public RelationshipEvent(MdBusinessDAOIF parent, MdBusinessDAOIF child)
  {
    this.parent = parent;
    this.child = child;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.view.RelationshipEventIF#getChild()
   */
  public MdBusinessDAOIF getChild()
  {
    return child;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.view.RelationshipEventIF#getParent()
   */
  public MdBusinessDAOIF getParent()
  {
    return parent;
  }
}
