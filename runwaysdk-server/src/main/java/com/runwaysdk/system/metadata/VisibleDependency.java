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
package com.runwaysdk.system.metadata;

public class VisibleDependency extends VisibleDependencyBase
{
  private static final long serialVersionUID = -195945956;
  
  public VisibleDependency(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public VisibleDependency(com.runwaysdk.system.metadata.MdField parent, com.runwaysdk.system.metadata.MdField child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
